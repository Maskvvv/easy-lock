package com.example.service;

import com.easy.lock.annotation.EasyLock;
import com.example.domain.DemoBody;
import com.example.domain.DemoParam;
import com.example.lock.DemoConvert;
import org.springframework.stereotype.Service;

/**
 * <p> </p>
 *
 * @author zhouhongyin
 * @since 2024/1/16 11:25
 */
@Service
public class DemoService {

    @EasyLock(keyConvert = DemoConvert.class, spEl = "{{#body.name}}-{{#body.age}}-{{#userid}}")
    public String addUser(DemoParam param, DemoBody body) {

        System.out.println("do something.....");
        System.out.println("param: " + param);
        System.out.println("body: " + body);

        return "success";
    }

}
