package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Set;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/** Parses a request to clear data or an explicit confirmation of that request. */
public class ClearCommandParser implements Parser<ClearCommand> {

    private static final Logger logger = LogsCenter.getLogger(ClearCommandParser.class);

    @Override
    public ClearCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentValidator argValidator = new ArgumentValidator(false, Set.of(), Set.of(), Set.of());
        boolean confirmed;

        ParsingStage stage = ParsingStage.TOKENIZATION;
        try {
            ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args);

            stage = ParsingStage.ARGUMENT_VALIDATION;
            argValidator.verifyArgs(argMultimap);

            stage = ParsingStage.FIELD_PARSING;
            confirmed = ParserUtil.parseClearConfirmation(argMultimap.getPreamble());
        } catch (ParseException pe) {
            logger.finer("Parsing of clear command failed at " + stage.getMessage());
            throw pe.withMessage(Messages.formatParserError(pe.getMessage(), ClearCommand.MESSAGE_USAGE));
        }

        return new ClearCommand(confirmed);
    }
}
