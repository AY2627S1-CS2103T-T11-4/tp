package seedu.address.model.exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class ExerciseTest {

    private static final ExerciseName VALID_NAME = new ExerciseName("Squats");
    private static final ExerciseWeight VALID_WEIGHT = new ExerciseWeight(20.5);
    private static final ExerciseReps VALID_REPETITIONS = new ExerciseReps(10);

    @Test
    public void constructor_validValues_storesValues() {
        Exercise exercise = new Exercise(VALID_NAME, VALID_WEIGHT, VALID_REPETITIONS);

        assertEquals(VALID_NAME, exercise.getName());
        assertEquals(VALID_WEIGHT, exercise.getWeight());
        assertEquals(VALID_REPETITIONS, exercise.getRepetitions());
    }

    @Test
    public void constructor_nullValues_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new Exercise(null, VALID_WEIGHT, VALID_REPETITIONS));
        assertThrows(NullPointerException.class, () ->
                new Exercise(VALID_NAME, null, VALID_REPETITIONS));
        assertThrows(NullPointerException.class, () ->
                new Exercise(VALID_NAME, VALID_WEIGHT, null));
    }

    @Test
    public void equals() {
        Exercise exercise = new Exercise(VALID_NAME, VALID_WEIGHT, VALID_REPETITIONS);
        Exercise sameValues = new Exercise(VALID_NAME, VALID_WEIGHT, VALID_REPETITIONS);

        assertTrue(exercise.equals(exercise));
        assertTrue(exercise.equals(sameValues));
        assertFalse(exercise.equals(null));
        assertFalse(exercise.equals(VALID_NAME));
        assertFalse(exercise.equals(new Exercise(new ExerciseName("Lunges"),
                VALID_WEIGHT, VALID_REPETITIONS)));
        assertFalse(exercise.equals(new Exercise(VALID_NAME, new ExerciseWeight(25.0),
                VALID_REPETITIONS)));
        assertFalse(exercise.equals(new Exercise(VALID_NAME, VALID_WEIGHT,
                new ExerciseReps(11))));
    }

    @Test
    public void hashCode_equalExercises_haveSameHashCode() {
        Exercise first = new Exercise(VALID_NAME, VALID_WEIGHT, VALID_REPETITIONS);
        Exercise second = new Exercise(VALID_NAME, VALID_WEIGHT, VALID_REPETITIONS);

        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    public void toStringMethod_returnsFormattedExercise() {
        Exercise exercise = new Exercise(VALID_NAME, VALID_WEIGHT, VALID_REPETITIONS);

        assertEquals("Squats (20.5 kg, 10 reps)", exercise.toString());
    }
}
