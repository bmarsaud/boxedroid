package fr.bmarsaud.boxedroid.entity.packages;

import java.util.Objects;

public class AvailableUpdates {
    private String id;
    private String installed;
    private String available;

    public AvailableUpdates(String id, String installed, String available) {
        this.id = id;
        this.installed = installed;
        this.available = available;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstalled() {
        return installed;
    }

    public void setInstalled(String installed) {
        this.installed = installed;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvailableUpdates that = (AvailableUpdates) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(installed, that.installed) &&
                Objects.equals(available, that.available);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, installed, available);
    }
}
