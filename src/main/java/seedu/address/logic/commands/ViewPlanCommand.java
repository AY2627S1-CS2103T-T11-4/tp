package seedu.address.logic.commands;


import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.student.Student;
import seedu.address.model.workoutplan.WorkoutPlan;

/** Displays one workout plan belonging to the currently selected student. */
public class ViewPlanCommand extends SingleIndexCommand {
    public static final String COMMAND_WORD = "viewPlan";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Displays selected workout plan\n"
            + "Parameters: WorkoutPlanId (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Viewing workout plan:\n%1$s";
    public static final String MESSAGE_NO_STUDENT_SELECTED =
            "No student is currently selected. Use viewStudent first.";
    public static final String MESSAGE_INVALID_WORKOUT_ID =
            "Workout ID exceeded total number of workout plans.";

    private Student currentStudent;

    /**
     * Creates a command to view the workout plan of the student at the given workoutplan index.
     *
     * @param targetIndex index of the workoutplan to be viewed
     */
    public ViewPlanCommand(Index targetIndex) {
        super(targetIndex);
    }

    @Override
    public void setCurrentStudent(Student currentStudent) {
        this.currentStudent = currentStudent;
    }

    @Override
    public Optional<Student> getCurrentStudent() {
        return Optional.ofNullable(currentStudent);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        if (currentStudent == null) {
            throw new CommandException(MESSAGE_NO_STUDENT_SELECTED);
        }

        List<WorkoutPlan> workoutPlans = currentStudent.getWorkoutPlansList().getWorkoutList();
        if (targetIndex.getZeroBased() >= workoutPlans.size()) {
            throw new CommandException(MESSAGE_INVALID_WORKOUT_ID);
        }

        WorkoutPlan workoutPlan = workoutPlans.get(targetIndex.getZeroBased());
        return new CommandResult(String.format(MESSAGE_SUCCESS, workoutPlan));
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
