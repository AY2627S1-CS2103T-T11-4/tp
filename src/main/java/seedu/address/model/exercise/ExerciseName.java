package seedu.address.model.exercise;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents the name of an exercise.
 * Guarantees: immutable, non-blank, and at most 100 characters.
 */
public final class ExerciseName {

    public static final int MAX_LENGTH = 100;
    public static final String MESSAGE_CONSTRAINTS =
            "Exercise names should not be blank and should not exceed 100 characters";

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

    /** Returns true if {@code name} is non-blank and within the maximum length. */
    public static boolean isValidName(String name) {
        requireNonNull(name);
        return !name.isBlank() && name.length() <= MAX_LENGTH;
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
