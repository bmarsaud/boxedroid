package entity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fr.bmarsaud.boxedroid.entity.Variant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("Variant class")
public class VariantTest {
    @Test
    @DisplayName("Variant.fromId() with valid ids")
    public void fromIdWithValidids() {
        for(Variant variant : Variant.values()) {
            assertEquals(variant, Variant.fromId(variant.getId()));
        }
    }

    @Test
    @DisplayName("Variant.fromId() with valid names")
    public void fromIdWithValidNames() {
        for(Variant variant : Variant.values()) {
            assertEquals(variant, Variant.fromId(variant.name()));
        }
    }

    @Test
    @DisplayName("Variant.fromId() with lowercase valid names")
    public void fromIdWithValidNamesLowercase() {
        for(Variant variant : Variant.values()) {
            assertEquals(variant, Variant.fromId(variant.name().toLowerCase()));
        }
    }

    @Test
    @DisplayName("Variant.fromId() with unknown string")
    public void fromIdUnkown() {
        assertNull(Variant.fromId("unknown"));
    }
}
