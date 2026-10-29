package seedu.address.model.exercise.exceptions;

/** Signals that an exercise index does not identify an exercise in the list. */
public class ExerciseIndexOutOfBoundsException extends RuntimeException {

    /** Creates an exception for an exercise index outside the list. */
    public ExerciseIndexOutOfBoundsException(int index) {
        super("Exercise index is out of bounds: " + index);
    }
}
