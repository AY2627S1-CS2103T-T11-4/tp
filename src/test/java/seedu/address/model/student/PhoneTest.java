package seedu.address.model.student;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class PhoneTest {

    // EP: phone number is null (invalid).
    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Phone(null));
    }

    // BV: length < 7 (invalid).
    @Test
    public void constructor_invalidPhone_throwsIllegalArgumentException() {
        String invalidPhone = "";
        assertThrows(IllegalArgumentException.class, () -> new Phone(invalidPhone));
    }

    @Test
    public void isValidPhone() {
        // EP: phone number is null (invalid).
        assertThrows(NullPointerException.class, () -> Phone.isValidPhone(null));

        // BV: length < 7 (invalid).
        assertFalse(Phone.isValidPhone(""));
        // EP: space-only phone number (invalid).
        assertFalse(Phone.isValidPhone(" "));
        // EP: length < 7 (invalid).
        assertFalse(Phone.isValidPhone("123"));
        // BV: length < 7 (invalid).
        assertFalse(Phone.isValidPhone("123456"));
        // BV: length > 15 (invalid).
        assertFalse(Phone.isValidPhone("1234567890123456"));
        // EP: alphabetic phone number (invalid).
        assertFalse(Phone.isValidPhone("phone"));
        // EP: alphabetic character among digits (invalid).
        assertFalse(Phone.isValidPhone("9011p041"));
        // EP: whitespace among digits (invalid).
        assertFalse(Phone.isValidPhone("9312 1534"));
        // BV: 7 <= length <= 15 (valid).
        assertTrue(Phone.isValidPhone("1234567"));
        // Each valid input at least once: a typical eight-digit phone number is accepted.
        assertTrue(Phone.isValidPhone("93121534"));
        // BV: 7 <= length <= 15 (valid).
        assertTrue(Phone.isValidPhone("123456789012345"));
    }

    @Test
    public void equals() {
        Phone phone = new Phone("1234567");

        // EP: Another phone number with the same value (equivalent).
        assertTrue(phone.equals(new Phone("1234567")));

        // EP: The same phone object (equivalent).
        assertTrue(phone.equals(phone));

        // EP: Null (non-equivalent).
        assertFalse(phone.equals(null));

        // EP: An object of another type (non-equivalent).
        assertFalse(phone.equals(5.0f));

        // EP: A different phone number (non-equivalent).
        assertFalse(phone.equals(new Phone("1234568")));
    }
}
