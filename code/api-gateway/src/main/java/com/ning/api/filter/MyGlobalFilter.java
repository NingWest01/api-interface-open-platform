package com.ning.api.filter;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.ning.api.constant.Constant;
import com.ning.api.service.UserGatewayService;
import com.ning.api.service.UserInterfaceGatewayService;
import com.ning.api.utils.Md5Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author NingWest
 * @date 2023/05/27 20:55
 */
@Slf4j
@Component
@ConfigurationProperties(prefix = "my")
public class MyGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private UserGatewayService userGatewayService;

    @DubboReference
    private UserInterfaceGatewayService userInterfaceGatewayService;

    /*-- 获取配置文件中的集合  方便设置白名单 --*/
    private List<String> whiteList;

    public List<String> getWhiteList() {
        return whiteList;
    }

    public void setWhiteList(List<String> whiteList) {
        this.whiteList = whiteList;
    }
    /*-- 结束 --*/


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {


        //1. 请求日志
        ServerHttpRequest request = exchange.getRequest();
        log.info("请求地址：[{}]", request.getPath().value());
        log.info("请求方法：[{}]", request.getMethod());
        // 请求地址
        String address = Objects.requireNonNull(request.getLocalAddress()).getHostString();
        log.info("请求来源地址：[{}]", address);
        //2. 黑白名单  只有127.0.0.1的才能访问
        ServerHttpResponse response = exchange.getResponse();
        if (!whiteList.contains(address)) {
            response.setStatusCode(HttpStatus.FORBIDDEN);
            // 返回
            return response.setComplete();
        }
        //3. 用户鉴权（判断 ak、sk是否合法）
        boolean isOk = checkMethod(request);
        if (!isOk) {
            response.setStatusCode(HttpStatus.FORBIDDEN);
            // 返回
            return response.setComplete();
        }
        HttpHeaders headers = request.getHeaders();
        // 用户id
        String userId = headers.getFirst(Constant.USER_ID);
        // 接口id
        String interfaceId = headers.getFirst(Constant.INTERFACE_ID);
        // 判断是否有请求接口的资格
        Object info = userInterfaceGatewayService.interfaceInfo(Long.parseLong(Objects.requireNonNull(interfaceId)), Long.parseLong(Objects.requireNonNull(userId)));

        if(Objects.isNull(info)){
            response.setStatusCode(HttpStatus.FORBIDDEN);
            // 返回
            return response.setComplete();
        }
        return handleResponse(exchange, chain, userId, interfaceId);
    }

    /**
     * 处理响应
     */
    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, String userId, String interfaceId) {

        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 缓存数据的工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // 拿到响应码
            HttpStatus statusCode = originalResponse.getStatusCode();
            if (statusCode == HttpStatus.OK) {
                // 装饰，增强能力
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    // 等调用完转发的接口后才会执行
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 往返回值里写数据
                            // 拼接字符串
                            return super.writeWith(
                                    fluxBody.handle((dataBuffer, sink) -> {
                                        try {
                                            // 7. 调用成功，接口调用次数 + 1 invokeCount
                                            boolean isOk = userInterfaceGatewayService.minusOne(Long.parseLong(interfaceId), Long.parseLong(userId));
                                            if (isOk) {
                                                log.info("接口调用次数 + 1 成功");
                                            } else {
                                                log.error("接口调用次数 + 1 失败");
                                                handleNoAuth(originalResponse);
                                            }
                                        } catch (Exception e) {
                                            log.error("接口调用次数 + 1 报错");
                                            handleNoAuth(originalResponse);
                                        }
                                        byte[] content = new byte[dataBuffer.readableByteCount()];
                                        dataBuffer.read(content);
                                        DataBufferUtils.release(dataBuffer);//释放掉内存
                                        // 构建日志
                                        StringBuilder sb2 = new StringBuilder(200);
                                        List<Object> rspArgs = new ArrayList<>();
                                        rspArgs.add(originalResponse.getStatusCode());
                                        String data = new String(content, StandardCharsets.UTF_8); //data
                                        sb2.append(data);
                                        // 打印日志
                                        log.info("响应结果：" + data);
                                        sink.next(bufferFactory.wrap(content));
                                    }));
                        } else {
                            // 8. 调用失败，返回一个规范的错误码
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 设置 response 对象为装饰过的
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange); // 降级处理返回数据
        } catch (Exception e) {
            log.error("网关处理响应异常" + e);
            return chain.filter(exchange);
        }
    }


    /**
     * 请求校验
     */
    private boolean checkMethod(ServerHttpRequest request) {
        // 获取请求头
        HttpHeaders headers = request.getHeaders();
        String sign = headers.getFirst(Constant.SIGN);
        String nonce = headers.getFirst(Constant.NONCE);
        String accessKey = headers.getFirst(Constant.ACCESS_KEY);
        String timestamp = headers.getFirst(Constant.TIMESTAMP);
        String params = headers.getFirst(Constant.DATA);

        //先判断时间戳、随机数是否正确
        // 时间戳
        Date now = new Date();
        // 现在时间戳
        long nowTime = now.getTime();
        // 根据当前时间 获取5分钟的时间戳
        long time = DateUtil.offsetMinute(now, -5).getTime();
        // 如果传递过来的时间在这个范围内就证明是合法的时间请求
        if (StrUtil.isNotBlank(timestamp)) {
            // 转换为时间戳
            long timeStamp = Long.parseLong(Objects.requireNonNull(timestamp));
            // 如果时间小于当前时间 并且大于 倒退2天 时间合理
            if (timeStamp >= nowTime || timeStamp < time) {
                return false;
            }
        }
        //  nonce  此处的随机数 可以在生成的时候存入 redis 并携带过来唯一的UUID 根据 request获取
        if (StrUtil.isBlank(nonce)) {
            return false;
        }
        // 远程请求数据
        HashMap<String, String> userInfo = (HashMap<String, String>) userGatewayService.akSKGetUser(accessKey);

        //  此处的 secretKey 应该是从数据库获取
        String newSign = Md5Utils.md5(params + userInfo.get("secretKey") + accessKey);
        if (!Objects.equals(newSign, sign)) {
            return false;
        }
        // 最终无异常返回
        return true;
    }


    /**
     * 无权限
     */
    public Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    public Mono<Void> handleInvokeError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }


    @Override
    public int getOrder() {
        return -1;
    }
}
