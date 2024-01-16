package com.example.lock;

import com.easy.lock.support.EasyLockEvaluationContextPostProcessor;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

/**
 * <p> </p>
 *
 * @author zhouhongyin
 * @since 2024/1/16 11:52
 */
@Component
public class DemoEasyLockEvaluationContextPostProcessor implements EasyLockEvaluationContextPostProcessor {
    @Override
    public void postProcess(StandardEvaluationContext context) {
        context.setVariable("userid", "user111");
    }
}
