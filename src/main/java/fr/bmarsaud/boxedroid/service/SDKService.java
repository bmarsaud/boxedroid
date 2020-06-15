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
import fr.bmarsaud.boxedroid.entity.exception.PackageNotAvailableException;
import fr.bmarsaud.boxedroid.entity.exception.SDKException;
import fr.bmarsaud.boxedroid.entity.packages.AvailablePackage;
import fr.bmarsaud.boxedroid.entity.packages.AvailableUpdates;
import fr.bmarsaud.boxedroid.entity.packages.InstalledPackage;
import fr.bmarsaud.boxedroid.program.SDKManager;
import fr.bmarsaud.boxedroid.program.observer.LicenceObserver;
import fr.bmarsaud.boxedroid.program.observer.ProgressObserver;
import fr.bmarsaud.boxedroid.program.parser.PackagesListParser;

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
     * @throws SDKException
     */
    public void install(APILevel apiLevel, ABI abi, Variant variant) throws SDKException {
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
     * @throws SDKException
     */
    public void install(APILevel apiLevel) throws SDKException {
        install(apiLevel, ABI.X86, Variant.DEFAULT);
    }

    /**
     * Install the system-images and platforms for a certain API level, ABI and variant
     * @param apiLevel The API level of the dependencies to install
     * @param abi The ABI of the dependencies to install
     * @param variant The variant of the dependencies to install
     * @throws IOException
     * @throws InterruptedException
     * @throws SDKException
     */
    private void installSDK(APILevel apiLevel, ABI abi, Variant variant) throws IOException, InterruptedException, SDKException {
        String systemImagePackage = SDKService.getSystemImagePackageName(apiLevel, abi, variant);
        String platformsPackage = SDKService.getPlatformsPackageName(apiLevel);

        Process process;
        LicenceObserver licenceObserver = new LicenceObserver();
        ProgressObserver progressObserver = new ProgressObserver((progress) -> {
            logger.info("Installing package " + progress.getProgress() + "% - " + progress.getCurrentStep());
        });

        sdkManager.onInfo(licenceObserver);
        sdkManager.onInfo(progressObserver);

        if(!isPackagedInstalled(systemImagePackage)) {
            if(isPackagedAvailable(systemImagePackage)) {
                logger.info("Installing package '" + systemImagePackage + "'...");
                process = sdkManager.installSystemImage(apiLevel, abi, variant);
                process.waitFor();
            } else {
                throw new PackageNotAvailableException(systemImagePackage);
            }
        } else {
            logger.info("Package '" + systemImagePackage + "' is already installed. Skipping...");
        }

        if(!isPackagedInstalled(platformsPackage)) {
            if(isPackagedAvailable(systemImagePackage)) {
                logger.info("Installing package '" + platformsPackage + "'...");
                process = sdkManager.installPlatforms(apiLevel);
                process.waitFor();
            } else {
                throw new PackageNotAvailableException(systemImagePackage);
            }
        } else {
            logger.info("Package '" + platformsPackage + "' is already installed. Skipping...");
        }

        sdkManager.unInfo(licenceObserver);
        sdkManager.unInfo(progressObserver);
    }

    /**
     * Install up-to-date platform-tools and emulator
     * @throws IOException
     * @throws InterruptedException
     */
    private void installTools() throws SDKException, IOException, InterruptedException {
        Process process;
        LicenceObserver licenceObserver = new LicenceObserver();
        ProgressObserver progressObserver = new ProgressObserver((progress) -> {
            logger.info("Installing tools " + progress.getProgress() + "% - " + progress.getCurrentStep());
        });

        sdkManager.onInfo(licenceObserver);
        sdkManager.onInfo(progressObserver);

        if(!isPackagedInstalled("platform-tools")) {
            logger.info("Installing package 'platform-tools'...");
            process = sdkManager.installPlatformsTools();
            process.waitFor();

            SDKException exception = sdkManager.getException();
            if(exception != null) {
                throw  exception;
            }
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

        sdkManager.unInfo(licenceObserver);
        sdkManager.unInfo(progressObserver);
    }

    /**
     * Load packages lists
     */
    private void loadPackages() {
        try {
            PackagesListParser listParser = new PackagesListParser();
            sdkManager.onInfo(listParser.getObserver());

            Process process = sdkManager.list();
            listParser.parse(process);

            sdkManager.unInfo(listParser.getObserver());

            availablePackages = listParser.getAvailableAvailablePackages();
            installedPackages = listParser.getInstalledPackages();
            availableUpdates = listParser.getAvailableUpdates();
        } catch(IOException | InterruptedException e) {
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

    /**
     * Return if a package is available or not
     * @param packagePath The package path to check
     * @return True if the packaged is available, False instead
     */
    private boolean isPackagedAvailable(String packagePath) {
        for(AvailablePackage availablePackage : availablePackages) {
            if(availablePackage.getPath().equalsIgnoreCase(packagePath)) {
                return true;
            }
        }

        return false;
    }
}
