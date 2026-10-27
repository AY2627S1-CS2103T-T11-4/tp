package seedu.address.model.exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class ExerciseDescriptionTest {

    private static final String DESCRIPTION_VALID = "Barbell back squat";
    private static final String DESCRIPTION_OTHER = "Bodyweight squat";

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ExerciseDescription(null));
    }

    @Test
    public void constructor_invalidExerciseDescription_throwsIllegalArgumentException() {
        String invalidExerciseDescription = "";
        assertThrows(IllegalArgumentException.class, () ->
                new ExerciseDescription(invalidExerciseDescription));
    }

    @Test
    public void constructor_validDescription_storesValue() {
        ExerciseDescription description = new ExerciseDescription(DESCRIPTION_VALID);

        assertEquals(DESCRIPTION_VALID, description.getValue());
    }

    @Test
    public void isValidExerciseDescription() {
        assertThrows(NullPointerException.class, () ->
                ExerciseDescription.isValidExerciseDescription(null));
        assertFalse(ExerciseDescription.isValidExerciseDescription(""));
        assertFalse(ExerciseDescription.isValidExerciseDescription(" "));
        assertFalse(ExerciseDescription.isValidExerciseDescription("\t"));
        assertTrue(ExerciseDescription.isValidExerciseDescription(DESCRIPTION_VALID));
    }

    @Test
    public void equals() {
        ExerciseDescription description = new ExerciseDescription(DESCRIPTION_VALID);

        assertTrue(description.equals(new ExerciseDescription(DESCRIPTION_VALID)));
        assertTrue(description.equals(description));
        assertFalse(description.equals(null));
        assertFalse(description.equals(DESCRIPTION_VALID));
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
