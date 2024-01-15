package com.easy.lock.support;

import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;

/**
 * <p> EasyLockEvaluationContext </p>
 *
 * @author zhouhongyin
 * @since 2024/1/15 22:35
 */
public class EasyLockEvaluationContext extends MethodBasedEvaluationContext {

    public EasyLockEvaluationContext(Object rootObject, Method method, Object[] arguments,
                                     ParameterNameDiscoverer parameterNameDiscoverer) {
        super(rootObject, method, arguments, parameterNameDiscoverer);

    }
}
