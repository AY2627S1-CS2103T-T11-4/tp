package seedu.address.model.exercise;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents the name of an exercise.
 * Guarantees: immutable and valid as declared in {@link #isValidName(String)}.
 */
public final class ExerciseName {

    public static final String MESSAGE_CONSTRAINTS = "Exercise names should not be blank";
    public static final String VALIDATION_REGEX = ".*\\S.*";

    private final String value;

    /**
     * Constructs an {@code ExerciseName} with a validated value.
     *
     * @param name the exercise name
     */
    public ExerciseName(String name) {
        requireNonNull(name);
        checkArgument(isValidName(name), MESSAGE_CONSTRAINTS);
        value = name;
    }

    /** Returns true if {@code name} contains at least one non-whitespace character. */
    public static boolean isValidName(String name) {
        requireNonNull(name);
        return name.matches(VALIDATION_REGEX);
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
        return other instanceof ExerciseName otherName && value.equals(otherName.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
