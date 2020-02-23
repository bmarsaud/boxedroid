package fr.bmarsaud.boxedroid.program;

import java.io.IOException;

import fr.bmarsaud.boxedroid.entities.APILevel;

/**
 * An abstraction of the SDKManager program from the Android SDK
 */
public class SDKManager extends Program {
    public SDKManager(String path) {
        super(path);
    }

    /**
     * Run the "--licences" command accepting the Android SDK licences if needed
     * @return The process of the executed command
     */
    public Process acceptLicence() throws IOException {
        return this.execute("--licences");
    }

    /**
     * Run the command installing the system images with google apis of a certain API level
     * @param apiLevel The API level of the system images to install
     * @return The process of the executed command
     */
    public Process installSystemImage(APILevel apiLevel) throws IOException {
        return this.execute("system-images;android-" + apiLevel.getCode() + ";google_apis;x86");
    }
}
