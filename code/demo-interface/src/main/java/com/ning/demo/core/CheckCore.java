package com.ning.demo.core;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.ning.demo.constant.Constant;
import com.ning.demo.utils.Md5Utils;
import com.ning.sdk.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author NingWest
 * @date 2023/05/10 11:14
 */
public class CheckCore {

    public static boolean checkParams(User user, HttpServletRequest request) {
        String sign = request.getHeader(Constant.SIGN);
        String nonce = request.getHeader(Constant.NONCE);
        String accessKey = request.getHeader(Constant.ACCESS_KEY);
        String timestamp = request.getHeader(Constant.TIMESTAMP);

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
            long timeStamp = Long.parseLong(timestamp);
            // 如果时间小于当前时间 并且大于 倒退2天 时间合理
            if (timeStamp >= nowTime || timeStamp < time) {
                return false;
            }
        }
        // todo nonce  此处的随机数 可以在生成的时候存入 redis 并携带过来唯一的UUID 根据 request获取
        if (StrUtil.isBlank(nonce)) {
            return false;
        }

        // todo 此处的 secretKey 应该是从数据库获取
        String newSign = Md5Utils.md5(user + "abcdefg" + accessKey);
        if (!newSign.equals(sign)) {
            return false;
        }

        // 最终无异常返回
        return true;
    }
}
