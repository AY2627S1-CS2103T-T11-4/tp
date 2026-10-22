package seedu.address.model.workoutplan.exceptions;

/** Signals that an operation would create a duplicate workout plan. */
public class DuplicateWorkoutPlanException extends RuntimeException {
    /** Creates an exception for a duplicate workout plan. */
    public DuplicateWorkoutPlanException() {
        super("Operation would result in duplicate workout plans");
    }
}
