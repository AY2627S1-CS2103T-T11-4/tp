package seedu.address.model.workoutplan;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents the description of a workout plan.
 * Guarantees: immutable and not blank.
 */
public final class WorkoutDescription {

    public static final String MESSAGE_CONSTRAINTS = "Workout descriptions should not be blank";

    private final String value;

    /**
     * Constructs a {@code WorkoutDescription}.
     *
     * @param description A non-blank workout description.
     */
    public WorkoutDescription(String description) {
        requireNonNull(description);
        checkArgument(isValidWorkoutDescription(description), MESSAGE_CONSTRAINTS);
        value = description;
    }

    /**
     * Returns true if the description contains non-whitespace characters.
     */
    public static boolean isValidWorkoutDescription(String test) {
        return !test.isBlank();
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
        return other instanceof WorkoutDescription otherDescription && value.equals(otherDescription.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
