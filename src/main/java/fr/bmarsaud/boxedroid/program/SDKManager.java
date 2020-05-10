package fr.bmarsaud.boxedroid.program;

import java.io.IOException;

import fr.bmarsaud.boxedroid.entities.ABI;
import fr.bmarsaud.boxedroid.entities.APILevel;
import fr.bmarsaud.boxedroid.entities.Platform;

/**
 * An abstraction of the SDKManager program from the Android SDK
 */
public class SDKManager extends Program {
    private String sdkPath;

    public SDKManager(String path, String sdkPath) {
        super(path);
        this.sdkPath = sdkPath;
    }

    /**
     * Run the "--licences" command accepting the Android SDK licences if needed
     * @return The process of the executed command
     */
    public Process acceptLicence() throws IOException {
        return this.execute("--sdk_root=" + sdkPath, "--licences");
    }

    /**
     * Run the command installing the system images of a certain API level, ABI and platform
     * @param apiLevel The API level of the system images to install
     * @param abi The ABI supported by the system images to install
     * @param platform The platform of the system images to install
     * @return The process of the executed command
     */
    public Process installSystemImage(APILevel apiLevel, ABI abi, Platform platform) throws IOException {
        return this.execute("--sdk_root=" + sdkPath,
                "system-images;android-" + apiLevel.getCode() + ";" + platform.getId() +";" + abi.getId());
    }
}
