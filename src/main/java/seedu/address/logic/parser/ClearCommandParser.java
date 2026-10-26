package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.parser.exceptions.InputFormatInvalidException;
import seedu.address.logic.parser.exceptions.ParseException;

/** Parses a request to clear data or an explicit confirmation of that request. */
public class ClearCommandParser implements Parser<ClearCommand> {

    @Override
    public ClearCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String argument = args.trim();
        if (argument.isEmpty()) {
            return new ClearCommand();
        }
        if (argument.equals(ClearCommand.CONFIRMATION_ARGUMENT)) {
            return new ClearCommand(true);
        }
        throw new InputFormatInvalidException(Messages.formatParserError(
                "Use clear to request confirmation, or clear confirm to delete all data.", ClearCommand.MESSAGE_USAGE));
    }
}
