package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.exercise.Exercise;
import seedu.address.model.exercise.ExerciseDescription;
import seedu.address.model.exercise.ExerciseName;

/**
 * Represents a Jackson-friendly version of {@link Exercise}.
 */
class JsonAdaptedExercise {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Exercise's %s field is missing!";

    private final String name;
    private final String description;

    /**
     * Creates an adapted exercise from stored field values.
     */
    @JsonCreator
    public JsonAdaptedExercise(@JsonProperty("name") String name,
                               @JsonProperty("description") String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Creates an adapted exercise from a model exercise.
     */
    public JsonAdaptedExercise(Exercise source) {
        name = source.getName().getValue();
        description = source.getDescription().getValue();
    }

    /**
     * Converts the stored fields into a validated exercise.
     *
     * @return The model exercise.
     * @throws IllegalValueException If a field is missing or invalid.
     */
    public Exercise toModelType() throws IllegalValueException {
        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    ExerciseName.class.getSimpleName()));
        }
        if (!ExerciseName.isValidName(name)) {
            throw new IllegalValueException(ExerciseName.MESSAGE_CONSTRAINTS);
        }
        if (description == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    ExerciseDescription.class.getSimpleName()));
        }
        if (!ExerciseDescription.isValidExerciseDescription(description)) {
            throw new IllegalValueException(ExerciseDescription.MESSAGE_CONSTRAINTS);
        }
        return new Exercise(new ExerciseName(name), new ExerciseDescription(description));
    }
}
