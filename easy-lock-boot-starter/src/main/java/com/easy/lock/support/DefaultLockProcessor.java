package com.easy.lock.support;

import org.aopalliance.intercept.MethodInvocation;

/**
 * <p> Lock 单机实现(没有锁的过期时间) </p>
 *
 * @author zhouhongyin
 * @since 2023/12/21 10:59
 */
public class DefaultLockProcessor implements LockProcessor {

    public static final String BEAN_NAME = "defaultLockProcessor";

    @Override
    public Object proceed(MethodInvocation invocation, String key, long leaseTime) throws Throwable {
        synchronized (key.intern()) {
            return invocation.proceed();
        }
    }
}
