package seedu.address.model.exercise.exceptions;

/** Signals that an operation would result in duplicate exercises. */
public class DuplicateExerciseException extends RuntimeException {

    /** Creates an exception for duplicate exercises. */
    public DuplicateExerciseException() {
        super("Operation would result in duplicate exercises");
    }
}
