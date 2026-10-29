package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.Messages.MESSAGE_UNEXPECTED_PARAMETERS;

import java.util.function.Supplier;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.Command;
import seedu.address.logic.parser.exceptions.FieldValueInvalidException;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses commands that do not accept arguments.
 *
 * @param <C> command type produced by this parser
 */
public class NoArgumentCommandParser<C extends Command> implements Parser<C> {
    private final Supplier<C> commandConstructor;
    private final String commandMessageUsage;

    /**
     * Creates a parser for a command without arguments.
     *
     * @param commandConstructor constructor for the command
     * @param commandMessageUsage usage message for the command
     */
    public NoArgumentCommandParser(Supplier<C> commandConstructor, String commandMessageUsage) {
        requireAllNonNull(commandConstructor, commandMessageUsage);
        this.commandConstructor = commandConstructor;
        this.commandMessageUsage = commandMessageUsage;
    }

    /**
     * Parses arguments for a command that does not accept arguments.
     *
     * @throws ParseException if non-whitespace arguments are present
     */
    @Override
    public C parse(String args) throws ParseException {
        requireNonNull(args);
        if (!args.isBlank()) {
            throw new FieldValueInvalidException(
                    Messages.formatParserError(MESSAGE_UNEXPECTED_PARAMETERS, commandMessageUsage));
        }
        return commandConstructor.get();
    }
}
