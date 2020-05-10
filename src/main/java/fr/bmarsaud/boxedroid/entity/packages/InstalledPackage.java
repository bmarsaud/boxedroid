package fr.bmarsaud.boxedroid.entity.packages;

public class InstalledPackage extends AvailablePackage {
    private String location;

    public InstalledPackage(String path, String version, String description, String location) {
        super(path, version, description);
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
