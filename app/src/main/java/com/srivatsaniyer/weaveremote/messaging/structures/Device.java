package com.srivatsaniyer.piremote.messaging.structures;

import java.util.List;

/**
 * Created by thrustmaster on 9/30/17.
 */

public class Device {
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceCommandsQueue() {
        return deviceCommandsQueue;
    }

    public void setDeviceCommandsQueue(String deviceCommandsQueue) {
        this.deviceCommandsQueue = deviceCommandsQueue;
    }

    public List<DeviceCommand> getDeviceCommands() {
        return deviceCommands;
    }

    public void setDeviceCommands(List<DeviceCommand> deviceCommands) {
        this.deviceCommands = deviceCommands;
    }

    private String deviceId;
    private String deviceCommandsQueue;
    private List<DeviceCommand> deviceCommands;
}
