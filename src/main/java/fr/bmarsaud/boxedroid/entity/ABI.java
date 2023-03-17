package fr.bmarsaud.boxedroid.entity;

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

    /**
     * Get an ABI from its id
     * @param id The ABI id
     * @return The ABI if found, null instead
     */
    public static ABI fromId(String id) {
        for(ABI abi : ABI.values()) {
            if(abi.getId().equals(id) || abi.name().equalsIgnoreCase(id)) {
                return abi;
            }
        }

        return null;
    }
}
