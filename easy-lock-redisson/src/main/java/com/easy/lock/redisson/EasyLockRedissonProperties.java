package com.easy.lock.redisson;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * <p> EasyLockRedissonProperties </p>
 *
 * @author zhouhongyin
 * @since 2024/1/17 11:03
 */
@ConfigurationProperties(prefix = EasyLockRedissonConstants.PREFIX)
public class EasyLockRedissonProperties {

    private boolean enable;

    private Type type = Type.SINGLE_SERVER;

    private SingleServer singleServer;

    private ClusterServers clusterServers;

    public enum Type {
        SINGLE_SERVER,
        CLUSTER_SERVERS
    }


    public static class SingleServer {

        private String address;

        private String password;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class ClusterServers {

        private List<String> nodeAddress;

        private String password;

        public List<String> getNodeAddress() {
            return nodeAddress;
        }

        public void setNodeAddress(List<String> nodeAddress) {
            this.nodeAddress = nodeAddress;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public boolean isEnable() {
        return enable;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public SingleServer getSingleServer() {
        return singleServer;
    }

    public void setSingleServer(SingleServer singleServer) {
        this.singleServer = singleServer;
    }

    public ClusterServers getClusterServers() {
        return clusterServers;
    }

    public void setClusterServers(ClusterServers clusterServers) {
        this.clusterServers = clusterServers;
    }
}
