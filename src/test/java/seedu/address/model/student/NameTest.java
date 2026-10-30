package seedu.address.model.student;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class NameTest {

    // EP: student name is null (invalid).
    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Name(null));
    }

    // BV: length < 1 (invalid).
    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        String invalidName = "";
        assertThrows(IllegalArgumentException.class, () -> new Name(invalidName));
    }

    @Test
    public void isValidName() {
        // EP: student name is null (invalid).
        assertThrows(NullPointerException.class, () -> Name.isValidName(null));

        // BV: length < 1 (invalid).
        assertFalse(Name.isValidName(""));
        // EP: space-only student name (invalid).
        assertFalse(Name.isValidName(" "));
        // EP: name containing only symbols (invalid).
        assertFalse(Name.isValidName("^"));
        // EP: alphanumeric name containing a symbol (invalid).
        assertFalse(Name.isValidName("peter*"));
        // Each valid input at least once: an alphabetic name is accepted.
        assertTrue(Name.isValidName("peter jack"));
        // Each valid input at least once: a numeric name is accepted.
        assertTrue(Name.isValidName("12345"));
        // Each valid input at least once: an alphanumeric name is accepted.
        assertTrue(Name.isValidName("peter the 2nd"));
        // EP: mixed-case student name (valid).
        assertTrue(Name.isValidName("Capital Tan"));
        // EP: long alphanumeric student name (valid).
        assertTrue(Name.isValidName("David Roger Jackson Ray Jr 2nd"));
    }

    @Test
    public void isSameName() {
        Name name = new Name("Jane Doe");

        // EP: exact name match (same identity).
        assertTrue(name.isSameName(new Name("Jane Doe")));
        // EP: case-insensitive name match (same identity).
        assertTrue(name.isSameName(new Name("jane doe")));
        // EP: different name value (different identity).
        assertFalse(name.isSameName(new Name("Jane Smith")));
        // EP: null (different identity).
        assertFalse(name.isSameName(null));
    }

    @Test
    public void equals() {
        Name name = new Name("Valid Name");

        // EP: Another name with the same value (equivalent).
        assertTrue(name.equals(new Name("Valid Name")));

        // EP: The same name object (equivalent).
        assertTrue(name.equals(name));

        // EP: Null (non-equivalent).
        assertFalse(name.equals(null));

        // EP: An object of another type (non-equivalent).
        assertFalse(name.equals(5.0f));

        // EP: A different name value (non-equivalent).
        assertFalse(name.equals(new Name("Other Valid Name")));
    }
}
