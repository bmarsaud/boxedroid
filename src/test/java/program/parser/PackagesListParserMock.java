package program.parser;

import java.util.ArrayList;
import java.util.List;

import fr.bmarsaud.boxedroid.entity.packages.AvailablePackage;
import fr.bmarsaud.boxedroid.entity.packages.AvailableUpdates;
import fr.bmarsaud.boxedroid.entity.packages.InstalledPackage;

public class PackagesListParserMock {
    public static String getPackagesListProgramOutput() {
        return "Installed packages:\n" +
                "  Path                                     | Version | Description                             | Location                                 \n" +
                "  -------                                  | ------- | -------                                 | -------                                  \n" +
                "  emulator                                 | 30.0.12 | Android Emulator                        | emulator/                                \n" +
                "\n" +
                "Available Packages:\n" +
                "  Path                                                                                     | Version      | Description                                                         \n" +
                "  -------                                                                                  | -------      | -------                                                             \n" +
                "  add-ons;addon-google_apis-google-15                                                      | 3            | Google APIs    \n" +
                "\n" +
                "Available Updates:\n" +
                "  ID      | Installed | Available\n" +
                "  ------- | -------   | -------  \n" +
                "  tools   | 1.0.0     | 26.1.1 ";
    }

    public static List<InstalledPackage> getInstalledPackages() {
        List<InstalledPackage> packages = new ArrayList<>();
        packages.add(new InstalledPackage("emulator", "30.0.12", "Android Emulator", "emulator/"));

        return packages;
    }

    public static List<AvailablePackage> getAvailablePackages() {
        List<AvailablePackage> packages = new ArrayList<>();
        packages.add(new AvailablePackage("add-ons;addon-google_apis-google-15", "3", "Google APIs"));

        return packages;
    }

    public static List<AvailableUpdates> getAvailableUpdates() {
        List<AvailableUpdates> updates = new ArrayList<>();
        updates.add(new AvailableUpdates("tools", "1.0.0", "26.1.1"));

        return updates;
    }
}
