package seedu.address.model.exercise;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

/**
 * Represents an exercise in a workout plan.
 * Guarantees: name and description are valid and immutable.
 */
public final class Exercise {

    private final ExerciseName name;
    private final ExerciseDescription description;

    /**
     * Constructs an {@code Exercise} with validated fields.
     *
     * @param name the exercise name
     * @param description the exercise description
     */
    public Exercise(ExerciseName name, ExerciseDescription description) {
        requireAllNonNull(name, description);
        this.name = name;
        this.description = description;
    }

    public ExerciseName getName() {
        return name;
    }

    public ExerciseDescription getDescription() {
        return description;
    }

    /**
     * Returns whether this exercise represents the same exercise as {@code otherExercise}.
     *
     * @param otherExercise the exercise to compare against
     * @return true if both exercises have the same name and description
     */
    public boolean isSameExercise(Exercise otherExercise) {
        if (otherExercise == this) {
            return true;
        }
        return otherExercise != null
                && name.equals(otherExercise.name)
                && description.equals(otherExercise.description);
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
                && description.equals(otherExercise.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }

    @Override
    public String toString() {
        return name + " (" + description + ")";
    }
}
