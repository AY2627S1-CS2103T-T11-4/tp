package seedu.address.model.exercise;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.List;

import seedu.address.model.exercise.exceptions.DuplicateExerciseException;
import seedu.address.model.exercise.exceptions.ExerciseIndexOutOfBoundsException;
import seedu.address.model.exercise.exceptions.ExerciseListFullException;
import seedu.address.model.exercise.exceptions.ExerciseNotFoundException;

/** Represents an immutable, ordered list of exercises. */
public final class ExercisesList {

    public static final int MAX_EXERCISES = 100;

    private final List<Exercise> exercises;

    /** Constructs an empty list of exercises. */
    public ExercisesList() {
        this(List.of());
    }

    /**
     * Constructs a list containing the given exercises in order.
     * Rejects null values and duplicate exercises.
     */
    public ExercisesList(List<Exercise> exercises) {
        requireAllNonNull(exercises);
        this.exercises = List.copyOf(exercises);
        ensureExercisesAreUnique(this.exercises);
        ensureExerciseCountWithinLimit(this.exercises.size());
    }

    /** Returns the unmodifiable list of exercises. */
    public List<Exercise> getExerciseList() {
        return exercises;
    }

    /** Returns a new list containing the given exercises. */
    public ExercisesList withExercises(List<Exercise> exercises) {
        return new ExercisesList(exercises);
    }

    /** Returns the exercise at the given zero-based index. */
    public Exercise getExercise(int index) {
        checkIndex(index);
        return exercises.get(index);
    }

    /** Returns a new list with {@code exercise} appended. */
    public ExercisesList addExercise(Exercise exercise) {
        requireAllNonNull(exercise);
        if (exercises.stream().anyMatch(existingExercise -> existingExercise.isSameExercise(exercise))) {
            throw new DuplicateExerciseException();
        }
        if (exercises.size() >= MAX_EXERCISES) {
            throw new ExerciseListFullException();
        }
        List<Exercise> updated = new ArrayList<>(exercises);
        updated.add(exercise);
        return withExercises(updated);
    }

    /**
     * Returns a new list with {@code target} replaced by {@code replacement} at the same position.
     * The target may be an equal-valued instance rather than the stored instance.
     */
    public ExercisesList setExercise(Exercise target, Exercise replacement) {
        requireAllNonNull(target, replacement);
        int index = findTargetIndex(target);
        if (index < 0) {
            throw new ExerciseNotFoundException(target);
        }
        if (target.isSameExercise(replacement)) {
            return this;
        }

        List<Exercise> updated = new ArrayList<>(exercises);
        updated.set(index, replacement);
        return withExercises(updated);
    }

    /** Returns a new list with the first exercise equal to {@code target} removed. */
    public ExercisesList removeExercise(Exercise target) {
        requireAllNonNull(target);
        int index = findTargetIndex(target);
        if (index < 0) {
            throw new ExerciseNotFoundException(target);
        }

        List<Exercise> updated = new ArrayList<>(exercises);
        updated.remove(index);
        return withExercises(updated);
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= exercises.size()) {
            throw new ExerciseIndexOutOfBoundsException(index);
        }
    }

    private int findTargetIndex(Exercise target) {
        for (int i = 0; i < exercises.size(); i++) {
            if (exercises.get(i) == target) {
                return i;
            }
        }
        for (int i = 0; i < exercises.size(); i++) {
            if (exercises.get(i).isSameExercise(target)) {
                return i;
            }
        }
        return -1;
    }

    private static void ensureExercisesAreUnique(List<Exercise> exercises) {
        for (int i = 0; i < exercises.size() - 1; i++) {
            for (int j = i + 1; j < exercises.size(); j++) {
                if (exercises.get(i).isSameExercise(exercises.get(j))) {
                    throw new DuplicateExerciseException();
                }
            }
        }
    }

    private static void ensureExerciseCountWithinLimit(int exerciseCount) {
        if (exerciseCount > MAX_EXERCISES) {
            throw new ExerciseListFullException();
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ExercisesList otherList)) {
            return false;
        }
        return exercises.equals(otherList.exercises);
    }

    @Override
    public int hashCode() {
        return exercises.hashCode();
    }

    @Override
    public String toString() {
        return exercises.toString();
    }
}
