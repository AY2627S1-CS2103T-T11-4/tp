package seedu.address.model.exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class ExerciseWeightTest {

    private static final double WEIGHT_VALID = 12.5;
    private static final double WEIGHT_OTHER = 10.0;

    @Test
    public void constructor_invalidWeight_throwsIllegalArgumentException() {
        double invalidWeight = -1.0;
        assertThrows(IllegalArgumentException.class, () -> new ExerciseWeight(invalidWeight));
    }

    @Test
    public void constructor_validWeight_storesValue() {
        assertEquals(0.0, new ExerciseWeight(0.0).getValue());
        assertEquals(20.0, new ExerciseWeight(20.0).getValue());
        assertEquals(WEIGHT_VALID, new ExerciseWeight(WEIGHT_VALID).getValue());
        assertEquals(0.0, new ExerciseWeight(-0.0).getValue());
    }

    @Test
    public void isValidWeight() {
        // invalid weights
        assertFalse(ExerciseWeight.isValidWeight(-1.0));
        assertFalse(ExerciseWeight.isValidWeight(Double.NaN));
        assertFalse(ExerciseWeight.isValidWeight(Double.POSITIVE_INFINITY));
        assertFalse(ExerciseWeight.isValidWeight(Double.NEGATIVE_INFINITY));

        // valid weights
        assertTrue(ExerciseWeight.isValidWeight(0.0));
        assertTrue(ExerciseWeight.isValidWeight(WEIGHT_VALID));
    }

    @Test
    public void equals() {
        ExerciseWeight weight = new ExerciseWeight(WEIGHT_VALID);

        // same values -> returns true
        assertTrue(weight.equals(new ExerciseWeight(WEIGHT_VALID)));

        // same object -> returns true
        assertTrue(weight.equals(weight));

        // null -> returns false
        assertFalse(weight.equals(null));

        // different type -> returns false
        assertFalse(weight.equals(12.5));

        // different values -> returns false
        assertFalse(weight.equals(new ExerciseWeight(WEIGHT_OTHER)));
    }

    @Test
    public void hashCode_equalWeights_haveSameHashCode() {
        assertEquals(new ExerciseWeight(WEIGHT_VALID).hashCode(),
                new ExerciseWeight(WEIGHT_VALID).hashCode());
    }

    @Test
    public void toStringMethod() {
        assertEquals("12.5", new ExerciseWeight(WEIGHT_VALID).toString());
    }
}
