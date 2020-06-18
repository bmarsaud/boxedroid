package fr.bmarsaud.boxedroid.entity.packages;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        InstalledPackage that = (InstalledPackage) o;
        return Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), location);
    }
}
