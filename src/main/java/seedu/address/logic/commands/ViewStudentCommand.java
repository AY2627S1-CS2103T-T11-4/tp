package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.student.Student;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.WorkoutPlanStatus;

/**
 * Refreshes workout plan statuses and displays all plans belonging to a selected student.
 */
public class ViewStudentCommand extends SingleIndexCommand {

    public static final String COMMAND_WORD = "viewStudent";

    public static final String MESSAGE_DESCRIPTION = "Displays all workout plans belonging to a student.";
    public static final String MESSAGE_PARAMETERS = "STUDENT_ID (must be a positive integer)";
    public static final String MESSAGE_EXAMPLE = COMMAND_WORD + " 1";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": " + MESSAGE_DESCRIPTION + "\n"
            + "Parameters: " + MESSAGE_PARAMETERS + "\n"
            + "Example: " + MESSAGE_EXAMPLE;

    public static final String MESSAGE_SUCCESS = "Viewing plans for %1$s:\n%2$s";
    public static final String MESSAGE_NO_WORKOUTS = "No workout plans assigned.";

    /**
     * Creates a command to view the workout plans of the student at the given index.
     *
     * @param targetIndex index of the student whose plans should be viewed
     */
    public ViewStudentCommand(Index targetIndex) {
        super(targetIndex);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Student> students = model.getFilteredStudentList();

        if (targetIndex.getZeroBased() >= students.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
        }

        Student selectedStudent = students.get(targetIndex.getZeroBased());
        Student refreshedStudent = model.updateStudentWorkoutsState(selectedStudent);
        model.setCurrentStudent(refreshedStudent);
        model.clearCurrentWorkoutPlan();
        List<WorkoutPlan> workoutPlans = refreshedStudent.getWorkoutPlansList().getWorkoutList();

        return new CommandResult(String.format(
                MESSAGE_SUCCESS,
                refreshedStudent.getName(),
                formatWorkoutPlans(workoutPlans)));
    }

    private static String formatWorkoutPlans(List<WorkoutPlan> workoutPlans) {
        if (workoutPlans.isEmpty()) {
            return MESSAGE_NO_WORKOUTS;
        }

        return formatWorkoutPlansByStatus(workoutPlans, WorkoutPlanStatus.COMPLETED, "Done") + "\n"
                + formatWorkoutPlansByStatus(workoutPlans, WorkoutPlanStatus.OVERDUE, "Overdue") + "\n"
                + formatWorkoutPlansByStatus(workoutPlans, WorkoutPlanStatus.PENDING, "Pending");
    }

    private static String formatWorkoutPlansByStatus(List<WorkoutPlan> workoutPlans,
            WorkoutPlanStatus status, String heading) {
        String plans = IntStream.range(0, workoutPlans.size())
                .filter(index -> workoutPlans.get(index).getStatus() == status)
                .mapToObj(index -> String.format("%d. %s", index + 1,
                        Messages.format(workoutPlans.get(index))))
                .collect(Collectors.joining("\n"));
        return heading + ":\n" + (plans.isEmpty() ? "  (none)" : plans);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ViewStudentCommand otherViewStudentCommand)) {
            return false;
        }

        return targetIndex.equals(otherViewStudentCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
