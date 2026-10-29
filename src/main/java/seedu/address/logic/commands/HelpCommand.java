package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Model;

/**
 * Formats full help instructions for every command for display.
 */
public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";

    public static final String MESSAGE_DESCRIPTION = "Shows program usage instructions.";
    public static final String MESSAGE_PARAMETERS = "";
    public static final String MESSAGE_EXAMPLE = COMMAND_WORD;
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": " + MESSAGE_DESCRIPTION + "\n"
            + "Example: " + MESSAGE_EXAMPLE;

    public static final String SHOWING_HELP_MESSAGE = "Opened help window.";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        return new CommandResult(SHOWING_HELP_MESSAGE, true, false);
    }
}
