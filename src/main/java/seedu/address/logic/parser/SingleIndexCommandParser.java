package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.function.Function;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.SingleIndexCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses commands that takes in a single index as an argument,
 * and returns the associated command object
 *
 * @param <C> a SingleIndexCommand object
 */
public class SingleIndexCommandParser<C extends SingleIndexCommand> implements Parser<C> {

    private static final Logger logger = LogsCenter.getLogger(SingleIndexCommandParser.class);

    private final Function<Index, C> commandConstructor;
    private final String commandMessageUsage;
    private final String commandWord;

    /**
     * Initiates a SingleIndexCommandParser object
     * Parameters are needed as they cannot be accessed purely through the type parameter
     *
     * @param commandConstructor the constructor method of C
     * @param commandMessageUsage the MESSAGE_USAGE constant field of C
     */
    public SingleIndexCommandParser(
            Function<Index, C> commandConstructor,
            String commandMessageUsage,
            String commandWord) {
        requireAllNonNull(commandConstructor, commandMessageUsage, commandWord);
        this.commandConstructor = commandConstructor;
        this.commandMessageUsage = commandMessageUsage;
        this.commandWord = commandWord;
    }

    /**
     * Parses the given {@code String} of arguments containing a single index,
     * and returns the ommand object for execution
     *
     * @throws ParseException if the user input does not conform to the expected format
     */
    public C parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentValidator argValidator = new ArgumentValidator();
        Index index;

        ParsingStage stage = ParsingStage.TOKENIZATION;
        try {
            ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args);

            stage = ParsingStage.ARGUMENT_VALIDATION;
            argValidator.verifyArgs(argMultimap);

            stage = ParsingStage.FIELD_PARSING;
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            logger.finer("Parsing of " + this.commandWord + " failed at " + stage.getMessage());
            throw new ParseException(Messages.formatParserError(pe.getMessage(), this.commandMessageUsage), pe);
        }

        return this.commandConstructor.apply(index);
    }
}
