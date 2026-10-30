package seedu.address.model.exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class ExerciseDescriptionTest {

    private static final String DESCRIPTION_VALID = "Barbell back squat";
    private static final String DESCRIPTION_OTHER = "Bodyweight squat";

    // EP: exercise description is null (invalid).
    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ExerciseDescription(null));
    }

    // BV: length < 1 (invalid).
    @Test
    public void constructor_invalidExerciseDescription_throwsIllegalArgumentException() {
        String invalidExerciseDescription = "";
        assertThrows(IllegalArgumentException.class, () ->
                new ExerciseDescription(invalidExerciseDescription));
    }

    // EP: non-blank exercise description (valid).
    @Test
    public void constructor_validDescription_storesValue() {
        ExerciseDescription description = new ExerciseDescription(DESCRIPTION_VALID);

        assertEquals(DESCRIPTION_VALID, description.getValue());
    }

    @Test
    public void isValidExerciseDescription() {
        // EP: exercise description is null (invalid).
        assertThrows(NullPointerException.class, () ->
                ExerciseDescription.isValidExerciseDescription(null));
        // BV: length < 1 (invalid).
        assertFalse(ExerciseDescription.isValidExerciseDescription(""));
        // EP: space-only exercise description (invalid).
        assertFalse(ExerciseDescription.isValidExerciseDescription(" "));
        // EP: tab-only exercise description (invalid).
        assertFalse(ExerciseDescription.isValidExerciseDescription("\t"));
        // BV: 1 <= length <= MAX_LENGTH (valid).
        assertTrue(ExerciseDescription.isValidExerciseDescription(
                "a".repeat(ExerciseDescription.MAX_LENGTH)));
        // BV: length > MAX_LENGTH (invalid).
        assertFalse(ExerciseDescription.isValidExerciseDescription(
                "a".repeat(ExerciseDescription.MAX_LENGTH + 1)));
        // Each valid input at least once: a typical description is accepted.
        assertTrue(ExerciseDescription.isValidExerciseDescription(DESCRIPTION_VALID));
    }

    @Test
    public void equals() {
        ExerciseDescription description = new ExerciseDescription(DESCRIPTION_VALID);

        // EP: Another description with the same value (equivalent).
        assertTrue(description.equals(new ExerciseDescription(DESCRIPTION_VALID)));
        // EP: The same description object (equivalent).
        assertTrue(description.equals(description));
        // EP: Null (non-equivalent).
        assertFalse(description.equals(null));
        // EP: An object of another type (non-equivalent).
        assertFalse(description.equals(DESCRIPTION_VALID));
        // EP: A different description value (non-equivalent).
        assertFalse(description.equals(new ExerciseDescription(DESCRIPTION_OTHER)));
    }

    @Test
    public void hashCode_equalDescriptions_haveSameHashCode() {
        assertEquals(new ExerciseDescription(DESCRIPTION_VALID).hashCode(),
                new ExerciseDescription(DESCRIPTION_VALID).hashCode());
    }

    @Test
    public void toStringMethod() {
        assertEquals(DESCRIPTION_VALID, new ExerciseDescription(DESCRIPTION_VALID).toString());
    }
}
