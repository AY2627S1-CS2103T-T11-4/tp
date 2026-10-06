package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.student.Student;
import seedu.address.model.workout.WorkoutPlan;

/**
 * Adds a workout plan to the Student currently being viewed.
 */
public class AddPlanCommand extends Command {

    public static final String COMMAND_WORD = "addPlan";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds a workout plan to the Student currently being viewed.\n"
            + "Parameters: PLAN_NAME --plan DESCRIPTION --date DDMMYY\n"
            + "Example: " + COMMAND_WORD + " Strength --plan Upper body --date 081025";

    public static final String MESSAGE_SUCCESS = "New workout added: %1$s";
    public static final String MESSAGE_NO_STUDENT_SELECTED =
            "No student is currently selected. Use viewStudent first.";
    public static final String MESSAGE_DUPLICATE_PLAN = "This workout plan already exists.";

    private final WorkoutPlan toAdd;

    public AddPlanCommand(WorkoutPlan toAdd) {
        requireNonNull(toAdd);
        this.toAdd = toAdd;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Student currentStudent = model.getCurrentStudent();
        if (currentStudent == null) {
            throw new CommandException(MESSAGE_NO_STUDENT_SELECTED);
        }

        if (model.hasWorkout(currentStudent, toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PLAN);
        }

        model.addWorkout(currentStudent, toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AddPlanCommand otherAddPlanCommand)) {
            return false;
        }

        return toAdd.equals(otherAddPlanCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
