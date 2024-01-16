package com.easy.lock.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;

import java.lang.reflect.Method;
import java.util.List;

/**
 * <p> EasyLockValueParser </p>
 *
 * @author zhouhongyin
 * @since 2024/1/15 22:46
 */
public class EasyLockValueParser implements BeanFactoryAware {

    private final EasyLockExpressionEvaluator expressionEvaluator = new EasyLockExpressionEvaluator();
    private BeanFactory beanFactory;
    private List<EasyLockEvaluationContextPostProcessor> evaluationContextPostProcessors;


    public String processExpression(String expression, Method method, Object[] args, Class<?> targetClass) {
        if (expression == null || !expression.contains("{")) {
            return expression;
        }

        EvaluationContext evaluationContext = expressionEvaluator.createEvaluationContext(
                method, args, targetClass,
                beanFactory,
                evaluationContextPostProcessors);

        AnnotatedElementKey annotatedElementKey = new AnnotatedElementKey(method, targetClass);

        return expressionEvaluator.parseExpression(expression, annotatedElementKey, evaluationContext);
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public void setEvaluationContextPostProcessors(List<EasyLockEvaluationContextPostProcessor> evaluationContextPostProcessors) {
        this.evaluationContextPostProcessors = evaluationContextPostProcessors;
    }
}
