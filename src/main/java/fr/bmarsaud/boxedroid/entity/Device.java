package fr.bmarsaud.boxedroid.entity;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Device device = (Device) o;
        return id == device.id &&
                Objects.equals(code, device.code) &&
                Objects.equals(name, device.name) &&
                Objects.equals(oem, device.oem) &&
                Objects.equals(tag, device.tag);
    }
}
