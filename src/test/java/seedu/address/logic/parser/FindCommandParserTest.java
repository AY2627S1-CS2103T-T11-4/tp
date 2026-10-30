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

    // BV: length < 1 (invalid).
    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", this.formatParserError(MESSAGE_MISSING_PREAMBLE));
    }

    // One invalid input at a time: only the prefix is unknown.
    @Test
    public void parse_invalidPrefix_throwsParseException() {
        assertParseFailure(parser, "Alice --invalid Bob",
                this.formatParserError(Messages.getErrorMessageForInvalidPrefixes(PREFIX_INVALID)));
    }

    // EP: bare dashes are searchable text (valid).
    @Test
    public void parse_bareDashes_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(List.of("Alice", "--", "Bob")));
        assertParseSuccess(parser, "Alice -- Bob", expectedFindCommand);
    }

    // EP: every keyword violates name constraints (invalid).
    @Test
    public void parse_allKeywordsInvalid_throwsParseException() {
        for (String input : List.of("!!!", "!!! ???", "Alice! Bob?", "A_B", "O'Neil", " \t !!! \t ??? ")) {
            assertParseFailure(parser, input, this.formatParserError(FindCommand.MESSAGE_INVALID_KEYWORDS));
        }
    }

    // EP: at least one keyword satisfies name constraints (valid).
    @Test
    public void parse_mixedValidAndInvalidKeywords_returnsFindCommand() {
        assertParseSuccess(parser, "Alice !!!",
                new FindCommand(new NameContainsKeywordsPredicate(List.of("Alice", "!!!"))));
        assertParseSuccess(parser, "!!! Bob? Alice",
                new FindCommand(new NameContainsKeywordsPredicate(List.of("!!!", "Bob?", "Alice"))));
    }

    // Each valid input at least once: alphabetic and numeric keywords are accepted.
    @Test
    public void parse_alphanumericKeywords_returnsFindCommand() {
        assertParseSuccess(parser, "Alice123 123",
                new FindCommand(new NameContainsKeywordsPredicate(List.of("Alice123", "123"))));
    }

    // Each valid input at least once: multiple valid keywords parse successfully.
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
