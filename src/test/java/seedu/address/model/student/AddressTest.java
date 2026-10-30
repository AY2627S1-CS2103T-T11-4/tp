package seedu.address.model.student;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class AddressTest {

    // EP: address is null (invalid).
    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Address(null));
    }

    // BV: length < 1 (invalid).
    @Test
    public void constructor_invalidAddress_throwsIllegalArgumentException() {
        String invalidAddress = "";
        assertThrows(IllegalArgumentException.class, () -> new Address(invalidAddress));
    }

    @Test
    public void isValidAddress() {
        // EP: address is null (invalid).
        assertThrows(NullPointerException.class, () -> Address.isValidAddress(null));

        // BV: length < 1 (invalid).
        assertFalse(Address.isValidAddress(""));
        // EP: space-only address (invalid).
        assertFalse(Address.isValidAddress(" "));
        // BV: length > MAX_LENGTH (invalid).
        assertFalse(Address.isValidAddress("a".repeat(Address.MAX_LENGTH + 1)));
        // Each valid input at least once: a typical address is accepted.
        assertTrue(Address.isValidAddress("Blk 456, Den Road, #01-355"));
        // BV: 1 <= length <= MAX_LENGTH (valid).
        assertTrue(Address.isValidAddress("-"));
        // EP: 1 < length < MAX_LENGTH (valid).
        assertTrue(Address.isValidAddress("Leng Inc; 1234 Market St; San Francisco CA 2349879; USA"));
        // BV: 1 <= length <= MAX_LENGTH (valid).
        assertTrue(Address.isValidAddress("a".repeat(Address.MAX_LENGTH)));
    }

    @Test
    public void equals() {
        Address address = new Address("Valid Address");

        // EP: Another address with the same value (equivalent).
        assertTrue(address.equals(new Address("Valid Address")));

        // EP: The same address object (equivalent).
        assertTrue(address.equals(address));

        // EP: Null (non-equivalent).
        assertFalse(address.equals(null));

        // EP: An object of another type (non-equivalent).
        assertFalse(address.equals(5.0f));

        // EP: A different address value (non-equivalent).
        assertFalse(address.equals(new Address("Other Valid Address")));
    }
}
