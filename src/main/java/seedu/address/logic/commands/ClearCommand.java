package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.BrotherGym;
import seedu.address.model.Model;

/**
 * Clears the student list.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "BrotherGym has been cleared!";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.setBrotherGym(new BrotherGym());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
