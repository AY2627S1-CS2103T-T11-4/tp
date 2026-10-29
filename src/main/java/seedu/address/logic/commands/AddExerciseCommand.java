package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.exercise.Exercise;
import seedu.address.model.exercise.exceptions.DuplicateExerciseException;
import seedu.address.model.exercise.exceptions.ExerciseListFullException;
import seedu.address.model.student.Student;
import seedu.address.model.workoutplan.WorkoutPlan;

/**
 * Adds an exercise to the currently selected workout plan.
 */
public class AddExerciseCommand extends Command {
    public static final String COMMAND_WORD = "addExercise";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds an exercise to the workout plan currently being viewed.\n"
            + "Parameters: EXERCISE_TITLE " + PREFIX_DESCRIPTION + " DESCRIPTION\n"
            + "Example: " + COMMAND_WORD + " Squats " + PREFIX_DESCRIPTION + " Barbell back squat 30kg * 5 reps";
    public static final String MESSAGE_SUCCESS = "New exercise added: %1$s";
    public static final String MESSAGE_NO_PLAN_SELECTED =
            "No workout plan is currently selected. Use viewPlan first.";
    public static final String MESSAGE_DUPLICATE_EXERCISE = "This exercise already exists in the workout plan.";
    public static final String MESSAGE_EXERCISE_LIST_FULL = "This workout plan has the maximum number of exercises.";

    private final Exercise toAdd;

    /**
     * Creates a command to add the given exercise to the selected workout plan.
     *
     * @param toAdd Exercise to add.
     */
    public AddExerciseCommand(Exercise toAdd) {
        this.toAdd = requireNonNull(toAdd);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        WorkoutPlan currentPlan = model.getCurrentWorkoutPlan()
                .orElseThrow(() -> new CommandException(MESSAGE_NO_PLAN_SELECTED));
        Student currentStudent = model.getCurrentStudent()
                .orElseThrow(() -> new CommandException(MESSAGE_NO_PLAN_SELECTED));

        try {
            model.addExerciseForWorkoutPlan(currentStudent, currentPlan, toAdd);
        } catch (DuplicateExerciseException e) {
            throw new CommandException(MESSAGE_DUPLICATE_EXERCISE, e);
        } catch (ExerciseListFullException e) {
            throw new CommandException(MESSAGE_EXERCISE_LIST_FULL, e);
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public boolean equals(Object other) {
        return other == this || other instanceof AddExerciseCommand command && toAdd.equals(command.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("toAdd", toAdd).toString();
    }
}
