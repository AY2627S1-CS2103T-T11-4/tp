package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.exercise.Exercise;
import seedu.address.model.exercise.ExerciseDescription;
import seedu.address.model.exercise.ExerciseName;

public class JsonAdaptedExerciseTest {

    // EP: complete exercise survives a JSON round trip (valid).
    @Test
    public void modelToJsonAndBack_preservesFields() throws Exception {
        Exercise original = new Exercise(new ExerciseName("Bench press"),
                new ExerciseDescription("3 sets of 8 reps"));

        String json = JsonUtil.toJsonString(new JsonAdaptedExercise(original));
        Exercise restored = JsonUtil.fromJsonString(json, JsonAdaptedExercise.class).toModelType();

        assertEquals(original, restored);
    }

    @Test
    public void toModelType_missingOrInvalidFields_throwsIllegalValueException() {
        // One invalid input at a time: only the exercise name is missing.
        assertThrows(IllegalValueException.class, () -> new JsonAdaptedExercise(null, "Valid").toModelType());
        // One invalid input at a time: only the exercise name is blank.
        assertThrows(IllegalValueException.class, () -> new JsonAdaptedExercise("  ", "Valid").toModelType());
        // One invalid input at a time: only the exercise description is missing.
        assertThrows(IllegalValueException.class, () -> new JsonAdaptedExercise("Squat", null).toModelType());
        // One invalid input at a time: only the exercise description is blank.
        assertThrows(IllegalValueException.class, () -> new JsonAdaptedExercise("Squat", "\t").toModelType());
        // BV: length > MAX_LENGTH (invalid).
        assertThrows(IllegalValueException.class, () -> new JsonAdaptedExercise("Squat",
                "x".repeat(ExerciseDescription.MAX_LENGTH + 1))
                .toModelType());
    }

    // BV: 1 <= length <= MAX_LENGTH (valid).
    @Test
    public void toModelType_descriptionAtLimit_isValid() throws Exception {
        Exercise restored = new JsonAdaptedExercise("Squat", "x".repeat(ExerciseDescription.MAX_LENGTH)).toModelType();
        assertEquals(ExerciseDescription.MAX_LENGTH, restored.getDescription().getValue().length());
    }
}
