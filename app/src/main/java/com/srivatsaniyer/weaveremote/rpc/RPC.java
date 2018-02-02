package com.srivatsaniyer.piremote.rpc;

import java.util.Map;

/**
 * Created by thrustmaster on 1/21/18.
 */

public class RPC {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, API> getApis() {
        return apis;
    }

    public void setApis(Map<String, API> apis) {
        this.apis = apis;
    }

    private String name;
    private String description;
    private Map<String, API> apis;
}
