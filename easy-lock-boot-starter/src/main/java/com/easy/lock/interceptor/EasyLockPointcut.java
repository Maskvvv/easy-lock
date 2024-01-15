package com.easy.lock.interceptor;

import com.easy.lock.annotation.EasyLockOperationSource;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * <p> EasyLockPointcut  </p>
 *
 *
 * @author zhouhongyin
 * @since 2024/1/15 11:52
 */
public class EasyLockPointcut extends StaticMethodMatcherPointcut implements Serializable {

    private EasyLockOperationSource easyLockOperationSource;

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return easyLockOperationSource.computeEasyLockOperations(method, targetClass) != null;
    }

    public void setEasyLockOperationSource(EasyLockOperationSource easyLockOperationSource) {
        this.easyLockOperationSource = easyLockOperationSource;
    }
}
