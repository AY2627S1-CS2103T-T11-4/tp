package seedu.address.model.workoutplan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class WorkoutTitleTest {

    private static final String TITLE_VALID = "Strength training";
    private static final String TITLE_OTHER = "Cardio";

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new WorkoutTitle(null));
    }

    @Test
    public void constructor_invalidWorkoutTitle_throwsIllegalArgumentException() {
        String invalidWorkoutTitle = "";
        assertThrows(IllegalArgumentException.class, () -> new WorkoutTitle(invalidWorkoutTitle));
    }

    @Test
    public void constructor_validTitle_storesValue() {
        WorkoutTitle title = new WorkoutTitle(TITLE_VALID);
        assertEquals(TITLE_VALID, title.getValue());
    }

    @Test
    public void isValidWorkoutTitle() {
        // null title
        assertThrows(NullPointerException.class, () -> WorkoutTitle.isValidWorkoutTitle(null));

        // invalid title
        assertFalse(WorkoutTitle.isValidWorkoutTitle("")); // empty string
        assertFalse(WorkoutTitle.isValidWorkoutTitle(" ")); // spaces only

        // valid title
        assertTrue(WorkoutTitle.isValidWorkoutTitle(TITLE_VALID));
    }

    @Test
    public void equals() {
        WorkoutTitle title = new WorkoutTitle(TITLE_VALID);

        // same values -> returns true
        assertTrue(title.equals(new WorkoutTitle(TITLE_VALID)));

        // same object -> returns true
        assertTrue(title.equals(title));

        // null -> returns false
        assertFalse(title.equals(null));

        // different type -> returns false
        assertFalse(title.equals(TITLE_VALID));

        // different values -> returns false
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
