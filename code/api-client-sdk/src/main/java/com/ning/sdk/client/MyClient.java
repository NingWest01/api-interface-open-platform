package com.ning.sdk.client;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.ning.sdk.model.User;
import com.ning.sdk.utils.SignUtils;

import java.util.HashMap;

/**
 * @author NingWest
 * @date 2023/05/09 21:17
 */
public class MyClient {

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

        String result = HttpUtil.get("http://localhost:8103/api/demo", paramMap);
        System.out.println(result);
        return result;
    }

    public String postMyName() {

        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", "123");

        String result = HttpUtil.post("http://localhost:8103/api/demo", paramMap);
        System.out.println(result);
        return result;
    }

    public String postJsonName(User user) {

        String jsonStr = JSONUtil.toJsonStr(user);
        return
                HttpRequest.post("http://localhost:8103/api/demo/user")
                        .addHeaders(SignUtils.getHeader(accessKey, secretKey, user))
                        .body(jsonStr)
                        .execute()
                        .body();
    }


    public static void main(String[] args) {
        MyClient myClient = new MyClient("ning", "abcdefg");
        User user = new User();
        user.setName("宁西");
        String jsonUser = myClient.postJsonName(user);
        System.out.println(jsonUser);


    }

}
