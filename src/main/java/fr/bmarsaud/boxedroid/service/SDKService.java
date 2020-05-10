package fr.bmarsaud.boxedroid.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.bmarsaud.boxedroid.entity.ABI;
import fr.bmarsaud.boxedroid.entity.APILevel;
import fr.bmarsaud.boxedroid.entity.Variant;
import fr.bmarsaud.boxedroid.entity.packages.AvailablePackage;
import fr.bmarsaud.boxedroid.entity.packages.AvailableUpdates;
import fr.bmarsaud.boxedroid.entity.packages.InstalledPackage;
import fr.bmarsaud.boxedroid.program.SDKManager;
import fr.bmarsaud.boxedroid.util.PackagesListParser;

public class SDKService {
    private Logger logger = LoggerFactory.getLogger(SDKService.class);

    private List<AvailablePackage> availablePackages;
    private List<InstalledPackage> installedPackages;
    private List<AvailableUpdates> availableUpdates;

    private static final String SDK_MANAGER_PATH = "tools/bin/sdkmanager";

    private SDKManager sdkManager;

    public SDKService(String sdkPath) {
        this.sdkManager = new SDKManager(new File(sdkPath, SDK_MANAGER_PATH).getAbsolutePath(), sdkPath);
        this.availablePackages = new ArrayList<>();
        this.installedPackages = new ArrayList<>();
        this.availableUpdates = new ArrayList<>();
    }

    /**
     * Return the system-images package name for a certain API level, ABI and variant
     * @param apiLevel The API level
     * @param abi The API
     * @param variant The variant
     * @return The package name
     */
    public static String getSystemImagePackageName(APILevel apiLevel, ABI abi, Variant variant) {
        return "system-images;android-" + apiLevel.getCode() + ";" + variant.getId() +";" + abi.getId();
    }

    /**
     * Return the platforms package name for a certain API level, ABI and variant
     * @param apiLevel The API level
     * @return The package name
     */
    public static String getPlatformsPackageName(APILevel apiLevel) {
        return "platforms;android-" + apiLevel.getCode();
    }

    /**
     * Install all needed dependencies for a certain API level, ABI and variant
     * @param apiLevel The API level of the dependencies to install
     * @param abi The ABI of the dependencies to install
     * @param variant The variant of the dependencies to install
     */
    public void install(APILevel apiLevel, ABI abi, Variant variant) {
        loadPackages();
        try {
            installTools();
            installSDK(apiLevel, abi, variant);
        } catch(IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Install all needed dependencies for a certain API level, x86 ABI and default variant
     * @param apiLevel The API level of the dependencies to install
     */
    public void install(APILevel apiLevel) {
        install(apiLevel, ABI.X86, Variant.DEFAULT);
    }

    /**
     * Install the system-images and platforms for a certain API level, ABI and variant
     * @param apiLevel The API level of the dependencies to install
     * @param abi The ABI of the dependencies to install
     * @param variant The variant of the dependencies to install
     * @throws IOException
     * @throws InterruptedException
     */
    private void installSDK(APILevel apiLevel, ABI abi, Variant variant) throws IOException, InterruptedException {
        String systemImagePackage = SDKService.getSystemImagePackageName(apiLevel, abi, variant);
        String platformsPackage = SDKService.getPlatformsPackageName(apiLevel);

        Process process;

        if(!isPackagedInstalled(systemImagePackage)) {
            logger.info("Installing package '" + systemImagePackage + "'...");
            process = sdkManager.installSystemImage(apiLevel, abi, variant);
            process.waitFor();
        } else {
            logger.info("Package '" + systemImagePackage + "' is already installed. Skipping...");
        }

        if(!isPackagedInstalled(platformsPackage)) {
            logger.info("Installing package '" + platformsPackage + "'...");
            process = sdkManager.installPlatforms(apiLevel);
            process.waitFor();
        } else {
            logger.info("Package '" + platformsPackage + "' is already installed. Skipping...");
        }
    }

    /**
     * Install up-to-date platform-tools and emulator
     * @throws IOException
     * @throws InterruptedException
     */
    private void installTools() throws IOException, InterruptedException {
        Process process;

        if(!isPackagedInstalled("platform-tools")) {
            logger.info("Installing package 'platform-tools'...");
            process = sdkManager.installPlatformsTools();
            process.waitFor();
        } else {
            logger.info("Package 'platform-tools' already installed. Skipping...");
        }

        if(!isPackagedInstalled("emulator")) {
            logger.info("Installing package 'emulator'...");
            process = sdkManager.installEmulator();
            process.waitFor();
        } else {
            logger.info("Package 'emulator' already installed. Skipping...");
        }
    }

    /**
     * Load packages lists
     */
    private void loadPackages() {
        try {
            PackagesListParser listParser = new PackagesListParser();
            Process process = sdkManager.list();
            listParser.parse(process);

            availablePackages = listParser.getAvailableAvailablePackages();
            installedPackages = listParser.getInstalledPackages();
            availableUpdates = listParser.getAvailableUpdates();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return if a package is already installed or not
     * @param packagePath The package path to check
     * @return True if the packaged is installed, False instead
     */
    private boolean isPackagedInstalled(String packagePath) {
        for(InstalledPackage installedPackage : installedPackages) {
            if(installedPackage.getPath().equalsIgnoreCase(packagePath)) {
                return true;
            }
        }

        return false;
    }
}
