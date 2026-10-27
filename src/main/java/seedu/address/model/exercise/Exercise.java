package seedu.address.model.exercise;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

/**
 * Represents an exercise in a workout plan.
 * Guarantees: name, weight, and repetitions are valid and immutable.
 */
public final class Exercise {

    private final ExerciseName name;
    private final ExerciseWeight weight;
    private final ExerciseReps repetitions;

    /**
     * Constructs an {@code Exercise} with validated fields.
     *
     * @param name the exercise name
     * @param weight the exercise weight in kilograms
     * @param repetitions the number of repetitions
     */
    public Exercise(ExerciseName name, ExerciseWeight weight, ExerciseReps repetitions) {
        requireAllNonNull(name, weight, repetitions);
        this.name = name;
        this.weight = weight;
        this.repetitions = repetitions;
    }

    public ExerciseName getName() {
        return name;
    }

    public ExerciseWeight getWeight() {
        return weight;
    }

    public ExerciseReps getRepetitions() {
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
                && weight.equals(otherExercise.weight)
                && repetitions.equals(otherExercise.repetitions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, weight, repetitions);
    }

    @Override
    public String toString() {
        return name + " (" + weight + " kg, " + repetitions + " reps)";
    }
}
