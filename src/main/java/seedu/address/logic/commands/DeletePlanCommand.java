package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Deletes a plan identified using its displayed index from the workouts list for the student.
 */
public class DeletePlanCommand extends Command {

    public static final String COMMAND_WORD = "deletePlan";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the plan identified by the index number used in the displayed workouts list for the student.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_PLAN_SUCCESS = "Deleted plan: %1$s";

    private final Index targetIndex;

    public DeletePlanCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        throw new CommandException("Yet to be implemented");
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

