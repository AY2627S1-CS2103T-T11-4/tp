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
 * Deletes a workout plan from the Student currently being viewed.
 */
public class DeletePlanCommand extends SingleIndexCommand {

    public static final String COMMAND_WORD = "deletePlan";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes a workout plan from the Student currently being viewed.\n"
            + "Parameters: PLAN_ID (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Deleted workout: %1$s";
    public static final String MESSAGE_NO_STUDENT_SELECTED =
            "No student is currently selected. Use viewStudent first.";
    public static final String MESSAGE_INVALID_WORKOUT_ID =
            "Workout Plan ID exceeded total number of workout plans.";

    /**
     * Creates a command to delete the workout plan at the given index.
     *
     * @param targetIndex index of the workout plan to delete
     */
    public DeletePlanCommand(Index targetIndex) {
        super(targetIndex);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Student currentStudent = model.getCurrentStudent()
                .orElseThrow(() -> new CommandException(MESSAGE_NO_STUDENT_SELECTED));

        final WorkoutPlan planToDelete;
        final WorkoutPlansList updatedPlans;
        try {
            planToDelete = currentStudent.getWorkoutPlansList().getWorkoutPlan(targetIndex.getZeroBased());
            updatedPlans = currentStudent.getWorkoutPlansList().removeWorkoutPlan(targetIndex.getZeroBased());
        } catch (WorkoutPlanIndexOutOfBoundsException e) {
            throw new CommandException(MESSAGE_INVALID_WORKOUT_ID, e);
        }
        Student updatedStudent = new Student(currentStudent.getName(), currentStudent.getPhone(),
                currentStudent.getEmail(), currentStudent.getAddress(), currentStudent.getTags(), updatedPlans);
        model.setStudent(currentStudent, updatedStudent);

        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(planToDelete)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeletePlanCommand otherDeletePlanCommand)) {
            return false;
        }

        return targetIndex.equals(otherDeletePlanCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
