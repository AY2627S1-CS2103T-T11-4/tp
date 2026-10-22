package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.student.Student;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.exceptions.WorkoutPlanIndexOutOfBoundsException;

/** Displays one workout plan belonging to the currently selected student. */
public class ViewPlanCommand extends SingleIndexCommand {
    public static final String COMMAND_WORD = "viewPlan";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Displays selected workout plan\n"
            + "Parameters: PLAN_ID (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Viewing workout plan:\n%1$s";
    public static final String MESSAGE_NO_STUDENT_SELECTED =
            "No student is currently selected. Use viewStudent first.";
    public static final String MESSAGE_INVALID_WORKOUT_ID =
            "Workout Plan ID exceeded total number of workout plans.";

    /**
     * Creates a command to view the workout plan of the student at the given workoutplan index.
     *
     * @param targetIndex index of the workoutplan to be viewed
     */
    public ViewPlanCommand(Index targetIndex) {
        super(targetIndex);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Student currentStudent = model.getCurrentStudent()
                .orElseThrow(() -> new CommandException(MESSAGE_NO_STUDENT_SELECTED));

        final WorkoutPlan workoutPlan;
        try {
            workoutPlan = currentStudent.getWorkoutPlansList().getWorkoutPlan(targetIndex.getZeroBased());
        } catch (WorkoutPlanIndexOutOfBoundsException e) {
            throw new CommandException(MESSAGE_INVALID_WORKOUT_ID, e);
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(workoutPlan)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ViewPlanCommand otherViewPlanCommand)) {
            return false;
        }

        return targetIndex.equals(otherViewPlanCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
