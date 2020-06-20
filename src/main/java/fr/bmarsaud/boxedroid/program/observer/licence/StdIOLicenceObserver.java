package fr.bmarsaud.boxedroid.program.observer.licence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Scanner;

import fr.bmarsaud.boxedroid.entity.exception.LicenceDeclinedException;
import fr.bmarsaud.boxedroid.program.Program;

/**
 * An observer displaying the licences to the standard output and asking the user to
 * accept it
 */
public class StdIOLicenceObserver extends LicenceObserver {
    private Logger logger = LoggerFactory.getLogger(StdIOLicenceObserver.class);

    @Override
    public void onLicenseDisplay(String licenceLine) {
        logger.info("Licence - " + licenceLine);
    }

    @Override
    public void onLicenceAcceptanceRequest(Program program) {
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
    }
}
