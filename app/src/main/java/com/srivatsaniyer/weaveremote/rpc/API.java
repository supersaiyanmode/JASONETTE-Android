package com.srivatsaniyer.weaveremote.rpc;

import java.util.List;
import java.util.Map;

/**
 * Created by thrustmaster on 1/21/18.
 */

public class API {
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

    public List<Argument> getArgs() {
        return args;
    }

    public void setArgs(List<Argument> args) {
        this.args = args;
    }

    public Map<String, Argument> getKwargs() {
        return kwargs;
    }

    public void setKwargs(Map<String, Argument> kwargs) {
        this.kwargs = kwargs;
    }

    private String name;
    private String description;
    private List<Argument> args;
    private Map<String, Argument> kwargs;
}
