package fr.bmarsaud.boxedroid.program.observer.licence;

import java.util.Observable;
import java.util.Observer;

import fr.bmarsaud.boxedroid.program.Program;

/**
 * An observer looking do licence display
 */
public abstract class LicenceObserver implements Observer {
    public static final String LICENSE_START = "Terms and Conditions";
    public static final String LICENSE_DELIMITER = "---------------------------------------";
    public static final int LICENSE_ACCEPT_INDEX = 2; // The end of the licence is defined by this index

    private boolean licencing;
    private int currentIndex;

    public LicenceObserver() {
        this.licencing = false;
        this.currentIndex = 0;
    }

    @Override
    public void update(Observable observable, Object obj) {
        String line = (String) obj;
        Program program = (Program) observable;

        if(line.contains(LICENSE_START)) {
            licencing = true;
        } else if(line.contains(LICENSE_DELIMITER)) {
            currentIndex++;
        }

        if(licencing) {
            onLicenseDisplay(line);
        }

        if(currentIndex == LICENSE_ACCEPT_INDEX) {
            onLicenceAcceptanceRequest(program);
            currentIndex = 0;
            licencing = false;
        }
    }

    public abstract void onLicenseDisplay(String licenceLine);
    public abstract void onLicenceAcceptanceRequest(Program program);
}
