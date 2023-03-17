package fr.bmarsaud.boxedroid.entity;

public enum AndroidVersion {
    ANDROID_5(APILevel.API_21, "5", "5.0"),
    ANDROID_5_1(APILevel.API_22, "5.1", "5.1.1"),
    ANDROID_6(APILevel.API_23, "6", "6.0"),
    ANDROID_7(APILevel.API_24, "7", "7.0"),
    ANDROID_7_1(APILevel.API_25, "7.1", "7.1.1", "7.1.2"),
    ANDROID_8(APILevel.API_26, "8", "8.0"),
    ANDROID_8_1(APILevel.API_27, "8.1"),
    ANDROID_9(APILevel.API_28, "9", "9.0"),
    ANDROID_10(APILevel.API_29, "10", "10.0"),
    ANDROID_11(APILevel.API_30, "11", "11.0"),
    ANDROID_12(APILevel.API_31, "12", "12.0"),
    ANDROID_12L(APILevel.API_32, "12.1", "12L"),
    ANDROID_13(APILevel.API_33, "13", "13.0");

    private APILevel apiLevel;
    private String[] codes;

    AndroidVersion(APILevel apiLevel, String... codes) {
        this.apiLevel = apiLevel;
        this.codes = codes;
    }

    public APILevel getApiLevel() {
        return apiLevel;
    }

    public String[] getCodes() {
        return codes;
    }

    /**
     * Get an Android version from its code
     * @param code The code of the Android version
     * @return The Android version if found, null instead
     */
    public static AndroidVersion fromCode(String code) {
        for(AndroidVersion version : AndroidVersion.values()) {
            for(int i = 0; i < version.getCodes().length; i++) {
                if(code.equals(version.getCodes()[i])) {
                    return version;
                }
            }
        }
        return null;
    }
}
