package com.easy.lock.annotation;

import com.easy.lock.core.LockProcessor;
import com.easy.lock.support.KeyConvert;

/**
 * <p> </p>
 *
 * @author zhouhongyin
 * @since 2024/1/15 14:28
 */
public class EasyLockAttribute {

    private String prefix;

    private Class<? extends KeyConvert> keyConvert;

    private String spEl;

    private String keySeparator;

    private String leaseTime;

    private Class<? extends LockProcessor> lockProcessor;

    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {
        private String prefix;
        private Class<? extends KeyConvert> keyConvert;
        private String spEl;
        private String keySeparator;
        private String leaseTime;
        private Class<? extends LockProcessor> lockProcessor;

        private Builder() {
        }

        public static Builder anEasyLockAttribute() {
            return new Builder();
        }

        public Builder prefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public Builder keyConvert(Class<? extends KeyConvert> keyConvert) {
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

        public Builder leaseTime(String leaseTime) {
            this.leaseTime = leaseTime;
            return this;
        }

        public Builder lockProcessor(Class<? extends LockProcessor> lockProcessor) {
            this.lockProcessor = lockProcessor;
            return this;
        }

        public EasyLockAttribute build() {
            EasyLockAttribute easyLockAttribute = new EasyLockAttribute();
            easyLockAttribute.spEl = this.spEl;
            easyLockAttribute.lockProcessor = this.lockProcessor;
            easyLockAttribute.leaseTime = this.leaseTime;
            easyLockAttribute.keySeparator = this.keySeparator;
            easyLockAttribute.prefix = this.prefix;
            easyLockAttribute.keyConvert = this.keyConvert;
            return easyLockAttribute;
        }
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Class<? extends KeyConvert> getKeyConvert() {
        return keyConvert;
    }

    public void setKeyConvert(Class<? extends KeyConvert> keyConvert) {
        this.keyConvert = keyConvert;
    }

    public String getSpEl() {
        return spEl;
    }

    public void setSpEl(String spEl) {
        this.spEl = spEl;
    }

    public String getKeySeparator() {
        return keySeparator;
    }

    public void setKeySeparator(String keySeparator) {
        this.keySeparator = keySeparator;
    }

    public String getLeaseTime() {
        return leaseTime;
    }

    public void setLeaseTime(String leaseTime) {
        this.leaseTime = leaseTime;
    }

    public Class<? extends LockProcessor> getLockProcessor() {
        return lockProcessor;
    }

    public void setLockProcessor(Class<? extends LockProcessor> lockProcessor) {
        this.lockProcessor = lockProcessor;
    }
}
