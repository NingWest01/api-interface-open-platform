package com.ning.demo;

import com.ning.sdk.client.MyClient;
import com.ning.sdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class DemoInterfaceApplicationTests {

    @Resource
    private MyClient myClient;

    @Test
    void contextLoads() {
        User user = new User();
        user.setName("ning");
        String myName = myClient.postJsonName(user);
        System.out.println(myName);
    }

}
