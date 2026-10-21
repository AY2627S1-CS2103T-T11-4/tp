package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.student.NameContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    private static final Logger logger = LogsCenter.getLogger(FindCommandParser.class);

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentValidator argValidator = new ArgumentValidator();
        NameContainsKeywordsPredicate nckp;
        ParsingStage stage = ParsingStage.TOKENIZATION;
        try {
            ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args);

            stage = ParsingStage.ARGUMENT_VALIDATION;
            argValidator.verifyArgs(argMultimap);

            stage = ParsingStage.FIELD_PARSING;
            nckp = this.parseValidFields(argMultimap.getPreamble());

        } catch (ParseException pe) {
            logger.finer("Parsing of find command failed at " + stage.getMessage());
            throw pe.withMessage(Messages.formatParserError(pe.getMessage(), FindCommand.MESSAGE_USAGE));
        }

        return new FindCommand(nckp);
    }

    /**
     * Parses the validated {@code args} string to produce the FindCommand object.
     */
    private NameContainsKeywordsPredicate parseValidFields(String queries) {
        String[] nameKeywords = queries.split("\\s+");

        return new NameContainsKeywordsPredicate(List.of(nameKeywords));
    }

}
