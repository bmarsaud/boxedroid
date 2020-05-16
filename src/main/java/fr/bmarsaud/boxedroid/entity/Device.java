package fr.bmarsaud.boxedroid.entity;

/**
 * An available device for AVD configuration
 */
public class Device {
    private int id;
    private String code;
    private String name;
    private String oem;
    private String tag;

    public Device(int id, String code, String name, String oem, String tag) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.oem = oem;
        this.tag = tag;
    }

    public Device() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOem() {
        return oem;
    }

    public void setOem(String oem) {
        this.oem = oem;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
