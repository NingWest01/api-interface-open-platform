package com.ning.sdk.utils;

import cn.hutool.core.util.RandomUtil;
import com.ning.sdk.constant.Constant;
import com.ning.sdk.model.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author NingWest
 * @date 2023/05/10 10:50
 */
public class SignUtils {

    // 生成 签名信息
    public static Map<String, String> getHeader(String accessKey, String secretKey, User parma) {
        Map<String, String> map = new HashMap<>();
        map.put(Constant.ACCESS_KEY, accessKey);
        // 不可直接发送
        //map.put(Constant.SECRET_KEY, accessKey);
        // 生成签名 生成的顺序  用户参数 + secretKey + accessKey
        map.put(Constant.SIGN, Md5Utils.md5(parma + secretKey + accessKey));
        long time = new Date().getTime();
        // 时间戳
//        map.put(Constant.TIMESTAMP, Long.toString(time));
        map.put(Constant.TIMESTAMP, Long.toString(time));
        // 随机数
        map.put(Constant.NONCE, RandomUtil.randomString(5));

        return map;
    }
}
