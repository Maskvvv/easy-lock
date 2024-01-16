package com.example.lock;

import com.easy.lock.support.LockProcessor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * <p> </p>
 *
 * @author zhouhongyin
 * @since 2024/1/16 11:49
 */
@Primary
@Component
public class DemoLockProcessor implements LockProcessor {

    @Override
    public Object proceed(MethodInvocation invocation, String key, long leaseTime) throws Throwable {
        System.out.println("lock key is :"+ key);
        System.out.println("leaseTime is :"+ leaseTime);
        return invocation.proceed();
    }
}
