package seedu.address.model.exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class ExerciseNameTest {

    private static final String NAME_VALID = "Squats";
    private static final String NAME_OTHER = "Push-ups";

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
        assertFalse(ExerciseName.isValidName("a".repeat(ExerciseName.MAX_LENGTH + 1)));

        // valid names
        assertTrue(ExerciseName.isValidName("a".repeat(ExerciseName.MAX_LENGTH)));
        assertTrue(ExerciseName.isValidName(NAME_VALID));
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
