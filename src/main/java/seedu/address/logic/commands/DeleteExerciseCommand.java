package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.exercise.Exercise;
import seedu.address.model.exercise.exceptions.ExerciseIndexOutOfBoundsException;
import seedu.address.model.exercise.exceptions.ExerciseNotFoundException;
import seedu.address.model.student.Student;
import seedu.address.model.student.exceptions.StudentNotFoundException;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.exceptions.WorkoutPlanNotFoundException;

/**
 * Deletes an exercise from the currently selected workout plan.
 */
public class DeleteExerciseCommand extends SingleIndexCommand {

    public static final String COMMAND_WORD = "deleteExercise";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes an exercise from the selected workout plan.\n"
            + "Parameters: EXERCISE_ID (must be a positive integer shown in viewPlan)\n"
            + "Example: " + COMMAND_WORD + " 1";
    public static final String MESSAGE_SUCCESS = "Deleted exercise: %1$s";
    public static final String MESSAGE_NO_STUDENT_SELECTED =
            "No student is currently selected. Use viewStudent first.";
    public static final String MESSAGE_NO_PLAN_SELECTED =
            "No workout plan is currently selected. Use viewPlan first.";
    public static final String MESSAGE_INVALID_EXERCISE_ID =
            "The exercise index provided is invalid for the selected workout plan.";
    public static final String MESSAGE_PLAN_NO_LONGER_AVAILABLE =
            "The selected workout plan is no longer available. Use viewPlan to select a plan.";

    /**
     * Creates a command to delete the exercise at the given index.
     *
     * @param targetIndex index of the exercise to delete
     */
    public DeleteExerciseCommand(Index targetIndex) {
        super(targetIndex);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Student currentStudent = model.getCurrentStudent()
                .orElseThrow(() -> new CommandException(MESSAGE_NO_STUDENT_SELECTED));
        WorkoutPlan selectedPlan = model.getCurrentWorkoutPlan()
                .orElseThrow(() -> new CommandException(MESSAGE_NO_PLAN_SELECTED));

        final Exercise exerciseToDelete;
        try {
            exerciseToDelete = selectedPlan.getExercisesList().getExercise(targetIndex.getZeroBased());
        } catch (ExerciseIndexOutOfBoundsException e) {
            throw new CommandException(MESSAGE_INVALID_EXERCISE_ID, e);
        }

        try {
            model.removeExerciseForWorkoutPlan(currentStudent, selectedPlan, exerciseToDelete);
        } catch (StudentNotFoundException | WorkoutPlanNotFoundException e) {
            throw new CommandException(MESSAGE_PLAN_NO_LONGER_AVAILABLE, e);
        } catch (ExerciseNotFoundException e) {
            throw new CommandException(MESSAGE_INVALID_EXERCISE_ID, e);
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, exerciseToDelete));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteExerciseCommand otherDeleteExerciseCommand)) {
            return false;
        }

        return targetIndex.equals(otherDeleteExerciseCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
