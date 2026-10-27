package seedu.address.model.exercise;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Objects;

/**
 * Represents an exercise in a workout plan.
 * Guarantees: name and description are non-blank and at most 100 characters, and repetitions are valid.
 */
public final class Exercise {

    public static final int MAX_NAME_LENGTH = 100;
    public static final int MAX_DESCRIPTION_LENGTH = 100;
    public static final int MIN_REPETITIONS = 1;
    public static final int MAX_REPETITIONS = 1000;

    public static final String NAME_MESSAGE_CONSTRAINTS =
            "Exercise names should not be blank and should not exceed 100 characters";
    public static final String DESCRIPTION_MESSAGE_CONSTRAINTS =
            "Exercise descriptions should not be blank and should not exceed 100 characters";
    public static final String REPETITIONS_MESSAGE_CONSTRAINTS =
            "Exercise repetitions should be between 1 and 1000";

    private final String name;
    private final String description;
    private final int repetitions;

    /**
     * Constructs an {@code Exercise} with validated fields.
     *
     * @param name the exercise name
     * @param description the exercise description
     * @param repetitions the number of repetitions
     */
    public Exercise(String name, String description, int repetitions) {
        requireNonNull(name);
        requireNonNull(description);
        checkArgument(isValidName(name), NAME_MESSAGE_CONSTRAINTS);
        checkArgument(isValidDescription(description), DESCRIPTION_MESSAGE_CONSTRAINTS);
        checkArgument(isValidRepetitions(repetitions), REPETITIONS_MESSAGE_CONSTRAINTS);
        this.name = name;
        this.description = description;
        this.repetitions = repetitions;
    }

    /** Returns true if {@code name} is non-blank and within the maximum length. */
    public static boolean isValidName(String name) {
        requireNonNull(name);
        return !name.isBlank() && name.length() <= MAX_NAME_LENGTH;
    }

    /** Returns true if {@code description} is non-blank and within the maximum length. */
    public static boolean isValidDescription(String description) {
        requireNonNull(description);
        return !description.isBlank() && description.length() <= MAX_DESCRIPTION_LENGTH;
    }

    /** Returns true if {@code repetitions} is within the supported range. */
    public static boolean isValidRepetitions(int repetitions) {
        return repetitions >= MIN_REPETITIONS && repetitions <= MAX_REPETITIONS;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getRepetitions() {
        return repetitions;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Exercise otherExercise)) {
            return false;
        }
        return name.equals(otherExercise.name)
                && description.equals(otherExercise.description)
                && repetitions == otherExercise.repetitions;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, repetitions);
    }

    @Override
    public String toString() {
        return name + " - " + description + " (" + repetitions + " reps)";
    }
}
