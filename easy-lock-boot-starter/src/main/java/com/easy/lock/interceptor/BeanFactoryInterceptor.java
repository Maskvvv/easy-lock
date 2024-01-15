package com.easy.lock.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;

/**
 * <p> </p>
 *
 * @author zhouhongyin
 * @since 2024/1/15 16:51
 */
public interface BeanFactoryInterceptor extends MethodInterceptor, BeanFactoryAware, InitializingBean {

}
