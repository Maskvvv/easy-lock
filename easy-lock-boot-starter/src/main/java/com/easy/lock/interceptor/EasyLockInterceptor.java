package com.easy.lock.interceptor;


import com.easy.lock.annotation.EasyLockAttribute;
import com.easy.lock.annotation.EasyLockOperationSource;
import com.easy.lock.support.EasyLockEvaluationContextPostProcessor;
import com.easy.lock.support.LockProcessor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p>  </p>
 *
 * @author zhouhongyin
 * @since 2024/1/15 13:58
 */
public class EasyLockInterceptor implements BeanFactoryInterceptor, Serializable, ApplicationContextAware {

    private EasyLockOperationSource easyLockOperationSource;


    @Nullable
    private BeanFactory beanFactory;

    private Map<Class<?>, LockProcessor> lockProcessors = new ConcurrentHashMap<>();

    private List<EasyLockEvaluationContextPostProcessor> easyLockEvaluationContextPostProcessors = new CopyOnWriteArrayList<>();

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        return execute(invocation, invocation.getThis(), method, invocation.getArguments());
    }

    private Object execute(MethodInvocation invoker, Object target, Method method, Object[] args) throws Throwable {
        //代理不拦截
        if (AopUtils.isAopProxy(target)) {
            return invoker.proceed();
        }

        Class<?> targetClass = getTargetClass(target);
        Object ret = null;

        Map<String, String> functionNameAndReturnMap = new HashMap<>();
        try {
            EasyLockAttribute easyLockAttribute = easyLockOperationSource.computeEasyLockOperations(method, targetClass);





            operations = logRecordOperationSource.computeLogRecordOperations(method, targetClass);
            List<String> spElTemplates = getBeforeExecuteFunctionTemplate(operations);
            functionNameAndReturnMap = processBeforeExecuteFunctionTemplate(spElTemplates, targetClass, method, args);
        } catch (Exception e) {
            log.error("log record parse before function exception", e);
        } finally {
            stopWatch.stop();
        }

        try {
            ret = invoker.proceed();
            methodExecuteResult.setResult(ret);
            methodExecuteResult.setSuccess(true);
        } catch (Exception e) {
            methodExecuteResult.setSuccess(false);
            methodExecuteResult.setThrowable(e);
            methodExecuteResult.setErrorMsg(e.getMessage());
        }
        stopWatch.start(MONITOR_TASK_AFTER_EXECUTE);
        try {
            if (!CollectionUtils.isEmpty(operations)) {
                recordExecute(methodExecuteResult, functionNameAndReturnMap, operations);
            }
        } catch (Exception t) {
            log.error("log record parse exception", t);
            throw t;
        } finally {
            LogRecordContext.clear();
            stopWatch.stop();
            try {
                logRecordPerformanceMonitor.print(stopWatch);
            } catch (Exception e) {
                log.error("execute exception", e);
            }
        }

        if (methodExecuteResult.getThrowable() != null) {
            throw methodExecuteResult.getThrowable();
        }
        return ret;
    }


    private Class<?> getTargetClass(Object target) {
        return AopProxyUtils.ultimateTargetClass(target);
    }


    public void setEasyLockOperationSource(EasyLockOperationSource easyLockOperationSource) {
        this.easyLockOperationSource = easyLockOperationSource;
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        if (beanFactory instanceof ListableBeanFactory) {
            ListableBeanFactory defaultBeanFactory = (ListableBeanFactory) beanFactory;
            Map<String, LockProcessor> beansOfType = defaultBeanFactory.getBeansOfType(LockProcessor.class);
            for (LockProcessor lockProcessor : beansOfType.values()) {



            }

        }

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        applicationContext.
    }
}
