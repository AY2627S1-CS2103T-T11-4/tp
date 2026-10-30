package seedu.address.logic.parser;

import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PREFIX_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLEAR_TAGS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INVALID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditStudentCommand;
import seedu.address.model.student.Address;
import seedu.address.model.student.Email;
import seedu.address.model.student.Name;
import seedu.address.model.student.Phone;
import seedu.address.model.tag.StudentTag;
import seedu.address.testutil.EditStudentDescriptorBuilder;

public class EditStudentCommandParserTest {

    private static final String TAG_EMPTY = " " + PREFIX_TAG;
    private static final String CLEAR_TAGS = " " + PREFIX_CLEAR_TAGS;

    private EditStudentCommandParser parser = new EditStudentCommandParser();

    private String formatParserError(String errorMessage) {
        return Messages.formatParserError(errorMessage, EditStudentCommand.MESSAGE_USAGE);
    }

    @Test
    public void parse_missingParts_failure() {
        // One invalid input at a time: only the index is missing.
        assertParseFailure(parser, VALID_NAME_AMY, this.formatParserError(MESSAGE_INVALID_INDEX));

        // One invalid input at a time: only the edit field is missing.
        assertParseFailure(parser, "1", EditStudentCommand.MESSAGE_NOT_EDITED);

    }

    @Test
    public void parse_invalidPreamble_failure() {
        // EP: negative index (invalid).
        assertParseFailure(parser, "-5" + NAME_DESC_AMY, this.formatParserError(MESSAGE_INVALID_INDEX));

        // BV: index < 1 (invalid).
        assertParseFailure(parser, "0" + NAME_DESC_AMY, this.formatParserError(MESSAGE_INVALID_INDEX));

        // EP: non-index preamble text (invalid).
        assertParseFailure(parser, "1 some random string", this.formatParserError(MESSAGE_INVALID_INDEX));

        // EP: malformed prefix in the preamble (invalid).
        assertParseFailure(parser, "1 i/ string", this.formatParserError(MESSAGE_INVALID_INDEX));
    }

    @Test
    public void parse_invalidValue_failure() {
        // One invalid input at a time: only the name is invalid.
        assertParseFailure(parser, "1" + INVALID_NAME_DESC,
                this.formatParserError(Name.MESSAGE_CONSTRAINTS)); // invalid name
        // One invalid input at a time: only the phone is invalid.
        assertParseFailure(parser, "1" + INVALID_PHONE_DESC,
                this.formatParserError(Phone.MESSAGE_CONSTRAINTS)); // invalid phone
        // One invalid input at a time: only the email is invalid.
        assertParseFailure(parser, "1" + INVALID_EMAIL_DESC,
                this.formatParserError(Email.MESSAGE_CONSTRAINTS)); // invalid email
        // One invalid input at a time: only the address is invalid.
        assertParseFailure(parser, "1" + INVALID_ADDRESS_DESC,
                this.formatParserError(Address.MESSAGE_CONSTRAINTS)); // invalid address
        // One invalid input at a time: only the tag is invalid.
        assertParseFailure(parser, "1" + INVALID_TAG_DESC + CLEAR_TAGS,
                this.formatParserError(StudentTag.MESSAGE_CONSTRAINTS)); // invalid tag

        // One invalid input at a time: phone is invalid while email remains valid.
        assertParseFailure(parser, "1" + INVALID_PHONE_DESC + EMAIL_DESC_AMY,
                this.formatParserError(Phone.MESSAGE_CONSTRAINTS));

        // One invalid input at a time: only the tag is empty.
        assertParseFailure(parser, "1" + TAG_EMPTY,
                this.formatParserError(StudentTag.MESSAGE_CONSTRAINTS));

        // Invalid inputs combined after separate tests: name and email are invalid.
        assertParseFailure(parser, "1" + INVALID_NAME_DESC + INVALID_EMAIL_DESC + VALID_ADDRESS_AMY + VALID_PHONE_AMY,
                this.formatParserError(Name.MESSAGE_CONSTRAINTS));
    }

