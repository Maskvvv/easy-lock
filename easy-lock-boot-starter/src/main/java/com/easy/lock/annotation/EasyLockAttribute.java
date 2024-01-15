package com.easy.lock.annotation;

import com.easy.lock.support.KeyConvert;
import com.easy.lock.support.LockProcessor;

/**
 * <p> </p>
 *
 * @author zhouhongyin
 * @since 2024/1/15 14:28
 */
public class EasyLockAttribute {

    private String prefix;

    private Class<? extends KeyConvert>[] keyConvert;

    private String spEl;

    private String keySeparator;

    private long leaseTime;

    private Class<? extends LockProcessor>[] lockProcessor;

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String prefix;
        private Class<? extends KeyConvert>[] keyConvert;
        private String spEl;
        private String keySeparator;
        private long leaseTime;
        private Class<? extends LockProcessor>[] lockProcessor;

        private Builder() {
        }

        public Builder prefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public Builder keyConvert(Class<? extends KeyConvert>[] keyConvert) {
            this.keyConvert = keyConvert;
            return this;
        }

        public Builder spEl(String spEl) {
            this.spEl = spEl;
            return this;
        }

        public Builder keySeparator(String keySeparator) {
            this.keySeparator = keySeparator;
            return this;
        }

        public Builder leaseTime(long leaseTime) {
            this.leaseTime = leaseTime;
            return this;
        }

        public Builder lockProcessor(Class<? extends LockProcessor>[] lockProcessor) {
            this.lockProcessor = lockProcessor;
            return this;
        }

        public EasyLockAttribute build() {
            EasyLockAttribute easyLockAttribute = new EasyLockAttribute();
            easyLockAttribute.lockProcessor = this.lockProcessor;
            easyLockAttribute.prefix = this.prefix;
            easyLockAttribute.keyConvert = this.keyConvert;
            easyLockAttribute.keySeparator = this.keySeparator;
            easyLockAttribute.leaseTime = this.leaseTime;
            easyLockAttribute.spEl = this.spEl;
            return easyLockAttribute;
        }
    }
}
