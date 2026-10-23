package seedu.address.model.workoutplan.exceptions;

import java.util.NoSuchElementException;

import seedu.address.model.workoutplan.WorkoutPlan;

/**
 * Signals that the specified workout plan could not be found.
 */
public class WorkoutPlanNotFoundException extends NoSuchElementException {

    /**
     * Creates an exception for the missing plan.
     */
    public WorkoutPlanNotFoundException(WorkoutPlan plan) {
        super("Workout plan not found: " + plan);
    }
}
