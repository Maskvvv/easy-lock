package com.example.service;

import com.easy.lock.annotation.EasyLock;
import com.easy.lock.redisson.RedissonLockProcessor;
import com.example.domain.DemoBody;
import com.example.domain.DemoParam;
import com.example.lock.DemoConvert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * <p> DemoService </p>
 *
 * @author zhouhongyin
 * @since 2024/1/16 11:25
 */
@Service
public class DemoService {

    private static final Logger logger = LoggerFactory.getLogger(DemoService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * common use for easy-lock
     */
    @EasyLock(keyConvert = DemoConvert.class, spEl = "{{#body.name}}-{{#body.age}}-{{#userid}}")
    public String addUser(DemoParam param, DemoBody body) {

        System.out.println("addUser do something.....");
        System.out.println("param: " + param);
        System.out.println("body: " + body);

        return "success";
    }

    /**
     * use for easy-lock Redisson Lock implement
     */
    @EasyLock(keyConvert = DemoConvert.class, spEl = "{{#body.name}}-{{#body.age}}-{{#userid}}", lockProcessor = RedissonLockProcessor.class, leaseTime = "30000")
    public String addUserWithRedisson(DemoParam param, DemoBody body) {

        System.out.println("addUser do something.....");
        System.out.println("param: " + param);
        System.out.println("body: " + body);

        return "success";
    }

    /**
     * easy-lock use with {@link Transactional}
     */
    @Transactional
    @EasyLock(keyConvert = DemoConvert.class, spEl = "{{#body.name}}-{{#body.age}}-{{#userid}}", lockProcessor = RedissonLockProcessor.class)
    public String addUserWithTransactional(DemoParam param, DemoBody body) {

        String userListSql = "SELECT * FROM `user` where id = ?;";

        List<Map<String, Object>> userList = jdbcTemplate.queryForList(userListSql, param.getId());
        System.out.println(userList);

        if (CollectionUtils.isEmpty(userList)) {
            String insertSql = String.format("INSERT INTO `test`.`user` (`id`, `name`, `age`) VALUES (%s, '%s', %s);", param.getId(), body.getName(), body.getAge());
            jdbcTemplate.execute(insertSql);

            logger.info("insert user success : param: {}, body: {} ", param, body);

        } else {
            logger.warn("user is exist: param: {}, body: {} ", param, body);
        }
        return "success";
    }

}
