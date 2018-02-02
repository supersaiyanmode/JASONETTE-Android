package com.srivatsaniyer.weaveremote.messaging.structures;

/**
 * Created by thrustmaster on 10/11/17.
 */

public class ExecuteDeviceCommand {
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCommandId() {
        return commandId;
    }

    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }

    private String deviceId;
    private String commandId;
}
