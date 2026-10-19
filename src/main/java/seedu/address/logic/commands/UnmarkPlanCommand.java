package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.student.Student;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.WorkoutPlansList;

/**
 * Unmarks a workout plan belonging to the student currently being viewed.
 */
public class UnmarkPlanCommand extends SingleIndexCommand {

    public static final String COMMAND_WORD = "unmarkPlan";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Unmarks a workout plan belonging to the student currently being viewed.\n"
            + "Parameters: WORKOUT_ID (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Unmarked workout: %1$s";
    public static final String MESSAGE_NO_STUDENT_SELECTED =
            "No student is currently selected. Use viewStudent first.";
    public static final String MESSAGE_INVALID_WORKOUT_ID =
            "Workout ID exceeded total number of workout plans.";

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

        List<WorkoutPlan> workoutPlans = currentStudent.getWorkoutPlansList().getWorkoutList();
        if (targetIndex.getZeroBased() >= workoutPlans.size()) {
            throw new CommandException(MESSAGE_INVALID_WORKOUT_ID);
        }

        WorkoutPlansList updatedPlans = currentStudent.getWorkoutPlansList()
                .unmarkWorkoutPlan(targetIndex.getZeroBased());
        Student updatedStudent = new Student(currentStudent.getName(), currentStudent.getPhone(),
                currentStudent.getEmail(), currentStudent.getAddress(), currentStudent.getTags(), updatedPlans);
        model.setStudent(currentStudent, updatedStudent);

        WorkoutPlan unmarkedPlan = updatedPlans.getWorkoutList().get(targetIndex.getZeroBased());
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
