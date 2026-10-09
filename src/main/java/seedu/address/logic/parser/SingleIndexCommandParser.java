package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.function.Function;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.SingleIndexCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses commands that takes in a single index as an argument,
 * and returns the associated command object
 *
 * @param <C> a SingleIndexCommand object
 */
public class SingleIndexCommandParser<C extends SingleIndexCommand> implements Parser<C> {

    private final Function<Index, C> commandConstructor;
    private final String commandMessageUsage;

    /**
     * Initiates a SingleIndexCommandParser object
     * Parameters are needed as they cannot be accessed purely through the type parameter
     *
     * @param commandConstructor the constructor method of C
     * @param commandMessageUsage the MESSAGE_USAGE constant field of C
     */
    public SingleIndexCommandParser(
            Function<Index, C> commandConstructor,
            String commandMessageUsage) {
        this.commandConstructor = commandConstructor;
        this.commandMessageUsage = commandMessageUsage;
    }

    /**
     * Parses the given {@code String} of arguments containing a single index,
     * and returns the ommand object for execution
     *
     * @throws ParseException if the user input does not conform to the expected format
     */
    public C parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return this.commandConstructor.apply(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, this.commandMessageUsage), pe);
        }
    }
}
