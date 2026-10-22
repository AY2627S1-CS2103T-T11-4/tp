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
 * Marks a workout plan belonging to the student currently being viewed as completed.
 */
public class MarkPlanCommand extends SingleIndexCommand {

    public static final String COMMAND_WORD = "markPlan";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks a workout plan belonging to the student currently being viewed as completed.\n"
            + "Parameters: PLAN_ID (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Marked workout as done: %1$s";
    public static final String MESSAGE_NO_STUDENT_SELECTED =
            "No student is currently selected. Use viewStudent first.";
    public static final String MESSAGE_INVALID_WORKOUT_ID =
            "Workout Plan ID exceeded total number of workout plans.";

    /**
     * Creates a command to mark the workout plan at the given index.
     *
     * @param targetIndex index of the workout plan to mark
     */
    public MarkPlanCommand(Index targetIndex) {
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
                .markWorkoutPlan(targetIndex.getZeroBased());
        Student updatedStudent = new Student(currentStudent.getName(), currentStudent.getPhone(),
                currentStudent.getEmail(), currentStudent.getAddress(), currentStudent.getTags(), updatedPlans);
        model.setStudent(currentStudent, updatedStudent);

        WorkoutPlan markedPlan = updatedPlans.getWorkoutList().get(targetIndex.getZeroBased());
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(markedPlan)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof MarkPlanCommand otherMarkPlanCommand)) {
            return false;
        }

        return targetIndex.equals(otherMarkPlanCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
