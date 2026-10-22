package seedu.address.model.workoutplan.exceptions;

/** Signals that a workout plan index does not identify a plan in the list. */
public class WorkoutPlanIndexOutOfBoundsException extends RuntimeException {
    /** Creates an exception for a workout plan index outside the list. */
    public WorkoutPlanIndexOutOfBoundsException(int index) {
        super("Workout plan index is out of bounds: " + index);
    }
}
