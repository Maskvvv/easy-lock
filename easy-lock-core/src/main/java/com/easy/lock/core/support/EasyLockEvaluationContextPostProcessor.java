package com.easy.lock.core.support;

import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * <p> EvaluationContext </p>
 *
 * @author zhouhongyin
 * @since 2024/1/2 14:58
 */
public interface EasyLockEvaluationContextPostProcessor {

    void postProcess(StandardEvaluationContext context);

}
