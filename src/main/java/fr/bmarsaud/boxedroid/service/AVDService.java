package fr.bmarsaud.boxedroid.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import fr.bmarsaud.boxedroid.entity.ABI;
import fr.bmarsaud.boxedroid.entity.APILevel;
import fr.bmarsaud.boxedroid.entity.AVD;
import fr.bmarsaud.boxedroid.entity.Device;
import fr.bmarsaud.boxedroid.entity.Variant;
import fr.bmarsaud.boxedroid.entity.exception.AVDNameUsedException;
import fr.bmarsaud.boxedroid.entity.exception.DeviceNotAvailableException;
import fr.bmarsaud.boxedroid.entity.exception.SDKException;
import fr.bmarsaud.boxedroid.entity.exception.UnknownAVDException;
import fr.bmarsaud.boxedroid.program.AVDManager;
import fr.bmarsaud.boxedroid.program.Emulator;
import fr.bmarsaud.boxedroid.program.parser.DeviceListParser;
import fr.bmarsaud.boxedroid.util.IOUtils;

public class AVDService {
    private static final String AVD_MANAGER_PATH = "avdmanager";
    private static final String EMULATOR_PATH = "emulator/emulator";
    private static final String AVD_DIR = "avd";
    private static final String SYS_IMAGE_PREFIX = "Sdk/";

    private Logger logger = LoggerFactory.getLogger(AVDService.class);

    private AVDManager avdManager;
    private Emulator emulator;

    private List<Device> availableDevices;
    private List<AVD> avds;
    private String avdsPath;

    public AVDService(String sdkPath, String cmdlineToolsPath, String boxedroidPath) {
        this.avdManager = new AVDManager(Paths.get(sdkPath, cmdlineToolsPath, AVD_MANAGER_PATH).toAbsolutePath().toString());
        this.emulator = new Emulator(new File(sdkPath, EMULATOR_PATH).getAbsolutePath(), sdkPath);
        this.avdsPath = new File(boxedroidPath, AVD_DIR).getAbsolutePath();
        this.avds = new ArrayList<>();
        this.availableDevices = new ArrayList<>();
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
        loadAVDs();

        if(!isDeviceAvailable(device)) {
            throw new DeviceNotAvailableException(device);
        }

        if(avdExists(avdName)) {
            throw new AVDNameUsedException(avdName);
        }

        try {
            logger.info("Creating avd '" + avdName + "'...");
            Process process = avdManager.createAVD(avdName, apiLevel, abi,variant,device, avdsPath);
            process.waitFor();
        } catch(IOException | InterruptedException e) {
            e.printStackTrace();
        }

        fixAVDSysImagePath(avdName);
    }

    /**
     * Launch an AVD in an emulator
     * @param avdName The AVD name to launch
     * @throws SDKException
     */
    public void launch(String avdName) throws SDKException {
        loadAVDs();

        if(!avdExists(avdName)) {
            throw new UnknownAVDException(avdName);
        }

        try {
            logger.info("Launching avd '" + avdName + "'...");

            emulator.redirectInfo(true);
            emulator.redirectError(true);

            Process process = emulator.launch(avdName);
            process.waitFor();

            emulator.redirectInfo(false);
            emulator.redirectError(false);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create if needed an AVD and launch it
     * @param apiLevel The API level of the AVD
     * @param abi The ABI of the AVD
     * @param variant The variant of the AVD
     * @param device The device name of the AVD
     * @throws SDKException
     */
    public void createAndLaunch(APILevel apiLevel, ABI abi, Variant variant, String device) throws SDKException {
        loadAVDs();
        loadDevices();

        String avdName = "boxedroid_" + apiLevel.getCode() + "_" + abi.getId() + "_" + variant.getId();

        if(!avdExists(avdName)) {
            create(avdName, apiLevel, abi, variant, device);
        }

        launch(avdName);
    }

    /**
     * Load available devices list
     * @param force If true, deactivate lazy loading and force the list update
     */
    private void loadDevices(boolean force) {
        if(!force && availableDevices.size() > 0) return;

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
     * Lazy load available devices list
     */
    private void loadDevices() {
        loadDevices(false);
    }

    /**
     * Check if a given device name exists and is available
     * @param deviceName The device name to check
     * @return True if the device is available, false instead
     */
    private boolean isDeviceAvailable(String deviceName) {
        for(Device device : availableDevices) {
            if(device.getCode().equals(deviceName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Load avds from avds folder
     * @param force If true, deactivate lazy loading and force the list update
     */
    private void loadAVDs(boolean force) {
        if(!force && avds.size() > 0) return;

        avds.clear();
        new File(avdsPath).mkdirs();

        List<String> avdDirs = IOUtils.getFilesInDir(avdsPath, false);
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
     * Lazy load avds from avds folder
     */
    private void loadAVDs() {
        loadAVDs(false);
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

    /**
     * Fix the system image path of the AVD by removing the prefixed directory added in the path by
     * avdmanager
     * @param avdName The AVD name to fix
     * @throws UnknownAVDException
     */
    private void fixAVDSysImagePath(String avdName) throws UnknownAVDException {
        loadAVDs(true);
        Optional<AVD> optAVD = avds.stream().filter(avd -> avd.getName().equals(avdName)).findFirst();

        if(!optAVD.isPresent()) {
            throw new UnknownAVDException(avdName);
        }

        try {
            AVD avd = optAVD.get();
            avd.loadConfig();

            String sysImage = avd.getConfigValue(AVD.SYS_IMAGE_INI_KEY);
            if(sysImage.startsWith(SYS_IMAGE_PREFIX)) {
                logger.info("Fixing wrong system image path '" + sysImage + "'...");
                avd.editConfig(AVD.SYS_IMAGE_INI_KEY, sysImage.substring(SYS_IMAGE_PREFIX.length()));
                avd.save();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Device> getAvailableDevices() {
        return availableDevices;
    }
}
