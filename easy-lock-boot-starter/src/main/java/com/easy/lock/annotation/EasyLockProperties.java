package com.easy.lock.annotation;


import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * <p>  </p>
 *
 * @author zhouhongyin
 * @since 2024/1/15 14:08
 */
@ConfigurationProperties(prefix = "easy-lock")
public class EasyLockProperties {

    private String prefix;

    private String keySeparator = ":";

    private long leaseTime = 30 * 1000;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getKeySeparator() {
        return keySeparator;
    }

    public void setKeySeparator(String keySeparator) {
        this.keySeparator = keySeparator;
    }

    public long getLeaseTime() {
        return leaseTime;
    }

    public void setLeaseTime(long leaseTime) {
        this.leaseTime = leaseTime;
    }
}
