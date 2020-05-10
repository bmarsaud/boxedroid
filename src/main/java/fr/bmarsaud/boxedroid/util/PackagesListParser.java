package fr.bmarsaud.boxedroid.util;

import java.util.ArrayList;
import java.util.List;

import fr.bmarsaud.boxedroid.entity.packages.AvailableUpdates;
import fr.bmarsaud.boxedroid.entity.packages.InstalledPackage;
import fr.bmarsaud.boxedroid.entity.packages.AvailablePackage;

/**
 * Parse the output of the SDK Manager list command to three {@link List} containing
 * the installed, available and updatable packages
 */
public class PackagesListParser {
    private static final String HEADER_SEPARATOR = "-------";
    private static final String[] HEADER_SYMBOL = {"Path", "ID"};
    private static final String COLUMN_SEPARATOR = " | ";
    private static final String ESCAPED_COLUMN_SEPARATOR = " \\| ";

    private List<AvailablePackage> availableAvailablePackages;
    private List<InstalledPackage> installedPackages;
    private List<AvailableUpdates> availableUpdates;

    private enum PackageStatus {
        INSTALLED("Installed packages:"),
        AVAILABLE("Available Packages:"),
        UPDATABLE("Available Updates:");

        private String text;

        PackageStatus(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }

    public PackagesListParser() {
        this.availableAvailablePackages = new ArrayList<>();
        this.installedPackages = new ArrayList<>();
        this.availableUpdates = new ArrayList<>();
    }

    /**
     * Parse the output of the SDK Manager list command process.
     *
     * The packages will be available in the lists accessible with {@link PackagesListParser#getAvailableAvailablePackages()},
     * {@link PackagesListParser#getInstalledPackages()} and {@link PackagesListParser#getAvailableUpdates()}
     * @param process The SDK Manager list command process
     */
    public void parse(Process process) {
        availableUpdates.clear();
        installedPackages.clear();
        availableUpdates.clear();

        String output = IOUtils.streamToString(process.getInputStream());

        PackageStatus status = null;
        String[] lines = output.split("\n");

        for(String line : lines) {
            // Identifies the next packages status
            for(PackageStatus stat : PackageStatus.values()) {
                if(line.equalsIgnoreCase(stat.getText())) {
                    status = stat;
                    continue;
                }
            }

            // Skip table header and blank lines
            if(line.contains(COLUMN_SEPARATOR) && !line.contains(HEADER_SEPARATOR) && !line.contains(HEADER_SYMBOL[0]) && !line.contains(HEADER_SYMBOL[1])) {
                String columns[] = line.split(ESCAPED_COLUMN_SEPARATOR);

                if(status == PackageStatus.INSTALLED) {
                    installedPackages.add(new InstalledPackage(
                            IOUtils.removeSurroundingSpaces(columns[0]),
                            IOUtils.removeSurroundingSpaces(columns[1]),
                            IOUtils.removeSurroundingSpaces(columns[2]),
                            IOUtils.removeSurroundingSpaces(columns[3])
                    ));
                } else if(status == PackageStatus.AVAILABLE) {
                    availableAvailablePackages.add(new AvailablePackage(
                            IOUtils.removeSurroundingSpaces(columns[0]),
                            IOUtils.removeSurroundingSpaces(columns[1]),
                            IOUtils.removeSurroundingSpaces(columns[2])
                    ));

                } else if(status == PackageStatus.UPDATABLE) {
                    availableUpdates.add(new AvailableUpdates(
                            IOUtils.removeSurroundingSpaces(columns[0]),
                            IOUtils.removeSurroundingSpaces(columns[1]),
                            IOUtils.removeSurroundingSpaces(columns[2])
                    ));
                }
            }
        }
    }

    /**
     * Returns the parsed list of available packages.
     * Will be empty if {@link PackagesListParser#parse(Process)} was not called before
     * @return The parsed list of available packages
     */
    public List<AvailablePackage> getAvailableAvailablePackages() {
        return availableAvailablePackages;
    }

    /**
     * Returns the parsed list of installed packages.
     * Will be empty if {@link PackagesListParser#parse(Process)} was not called before
     * @return The parsed list of installed packages
     */
    public List<InstalledPackage> getInstalledPackages() {
        return installedPackages;
    }

    /**
     * Returns the parsed list of updatable packages.
     * Will be empty if {@link PackagesListParser#parse(Process)} was not called before
     * @return The parsed list of updatable packages
     */
    public List<AvailableUpdates> getAvailableUpdates() {
        return availableUpdates;
    }
}
