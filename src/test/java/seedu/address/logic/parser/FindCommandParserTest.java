package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_MISSING_PREAMBLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INVALID;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.FindCommand;
import seedu.address.model.student.NameContainsKeywordsPredicate;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    private String formatParserError(String errorMessage) {
        return Messages.formatParserError(errorMessage, FindCommand.MESSAGE_USAGE);
    }

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", this.formatParserError(MESSAGE_MISSING_PREAMBLE));
    }

    @Test
    public void parse_invalidPrefix_throwsParseException() {
        assertParseFailure(parser, "Alice --invalid Bob",
                this.formatParserError(Messages.getErrorMessageForInvalidPrefixes(PREFIX_INVALID)));
    }

    @Test
    public void parse_bareDashes_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(List.of("Alice", "--", "Bob")));
        assertParseSuccess(parser, "Alice -- Bob", expectedFindCommand);
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(List.of("Alice", "Bob")));
        assertParseSuccess(parser, "Alice Bob", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n Alice \n \t Bob  \t", expectedFindCommand);
    }

}
