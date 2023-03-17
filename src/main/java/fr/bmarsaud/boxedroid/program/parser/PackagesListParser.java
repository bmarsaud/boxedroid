package fr.bmarsaud.boxedroid.program.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import fr.bmarsaud.boxedroid.entity.packages.AvailableUpdates;
import fr.bmarsaud.boxedroid.entity.packages.InstalledPackage;
import fr.bmarsaud.boxedroid.entity.packages.AvailablePackage;
import fr.bmarsaud.boxedroid.program.observer.AggregateObserver;
import fr.bmarsaud.boxedroid.util.IOUtils;

/**
 * Parse the output of the SDK Manager list command to three {@link List} containing
 * the installed, available and updatable packages
 */
public class PackagesListParser {
    private static final String HEADER_SEPARATOR = "-------";
    private static final String[] HEADER_SYMBOL = {"Path", "ID"};
    private static final String COLUMN_SEPARATOR = " | ";
    private static final String ESCAPED_COLUMN_SEPARATOR = " \\| ";

    private List<AvailablePackage> availablePackages;
    private List<InstalledPackage> installedPackages;
    private List<AvailableUpdates> availableUpdates;

    private List<String> programOutput;

    private Observer observer;

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
        this.availablePackages = new ArrayList<>();
        this.installedPackages = new ArrayList<>();
        this.availableUpdates = new ArrayList<>();
        this.programOutput = new ArrayList<>();
        this.observer = new AggregateObserver(this.programOutput);
    }

    /**
     * Parse the output of the SDK Manager list command process.
     *
     * The packages will be available in the lists accessible with {@link PackagesListParser#getAvailablePackages()},
     * {@link PackagesListParser#getInstalledPackages()} and {@link PackagesListParser#getAvailableUpdates()}
     * @param process The SDK Manager list command process
     */
    public void parse(Process process) throws InterruptedException {
        process.waitFor(); // Wait for process to end before processing output

        availableUpdates.clear();
        availablePackages.clear();
        availableUpdates.clear();

        PackageStatus status = null;

        for(String line : programOutput) {
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
                            columns[0].trim(),
                            columns[1].trim(),
                            columns[2].trim(),
                            columns[3].trim()
                    ));
                } else if(status == PackageStatus.AVAILABLE) {
                    availablePackages.add(new AvailablePackage(
                            columns[0].trim(),
                            columns[1].trim(),
                            columns[2].trim()
                    ));

                } else if(status == PackageStatus.UPDATABLE) {
                    availableUpdates.add(new AvailableUpdates(
                            columns[0].trim(),
                            columns[1].trim(),
                            columns[2].trim()
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
    public List<AvailablePackage> getAvailablePackages() {
        return availablePackages;
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

    /**
     * Returns the observer aggregating the program output
     * @return
     */
    public Observer getObserver() {
        programOutput.clear();
        return observer;
    }
}
