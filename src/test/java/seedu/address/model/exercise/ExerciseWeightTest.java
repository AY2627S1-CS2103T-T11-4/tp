package seedu.address.model.exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class ExerciseWeightTest {

    @Test
    public void constructor_validValues_storesValue() {
        assertEquals(0.0, new ExerciseWeight(0.0).getValue());
        assertEquals(20.0, new ExerciseWeight(20.0).getValue());
        assertEquals(12.5, new ExerciseWeight(12.5).getValue());
    }

    @Test
    public void constructor_negativeNonFiniteValues_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new ExerciseWeight(-1.0));
        assertThrows(IllegalArgumentException.class, () -> new ExerciseWeight(Double.NaN));
        assertThrows(IllegalArgumentException.class, () ->
                new ExerciseWeight(Double.POSITIVE_INFINITY));
        assertThrows(IllegalArgumentException.class, () ->
                new ExerciseWeight(Double.NEGATIVE_INFINITY));
    }

    @Test
    public void constructor_negativeZero_normalizesToZero() {
        ExerciseWeight weight = new ExerciseWeight(-0.0);

        assertEquals(0.0, weight.getValue());
        assertEquals(new ExerciseWeight(0.0), weight);
    }

    @Test
    public void validationHelper_returnsExpectedResults() {
        assertTrue(ExerciseWeight.isValidWeight(0.0));
        assertTrue(ExerciseWeight.isValidWeight(12.5));
        assertFalse(ExerciseWeight.isValidWeight(-1.0));
        assertFalse(ExerciseWeight.isValidWeight(Double.NaN));
        assertFalse(ExerciseWeight.isValidWeight(Double.POSITIVE_INFINITY));
    }

    @Test
    public void equals() {
        ExerciseWeight weight = new ExerciseWeight(12.5);
        ExerciseWeight sameValues = new ExerciseWeight(12.5);

        assertTrue(weight.equals(weight));
        assertTrue(weight.equals(sameValues));
        assertFalse(weight.equals(null));
        assertFalse(weight.equals(12.5));
        assertFalse(weight.equals(new ExerciseWeight(10.0)));
    }

    @Test
    public void hashCode_equalWeights_haveSameHashCode() {
        assertEquals(new ExerciseWeight(12.5).hashCode(),
                new ExerciseWeight(12.5).hashCode());
    }
}
