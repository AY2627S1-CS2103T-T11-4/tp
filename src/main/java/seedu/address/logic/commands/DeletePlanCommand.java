package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.student.Student;
import seedu.address.model.workout.WorkoutPlan;

/**
 * Deletes a workout plan from the Student currently being viewed.
 */
public class DeletePlanCommand extends Command {

    public static final String COMMAND_WORD = "deletePlan";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes a workout plan from the Student currently being viewed.\n"
            + "Parameters: WORKOUT_ID (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Deleted workout: %1$s";
    public static final String MESSAGE_NO_STUDENT_SELECTED =
            "No student is currently selected. Use viewStudent first.";
    public static final String MESSAGE_INVALID_WORKOUT_ID =
            "Workout ID exceeded total number of workout plans.";

    private final Index targetIndex;

    public DeletePlanCommand(Index targetIndex) {
        requireNonNull(targetIndex);
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Student currentStudent = model.getCurrentStudent();
        if (currentStudent == null) {
            throw new CommandException(MESSAGE_NO_STUDENT_SELECTED);
        }

        List<WorkoutPlan> workoutPlans = currentStudent.getWorkoutPlans();
        if (targetIndex.getZeroBased() >= workoutPlans.size()) {
            throw new CommandException(MESSAGE_INVALID_WORKOUT_ID);
        }

        WorkoutPlan planToDelete = workoutPlans.get(targetIndex.getZeroBased());
        model.deleteWorkout(currentStudent, planToDelete);

        return new CommandResult(String.format(MESSAGE_SUCCESS, planToDelete));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

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
