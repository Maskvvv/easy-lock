package com.easy.lock.support;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Component;

/**
 * <p> Lock 单机实现(没有锁的过期时间) </p>
 *
 * @author zhouhongyin
 * @since 2023/12/21 10:59
 */
@Component
public class DefaultLockProcessor implements LockProcessor {

    @Override
    public Object proceed(MethodInvocation invocation, String key, long leaseTime) throws Throwable {
        synchronized (key.intern()) {
            return invocation.proceed();
        }
    }
}
