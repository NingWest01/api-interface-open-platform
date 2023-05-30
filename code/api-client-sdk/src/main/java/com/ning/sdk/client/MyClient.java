package com.ning.sdk.client;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.ning.sdk.model.InterfaceInfo;
import com.ning.sdk.model.User;
import com.ning.sdk.utils.SignUtils;

import java.util.HashMap;

/**
 * @author NingWest
 * @date 2023/05/09 21:17
 */
public class MyClient {

    public static final String HOST = "http://localhost:8090";

    private String accessKey;

    private String secretKey;

    public MyClient() {
    }

    public MyClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String getMyName() {

        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", "123");

        String result = HttpUtil.get(HOST + "/api/demo", paramMap);
        System.out.println(result);
        return result;
    }

    public String postMyName() {

        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", "123");

        String result = HttpUtil.post(HOST + "/api/demo", paramMap);
        System.out.println(result);
        return result;
    }

    public String postJsonName(InterfaceInfo requestParams, User user) {

        String jsonStr = JSONUtil.toJsonStr(user);
        return
                HttpRequest.post(requestParams.getUrl())
                        .addHeaders(SignUtils.getHeader(requestParams,accessKey, secretKey, user))
                        .body(jsonStr)
                        .execute()
                        .body();
    }



}
