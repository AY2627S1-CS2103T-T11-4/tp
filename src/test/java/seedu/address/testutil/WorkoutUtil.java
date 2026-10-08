package seedu.address.testutil;

import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PLAN;

import seedu.address.logic.commands.AddPlanCommand;
import seedu.address.model.workoutplan.WorkoutDate;
import seedu.address.model.workoutplan.WorkoutPlan;

/**
 * A util class for workouts
 */
public class WorkoutUtil {

    /**
     * Returns an addPlan command string for adding the {@code workout} to the current student
     */
    public static String getAddPlanCommand(WorkoutPlan workout) {
        return AddPlanCommand.COMMAND_WORD + " " + getWorkoutDetails(workout);
    }

    /**
     * Returns the part of the command string to assign the {@code workout} details
     */
    public static String getWorkoutDetails(WorkoutPlan workout) {
        StringBuilder sb = new StringBuilder();
        sb.append(workout.getTitle());
        sb.append(" " + PREFIX_PLAN + " " + workout.getWorkouts());
        sb.append(" " + PREFIX_DATE + " " + WorkoutDate.INPUT_FORMATTER.format(workout.getDate().getValue()));
        return sb.toString();
    }
}
