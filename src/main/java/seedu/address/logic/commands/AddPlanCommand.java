package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PLAN;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.workoutplan.WorkoutPlan;

/**
 * A stub class so that AddStudentCommandParser can work
 */
@SuppressWarnings("checkstyle:Regexp")
public class AddPlanCommand extends Command {
    public static final String COMMAND_WORD = "addPlan";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a student to the student list. "
            + "Parameters: TITLE "
            + PREFIX_PLAN + " DESC "
            + PREFIX_DATE + " DATE(DDMMYYYY) "
            + "Example: " + COMMAND_WORD + " Leg Day "
            + PREFIX_PLAN + " Squats 3x 100kg "
            + PREFIX_DATE + " 08102026";

    private final WorkoutPlan toAdd;

    /**
     * Creates an AddPlanCommand to add the specified {@code workout}
     */
    public AddPlanCommand(WorkoutPlan workout) {
        requireNonNull(workout);
        toAdd = workout;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        throw new CommandException("Yet to be implemented");
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
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
