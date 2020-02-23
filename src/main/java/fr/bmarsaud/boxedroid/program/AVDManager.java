package fr.bmarsaud.boxedroid.program;

import java.io.IOException;

import fr.bmarsaud.boxedroid.entities.APILevel;

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
     * @param device The device of the new AVD
     * @return The process of the executed command
     */
    public Process createAVD(String avdName, APILevel apiLevel, String device) throws IOException {
        return this.execute(
                "create", "avd",
                "--force",
                "--name", avdName,
                "--abi", "google_apis/x86",
                "--packages", "system-images;android-" + apiLevel.getCode() + ";google_apis;x86",
                "--device", device
        );
    }
}
