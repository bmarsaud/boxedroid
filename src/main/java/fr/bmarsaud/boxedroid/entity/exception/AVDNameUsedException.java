package fr.bmarsaud.boxedroid.entity.exception;

/**
 * Exception thrown when the user tries to create an avd with a name that is already used
 */
public class AVDNameUsedException extends SDKException {
    public AVDNameUsedException(String avdName) {
        super("AVD name \"" + avdName + "\" is already used by another AVD !");
    }
}
