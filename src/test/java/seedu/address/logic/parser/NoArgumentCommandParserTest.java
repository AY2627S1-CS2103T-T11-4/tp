package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_UNEXPECTED_PARAMETERS;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class NoArgumentCommandParserTest {
    private static final String MESSAGE_USAGE = ClearCommand.MESSAGE_USAGE;
    private final NoArgumentCommandParser<ClearCommand> parser =
            new NoArgumentCommandParser<>(ClearCommand::new, MESSAGE_USAGE);

    @Test
    public void parse_emptyArguments_returnsCommand() throws ParseException {
        assertTrue(parser.parse("") instanceof ClearCommand);
    }

    @Test
    public void parse_whitespaceArguments_returnsCommand() throws ParseException {
        assertTrue(parser.parse(" \t\n") instanceof ClearCommand);
    }

    @Test
    public void parse_unexpectedArguments_throwsParseException() {
        String expectedMessage = Messages.formatParserError(MESSAGE_UNEXPECTED_PARAMETERS, MESSAGE_USAGE);
        assertThrows(ParseException.class, expectedMessage, () -> parser.parse("unexpected"));
    }

    @Test
    public void parse_nullArguments_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> parser.parse(null));
    }
}