    // Each valid input at least once: all editable student fields parse successfully.
    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + TAG_DESC_HUSBAND
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + NAME_DESC_AMY + TAG_DESC_FRIEND;

        EditStudentCommand.EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder()
                .withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
                .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();
        EditStudentCommand expectedCommand = new EditStudentCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    // EP: a subset of editable student fields (valid).
    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + EMAIL_DESC_AMY;

        EditStudentCommand.EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder()
                .withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_AMY).build();
        EditStudentCommand expectedCommand = new EditStudentCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // Each valid input at least once: name succeeds in an edit test.
        Index targetIndex = INDEX_THIRD;
        String userInput = targetIndex.getOneBased() + NAME_DESC_AMY;
        EditStudentCommand.EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder()
                .withName(VALID_NAME_AMY).build();
        EditStudentCommand expectedCommand = new EditStudentCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // Each valid input at least once: phone succeeds in an edit test.
        userInput = targetIndex.getOneBased() + PHONE_DESC_AMY;
        descriptor = new EditStudentDescriptorBuilder().withPhone(VALID_PHONE_AMY).build();
        expectedCommand = new EditStudentCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // Each valid input at least once: email succeeds in an edit test.
        userInput = targetIndex.getOneBased() + EMAIL_DESC_AMY;
        descriptor = new EditStudentDescriptorBuilder().withEmail(VALID_EMAIL_AMY).build();
        expectedCommand = new EditStudentCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // Each valid input at least once: address succeeds in an edit test.
        userInput = targetIndex.getOneBased() + ADDRESS_DESC_AMY;
        descriptor = new EditStudentDescriptorBuilder().withAddress(VALID_ADDRESS_AMY).build();
        expectedCommand = new EditStudentCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // Each valid input at least once: tags succeed in an edit test.
        userInput = targetIndex.getOneBased() + TAG_DESC_FRIEND;
        descriptor = new EditStudentDescriptorBuilder().withTags(VALID_TAG_FRIEND).build();
        expectedCommand = new EditStudentCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    // EP: repeated non-repeatable phone field (invalid).
    @Test
    public void parse_multipleRepeatedFields_failure() {
        Index targetIndex = INDEX_FIRST;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_AMY + PHONE_DESC_BOB;

        assertParseFailure(parser, userInput,
                this.formatParserError(Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE)));
    }

    @Test
    public void parse_invalidField_failure() {
        // One invalid input at a time: only the prefix is unknown.
        assertParseFailure(parser, "1" + INVALID_PREFIX_DESC,
                this.formatParserError(Messages.getErrorMessageForInvalidPrefixes(PREFIX_INVALID)));

    }

    // BV: 0 <= count <= MAX_TAG_COUNT (valid).
    @Test
    public void parse_clearTags_success() {
        Index targetIndex = INDEX_THIRD;
        String userInput = targetIndex.getOneBased() + CLEAR_TAGS;

        EditStudentCommand.EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder().withTags().build();
        EditStudentCommand expectedCommand = new EditStudentCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_clearTagsWithOtherField_success() {
        Index targetIndex = INDEX_THIRD;
        String userInput = targetIndex.getOneBased() + NAME_DESC_AMY + CLEAR_TAGS;

        EditStudentCommand.EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder()
                .withName(VALID_NAME_AMY).withTags().build();
        EditStudentCommand expectedCommand = new EditStudentCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_tagAndClearTags_success() {
        Index targetIndex = INDEX_THIRD;
        String userInput = targetIndex.getOneBased() + TAG_DESC_FRIEND + CLEAR_TAGS;

        EditStudentCommand.EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder().withTags().build();
        EditStudentCommand expectedCommand = new EditStudentCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    // EP: clear-tags prefix has an unexpected value (invalid).
    @Test
    public void parse_clearTagsWithValue_failure() {
        assertParseFailure(parser, "1" + CLEAR_TAGS + " unexpected",
                this.formatParserError(Messages.getErrorMessageForExtraValues(PREFIX_CLEAR_TAGS)));
    }

}
