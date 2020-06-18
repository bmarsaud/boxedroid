package fr.bmarsaud.boxedroid.entity.packages;

import java.util.Objects;

public class AvailablePackage {
    private String path;
    private String version;
    private String description;

    public AvailablePackage(String path, String version, String description) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvailablePackage that = (AvailablePackage) o;
        return Objects.equals(path, that.path) &&
                Objects.equals(version, that.version) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, version, description);
    }
}
