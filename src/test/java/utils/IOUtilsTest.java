package utils;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fr.bmarsaud.boxedroid.util.IOUtils;

@DisplayName("IOUtils class")
public class IOUtilsTest {
    public static final String EXPECTED_SPACES = "expected";
    public static final String EXPECTED_SPACES_WITHIN = "ex p e cted";

    @Test
    @DisplayName("IOUtils.removeSurroundingSpaces() with spaces before")
    public void testRemoveSurroundingSpacesBefore() {
        final String oneSpace = " " + EXPECTED_SPACES;
        final String multipleSpaces = "  " + EXPECTED_SPACES;

        assertEquals(EXPECTED_SPACES, IOUtils.removeSurroundingSpaces(oneSpace));
        assertEquals(EXPECTED_SPACES, IOUtils.removeSurroundingSpaces(multipleSpaces));
    }

    @Test
    @DisplayName("IOUtils.removeSurroundingSpaces() with spaces after")
    public void testRemoveSurroundingSpacesAfter() {
        final String oneSpace = EXPECTED_SPACES + " ";
        final String multipleSpaces = EXPECTED_SPACES + "  ";

        assertEquals(EXPECTED_SPACES, IOUtils.removeSurroundingSpaces(oneSpace));
        assertEquals(EXPECTED_SPACES, IOUtils.removeSurroundingSpaces(multipleSpaces));
    }

    @Test
    @DisplayName("IOUtils.removeSurroundingSpaces() with spaces before and after")
    public void testRemoveSurroundingSpacesBoth() {
        final String oneSpace = " "  + EXPECTED_SPACES + " ";
        final String multipleSpaces = "  "  + EXPECTED_SPACES + "  ";

        assertEquals(EXPECTED_SPACES, IOUtils.removeSurroundingSpaces(oneSpace));
        assertEquals(EXPECTED_SPACES, IOUtils.removeSurroundingSpaces(multipleSpaces));
    }

    @Test
    @DisplayName("IOUtils.removeSurroundingSpaces() with spaces before, after and withing")
    public void testRemoveSurroundingSpacesContaining() {
        final String oneSpace = "" + EXPECTED_SPACES_WITHIN + "";
        final String multipleSpaces = "   " + EXPECTED_SPACES_WITHIN + "  ";

        assertEquals(EXPECTED_SPACES_WITHIN, IOUtils.removeSurroundingSpaces(oneSpace));
        assertEquals(EXPECTED_SPACES_WITHIN, IOUtils.removeSurroundingSpaces(multipleSpaces));
    }
}
