package seedu.address.model.exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class ExerciseTest {

    private static final ExerciseName NAME_VALID = new ExerciseName("Squats");
    private static final ExerciseName NAME_OTHER = new ExerciseName("Push-ups");
    private static final ExerciseWeight WEIGHT_VALID = new ExerciseWeight(20.5);
    private static final ExerciseWeight WEIGHT_OTHER = new ExerciseWeight(10.0);
    private static final ExerciseReps REPETITIONS_VALID = new ExerciseReps(10);
    private static final ExerciseReps REPETITIONS_OTHER = new ExerciseReps(11);

    @Test
    public void constructor_nullName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new Exercise(null, WEIGHT_VALID, REPETITIONS_VALID));
    }

    @Test
    public void constructor_nullWeight_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new Exercise(NAME_VALID, null, REPETITIONS_VALID));
    }

    @Test
    public void constructor_nullRepetitions_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new Exercise(NAME_VALID, WEIGHT_VALID, null));
    }

    @Test
    public void constructor_validFields_storesValues() {
        Exercise exercise = new Exercise(NAME_VALID, WEIGHT_VALID, REPETITIONS_VALID);

        assertEquals(NAME_VALID, exercise.getName());
        assertEquals(WEIGHT_VALID, exercise.getWeight());
        assertEquals(REPETITIONS_VALID, exercise.getRepetitions());
    }

    @Test
    public void equals() {
        Exercise exercise = new Exercise(NAME_VALID, WEIGHT_VALID, REPETITIONS_VALID);

        // same values -> returns true
        assertTrue(exercise.equals(new Exercise(NAME_VALID, WEIGHT_VALID, REPETITIONS_VALID)));

        // same object -> returns true
        assertTrue(exercise.equals(exercise));

        // null -> returns false
        assertFalse(exercise.equals(null));

        // different type -> returns false
        assertFalse(exercise.equals(NAME_VALID));

        // different name -> returns false
        assertFalse(exercise.equals(new Exercise(NAME_OTHER, WEIGHT_VALID, REPETITIONS_VALID)));

        // different weight -> returns false
        assertFalse(exercise.equals(new Exercise(NAME_VALID, WEIGHT_OTHER, REPETITIONS_VALID)));

        // different repetitions -> returns false
        assertFalse(exercise.equals(new Exercise(NAME_VALID, WEIGHT_VALID, REPETITIONS_OTHER)));
    }

    @Test
    public void hashCode_equalExercises_haveSameHashCode() {
        Exercise first = new Exercise(NAME_VALID, WEIGHT_VALID, REPETITIONS_VALID);
        Exercise second = new Exercise(NAME_VALID, WEIGHT_VALID, REPETITIONS_VALID);

        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    public void toStringMethod() {
        Exercise exercise = new Exercise(NAME_VALID, WEIGHT_VALID, REPETITIONS_VALID);

        assertEquals("Squats (20.5 kg, 10 reps)", exercise.toString());
    }
}
