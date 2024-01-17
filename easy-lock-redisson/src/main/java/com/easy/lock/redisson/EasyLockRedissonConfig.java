package com.easy.lock.redisson;

import com.easy.lock.core.LockProcessor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p> EasyLockRedissonConfig </p>
 *
 * @author zhouhongyin
 * @since 2024/1/17 11:33
 */
@Configuration
@ConditionalOnProperty(prefix = EasyLockRedissonConstants.PREFIX, name = EasyLockRedissonConstants.ENABLE)
@EnableConfigurationProperties(EasyLockRedissonProperties.class)
public class EasyLockRedissonConfig {


    @Bean
    @ConditionalOnMissingBean(RedissonClient.class)
    public RedissonClient easyLockRedisson(EasyLockRedissonProperties easyLockRedissonProperties) {
        EasyLockRedissonProperties.Type type = easyLockRedissonProperties.getType();

        Config config = new Config();
        if (EasyLockRedissonProperties.Type.SINGLE_SERVER.equals(type)) {
            // 单机模式
            EasyLockRedissonProperties.SingleServer singleServer = easyLockRedissonProperties.getSingleServer();
            config.useSingleServer()
                    .setAddress(singleServer.getAddress())
                    .setPassword(singleServer.getPassword());

        } else if (EasyLockRedissonProperties.Type.CLUSTER_SERVERS.equals(type)) {
            // 集群模式
            EasyLockRedissonProperties.ClusterServers clusterServers = easyLockRedissonProperties.getClusterServers();
            ClusterServersConfig clusterServersConfig = config.useClusterServers();
            clusterServersConfig.setNodeAddresses(clusterServers.getNodeAddress());
            clusterServersConfig.setPassword(clusterServers.getPassword());
        }
        return Redisson.create(config);
    }


    @ConditionalOnBean(RedissonClient.class)
    @Bean(RedissonLockProcessor.BEAN_NAME)
    public LockProcessor redissonLockProcessor(RedissonClient redissonClient) {
        return new RedissonLockProcessor(redissonClient);
    }

}
