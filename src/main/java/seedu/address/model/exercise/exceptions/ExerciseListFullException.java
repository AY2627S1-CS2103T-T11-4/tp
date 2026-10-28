package seedu.address.model.exercise.exceptions;

/** Signals that an operation would exceed the maximum number of exercises. */
public class ExerciseListFullException extends RuntimeException {

    /** Creates an exception for an exercise list at capacity. */
    public ExerciseListFullException() {
        super("Operation would exceed the maximum number of exercises");
    }
}
