package com.easy.lock.core.support;


import org.aopalliance.intercept.MethodInvocation;

/**
 * <p> Lock Processor </p>
 *
 * @author zhouhongyin
 * @since 2023/12/21 10:52
 */
public interface LockProcessor {

    Object proceed(MethodInvocation invocation, String key, String leaseTime) throws Throwable;

}
