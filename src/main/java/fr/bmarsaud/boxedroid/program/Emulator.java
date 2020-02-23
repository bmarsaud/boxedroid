package fr.bmarsaud.boxedroid.program;

import java.io.IOException;

/**
 * An abstraction of the emulator program for the Android SDK
 */
public class Emulator extends Program {
    public Emulator(String path) {
        super(path);
    }

    /**
     * Run the launch command launching an AVD
     * @param avdName The AVD name to launch
     * @return The process of the executed command
     */
    public Process launch(String avdName) throws IOException {
        return this.execute("-avd", avdName);
    }
}
