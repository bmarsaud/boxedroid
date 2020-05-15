package fr.bmarsaud.boxedroid.entity.exception;

import java.util.Observer;

public class LicenceDeclinedException extends SDKException {
    /**
     * Exception raised when the user doesn't accept a licence
     */
    public LicenceDeclinedException() {
        super("The licence has been declined");
    }
}
