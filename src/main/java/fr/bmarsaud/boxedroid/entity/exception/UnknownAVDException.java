package fr.bmarsaud.boxedroid.entity.exception;

/**
 * Exception thrown when an emulator is launched with an unknown AVD
 */
public class UnknownAVDException extends SDKException {
    public UnknownAVDException(String avdName) {
        super("AVD \"" + avdName + "\" doesn't exist !");
    }
}
