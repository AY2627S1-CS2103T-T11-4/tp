package seedu.address.model.exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class ExerciseNameTest {

    private static final String NAME_VALID = "Squats";
    private static final String NAME_OTHER = "Push Ups";

    // EP: exercise name is null (invalid).
    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ExerciseName(null));
    }

    // BV: length < 1 (invalid).
    @Test
    public void constructor_invalidExerciseName_throwsIllegalArgumentException() {
        String invalidExerciseName = "";
        assertThrows(IllegalArgumentException.class, () -> new ExerciseName(invalidExerciseName));
    }

    // EP: non-blank exercise name (valid).
    @Test
    public void constructor_validName_storesValue() {
        ExerciseName name = new ExerciseName(NAME_VALID);

        assertEquals(NAME_VALID, name.getValue());
    }

    @Test
    public void isValidName() {
        // EP: exercise name is null (invalid).
        assertThrows(NullPointerException.class, () -> ExerciseName.isValidName(null));

        // BV: length < 1 (invalid).
        assertFalse(ExerciseName.isValidName(""));
        // EP: space-only exercise name (invalid).
        assertFalse(ExerciseName.isValidName(" "));
        // EP: tab-only exercise name (invalid).
        assertFalse(ExerciseName.isValidName("\t"));
        // Each valid input at least once: a symbol-only name is accepted.
        assertTrue(ExerciseName.isValidName("^"));
        // Each valid input at least once: an alphanumeric-symbol name is accepted.
        assertTrue(ExerciseName.isValidName("peter*"));
        // Each valid input at least once: leading whitespace is accepted.
        assertTrue(ExerciseName.isValidName(" Squats"));
        // Each valid input at least once: trailing whitespace is accepted.
        assertTrue(ExerciseName.isValidName("Squats "));
        // Each valid input at least once: an alphabetic name is accepted.
        assertTrue(ExerciseName.isValidName("peter jack"));
        // Each valid input at least once: a numeric name is accepted.
        assertTrue(ExerciseName.isValidName("12345"));
        // Each valid input at least once: an alphanumeric name is accepted.
        assertTrue(ExerciseName.isValidName("peter the 2nd"));
        // EP: mixed-case exercise name (valid).
        assertTrue(ExerciseName.isValidName("Capital Tan"));
        // EP: long non-blank exercise name (valid).
        assertTrue(ExerciseName.isValidName("David Roger Jackson Ray Jr 2nd"));
    }

    @Test
    public void equals() {
        ExerciseName name = new ExerciseName(NAME_VALID);

        // EP: Another exercise name with the same value (equivalent).
        assertTrue(name.equals(new ExerciseName(NAME_VALID)));

        // EP: The same exercise name object (equivalent).
        assertTrue(name.equals(name));

        // EP: Null (non-equivalent).
        assertFalse(name.equals(null));

        // EP: An object of another type (non-equivalent).
        assertFalse(name.equals(NAME_VALID));

        // EP: A different name value (non-equivalent).
        assertFalse(name.equals(new ExerciseName(NAME_OTHER)));

        // EP: Same letters with different case (non-equivalent).
        assertFalse(name.equals(new ExerciseName("squats")));
    }

    @Test
    public void hashCode_equalNames_haveSameHashCode() {
        assertEquals(new ExerciseName(NAME_VALID).hashCode(),
                new ExerciseName(NAME_VALID).hashCode());
    }

    @Test
    public void toStringMethod() {
        assertEquals(NAME_VALID, new ExerciseName(NAME_VALID).toString());
    }
}
