package com.easy.lock.interceptor;


import com.easy.lock.annotation.EasyLockAttribute;
import com.easy.lock.annotation.EasyLockOperationSource;
import com.easy.lock.annotation.EasyLockProperties;
import com.easy.lock.core.support.KeyConvert;
import com.easy.lock.core.support.LockProcessor;
import com.easy.lock.support.EasyLockValueParser;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p> EasyLockInterceptor </p>
 *
 * @author zhouhongyin
 * @since 2024/1/15 13:58
 */
public class EasyLockInterceptor implements BeanFactoryInterceptor, Serializable {

    private final Log logger = LogFactory.getLog(getClass());

    private EasyLockProperties easyLockProperties;

    private EasyLockOperationSource easyLockOperationSource;

    private BeanFactory beanFactory;

    private EasyLockValueParser easyLockValueParser;

    private final Map<Class<?>, LockProcessor> lockProcessorMap = new ConcurrentHashMap<>();

    private LockProcessor defaultLockProcessor;

    private final Map<Class<?>, KeyConvert> keyConvertMap = new ConcurrentHashMap<>();

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

        EasyLockAttribute easyLockAttribute = easyLockOperationSource.computeEasyLockOperations(method, targetClass);
        evaluateEasyLockAttribute(easyLockAttribute, easyLockProperties);

        String lockKey = createLockKey(easyLockAttribute, method, args, targetClass);

        return lockProcessorMap.get(easyLockAttribute.getLockProcessor()).proceed(invoker, lockKey, easyLockAttribute.getLeaseTime());
    }

    private void evaluateEasyLockAttribute(EasyLockAttribute easyLockAttribute, EasyLockProperties easyLockProperties) {
        String prefix = easyLockAttribute.getPrefix();
        if (!StringUtils.hasText(prefix)) {
            easyLockAttribute.setPrefix(easyLockProperties.getPrefix());
        }

        String keySeparator = easyLockAttribute.getKeySeparator();
        if (!StringUtils.hasText(keySeparator)) {
            easyLockAttribute.setKeySeparator(easyLockProperties.getKeySeparator());
        }

        String leaseTime = easyLockAttribute.getLeaseTime();
        if (!StringUtils.hasText(leaseTime)) {
            easyLockAttribute.setLeaseTime(easyLockProperties.getLeaseTime());
        }

        Class<? extends LockProcessor> lockProcessor = easyLockAttribute.getLockProcessor();
        if (lockProcessor == null) {
            easyLockAttribute.setLockProcessor(defaultLockProcessor.getClass());
        }

    }

    private String createLockKey(EasyLockAttribute easyLockAttribute, Method method, Object[] args, Class<?> targetClass) {

        String prefix = easyLockAttribute.getPrefix();
        String convertKey = parserKeyConvert(easyLockAttribute.getKeyConvert(), args);
        String spELKey = parserSpEL(easyLockAttribute.getSpEl(), method, args, targetClass);
        return keyJoin(easyLockAttribute.getKeySeparator(), prefix, convertKey, spELKey);
    }

    private String parserKeyConvert(Class<? extends KeyConvert> clazz, Object[] args) {
        if (clazz == null) {
            return "";
        }
        KeyConvert keyConvert = keyConvertMap.get(clazz);
        return keyConvert.getKey(args);
    }

    private String parserSpEL(String expression, Method method, Object[] args, Class<?> targetClass) {
        if (!StringUtils.hasText(expression)) {
            return "";
        }

        return easyLockValueParser.processExpression(expression, method, args, targetClass);
    }

    private String keyJoin(String delimiter, String... keys) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < keys.length; i++) {
            String k = keys[i];
            if (StringUtils.hasText(k)) {
                stringBuilder.append(k).append(delimiter);
            }
        }

        if (stringBuilder.length() > 1) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            return stringBuilder.toString();
        }

        throw new RuntimeException("lock key is null!");
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
    public void afterSingletonsInstantiated() {
        this.easyLockProperties = beanFactory.getBean(EasyLockProperties.class);

        this.easyLockValueParser = beanFactory.getBean(EasyLockValueParser.class);

        this.defaultLockProcessor = beanFactory.getBean(this.easyLockProperties.getDefaultLockProcessorBeanName(), LockProcessor.class);

        if (beanFactory instanceof ListableBeanFactory) {
            ListableBeanFactory listableBeanFactory = (ListableBeanFactory) beanFactory;

            Map<String, LockProcessor> lockProcessors = listableBeanFactory.getBeansOfType(LockProcessor.class);
            for (LockProcessor lockProcessor : lockProcessors.values()) {
                this.lockProcessorMap.put(lockProcessor.getClass(), lockProcessor);
            }

            Map<String, KeyConvert> keyConverts = listableBeanFactory.getBeansOfType(KeyConvert.class);
            for (KeyConvert keyConvert : keyConverts.values()) {
                this.keyConvertMap.put(keyConvert.getClass(), keyConvert);
            }
        }
    }
}
