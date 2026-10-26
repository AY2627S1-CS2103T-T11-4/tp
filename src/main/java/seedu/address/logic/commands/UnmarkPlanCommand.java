package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.student.Student;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.WorkoutPlansList;
import seedu.address.model.workoutplan.exceptions.WorkoutPlanIndexOutOfBoundsException;

/**
 * Unmarks a workout plan belonging to the student currently being viewed.
 */
public class UnmarkPlanCommand extends SingleIndexCommand {

    public static final String COMMAND_WORD = "unmarkPlan";

    public static final String MESSAGE_DESCRIPTION =
            "Unmarks a workout plan belonging to the student currently being viewed.";
    public static final String MESSAGE_PARAMETERS = "PLAN_ID (must be a positive integer)";
    public static final String MESSAGE_EXAMPLE = COMMAND_WORD + " 1";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": " + MESSAGE_DESCRIPTION + "\n"
            + "Parameters: " + MESSAGE_PARAMETERS + "\n"
            + "Example: " + MESSAGE_EXAMPLE;

    public static final String MESSAGE_SUCCESS = "Unmarked workout: %1$s";
    public static final String MESSAGE_NO_STUDENT_SELECTED =
            "No student is currently selected. Use viewStudent first.";
    public static final String MESSAGE_INVALID_WORKOUT_ID =
            "Workout Plan ID exceeded total number of workout plans.";

    /**
     * Creates a command to unmark the workout plan at the given index.
     *
     * @param targetIndex index of the workout plan to unmark
     */
    public UnmarkPlanCommand(Index targetIndex) {
        super(targetIndex);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Student currentStudent = model.getCurrentStudent()
                .orElseThrow(() -> new CommandException(MESSAGE_NO_STUDENT_SELECTED));

        final WorkoutPlansList updatedPlans;
        try {
            updatedPlans = currentStudent.getWorkoutPlansList().unmarkWorkoutPlan(targetIndex.getZeroBased());
        } catch (WorkoutPlanIndexOutOfBoundsException e) {
            throw new CommandException(MESSAGE_INVALID_WORKOUT_ID, e);
        }
        Student updatedStudent = new Student(currentStudent.getName(), currentStudent.getPhone(),
                currentStudent.getEmail(), currentStudent.getAddress(), currentStudent.getTags(), updatedPlans);
        model.setStudent(currentStudent, updatedStudent);

        WorkoutPlan unmarkedPlan = updatedPlans.getWorkoutPlan(targetIndex.getZeroBased());
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(unmarkedPlan)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UnmarkPlanCommand otherUnmarkPlanCommand)) {
            return false;
        }

        return targetIndex.equals(otherUnmarkPlanCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
