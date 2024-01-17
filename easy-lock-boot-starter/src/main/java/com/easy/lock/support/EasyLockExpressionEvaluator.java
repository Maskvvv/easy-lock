package com.easy.lock.support;

import com.easy.lock.core.support.EasyLockEvaluationContextPostProcessor;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p> EasyLockExpressionEvaluator </p>
 *
 * @author zhouhongyin
 * @since 2024/1/15 22:36
 */
public class EasyLockExpressionEvaluator extends CachedExpressionEvaluator {

    private final Map<AnnotatedElementKey, Method> targetMethodCache = new ConcurrentHashMap<>(64);
    private final Map<ExpressionKey, Expression> expressionCache = new ConcurrentHashMap<>(64);
    private final TemplateParserContext templateParserContext = new TemplateParserContext("{{", "}}");

    public String parseExpression(String conditionExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
        return getExpression(this.expressionCache, methodKey, conditionExpression).getValue(evalContext, String.class);
    }

    /**
     * Create an {@link EvaluationContext}.
     *
     * @param method      the method
     * @param args        the method arguments
     * @param targetClass the target class
     * @param beanFactory Spring beanFactory
     * @return the evaluation context
     */
    public EvaluationContext createEvaluationContext(Method method,
                                                     Object[] args,
                                                     Class<?> targetClass,
                                                     BeanFactory beanFactory,
                                                     List<EasyLockEvaluationContextPostProcessor> evaluationContextPostProcessors) {
        Method targetMethod = getTargetMethod(targetClass, method);
        EasyLockEvaluationContext evaluationContext = new EasyLockEvaluationContext(
                null, targetMethod, args, getParameterNameDiscoverer());
        if (beanFactory != null) {
            evaluationContext.setBeanResolver(new BeanFactoryResolver(beanFactory));
        }
        if (!CollectionUtils.isEmpty(evaluationContextPostProcessors)) {
            for (EasyLockEvaluationContextPostProcessor contextPostProcessor : evaluationContextPostProcessors) {
                contextPostProcessor.postProcess(evaluationContext);
            }
        }

        return evaluationContext;
    }

    private Method getTargetMethod(Class<?> targetClass, Method method) {
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, targetClass);
        return targetMethodCache.computeIfAbsent(methodKey, k -> AopUtils.getMostSpecificMethod(method, targetClass));
    }

    @Override
    protected Expression parseExpression(String expression) {
        return getParser().parseExpression(expression, templateParserContext);
    }
}
