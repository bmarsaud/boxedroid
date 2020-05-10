package fr.bmarsaud.boxedroid.entity.exception;

/**
 * Exception thrown when a package is not available
 */
public class PackageNotAvailableException extends SDKException {
    /**
     * @param packageName The name of the not available package
     */
    public PackageNotAvailableException(String packageName) {
        super("Package '" + packageName + "' is not available !");
    }
}
