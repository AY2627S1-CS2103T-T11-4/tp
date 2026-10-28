package seedu.address.model.exercise;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents the description of an exercise.
 * Guarantees: immutable and not blank.
 */
public final class ExerciseDescription {

    public static final String MESSAGE_CONSTRAINTS = "Exercise descriptions should not be blank";

    private final String value;

    /**
     * Constructs an {@code ExerciseDescription}.
     *
     * @param description A non-blank exercise description.
     */
    public ExerciseDescription(String description) {
        requireNonNull(description);
        checkArgument(isValidExerciseDescription(description), MESSAGE_CONSTRAINTS);
        value = description;
    }

    /** Returns true if the description contains non-whitespace characters. */
    public static boolean isValidExerciseDescription(String description) {
        requireNonNull(description);
        return !description.isBlank();
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        return other instanceof ExerciseDescription otherDescription
                && value.equals(otherDescription.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
