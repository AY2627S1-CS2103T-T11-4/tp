package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.BrotherGym;
import seedu.address.model.Model;

/**
 * Requests confirmation before clearing all students and their workout plans.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String CONFIRMATION_ARGUMENT = "confirm";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Requests confirmation to delete all students and their workout plans.\n"
            + "Enter clear confirm to confirm deletion.\n"
            + "Example: " + COMMAND_WORD;
    public static final String MESSAGE_CONFIRMATION =
            "This will permanently delete all students and their workout plans.\n"
            + "Enter clear confirm to delete everything, or continue with another command to keep your data.";
    public static final String MESSAGE_SUCCESS = "BrotherGym has been cleared!";

    private final boolean confirmed;

    /** Creates a request for confirmation, without deleting data. */
    public ClearCommand() {
        this(false);
    }

    /** Creates a clear command that deletes data only when explicitly confirmed. */
    public ClearCommand(boolean confirmed) {
        this.confirmed = confirmed;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        if (!confirmed) {
            return new CommandResult(MESSAGE_CONFIRMATION);
        }
        model.setBrotherGym(new BrotherGym());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
