package fr.bmarsaud.boxedroid.program.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import fr.bmarsaud.boxedroid.entity.exception.LicenceDeclinedException;
import fr.bmarsaud.boxedroid.program.Program;

/**
 * An observer displaying the licences to the standard output and asking the user to
 * accept it
 */
public class LicenceObserver implements Observer {
    private Logger logger = LoggerFactory.getLogger(LicenceObserver.class);

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
            logger.info("Licence - " + line);
        }

        if(currentIndex == LICENSE_ACCEPT_INDEX) {
            logger.info("Do you accept the licence ? (y/N)");
            Scanner input = new Scanner(System.in);
            String response = input.nextLine();

            try {
                program.sendInput(response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(!response.equalsIgnoreCase("y")) {
                program.setException(new LicenceDeclinedException());
            }

            currentIndex = 0;
            licencing = false;
        }
    }
}
