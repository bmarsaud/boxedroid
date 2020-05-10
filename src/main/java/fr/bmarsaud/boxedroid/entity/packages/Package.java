package fr.bmarsaud.boxedroid.entity.packages;

public class Package {
    private String path;
    private String version;
    private String description;

    public Package(String path, String version, String description) {
        this.path = path;
        this.version = version;
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
