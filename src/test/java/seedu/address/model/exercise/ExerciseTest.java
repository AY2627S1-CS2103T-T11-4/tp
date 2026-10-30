package seedu.address.model.exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class ExerciseTest {

    private static final ExerciseName NAME_VALID = new ExerciseName("Squats");
    private static final ExerciseName NAME_OTHER = new ExerciseName("Push Ups");
    private static final ExerciseDescription DESCRIPTION_VALID = new ExerciseDescription("Barbell back squat");
    private static final ExerciseDescription DESCRIPTION_OTHER = new ExerciseDescription("Bodyweight squat");

    @Test
    public void constructor_nullFields_throwsNullPointerException() {
        // One invalid input at a time: only the exercise name is null.
        assertThrows(NullPointerException.class, () ->
                new Exercise(null, DESCRIPTION_VALID));

        // One invalid input at a time: only the exercise description is null.
        assertThrows(NullPointerException.class, () ->
                new Exercise(NAME_VALID, null));
    }

    @Test
    public void constructor_validFields_storesValues() {
        Exercise exercise = new Exercise(NAME_VALID, DESCRIPTION_VALID);

        assertEquals(NAME_VALID, exercise.getName());
        assertEquals(DESCRIPTION_VALID, exercise.getDescription());
    }

    @Test
    public void isSameExercise() {
        Exercise exercise = new Exercise(NAME_VALID, DESCRIPTION_VALID);

        // EP: the same exercise object (same identity).
        assertTrue(exercise.isSameExercise(exercise));

        // EP: equal name and description (same identity).
        assertTrue(exercise.isSameExercise(
                new Exercise(NAME_VALID, DESCRIPTION_VALID)));

        // EP: null (different identity).
        assertFalse(exercise.isSameExercise(null));

        // EP: only the exercise name differs (different identity).
        assertFalse(exercise.isSameExercise(
                new Exercise(NAME_OTHER, DESCRIPTION_VALID)));

        // EP: only the exercise description differs (different identity).
        assertFalse(exercise.isSameExercise(
                new Exercise(NAME_VALID, DESCRIPTION_OTHER)));

        // EP: exercise name differs only by case (different identity).
        assertFalse(exercise.isSameExercise(
                new Exercise(new ExerciseName("squats"), DESCRIPTION_VALID)));
    }

    @Test
    public void equals() {
        Exercise exercise = new Exercise(NAME_VALID, DESCRIPTION_VALID);

        // EP: Another exercise with equal fields (equivalent).
        assertTrue(exercise.equals(new Exercise(NAME_VALID, DESCRIPTION_VALID)));

        // EP: The same exercise object (equivalent).
        assertTrue(exercise.equals(exercise));

        // EP: Null (non-equivalent).
        assertFalse(exercise.equals(null));

        // EP: An object of another type (non-equivalent).
        assertFalse(exercise.equals(NAME_VALID));

        // EP: Only the exercise name differs (non-equivalent).
        assertFalse(exercise.equals(new Exercise(NAME_OTHER, DESCRIPTION_VALID)));

        // EP: Only the exercise description differs (non-equivalent).
        assertFalse(exercise.equals(new Exercise(NAME_VALID, DESCRIPTION_OTHER)));
    }

    @Test
    public void hashCode_equalExercises_haveSameHashCode() {
        Exercise first = new Exercise(NAME_VALID, DESCRIPTION_VALID);
        Exercise second = new Exercise(NAME_VALID, DESCRIPTION_VALID);

        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    public void toStringMethod() {
        Exercise exercise = new Exercise(NAME_VALID, DESCRIPTION_VALID);

        assertEquals("Squats (Barbell back squat)", exercise.toString());
    }
}
