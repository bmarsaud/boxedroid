package entity;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import fr.bmarsaud.boxedroid.entity.ABI;
import fr.bmarsaud.boxedroid.entity.APILevel;
import fr.bmarsaud.boxedroid.entity.AVD;
import fr.bmarsaud.boxedroid.entity.Variant;

@DisplayName("AVD entity")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AVDTest {
    private static final String SYS_IMAGE_VALUE = "system-images/android-27/default/x86/";
    private static final String DEVICE_NAME_KEY = "hw.device.name";

    private static final APILevel EXPECTED_API_LEVEL = APILevel.API_27;
    private static final Variant EXPECTED_VARIANT = Variant.DEFAULT;
    private static final ABI EXPECTED_ABI = ABI.X86;
    private static final String EXPECTED_DEVICE_NAME = "DeviceName";

    private static final String AVD_NAME = "MockedAVD";
    private static final String AVD_PATH = "fakePath";

    private AVD mockedAVD;

    @BeforeAll
    public void init() {
        mockedAVD = new AVD(AVD_NAME, AVD_PATH);
        mockedAVD.editConfig(AVD.SYS_IMAGE_INI_KEY, SYS_IMAGE_VALUE);
        mockedAVD.editConfig(DEVICE_NAME_KEY, EXPECTED_DEVICE_NAME);
    }

    @Test
    public void getAPILevel() {
        assertEquals(EXPECTED_API_LEVEL, mockedAVD.getAPILevel());
    }

    @Test
    public void getVariant() {
        assertEquals(EXPECTED_VARIANT, mockedAVD.getVariant());
    }

    @Test
    public void getABI() {
        assertEquals(EXPECTED_ABI, mockedAVD.getABI());
    }

    @Test
    public void getConfigValue() {
        assertEquals(SYS_IMAGE_VALUE, mockedAVD.getConfigValue(AVD.SYS_IMAGE_INI_KEY));
    }

    @Test
    public void getDeviceName() {
        assertEquals(EXPECTED_DEVICE_NAME, mockedAVD.getDeviceName());
    }

    @Test
    @DisplayName("Simple getters")
    public void simpleGetters() {
        assertEquals(AVD_NAME, mockedAVD.getName());
        assertEquals(AVD_PATH, mockedAVD.getPath());
    }
}
