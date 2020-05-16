package fr.bmarsaud.boxedroid.entity.exception;

/**
 * Exception thrown when we try to create an AVD with a device that doesn't exists
 */
public class DeviceNotAvailableException extends SDKException {
    public DeviceNotAvailableException(String deviceName) {
        super("The given device \"" + deviceName + "\" is not available !");
    }
}
