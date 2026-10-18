package seedu.address.testutil;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PLAN;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;

import seedu.address.logic.commands.AddPlanCommand;
import seedu.address.logic.commands.EditPlanCommand;
import seedu.address.model.tag.WorkoutPlanTag;
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
        sb.append(" " + PREFIX_DATE + " " + workout.getDate().toInputString());
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditPlanDescriptor}'s details.
     */
    public static String getEditPlanDescriptorDetails(EditPlanCommand.EditPlanDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getTitle().ifPresent(title ->
                sb.append(PREFIX_NAME).append(" ").append(title.getValue()).append(" "));
        descriptor.getDescription().ifPresent(plan ->
                sb.append(PREFIX_PLAN).append(" ").append(plan.getValue()).append(" "));
        descriptor.getDate().ifPresent(date ->
                sb.append(PREFIX_DATE).append(" ").append(date.toInputString()).append(" "));
        if (descriptor.getWorkoutPlanTags().isPresent()) {
            Set<WorkoutPlanTag> tags = descriptor.getWorkoutPlanTags().get();
            if (tags.isEmpty()) {
                sb.append(PREFIX_TAG);
            } else {
                tags.forEach(s -> sb.append(PREFIX_TAG).append(" ").append(s.tagName).append(" "));
            }
        }
        return sb.toString();
    }
}
