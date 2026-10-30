package seedu.address.model.workoutplan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class WorkoutTitleTest {

    private static final String TITLE_VALID = "Strength training";
    private static final String TITLE_OTHER = "Cardio";

    // EP: workout title is null (invalid).
    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new WorkoutTitle(null));
    }

    // BV: length < 1 (invalid).
    @Test
    public void constructor_invalidWorkoutTitle_throwsIllegalArgumentException() {
        String invalidWorkoutTitle = "";
        assertThrows(IllegalArgumentException.class, () -> new WorkoutTitle(invalidWorkoutTitle));
    }

    // EP: non-blank workout title (valid).
    @Test
    public void constructor_validTitle_storesValue() {
        WorkoutTitle title = new WorkoutTitle(TITLE_VALID);
        assertEquals(TITLE_VALID, title.getValue());
    }

    @Test
    public void isValidWorkoutTitle() {
        // EP: workout title is null (invalid).
        assertThrows(NullPointerException.class, () -> WorkoutTitle.isValidWorkoutTitle(null));

        // BV: length < 1 (invalid).
        assertFalse(WorkoutTitle.isValidWorkoutTitle(""));
        // EP: space-only workout title (invalid).
        assertFalse(WorkoutTitle.isValidWorkoutTitle(" "));

        // Each valid input at least once: a typical workout title is accepted.
        assertTrue(WorkoutTitle.isValidWorkoutTitle(TITLE_VALID));
    }

    @Test
    public void equals() {
        WorkoutTitle title = new WorkoutTitle(TITLE_VALID);

        // EP: Another title with the same value (equivalent).
        assertTrue(title.equals(new WorkoutTitle(TITLE_VALID)));

        // EP: The same title object (equivalent).
        assertTrue(title.equals(title));

        // EP: Null (non-equivalent).
        assertFalse(title.equals(null));

        // EP: An object of another type (non-equivalent).
        assertFalse(title.equals(TITLE_VALID));

        // EP: A different title value (non-equivalent).
        assertFalse(title.equals(new WorkoutTitle(TITLE_OTHER)));
    }

    @Test
    public void hashCode_equalTitles_haveSameHashCode() {
        assertEquals(new WorkoutTitle(TITLE_VALID).hashCode(),
                new WorkoutTitle(TITLE_VALID).hashCode());
    }

    @Test
    public void toStringMethod() {
        assertEquals(TITLE_VALID, new WorkoutTitle(TITLE_VALID).toString());
    }
}
