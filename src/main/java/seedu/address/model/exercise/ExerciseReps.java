package seedu.address.model.exercise;

import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents the number of repetitions for an exercise.
 * Guarantees: immutable and between 1 and 1000 inclusive.
 */
public final class ExerciseReps {

    public static final int MIN_REPS = 1;
    public static final int MAX_REPS = 1000;
    public static final String MESSAGE_CONSTRAINTS =
            "Exercise repetitions should be between 1 and 1000";

    private final int value;

    /**
     * Constructs an {@code ExerciseReps} with a validated value.
     *
     * @param repetitions the number of repetitions
     */
    public ExerciseReps(int repetitions) {
        checkArgument(isValidReps(repetitions), MESSAGE_CONSTRAINTS);
        value = repetitions;
    }

    /** Returns true if {@code repetitions} is between 1 and 1000 inclusive. */
    public static boolean isValidReps(int repetitions) {
        return repetitions >= MIN_REPS && repetitions <= MAX_REPS;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        return other instanceof ExerciseReps otherReps && value == otherReps.value;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }
}
