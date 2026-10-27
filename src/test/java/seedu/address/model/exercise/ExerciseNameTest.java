package seedu.address.model.exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class ExerciseNameTest {

    private static final String VALID_NAME = "Squats";

    @Test
    public void constructor_validValues_storesValue() {
        ExerciseName name = new ExerciseName(VALID_NAME);

        assertEquals(VALID_NAME, name.getValue());
        assertEquals(VALID_NAME, name.toString());
    }

    @Test
    public void constructor_nullBlankOrOverlongValue_throwsException() {
        assertThrows(NullPointerException.class, () -> new ExerciseName(null));
        assertThrows(IllegalArgumentException.class, () -> new ExerciseName(" "));
        assertThrows(IllegalArgumentException.class, () -> new ExerciseName("\t"));
        assertThrows(IllegalArgumentException.class, () ->
                new ExerciseName("a".repeat(ExerciseName.MAX_LENGTH + 1)));

        new ExerciseName("a".repeat(ExerciseName.MAX_LENGTH));
    }

    @Test
    public void validationHelper_returnsExpectedResults() {
        assertThrows(NullPointerException.class, () -> ExerciseName.isValidName(null));
        assertTrue(ExerciseName.isValidName("a".repeat(ExerciseName.MAX_LENGTH)));
        assertFalse(ExerciseName.isValidName("a".repeat(ExerciseName.MAX_LENGTH + 1)));
    }

    @Test
    public void equals() {
        ExerciseName name = new ExerciseName(VALID_NAME);
        ExerciseName sameValues = new ExerciseName(VALID_NAME);

        assertTrue(name.equals(name));
        assertTrue(name.equals(sameValues));
        assertFalse(name.equals(null));
        assertFalse(name.equals(VALID_NAME));
        assertFalse(name.equals(new ExerciseName(VALID_NAME.toLowerCase())));
    }

    @Test
    public void hashCode_equalNames_haveSameHashCode() {
        assertEquals(new ExerciseName(VALID_NAME).hashCode(),
                new ExerciseName(VALID_NAME).hashCode());
    }
}
