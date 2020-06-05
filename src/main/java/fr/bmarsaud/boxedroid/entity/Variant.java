package fr.bmarsaud.boxedroid.entity;

public enum Variant {
    DEFAULT("default"),
    GOOGLE_APIS("google_apis"),
    GOOGLE_APIS_PLAYSTORE("google_apis_playstore"),
    ANDROID_TV("android-tv"),
    ANDROID_WEAR("android-wear"),
    ANDROID_WEAR_CN("android-wear-cn");

    private String id;

    Variant(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    /**
     * Get a Variant from its id
     * @param id The Variant id
     * @return The Variant if found, null instead
     */
    public static Variant fromId(String id) {
        for(Variant variant : Variant.values()) {
            if(variant.getId().equals(id) || variant.name().equalsIgnoreCase(id)) {
                return variant;
            }
        }

        return null;
    }
}
