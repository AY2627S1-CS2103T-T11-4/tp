package seedu.address.model.workoutplan.exceptions;

/** Signals that a completed workout plan cannot be marked again. */
public class AlreadyMarkedException extends RuntimeException {
    /** Creates an exception for an already marked workout plan. */
    public AlreadyMarkedException() {
        super("Workout plan is already marked as completed");
    }
}
