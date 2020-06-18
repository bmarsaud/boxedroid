package entity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fr.bmarsaud.boxedroid.entity.ABI;

@DisplayName("ABI class")
public class ABITest {

    @Test
    @DisplayName("ABI.fromId() with valid ids")
    public void fromIdWithId() {
        for(ABI abi : ABI.values()) {
            assertEquals(abi, ABI.fromId(abi.getId()));
        }
    }

    @Test
    @DisplayName("ABI.fromId() with valid names")
    public void fromIdWithNames() {
        for(ABI abi : ABI.values()) {
            assertEquals(abi, ABI.fromId(abi.name()));
        }
    }

    @Test
    @DisplayName("ABI.fromId() with lowercase valid names")
    public void fromIdWithNamesLowercase() {
        for(ABI abi : ABI.values()) {
            assertEquals(abi, ABI.fromId(abi.name().toLowerCase()));
        }
    }

    @Test
    @DisplayName("ABI.fromId() with unknown string")
    public void fromIdWithUnknownString() {
        assertNull(ABI.fromId("unknown"));
    }
}
