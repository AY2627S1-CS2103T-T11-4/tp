package seedu.address.model.exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class ExerciseTest {

    private static final String VALID_NAME = "Squats";
    private static final String VALID_DESCRIPTION = "Barbell back squat";
    private static final int VALID_REPETITIONS = 10;

    @Test
    public void constructor_validValues_storesValues() {
        Exercise exercise = new Exercise(VALID_NAME, VALID_DESCRIPTION, VALID_REPETITIONS);

        assertEquals(VALID_NAME, exercise.getName());
        assertEquals(VALID_DESCRIPTION, exercise.getDescription());
        assertEquals(VALID_REPETITIONS, exercise.getRepetitions());
    }

    @Test
    public void constructor_boundaryValues_areAccepted() {
        String maximumLengthValue = "a".repeat(Exercise.MAX_NAME_LENGTH);
        Exercise exercise = new Exercise(maximumLengthValue, maximumLengthValue,
                Exercise.MAX_REPETITIONS);

        assertEquals(maximumLengthValue, exercise.getName());
        assertEquals(maximumLengthValue, exercise.getDescription());
        assertEquals(Exercise.MAX_REPETITIONS, exercise.getRepetitions());
    }

    @Test
    public void constructor_nullName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new Exercise(null, VALID_DESCRIPTION, VALID_REPETITIONS));
    }

    @Test
    public void constructor_nullDescription_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new Exercise(VALID_NAME, null, VALID_REPETITIONS));
    }

    @Test
    public void constructor_blankName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Exercise(" ", VALID_DESCRIPTION, VALID_REPETITIONS));
    }

    @Test
    public void constructor_blankDescription_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Exercise(VALID_NAME, "\t", VALID_REPETITIONS));
    }

    @Test
    public void constructor_overlongName_throwsIllegalArgumentException() {
        String overlongName = "a".repeat(Exercise.MAX_NAME_LENGTH + 1);

        assertThrows(IllegalArgumentException.class, () ->
                new Exercise(overlongName, VALID_DESCRIPTION, VALID_REPETITIONS));
    }

    @Test
    public void constructor_overlongDescription_throwsIllegalArgumentException() {
        String overlongDescription = "a".repeat(Exercise.MAX_DESCRIPTION_LENGTH + 1);

        assertThrows(IllegalArgumentException.class, () ->
                new Exercise(VALID_NAME, overlongDescription, VALID_REPETITIONS));
    }

    @Test
    public void constructor_invalidRepetitions_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Exercise(VALID_NAME, VALID_DESCRIPTION, Exercise.MIN_REPETITIONS - 1));
        assertThrows(IllegalArgumentException.class, () ->
                new Exercise(VALID_NAME, VALID_DESCRIPTION, Exercise.MAX_REPETITIONS + 1));
    }

    @Test
    public void validationHelpers_nullText_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> Exercise.isValidName(null));
        assertThrows(NullPointerException.class, () -> Exercise.isValidDescription(null));
    }

    @Test
    public void validationHelpers_boundaryValues_returnExpectedResults() {
        assertTrue(Exercise.isValidName("a".repeat(Exercise.MAX_NAME_LENGTH)));
        assertFalse(Exercise.isValidName("a".repeat(Exercise.MAX_NAME_LENGTH + 1)));
        assertTrue(Exercise.isValidDescription("a".repeat(Exercise.MAX_DESCRIPTION_LENGTH)));
        assertFalse(Exercise.isValidDescription("a".repeat(Exercise.MAX_DESCRIPTION_LENGTH + 1)));
        assertTrue(Exercise.isValidRepetitions(Exercise.MIN_REPETITIONS));
        assertTrue(Exercise.isValidRepetitions(Exercise.MAX_REPETITIONS));
        assertFalse(Exercise.isValidRepetitions(Exercise.MIN_REPETITIONS - 1));
        assertFalse(Exercise.isValidRepetitions(Exercise.MAX_REPETITIONS + 1));
    }

    @Test
    public void equals() {
        Exercise exercise = new Exercise(VALID_NAME, VALID_DESCRIPTION, VALID_REPETITIONS);
        Exercise sameValues = new Exercise(VALID_NAME, VALID_DESCRIPTION, VALID_REPETITIONS);

        assertTrue(exercise.equals(exercise));
        assertTrue(exercise.equals(sameValues));
        assertFalse(exercise.equals(null));
        assertFalse(exercise.equals(VALID_NAME));
        assertFalse(exercise.equals(new Exercise("Lunges", VALID_DESCRIPTION, VALID_REPETITIONS)));
        assertFalse(exercise.equals(new Exercise(VALID_NAME, "Bodyweight", VALID_REPETITIONS)));
        assertFalse(exercise.equals(new Exercise(VALID_NAME, VALID_DESCRIPTION, VALID_REPETITIONS + 1)));
        assertFalse(exercise.equals(new Exercise(VALID_NAME.toLowerCase(), VALID_DESCRIPTION, VALID_REPETITIONS)));
    }

    @Test
    public void hashCode_equalExercises_haveSameHashCode() {
        Exercise first = new Exercise(VALID_NAME, VALID_DESCRIPTION, VALID_REPETITIONS);
        Exercise second = new Exercise(VALID_NAME, VALID_DESCRIPTION, VALID_REPETITIONS);

        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    public void toStringMethod_returnsFormattedExercise() {
        Exercise exercise = new Exercise(VALID_NAME, VALID_DESCRIPTION, VALID_REPETITIONS);

        assertEquals("Squats - Barbell back squat (10 reps)", exercise.toString());
    }
}
