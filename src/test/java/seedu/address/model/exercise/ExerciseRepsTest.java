package seedu.address.model.exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class ExerciseRepsTest {

    @Test
    public void constructor_boundaryValues_areAccepted() {
        assertEquals(ExerciseReps.MIN_REPS, new ExerciseReps(ExerciseReps.MIN_REPS).getValue());
        assertEquals(ExerciseReps.MAX_REPS, new ExerciseReps(ExerciseReps.MAX_REPS).getValue());
    }

    @Test
    public void constructor_valuesOutsideRange_throwException() {
        assertThrows(IllegalArgumentException.class, () -> new ExerciseReps(0));
        assertThrows(IllegalArgumentException.class, () -> new ExerciseReps(1001));
    }

    @Test
    public void validationHelper_returnsExpectedResults() {
        assertTrue(ExerciseReps.isValidReps(1));
        assertTrue(ExerciseReps.isValidReps(1000));
        assertFalse(ExerciseReps.isValidReps(0));
        assertFalse(ExerciseReps.isValidReps(1001));
    }

    @Test
    public void equals() {
        ExerciseReps reps = new ExerciseReps(10);
        ExerciseReps sameValues = new ExerciseReps(10);

        assertTrue(reps.equals(reps));
        assertTrue(reps.equals(sameValues));
        assertFalse(reps.equals(null));
        assertFalse(reps.equals(10));
        assertFalse(reps.equals(new ExerciseReps(11)));
    }

    @Test
    public void hashCode_equalReps_haveSameHashCode() {
        assertEquals(new ExerciseReps(10).hashCode(), new ExerciseReps(10).hashCode());
    }
}
