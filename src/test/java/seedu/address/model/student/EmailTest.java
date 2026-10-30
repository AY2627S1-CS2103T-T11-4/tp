package seedu.address.model.student;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class EmailTest {

    // EP: email address is null (invalid).
    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Email(null));
    }

    // BV: length < minimum (invalid).
    @Test
    public void constructor_invalidEmail_throwsIllegalArgumentException() {
        String invalidEmail = "";
        assertThrows(IllegalArgumentException.class, () -> new Email(invalidEmail));
    }

    @Test
    public void isValidEmail() {
        // EP: email address is null (invalid).
        assertThrows(NullPointerException.class, () -> Email.isValidEmail(null));

        // BV: length < minimum (invalid).
        assertFalse(Email.isValidEmail(""));
        // EP: space-only email address (invalid).
        assertFalse(Email.isValidEmail(" "));

        // One invalid input at a time: only the local part is missing.
        assertFalse(Email.isValidEmail("@example.com"));
        // One invalid input at a time: only the @ separator is missing.
        assertFalse(Email.isValidEmail("peterjackexample.com"));
        // One invalid input at a time: only the domain is missing.
        assertFalse(Email.isValidEmail("peterjack@"));

        // One invalid input at a time: only the domain format is invalid.
        assertFalse(Email.isValidEmail("peterjack@-"));
        // One invalid input at a time: only the domain contains an underscore.
        assertFalse(Email.isValidEmail("peterjack@exam_ple.com"));
        // One invalid input at a time: only the local part contains a space.
        assertFalse(Email.isValidEmail("peter jack@example.com"));
        // One invalid input at a time: only the domain contains a space.
        assertFalse(Email.isValidEmail("peterjack@exam ple.com"));
        // EP: email address with leading whitespace (invalid).
        assertFalse(Email.isValidEmail(" peterjack@example.com"));
        // EP: email address with trailing whitespace (invalid).
        assertFalse(Email.isValidEmail("peterjack@example.com "));
        // EP: email address with two @ separators (invalid).
        assertFalse(Email.isValidEmail("peterjack@@example.com"));
        // One invalid input at a time: only the local part contains an @ separator.
        assertFalse(Email.isValidEmail("peter@jack@example.com"));
        // One invalid input at a time: only the local part starts with a hyphen.
        assertFalse(Email.isValidEmail("-peterjack@example.com"));
        // One invalid input at a time: only the local part ends with a hyphen.
        assertFalse(Email.isValidEmail("peterjack-@example.com"));
        // One invalid input at a time: only the local part has consecutive periods.
        assertFalse(Email.isValidEmail("peter..jack@example.com"));
        // One invalid input at a time: only the domain contains an @ separator.
        assertFalse(Email.isValidEmail("peterjack@example@com"));
        // One invalid input at a time: only the domain starts with a period.
        assertFalse(Email.isValidEmail("peterjack@.example.com"));
        // One invalid input at a time: only the domain ends with a period.
        assertFalse(Email.isValidEmail("peterjack@example.com."));
        // One invalid input at a time: only the domain starts with a hyphen.
        assertFalse(Email.isValidEmail("peterjack@-example.com"));
        // One invalid input at a time: only the domain ends with a hyphen.
        assertFalse(Email.isValidEmail("peterjack@example.com-"));
        // BV: length < 2 (invalid).
        assertFalse(Email.isValidEmail("peterjack@example.c"));
        // BV: length < minimum (invalid).
        assertFalse(Email.isValidEmail("a@bc"));
        // BV: length > MAX_LOCAL_PART_LENGTH (invalid).
        assertFalse(Email.isValidEmail("a".repeat(Email.MAX_LOCAL_PART_LENGTH + 1) + "@example.com"));
        // BV: length > MAX_DOMAIN_LENGTH (invalid).
        assertFalse(Email.isValidEmail("user@" + "a".repeat(Email.MAX_DOMAIN_LENGTH + 1)));

        // Each valid input at least once: an underscore in the local part is accepted.
        assertTrue(Email.isValidEmail("PeterJack_1190@example.com"));
        // Each valid input at least once: a period in the local part is accepted.
        assertTrue(Email.isValidEmail("PeterJack.1190@example.com"));
        // Each valid input at least once: a plus sign in the local part is accepted.
        assertTrue(Email.isValidEmail("PeterJack+1190@example.com"));
        // Each valid input at least once: a hyphen in the local part is accepted.
        assertTrue(Email.isValidEmail("PeterJack-1190@example.com"));
        // BV: length >= minimum (valid).
        assertTrue(Email.isValidEmail("a@b.co"));
        // BV: length <= MAX_LOCAL_PART_LENGTH (valid).
        assertTrue(Email.isValidEmail("a".repeat(Email.MAX_LOCAL_PART_LENGTH) + "@example.com"));
        // BV: length <= MAX_DOMAIN_LENGTH (valid).
        assertTrue(Email.isValidEmail("user@" + "a".repeat(Email.MAX_DOMAIN_LENGTH)));
        // Each valid input at least once: an alphabetic local part and domain are accepted.
        assertTrue(Email.isValidEmail("test@localhost"));
        // Each valid input at least once: a numeric local part and domain are accepted.
        assertTrue(Email.isValidEmail("123@145"));
        // EP: mixed alphanumeric and special local-part characters (valid).
        assertTrue(Email.isValidEmail("a1+be.d@example1.com"));
        // EP: long hyphenated domain (valid).
        assertTrue(Email.isValidEmail("peter_jack@very-very-very-long-example.com"));
        // EP: long local part (valid).
        assertTrue(Email.isValidEmail("if.you.dream.it_you.can.do.it@example.com"));
        // EP: domain with multiple periods (valid).
        assertTrue(Email.isValidEmail("e1234567@u.nus.edu"));
    }

    @Test
    public void equals() {
        Email email = new Email("valid@email");

        // EP: Another email with the same value (equivalent).
        assertTrue(email.equals(new Email("valid@email")));

        // EP: The same email object (equivalent).
        assertTrue(email.equals(email));

        // EP: Null (non-equivalent).
        assertFalse(email.equals(null));

        // EP: An object of another type (non-equivalent).
        assertFalse(email.equals(5.0f));

        // EP: A different email value (non-equivalent).
        assertFalse(email.equals(new Email("other.valid@email")));
    }
}
