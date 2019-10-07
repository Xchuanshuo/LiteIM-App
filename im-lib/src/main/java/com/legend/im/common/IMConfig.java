package com.legend.im.common;

/**
 * @author Legend
 * @data by on 19-9-23.
 * @description IM相关的配置
 */
public class IMConfig {

    public static final int DEFAULT_IDLE_TIME = 30;

    public static final int DEFAULT_HEARTBEAT_INTERVAL = 10;

    public static final int DEFAULT_MAX_RETRY = 5;

    /**
     * 主机名
     */
    private String host;

    /**
     * 端口号
     */
    private int port;

    /**
     * 读写空闲时间
     */
    private int idleTime;

    /**
     * 心跳包发送间隔时间
     */
    private int heartbeatInterval;

    /**
     * 断线后最大重连次数
     */
    private int maxRetry;

    private IMConfig(String host, int port, int idleTime, int heartbeatInterval, int maxRetry) {
        this.host = host;
        this.port = port;
        this.idleTime = idleTime;
        this.heartbeatInterval = heartbeatInterval;
        this.maxRetry = maxRetry;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getIdleTime() {
        return idleTime;
    }

    public void setIdleTime(int idleTime) {
        this.idleTime = idleTime;
    }

    public int getHeartbeatInterval() {
        return heartbeatInterval;
    }

    public void setHeartbeatInterval(int heartbeatInterval) {
        this.heartbeatInterval = heartbeatInterval;
    }

    public int getMaxRetry() {
        return maxRetry;
    }

    public void setMaxRetry(int maxRetry) {
        this.maxRetry = maxRetry;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String host;
        private int port;
        private int idleTime = DEFAULT_IDLE_TIME;
        private int heartbeatInterval = DEFAULT_HEARTBEAT_INTERVAL;
        private int maxRetry = DEFAULT_MAX_RETRY;

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder idleTime(int idleTime) {
            this.idleTime = idleTime;
            return this;
        }

        public Builder heartbeatInterval(int heartbeatInterval) {
            this.heartbeatInterval = heartbeatInterval;
            return this;
        }

        public Builder maxRetry( int maxRetry) {
            this.maxRetry = maxRetry;
            return this;
        }

        public IMConfig build() {
            return new IMConfig(host, port, idleTime, heartbeatInterval, maxRetry);
        }
    }
}
