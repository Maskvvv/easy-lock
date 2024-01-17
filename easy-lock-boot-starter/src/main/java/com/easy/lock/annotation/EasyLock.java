package com.easy.lock.annotation;

import com.easy.lock.core.support.KeyConvert;
import com.easy.lock.core.support.LockProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p> EasyLock annotation </p>
 *
 * @author zhouhongyin
 * @since 2023/3/2 17:55
 * @see EasyLockProperties
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EasyLock {

    /**
     *  default value is {@link EasyLockProperties#prefix}
     */
    String prefix() default "";

    Class<? extends KeyConvert>[] keyConvert() default {};

    String spEl() default "";

    /**
     *  default value is {@link EasyLockProperties#keySeparator}
     */
    String keySeparator() default "";

    /**
     *  default value is {@link EasyLockProperties#leaseTime}
     */
    String leaseTime() default "";

    /**
     *  default value is {@link EasyLockProperties#defaultLockProcessorBeanName}
     */
    Class<? extends LockProcessor>[] lockProcessor() default {};
}
