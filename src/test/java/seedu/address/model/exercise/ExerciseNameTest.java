package seedu.address.model.exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class ExerciseNameTest {

    private static final String NAME_VALID = "Squats";
    private static final String NAME_OTHER = "Push Ups";

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ExerciseName(null));
    }

    @Test
    public void constructor_invalidExerciseName_throwsIllegalArgumentException() {
        String invalidExerciseName = "";
        assertThrows(IllegalArgumentException.class, () -> new ExerciseName(invalidExerciseName));
    }

    @Test
    public void constructor_validName_storesValue() {
        ExerciseName name = new ExerciseName(NAME_VALID);

        assertEquals(NAME_VALID, name.getValue());
    }

    @Test
    public void isValidName() {
        // null name
        assertThrows(NullPointerException.class, () -> ExerciseName.isValidName(null));

        // invalid names
        assertFalse(ExerciseName.isValidName("")); // empty string
        assertFalse(ExerciseName.isValidName(" ")); // spaces only
        assertFalse(ExerciseName.isValidName("\t")); // whitespace only
        assertTrue(ExerciseName.isValidName("^")); // symbols are allowed when nonblank
        assertTrue(ExerciseName.isValidName("peter*")); // symbols are allowed when nonblank
        assertTrue(ExerciseName.isValidName(" Squats")); // leading whitespace is allowed
        assertTrue(ExerciseName.isValidName("Squats ")); // trailing whitespace is allowed

        // valid names
        assertTrue(ExerciseName.isValidName("peter jack")); // alphabets only
        assertTrue(ExerciseName.isValidName("12345")); // numbers only
        assertTrue(ExerciseName.isValidName("peter the 2nd")); // alphanumeric characters
        assertTrue(ExerciseName.isValidName("Capital Tan")); // with capital letters
        assertTrue(ExerciseName.isValidName("David Roger Jackson Ray Jr 2nd")); // long names
    }

    @Test
    public void equals() {
        ExerciseName name = new ExerciseName(NAME_VALID);

        // same values -> returns true
        assertTrue(name.equals(new ExerciseName(NAME_VALID)));

        // same object -> returns true
        assertTrue(name.equals(name));

        // null -> returns false
        assertFalse(name.equals(null));

        // different type -> returns false
        assertFalse(name.equals(NAME_VALID));

        // different values -> returns false
        assertFalse(name.equals(new ExerciseName(NAME_OTHER)));

        // case-sensitive values -> returns false
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
