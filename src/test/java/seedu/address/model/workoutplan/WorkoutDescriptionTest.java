package seedu.address.model.workoutplan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class WorkoutDescriptionTest {

    private static final String DESCRIPTION_VALID = "Squats, push-ups";
    private static final String DESCRIPTION_OTHER = "Running";

    // EP: workout description is null (invalid).
    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new WorkoutDescription(null));
    }

    // BV: length < 1 (invalid).
    @Test
    public void constructor_invalidWorkoutDescription_throwsIllegalArgumentException() {
        String invalidWorkoutDescription = "";
        assertThrows(IllegalArgumentException.class, () -> new WorkoutDescription(invalidWorkoutDescription));
    }

    // EP: non-blank workout description (valid).
    @Test
    public void constructor_validDescription_storesValue() {
        WorkoutDescription description = new WorkoutDescription(DESCRIPTION_VALID);
        assertEquals(DESCRIPTION_VALID, description.getValue());
    }

    @Test
    public void isValidWorkoutDescription() {
        // EP: workout description is null (invalid).
        assertThrows(NullPointerException.class, () -> WorkoutDescription.isValidWorkoutDescription(null));

        // BV: length < 1 (invalid).
        assertFalse(WorkoutDescription.isValidWorkoutDescription(""));
        // EP: space-only workout description (invalid).
        assertFalse(WorkoutDescription.isValidWorkoutDescription(" "));
        // BV: length > MAX_LENGTH (invalid).
        assertFalse(WorkoutDescription.isValidWorkoutDescription("a".repeat(WorkoutDescription.MAX_LENGTH + 1)));

        // Each valid input at least once: a typical workout description is accepted.
        assertTrue(WorkoutDescription.isValidWorkoutDescription(DESCRIPTION_VALID));
        // BV: 1 <= length <= MAX_LENGTH (valid).
        assertTrue(WorkoutDescription.isValidWorkoutDescription("a".repeat(WorkoutDescription.MAX_LENGTH)));
    }

    @Test
    public void equals() {
        WorkoutDescription description = new WorkoutDescription(DESCRIPTION_VALID);

        // EP: Another description with the same value (equivalent).
        assertTrue(description.equals(new WorkoutDescription(DESCRIPTION_VALID)));

        // EP: The same description object (equivalent).
        assertTrue(description.equals(description));

        // EP: Null (non-equivalent).
        assertFalse(description.equals(null));

        // EP: An object of another type (non-equivalent).
        assertFalse(description.equals(DESCRIPTION_VALID));

        // EP: A different description value (non-equivalent).
        assertFalse(description.equals(new WorkoutDescription(DESCRIPTION_OTHER)));
    }

    @Test
    public void hashCode_equalDescriptions_haveSameHashCode() {
        assertEquals(new WorkoutDescription(DESCRIPTION_VALID).hashCode(),
                new WorkoutDescription(DESCRIPTION_VALID).hashCode());
    }

    @Test
    public void toStringMethod() {
        assertEquals(DESCRIPTION_VALID, new WorkoutDescription(DESCRIPTION_VALID).toString());
    }
}
