package seedu.address.model.workoutplan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class WorkoutNameTest {

    private static final String NAME_VALID = "Strength training";
    private static final String NAME_OTHER = "Cardio";

    // EP: workout name is null (invalid).
    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new WorkoutName(null));
    }

    // BV: length < 1 (invalid).
    @Test
    public void constructor_invalidWorkoutName_throwsIllegalArgumentException() {
        String invalidWorkoutName = "";
        assertThrows(IllegalArgumentException.class, () -> new WorkoutName(invalidWorkoutName));
    }

    // EP: non-blank workout name (valid).
    @Test
    public void constructor_validName_storesValue() {
        WorkoutName name = new WorkoutName(NAME_VALID);
        assertEquals(NAME_VALID, name.getValue());
    }

    @Test
    public void isValidName() {
        // EP: workout name is null (invalid).
        assertThrows(NullPointerException.class, () -> WorkoutName.isValidName(null));

        // BV: length < 1 (invalid).
        assertFalse(WorkoutName.isValidName(""));
        // EP: space-only workout name (invalid).
        assertFalse(WorkoutName.isValidName(" "));

        // Each valid input at least once: a typical workout name is accepted.
        assertTrue(WorkoutName.isValidName(NAME_VALID));
    }

    @Test
    public void equals() {
        WorkoutName name = new WorkoutName(NAME_VALID);

        // EP: Another name with the same value (equivalent).
        assertTrue(name.equals(new WorkoutName(NAME_VALID)));

        // EP: The same name object (equivalent).
        assertTrue(name.equals(name));

        // EP: Null (non-equivalent).
        assertFalse(name.equals(null));

        // EP: An object of another type (non-equivalent).
        assertFalse(name.equals(NAME_VALID));

        // EP: A different name value (non-equivalent).
        assertFalse(name.equals(new WorkoutName(NAME_OTHER)));
    }

    @Test
    public void hashCode_equalNames_haveSameHashCode() {
        assertEquals(new WorkoutName(NAME_VALID).hashCode(),
                new WorkoutName(NAME_VALID).hashCode());
    }

    @Test
    public void toStringMethod() {
        assertEquals(NAME_VALID, new WorkoutName(NAME_VALID).toString());
    }
}
