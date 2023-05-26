package com.ning.demo.controller;

import com.ning.demo.core.CheckCore;
import com.ning.sdk.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author NingWest
 * @date 2023/05/09 17:46
 */
@RestController
@RequestMapping("/demo")
@Slf4j
public class ProviderInterfaceController {

    @GetMapping
    public Map<String, Object> getNameByGet(String name) {
        Map<String, Object> data = new HashMap<>();

        data.put("code", 200);
        data.put("msg", "GET");
        data.put("data", "GET - 你的名字：" + name);
        return data;
    }

    @PostMapping
    public Map<String, Object> getNameByPost(@RequestParam String name) {
        Map<String, Object> data = new HashMap<>();

        data.put("code", 200);
        data.put("msg", "POST");
        data.put("data", "POST - 你的名字：" + name);
        return data;
    }

    @PostMapping("/user")
    public Map<String, Object> getUserName(@RequestBody User user, HttpServletRequest request) {

        // 校验签名的正确性
        boolean isCheck = CheckCore.checkParams(user, request);

        Map<String, Object> data = new HashMap<>();

        if (!isCheck) {
            data.put("code", 500);
            data.put("msg", "ERROR");
            data.put("data", "ERROR 错误信息返回");
            return data;
        }

        data.put("code", 200);
        data.put("msg", "POST");
        data.put("data", "POST - 你的名字：" + user);
        return data;
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> getDeleteId(@PathVariable String id) {
        Map<String, Object> data = new HashMap<>();

        data.put("code", 200);
        data.put("msg", "DELETE");
        data.put("data", "DELETE - 你的id：" + id);
        return data;
    }

    @PutMapping
    public Map<String, Object> getPutId(String id) {
        Map<String, Object> data = new HashMap<>();

        data.put("code", 200);
        data.put("msg", "PUT");
        data.put("data", "PUT - 你的id：" + id);
        return data;
    }

}
