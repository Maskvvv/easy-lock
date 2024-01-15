package com.easy.lock.annotation;

import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ClassUtils;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * <p>  </p>
 *
 * @author zhouhongyin
 * @since 2024/1/15 11:49
 */
public class EasyLockOperationSource {


    public EasyLockAttribute computeEasyLockOperations(Method method, Class<?> targetClass) {
        // Don't allow no-public methods as required.
        if (!Modifier.isPublic(method.getModifiers())) {
            return null;
        }

        // The method may be on an interface, but we need attributes from the target class.
        // If the target class is null, the method will be unchanged.
        Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
        // If we are dealing with method with generic parameters, find the original method.
        specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);

        // First try is the method in the target class.
        return parseEasyLockAnnotation(specificMethod);
    }


    private EasyLockAttribute parseEasyLockAnnotation(AnnotatedElement ae) {
        EasyLock easyLock = AnnotatedElementUtils.findMergedAnnotation(ae, EasyLock.class);

        if (easyLock != null) {
            EasyLockAttribute easyLockAttribute = EasyLockAttribute.builder()
                    .prefix(easyLock.prefix())
                    .keyConvert(easyLock.keyConvert())
                    .spEl(easyLock.spEl())
                    .keySeparator(easyLock.keySeparator())
                    .leaseTime(easyLock.leaseTime())
                    .lockProcessor(easyLock.lockProcessor())
                    .build();
            return easyLockAttribute;
        }
        return null;
    }

}
