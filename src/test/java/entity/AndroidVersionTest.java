package entity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fr.bmarsaud.boxedroid.entity.AndroidVersion;

@DisplayName("AndroidVersion class")
public class AndroidVersionTest {
    @Test
    @DisplayName("AndroidVersion.fromCode() with valid codes")
    public void fromCodeWithValid() {
        for(AndroidVersion version : AndroidVersion.values()) {
            for(String code : version.getCodes()) {
                assertEquals(version, AndroidVersion.fromCode(code));
            }
        }
    }

    @Test
    @DisplayName("AndroidVersion.fromCode() with unkown code")
    public void fromCodeWithUnkown() {
        assertNull(AndroidVersion.fromCode("unknown"));
    }
}
