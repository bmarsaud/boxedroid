package entity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fr.bmarsaud.boxedroid.entity.APILevel;

@DisplayName("APILevel class")
public class APILevelTest {
    private static final String NAME_PREFIX = "android-";

    @Test
    @DisplayName("APILevel.fromName() with well formatted names")
    public void fromNameWithValid() {
        for(APILevel level : APILevel.values()) {
            String apiName = NAME_PREFIX + level.getCode();
            assertEquals(level, APILevel.fromName(apiName));
        }
    }

    @Test
    @DisplayName("APILevel.fromName() with not well formatted names")
    public void fromNameWithInvalid() {
        assertNull(APILevel.fromName("invalid-29"));
    }

    @Test
    @DisplayName("APILevel.fromName() with unknown name")
    public void fromNameWithUnknown() {
        assertNull(APILevel.fromName(NAME_PREFIX + "69"));
    }
}
