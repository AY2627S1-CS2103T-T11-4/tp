package seedu.address.model.workoutplan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class WorkoutDescriptionTest {

    private static final String DESCRIPTION_VALID = "Squats, push-ups";
    private static final String DESCRIPTION_OTHER = "Running";

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new WorkoutDescription(null));
    }

    @Test
    public void constructor_invalidWorkoutDescription_throwsIllegalArgumentException() {
        String invalidWorkoutDescription = "";
        assertThrows(IllegalArgumentException.class, () -> new WorkoutDescription(invalidWorkoutDescription));
    }

    @Test
    public void constructor_validDescription_storesValue() {
        WorkoutDescription description = new WorkoutDescription(DESCRIPTION_VALID);
        assertEquals(DESCRIPTION_VALID, description.getValue());
    }

    @Test
    public void isValidWorkoutDescription() {
        // null description
        assertThrows(NullPointerException.class, () -> WorkoutDescription.isValidWorkoutDescription(null));

        // invalid description
        assertFalse(WorkoutDescription.isValidWorkoutDescription("")); // empty string
        assertFalse(WorkoutDescription.isValidWorkoutDescription(" ")); // spaces only

        // valid description
        assertTrue(WorkoutDescription.isValidWorkoutDescription(DESCRIPTION_VALID));
    }

    @Test
    public void equals() {
        WorkoutDescription description = new WorkoutDescription(DESCRIPTION_VALID);

        // same values -> returns true
        assertTrue(description.equals(new WorkoutDescription(DESCRIPTION_VALID)));

        // same object -> returns true
        assertTrue(description.equals(description));

        // null -> returns false
        assertFalse(description.equals(null));

        // different type -> returns false
        assertFalse(description.equals(DESCRIPTION_VALID));

        // different values -> returns false
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
