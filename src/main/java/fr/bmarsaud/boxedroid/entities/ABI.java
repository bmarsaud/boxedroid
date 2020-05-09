package fr.bmarsaud.boxedroid.entities;

public enum ABI {
    X86("x86"),
    X86_64("x86_64"),
    ARM("armeabi-v7a"),
    ARM_64("arm64-v8a");

    private String id;

    ABI(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
