package fr.bmarsaud.boxedroid.program;

import java.io.IOException;

public class Emulator extends Program {
    public Emulator(String path) {
        super(path);
    }

    public Process launch(String avdName) throws IOException {
        return this.execute("-avd", avdName);
    }
}
