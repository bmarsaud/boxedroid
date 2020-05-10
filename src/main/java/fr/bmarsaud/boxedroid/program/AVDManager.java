package fr.bmarsaud.boxedroid.program;

import java.io.File;
import java.io.IOException;

import fr.bmarsaud.boxedroid.entity.ABI;
import fr.bmarsaud.boxedroid.entity.APILevel;
import fr.bmarsaud.boxedroid.entity.Platform;

/**
 * An abstraction of the AVDManager program form the Android SDK.
 */
public class AVDManager extends Program {
    public AVDManager(String path) {
        super(path);
    }

    /**
     * Execute the "list avd" command listing the Android Virtual Devices
     * @return The process of the executed command
     */
    public Process listAVDs() throws IOException {
        return this.execute("list", "avd");
    }

    /**
     * Execute the "list target" command listing the available targets
     * @return The process of the executed command
     */
    public Process listTargets() throws IOException {
        return this.execute("list", "target");
    }

    /**
     * Execute the "list device" command listing the available devices
     * @return The process of the executed command
     */
    public Process listDevices() throws IOException {
        return this.execute("list", "device");
    }

    /**
     * Execute the "create avd" command with an AVD name, an API level and a device
     * @param avdName The name to give to the new AVD
     * @param apiLevel The Android API level of the new AVD
     * @param abi The ABI of the new AVD
     * @param platform The platform of the new AVD
     * @param device The device of the new AVD
     * @param path The path where to store the new AVD
     * @return The process of the executed command
     */
    public Process createAVD(String avdName, APILevel apiLevel, ABI abi, Platform platform, String device, String path) throws IOException {
        return this.execute(
                "create", "avd",
                "--force",
                "--name", avdName,
                "--abi", platform.getId() + "/" + abi.getId(),
                "--package", "system-images;android-" + apiLevel.getCode() + ";" + platform.getId() + ";" + abi.getId(),
                "--device", device,
                "--path", new File(path, avdName).getAbsolutePath()
        );
    }
}
