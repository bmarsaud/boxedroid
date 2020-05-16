package fr.bmarsaud.boxedroid.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.bmarsaud.boxedroid.entity.ABI;
import fr.bmarsaud.boxedroid.entity.APILevel;
import fr.bmarsaud.boxedroid.entity.AVD;
import fr.bmarsaud.boxedroid.entity.Device;
import fr.bmarsaud.boxedroid.entity.Variant;
import fr.bmarsaud.boxedroid.entity.exception.AVDNameUsedException;
import fr.bmarsaud.boxedroid.entity.exception.DeviceNotAvailableException;
import fr.bmarsaud.boxedroid.entity.exception.SDKException;
import fr.bmarsaud.boxedroid.program.AVDManager;
import fr.bmarsaud.boxedroid.util.DeviceListParser;
import fr.bmarsaud.boxedroid.util.IOUtils;

public class AVDService {
    private Logger logger = LoggerFactory.getLogger(AVDService.class);
    private List<Device> availableDevices;
    private List<AVD> avds;

    private static final String AVD_MANAGER_PATH = "tools/bin/avdmanager";
    private static final String AVD_DIR = "avd";

    private AVDManager avdManager;
    private String avdsPath;

    public AVDService(String sdkPath, String boxedroidPath) {
        this.avdManager = new AVDManager(new File(sdkPath, AVD_MANAGER_PATH).getAbsolutePath());
        this.avdsPath = new File(boxedroidPath, AVD_DIR).getAbsolutePath();
        this.avds = new ArrayList<>();
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
    public void create(String avdName, APILevel apiLevel, ABI abi, Variant variant, String device) throws SDKException, IOException, InterruptedException {
        loadDevices();
        loadAVDs();

        if(!isDeviceAvailable(device)) {
            throw new DeviceNotAvailableException(device);
        }

        if(avdExists(avdName)) {
            throw new AVDNameUsedException(avdName);
        }

        Process process = avdManager.createAVD(avdName, apiLevel, abi,variant,device, avdsPath);
        process.waitFor();
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
     * Check if a given device name exists and is available
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

    /**
     * Load avds from Boxedroid folder
     */
    private void loadAVDs() {
        avds.clear();

        List<String> avdDirs = IOUtils.getFilesInDir(avdsPath);
        for(String avdDir : avdDirs) {
            String dirName = new File(avdDir).getName();

            try {
                AVD avd = new AVD(dirName, avdDir);
                avd.loadConfig();
                avds.add(avd);
            } catch(IOException e) {
                logger.error("Error loading avd \"" + avdDir + "\"");
            }
        }
    }

    /**
     * Check if a given avd name is already used by another avd
     * @param name The avd name to check
     * @return True if the name is already used, false instead
     */
    private boolean avdExists(String name) {
        for(AVD avd : avds) {
            if(avd.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }
}
