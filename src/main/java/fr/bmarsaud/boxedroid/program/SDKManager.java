package fr.bmarsaud.boxedroid.program;

import java.io.IOException;

import fr.bmarsaud.boxedroid.entities.APILevel;

public class SDKManager extends Program {
    public SDKManager(String path) {
        super(path);
    }

    public Process acceptLicence() throws IOException {
        return this.execute("--licences");
    }

    public Process installSystemImage(APILevel apiLevel) throws IOException {
        return this.execute("system-images;android-" + apiLevel.getCode() + ";google_apis;x86");
    }
}
