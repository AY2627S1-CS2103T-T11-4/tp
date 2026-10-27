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
        assertThrows(NullPointerException.class, () ->
                new Exercise(null, DESCRIPTION_VALID));

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

        // same object -> returns true
        assertTrue(exercise.isSameExercise(exercise));

        // same values -> returns true
        assertTrue(exercise.isSameExercise(
                new Exercise(NAME_VALID, DESCRIPTION_VALID)));

        // null -> returns false
        assertFalse(exercise.isSameExercise(null));

        // different name -> returns false
        assertFalse(exercise.isSameExercise(
                new Exercise(NAME_OTHER, DESCRIPTION_VALID)));

        // different description -> returns false
        assertFalse(exercise.isSameExercise(
                new Exercise(NAME_VALID, DESCRIPTION_OTHER)));

        // case-sensitive name comparison -> returns false
        assertFalse(exercise.isSameExercise(
                new Exercise(new ExerciseName("squats"), DESCRIPTION_VALID)));
    }

    @Test
    public void equals() {
        Exercise exercise = new Exercise(NAME_VALID, DESCRIPTION_VALID);

        // same values -> returns true
        assertTrue(exercise.equals(new Exercise(NAME_VALID, DESCRIPTION_VALID)));

        // same object -> returns true
        assertTrue(exercise.equals(exercise));

        // null -> returns false
        assertFalse(exercise.equals(null));

        // different type -> returns false
        assertFalse(exercise.equals(NAME_VALID));

        // different name -> returns false
        assertFalse(exercise.equals(new Exercise(NAME_OTHER, DESCRIPTION_VALID)));

        // different description -> returns false
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
