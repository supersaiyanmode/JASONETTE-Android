package com.srivatsaniyer.weaveremote.messaging;

/**
 * Created by sriiyer on 9/23/17.
 */

public class ServerSpecification {
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

    private String host;
    private int port;
}
