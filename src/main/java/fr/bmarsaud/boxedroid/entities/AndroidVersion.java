package fr.bmarsaud.boxedroid.entities;

public enum AndroidVersion {
    ANDROID_5(APILevel.API_21),
    ANDROID_5_1(APILevel.API_22),
    ANDROID_6(APILevel.API_23),
    ANDROID_7(APILevel.API_24),
    ANDROID_7_1(APILevel.API_25),
    ANDROID_8(APILevel.API_26),
    ANDROID_8_1(APILevel.API_27),
    ANDROID_9(APILevel.API_28),
    ANDROID_10(APILevel.API_29);

    private APILevel apiLevel;

    AndroidVersion(APILevel apiLevel) {
        this.apiLevel = apiLevel;
    }

    public APILevel getApiLevel() {
        return apiLevel;
    }
}
