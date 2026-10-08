package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.student.Student;
import seedu.address.model.workoutplan.WorkoutPlan;

/**
 * Displays all workout plans belonging to a selected student.
 */
public class ViewStudentCommand extends Command {

    public static final String COMMAND_WORD = "viewStudent";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Displays all workout plans belonging to a student.\n"
            + "Parameters: STUDENT_INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Viewing plans for %1$s:\n%2$s";
    public static final String MESSAGE_NO_WORKOUTS = "No workout plans assigned.";

    private final Index targetIndex;
    private Student selectedStudent;

    /**
     * Creates a command to view the workout plans of the student at the given index.
     *
     * @param targetIndex index of the student whose plans should be viewed
     */
    public ViewStudentCommand(Index targetIndex) {
        requireNonNull(targetIndex);
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Student> students = model.getFilteredStudentList();

        if (targetIndex.getZeroBased() >= students.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
        }

        selectedStudent = students.get(targetIndex.getZeroBased());
        List<WorkoutPlan> workoutPlans = selectedStudent.getWorkoutPlansList().getWorkoutList();

        return new CommandResult(String.format(
                MESSAGE_SUCCESS,
                selectedStudent.getName(),
                formatWorkoutPlans(workoutPlans)));
    }

    @Override
    public Optional<Student> getCurrentStudent() {
        return Optional.ofNullable(selectedStudent);
    }

    private static String formatWorkoutPlans(List<WorkoutPlan> workoutPlans) {
        if (workoutPlans.isEmpty()) {
            return MESSAGE_NO_WORKOUTS;
        }

        return IntStream.range(0, workoutPlans.size())
                .mapToObj(index -> String.format("%d. %s", index + 1, workoutPlans.get(index)))
                .collect(Collectors.joining("\n"));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

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
