package seedu.address.model.workoutplan.exceptions;

/** Signals that an unmarked workout plan cannot be unmarked again. */
public class AlreadyUnmarkedException extends RuntimeException {
    /** Creates an exception for an already unmarked workout plan. */
    public AlreadyUnmarkedException() {
        super("Workout plan is already unmarked");
    }
}
