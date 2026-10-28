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
        assertThrows(IllegalValueException.class, () -> new JsonAdaptedExercise(null, "Valid").toModelType());
        assertThrows(IllegalValueException.class, () -> new JsonAdaptedExercise("  ", "Valid").toModelType());
        assertThrows(IllegalValueException.class, () -> new JsonAdaptedExercise("Squat", null).toModelType());
        assertThrows(IllegalValueException.class, () -> new JsonAdaptedExercise("Squat", "\t").toModelType());
        assertThrows(IllegalValueException.class, () -> new JsonAdaptedExercise("Squat", "x".repeat(501))
                .toModelType());
    }

    @Test
    public void toModelType_descriptionAtLimit_isValid() throws Exception {
        Exercise restored = new JsonAdaptedExercise("Squat", "x".repeat(500)).toModelType();
        assertEquals(500, restored.getDescription().getValue().length());
    }
}
