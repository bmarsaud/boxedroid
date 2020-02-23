package fr.bmarsaud.boxedroid.program;

import java.io.IOException;

import fr.bmarsaud.boxedroid.entities.APILevel;

public class AVDManager extends Program {
    public AVDManager(String path) {
        super(path);
    }

    public Process listAVDs() throws IOException {
        return this.execute("list", "avd");
    }

    public Process listTargets() throws IOException {
        return this.execute("list", "target");
    }

    public Process listDevices() throws IOException {
        return this.execute("list", "device");
    }

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
