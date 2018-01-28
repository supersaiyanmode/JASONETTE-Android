package com.srivatsaniyer.piremote.rpc;

/**
 * Created by thrustmaster on 1/21/18.
 */

public class Argument {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String name;
    private String description;
    private String type;
}
