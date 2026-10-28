package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.parser.exceptions.FieldValueInvalidException;
import seedu.address.logic.parser.exceptions.InputFormatInvalidException;
import seedu.address.model.ModelManager;

public class ClearCommandParserTest {
    private final ClearCommandParser parser = new ClearCommandParser();

    @Test
    public void parse_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> parser.parse(null));
    }

    @Test
    public void parse_emptyArgs_requestsConfirmation() throws Exception {
        for (String argument : List.of("", " \t\r\n ")) {
            assertEquals(ClearCommand.MESSAGE_CONFIRMATION,
                    parser.parse(argument).execute(new ModelManager()).getFeedbackToUser());
        }
    }

    @Test
    public void parse_confirmation_returnsConfirmedCommand() throws Exception {
        for (String argument : List.of("confirm", " \t confirm \t ")) {
            assertEquals(ClearCommand.MESSAGE_SUCCESS,
                    parser.parse(argument).execute(new ModelManager()).getFeedbackToUser());
        }
    }

    @Test
    public void parse_invalidConfirmation_throwsFieldValueInvalidException() {
        for (String argument : List.of("3", "yes", "CONFIRM", "confirm extra", "confirm confirm")) {
            assertThrows(FieldValueInvalidException.class,
                    Messages.formatParserError(ParserUtil.MESSAGE_INVALID_CLEAR_CONFIRMATION,
                            ClearCommand.MESSAGE_USAGE), () -> parser.parse(argument));
        }
    }

    @Test
    public void parse_invalidPrefix_throwsInputFormatInvalidException() {
        for (String argument : List.of(" --invalid", " confirm --invalid value")) {
            assertThrows(InputFormatInvalidException.class,
                    Messages.formatParserError(Messages.getErrorMessageForInvalidPrefixes(CliSyntax.PREFIX_INVALID),
                            ClearCommand.MESSAGE_USAGE), () -> parser.parse(argument));
        }
    }

    @Test
    public void parse_emptyPrefix_throwsInputFormatInvalidException() {
        assertThrows(InputFormatInvalidException.class,
                Messages.formatParserError(Messages.MESSAGE_EMPTY_PREFIX, ClearCommand.MESSAGE_USAGE), ()
                    -> parser.parse(" confirm -- "));
    }
}
