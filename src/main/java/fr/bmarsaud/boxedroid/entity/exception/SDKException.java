package fr.bmarsaud.boxedroid.entity.exception;

/**
 * Exception thrown when something about the SDK and/or SDK Manager went wrong
 */
public class SDKException extends Exception {
    /**
     * @param msg The description of the error encountered
     */
    public SDKException(String msg) {
        super("SDK Exception: " + msg);
    }
}
