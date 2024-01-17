package com.easy.lock.redisson;

import com.easy.lock.core.LockProcessor;
import org.aopalliance.intercept.MethodInvocation;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * <p> Lock Processor Redisson 实现 </p>
 *
 * @author zhouhongyin
 * @since 2023/12/21 10:59
 */
public class RedissonLockProcessor implements LockProcessor {

    public static final String BEAN_NAME = "redissonLockProcessor";

    private final RedissonClient redisson;

    public RedissonLockProcessor(RedissonClient redisson) {
        this.redisson = redisson;
    }

    @Override
    public Object proceed(MethodInvocation invocation, String key, String leaseTime) throws Throwable {
        RLock lock = null;
        try {
            lock = redisson.getLock(key);
            lock.lock(Long.parseLong(leaseTime), TimeUnit.MILLISECONDS);

            return invocation.proceed();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (lock != null && lock.isLocked()) {
                lock.unlock();
            }
        }
    }
}
