package fr.bmarsaud.boxedroid.entity;

public enum APILevel {
    API_21(21, AndroidVersion.ANDROID_5),
    API_22(22, AndroidVersion.ANDROID_5_1),
    API_23(23, AndroidVersion.ANDROID_6),
    API_24(24, AndroidVersion.ANDROID_7),
    API_25(25, AndroidVersion.ANDROID_7_1),
    API_26(26, AndroidVersion.ANDROID_8),
    API_27(27, AndroidVersion.ANDROID_8_1),
    API_28(28, AndroidVersion.ANDROID_9),
    API_29(29, AndroidVersion.ANDROID_10),
    API_30(30, AndroidVersion.ANDROID_11),
    API_31(31, AndroidVersion.ANDROID_12),
    API_32(32, AndroidVersion.ANDROID_12L),
    API_33(33, AndroidVersion.ANDROID_13);

    private static final String NAME_PREFIX = "android-";

    private int code;
    private AndroidVersion androidVersion;

    APILevel(int code, AndroidVersion androidVersion) {
        this.code = code;
        this.androidVersion = androidVersion;
    }

    public int getCode() {
        return code;
    }

    public AndroidVersion getAndroidVersion() {
        return androidVersion;
    }

    /**
     * Get an API level from its name
     * @param name The name of that API level
     * @return The API level if found, null instead
     */
    public static APILevel fromName(String name) {
        int code = Integer.parseInt(name.replace(NAME_PREFIX, ""));
        for(APILevel level : APILevel.values()) {
            if(level.code == code) {
                return level;
            }
        }

        return null;
    }
}
