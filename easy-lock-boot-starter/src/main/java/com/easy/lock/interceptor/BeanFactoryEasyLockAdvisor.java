package com.easy.lock.interceptor;

import com.easy.lock.annotation.EasyLockOperationSource;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;

/**
 * <p> BeanFactoryEasyLockAdvisor </p>
 *
 * @author zhouhongyin
 * @since 2024/1/15 11:52
 */
public class BeanFactoryEasyLockAdvisor extends AbstractBeanFactoryPointcutAdvisor {

    private final EasyLockPointcut pointcut = new EasyLockPointcut();

    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }

    public void setEasyLockOperationSource(EasyLockOperationSource easyLockOperationSource) {
        pointcut.setEasyLockOperationSource(easyLockOperationSource);
    }
}
