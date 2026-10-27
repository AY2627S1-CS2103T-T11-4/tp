package seedu.address.model.exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class ExerciseRepsTest {

    private static final int REPS_VALID = 10;
    private static final int REPS_OTHER = 11;

    @Test
    public void constructor_invalidReps_throwsIllegalArgumentException() {
        int invalidReps = 0;
        assertThrows(IllegalArgumentException.class, () -> new ExerciseReps(invalidReps));
    }

    @Test
    public void constructor_validReps_storesValue() {
        assertEquals(ExerciseReps.MIN_REPS, new ExerciseReps(ExerciseReps.MIN_REPS).getValue());
        assertEquals(ExerciseReps.MAX_REPS, new ExerciseReps(ExerciseReps.MAX_REPS).getValue());
    }

    @Test
    public void isValidReps() {
        // invalid repetitions
        assertFalse(ExerciseReps.isValidReps(0));
        assertFalse(ExerciseReps.isValidReps(ExerciseReps.MAX_REPS + 1));

        // valid repetitions
        assertTrue(ExerciseReps.isValidReps(ExerciseReps.MIN_REPS));
        assertTrue(ExerciseReps.isValidReps(ExerciseReps.MAX_REPS));
    }

    @Test
    public void equals() {
        ExerciseReps reps = new ExerciseReps(REPS_VALID);

        // same values -> returns true
        assertTrue(reps.equals(new ExerciseReps(REPS_VALID)));

        // same object -> returns true
        assertTrue(reps.equals(reps));

        // null -> returns false
        assertFalse(reps.equals(null));

        // different type -> returns false
        assertFalse(reps.equals(10));

        // different values -> returns false
        assertFalse(reps.equals(new ExerciseReps(REPS_OTHER)));
    }

    @Test
    public void hashCode_equalReps_haveSameHashCode() {
        assertEquals(new ExerciseReps(REPS_VALID).hashCode(), new ExerciseReps(REPS_VALID).hashCode());
    }

    @Test
    public void toStringMethod() {
        assertEquals("10", new ExerciseReps(REPS_VALID).toString());
    }
}
