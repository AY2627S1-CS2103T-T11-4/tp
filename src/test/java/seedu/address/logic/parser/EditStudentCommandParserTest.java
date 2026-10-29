package seedu.address.logic.parser;

import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ADD_TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.CLEAR_TAGS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.DELETE_TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.DELETE_TAG_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMPTY_ADD_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADD_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PREFIX_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INVALID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD;

import java.util.Set;

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

    private EditStudentCommandParser parser = new EditStudentCommandParser();

    private String formatParserError(String errorMessage) {
        return Messages.formatParserError(errorMessage, EditStudentCommand.MESSAGE_USAGE);
    }

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_NAME_AMY, this.formatParserError(MESSAGE_INVALID_INDEX));

        // no field specified
        assertParseFailure(parser, "1", EditStudentCommand.MESSAGE_NOT_EDITED);

    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + NAME_DESC_AMY, this.formatParserError(MESSAGE_INVALID_INDEX));

        // zero index
        assertParseFailure(parser, "0" + NAME_DESC_AMY, this.formatParserError(MESSAGE_INVALID_INDEX));

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", this.formatParserError(MESSAGE_INVALID_INDEX));

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", this.formatParserError(MESSAGE_INVALID_INDEX));
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_NAME_DESC,
                this.formatParserError(Name.MESSAGE_CONSTRAINTS)); // invalid name
        assertParseFailure(parser, "1" + INVALID_PHONE_DESC,
                this.formatParserError(Phone.MESSAGE_CONSTRAINTS)); // invalid phone
        assertParseFailure(parser, "1" + INVALID_EMAIL_DESC,
                this.formatParserError(Email.MESSAGE_CONSTRAINTS)); // invalid email
        assertParseFailure(parser, "1" + INVALID_ADDRESS_DESC,
                this.formatParserError(Address.MESSAGE_CONSTRAINTS)); // invalid address
        assertParseFailure(parser, "1" + INVALID_ADD_TAG_DESC + CLEAR_TAGS_DESC,
                this.formatParserError(StudentTag.MESSAGE_CONSTRAINTS)); // invalid tag

        // invalid phone followed by valid email
        assertParseFailure(parser, "1" + INVALID_PHONE_DESC + EMAIL_DESC_AMY,
                this.formatParserError(Phone.MESSAGE_CONSTRAINTS));

        // an empty tag remains invalid since --clearTags is available for clearing tags
        assertParseFailure(parser, "1" + EMPTY_ADD_TAG_DESC,
                this.formatParserError(StudentTag.MESSAGE_CONSTRAINTS));

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, "1" + INVALID_NAME_DESC + INVALID_EMAIL_DESC + VALID_ADDRESS_AMY + VALID_PHONE_AMY,
                this.formatParserError(Name.MESSAGE_CONSTRAINTS));
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + ADD_TAG_DESC_FRIEND
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + NAME_DESC_AMY + DELETE_TAG_DESC_HUSBAND;

        EditStudentCommand.EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder()
                .withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
                .withTagsToAdd(VALID_TAG_FRIEND).withTagsToDelete(VALID_TAG_HUSBAND).build();
        EditStudentCommand expectedCommand = new EditStudentCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

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
        // name
        Index targetIndex = INDEX_THIRD;
        String userInput = targetIndex.getOneBased() + NAME_DESC_AMY;
        EditStudentCommand.EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder()
                .withName(VALID_NAME_AMY).build();
        EditStudentCommand expectedCommand = new EditStudentCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // phone
        userInput = targetIndex.getOneBased() + PHONE_DESC_AMY;
        descriptor = new EditStudentDescriptorBuilder().withPhone(VALID_PHONE_AMY).build();
        expectedCommand = new EditStudentCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // email
        userInput = targetIndex.getOneBased() + EMAIL_DESC_AMY;
        descriptor = new EditStudentDescriptorBuilder().withEmail(VALID_EMAIL_AMY).build();
        expectedCommand = new EditStudentCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // address
        userInput = targetIndex.getOneBased() + ADDRESS_DESC_AMY;
        descriptor = new EditStudentDescriptorBuilder().withAddress(VALID_ADDRESS_AMY).build();
        expectedCommand = new EditStudentCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // tags
        userInput = targetIndex.getOneBased() + ADD_TAG_DESC_FRIEND;
        descriptor = new EditStudentDescriptorBuilder().withTagsToAdd(VALID_TAG_FRIEND).build();
        expectedCommand = new EditStudentCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_failure() {
        Index targetIndex = INDEX_FIRST;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_AMY + PHONE_DESC_BOB;

        assertParseFailure(parser, userInput,
                this.formatParserError(Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE)));
    }

    @Test
    public void parse_invalidField_failure() {
        // invalid prefix alone
        assertParseFailure(parser, "1" + INVALID_PREFIX_DESC,
                this.formatParserError(Messages.getErrorMessageForInvalidPrefixes(PREFIX_INVALID)));
    }

    @Test
    public void parse_clearTags_success() {
        Index targetIndex = INDEX_THIRD;
        String userInput = targetIndex.getOneBased() + CLEAR_TAGS_DESC;

        EditStudentCommand.EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder()
                .withClearTags().build();
        EditStudentCommand expectedCommand = new EditStudentCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_clearTagsWithOtherField_success() {
        Index targetIndex = INDEX_THIRD;
        String userInput = targetIndex.getOneBased() + NAME_DESC_AMY + CLEAR_TAGS_DESC;

        EditStudentCommand.EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder()
                .withName(VALID_NAME_AMY).withClearTags().build();
        EditStudentCommand expectedCommand = new EditStudentCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_tagAndClearTags_success() {
        Index targetIndex = INDEX_THIRD;
        String userInput = targetIndex.getOneBased() + ADD_TAG_DESC_FRIEND + CLEAR_TAGS_DESC;

        EditStudentCommand.EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder()
                .withTagsToAdd(VALID_TAG_FRIEND).withClearTags().build();
        EditStudentCommand expectedCommand = new EditStudentCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_addDeleteSameTag_failure() {
        String userInput = INDEX_FIRST.getOneBased() + ADD_TAG_DESC_FRIEND + DELETE_TAG_DESC_FRIEND;
        assertParseFailure(parser, userInput, this.formatParserError(
                Messages.getErrorMessageForTagConflicts(Set.of(new StudentTag(VALID_TAG_FRIEND)))));
    }

}
