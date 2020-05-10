package fr.bmarsaud.boxedroid.service;

import java.io.File;
import java.io.IOException;

import fr.bmarsaud.boxedroid.entity.ABI;
import fr.bmarsaud.boxedroid.entity.APILevel;
import fr.bmarsaud.boxedroid.entity.Variant;
import fr.bmarsaud.boxedroid.program.SDKManager;

public class SDKService {
    private static final String SDK_MANAGER_PATH = "tools/bin/sdkmanager";
    private SDKManager sdkManager;

    public SDKService(String sdkPath) {
        this.sdkManager = new SDKManager(new File(sdkPath, SDK_MANAGER_PATH).getAbsolutePath(), sdkPath);
    }

    /**
     * Install all needed dependencies for a certain API level, ABI and variant
     * @param apiLevel The API level of the dependencies to install
     * @param abi The ABI of the dependencies to install
     * @param variant The variant of the dependencies to install
     */
    public void install(APILevel apiLevel, ABI abi, Variant variant) {
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
        Process process = sdkManager.installSystemImage(apiLevel, abi, variant);
        process.waitFor();

        process = sdkManager.installPlatforms(apiLevel);
        process.waitFor();
    }

    /**
     * Install up-to-date platform-tools and emulator
     * @throws IOException
     * @throws InterruptedException
     */
    private void installTools() throws IOException, InterruptedException {
        Process process = sdkManager.installPlatformsTools();
        process.waitFor();

        process = sdkManager.installEmulator();
        process.waitFor();
    }
}
