package fr.bmarsaud.boxedroid.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import fr.bmarsaud.boxedroid.entity.ABI;
import fr.bmarsaud.boxedroid.entity.APILevel;
import fr.bmarsaud.boxedroid.entity.Device;
import fr.bmarsaud.boxedroid.entity.Variant;
import fr.bmarsaud.boxedroid.entity.exception.DeviceNotAvailableException;
import fr.bmarsaud.boxedroid.entity.exception.SDKException;
import fr.bmarsaud.boxedroid.program.AVDManager;
import fr.bmarsaud.boxedroid.util.DeviceListParser;

public class AVDService {
    private List<Device> availableDevices;

    private static final String AVD_MANAGER_PATH = "tools/bin/avdmanager";

    private AVDManager avdManager;

    public AVDService(String sdkPath) {
        this.avdManager = new AVDManager(new File(sdkPath, AVD_MANAGER_PATH).getAbsolutePath());
    }

    /**
     * Create an AVD given a name, an API level, an ABI, a variant and a device name
     * @param avdName The AVD name
     * @param apiLevel The API level of the AVD
     * @param abi The ABI of the AVD
     * @param variant The variant of the AVD
     * @param device The device name of the AVD
     * @throws SDKException
     */
    public void create(String avdName, APILevel apiLevel, ABI abi, Variant variant, String device) throws SDKException {
        loadDevices();

        if(isDeviceAvailable(device)) {
            //TODO: check if the avd exists
        } else {
            throw new DeviceNotAvailableException(device);
        }
    }

    /**
     * Load available devices list
     */
    private void loadDevices() {
        try {
            DeviceListParser deviceListParser = new DeviceListParser();

            avdManager.onInfo(deviceListParser.getObserver());

            Process process = avdManager.listDevices();
            deviceListParser.parse(process);

            avdManager.unInfo(deviceListParser.getObserver());

            availableDevices = deviceListParser.getDevices();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if a give device name exists and is available
     * @param deviceName The device name to check
     * @return True if the device is available, false instead
     */
    private boolean isDeviceAvailable(String deviceName) {
        for(Device device : availableDevices) {
            if(device.getName().equals(deviceName)) {
                return true;
            }
        }
        return false;
    }
}
