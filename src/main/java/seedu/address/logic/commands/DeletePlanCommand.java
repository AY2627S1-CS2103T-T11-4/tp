package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.student.Student;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.WorkoutPlansList;

/**
 * Deletes a workout plan from the Student currently being viewed.
 */
public class DeletePlanCommand extends SingleIndexCommand {

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

    private Student currentStudent;
    private Student updatedStudent;

    /**
     * Creates a command to delete the workout plan at the given index.
     *
     * @param targetIndex index of the workout plan to delete
     */
    public DeletePlanCommand(Index targetIndex) {
        super(targetIndex);
    }

    public void setCurrentStudent(Student currentStudent) {
        this.currentStudent = currentStudent;
    }

    @Override
    public Optional<Student> getCurrentStudent() {
        return Optional.ofNullable(updatedStudent);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (currentStudent == null) {
            throw new CommandException(MESSAGE_NO_STUDENT_SELECTED);
        }

        List<WorkoutPlan> workoutPlans = currentStudent.getWorkoutPlansList().getWorkoutList();
        if (targetIndex.getZeroBased() >= workoutPlans.size()) {
            throw new CommandException(MESSAGE_INVALID_WORKOUT_ID);
        }

        WorkoutPlan planToDelete = workoutPlans.get(targetIndex.getZeroBased());
        WorkoutPlansList updatedPlans = currentStudent.getWorkoutPlansList().removeWorkoutPlan(planToDelete);
        updatedStudent = new Student(currentStudent.getName(), currentStudent.getPhone(),
                currentStudent.getEmail(), currentStudent.getAddress(), currentStudent.getTags(), updatedPlans);
        model.setStudent(currentStudent, updatedStudent);

        return new CommandResult(String.format(MESSAGE_SUCCESS, planToDelete));
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
