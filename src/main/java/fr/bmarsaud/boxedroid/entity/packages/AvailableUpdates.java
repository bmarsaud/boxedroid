package fr.bmarsaud.boxedroid.entity.packages;

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
}
