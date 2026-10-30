package seedu.address.logic.parser;

import static seedu.address.logic.commands.CommandTestUtil.DATE_DESC_LEGS;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DATE_DESC_LENGTH;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DATE_DESC_LETTERS;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PLAN_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PREFIX_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TITLE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.PLAN_DESC_CHEST;
import static seedu.address.logic.commands.CommandTestUtil.PLAN_DESC_LEGS;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HIIT;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_STRENGTH;
import static seedu.address.logic.commands.CommandTestUtil.TITLE_DESC_LEGS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HIIT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_STRENGTH;
import static seedu.address.logic.commands.CommandTestUtil.VALID_WORKOUT_DATE_LEGS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_WORKOUT_PLAN_LEGS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_WORKOUT_TITLE_LEGS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLEAR_TAGS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INVALID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PLAN;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditPlanCommand;
import seedu.address.model.tag.WorkoutPlanTag;
import seedu.address.model.workoutplan.WorkoutDate;
import seedu.address.model.workoutplan.WorkoutDescription;
import seedu.address.model.workoutplan.WorkoutTitle;
import seedu.address.testutil.EditPlanDescriptorBuilder;

public class EditPlanCommandParserTest {

    private static final String TAG_EMPTY = " " + PREFIX_TAG;
    private static final String CLEAR_TAGS = " " + PREFIX_CLEAR_TAGS;

    private EditPlanCommandParser parser = new EditPlanCommandParser();

    private String formatParserError(String errorMessage) {
        return Messages.formatParserError(errorMessage, EditPlanCommand.MESSAGE_USAGE);
    }

    @Test
    public void parse_invalidCalendarDate_reportsGenericDateError() {
        // EP: month exceeds 12 (invalid).
        assertParseFailure(parser, "1 --date 01132026",
                this.formatParserError(WorkoutDate.MESSAGE_CONSTRAINTS));
        // EP: day exceeds 31 (invalid).
        assertParseFailure(parser, "1 --date 32122026",
                this.formatParserError(WorkoutDate.MESSAGE_CONSTRAINTS));
        // EP: day is invalid for February (invalid).
        assertParseFailure(parser, "1 --date 31022026",
                this.formatParserError(WorkoutDate.MESSAGE_CONSTRAINTS));
        // BV: 29 February in a non-leap year (invalid).
        assertParseFailure(parser, "1 --date 29022026",
                this.formatParserError(WorkoutDate.MESSAGE_CONSTRAINTS));
    }

    // BV: 29 February in a leap year (valid).
    @Test
    public void parse_validLeapDay_success() {
        EditPlanCommand.EditPlanDescriptor descriptor =
                new EditPlanDescriptorBuilder().withDate(LocalDate.of(2024, 2, 29)).build();
        assertParseSuccess(parser, "1 --date 29022024", new EditPlanCommand(INDEX_FIRST, descriptor));
    }


    @Test
    public void parse_missingParts_failure() {
        // One invalid input at a time: only the index is missing.
        assertParseFailure(parser, VALID_WORKOUT_TITLE_LEGS, this.formatParserError(MESSAGE_INVALID_INDEX));

        // One invalid input at a time: only the edit field is missing.
        assertParseFailure(parser, "1", EditPlanCommand.MESSAGE_NOT_EDITED);

    }

    @Test
    public void parse_invalidPreamble_failure() {
        // EP: negative index (invalid).
        assertParseFailure(parser, "-5" + TITLE_DESC_LEGS, this.formatParserError(MESSAGE_INVALID_INDEX));

        // BV: index < 1 (invalid).
        assertParseFailure(parser, "0" + TITLE_DESC_LEGS, this.formatParserError(MESSAGE_INVALID_INDEX));

        // EP: non-index preamble text (invalid).
        assertParseFailure(parser, "1 some random string", this.formatParserError(MESSAGE_INVALID_INDEX));

        // EP: malformed prefix in the preamble (invalid).
        assertParseFailure(parser, "1 i/ string", this.formatParserError(MESSAGE_INVALID_INDEX));
    }

    @Test
    public void parse_invalidValue_failure() {
        // One invalid input at a time: only the workout title is invalid.
        assertParseFailure(parser, "1" + INVALID_TITLE_DESC,
                this.formatParserError(WorkoutTitle.MESSAGE_CONSTRAINTS)); // invalid title
        // One invalid input at a time: only the workout description is invalid.
        assertParseFailure(parser, "1" + INVALID_PLAN_DESC,
                this.formatParserError(WorkoutDescription.MESSAGE_CONSTRAINTS)); // invalid description
        // One invalid input at a time: only the date characters are invalid.
        assertParseFailure(parser, "1" + INVALID_DATE_DESC_LETTERS,
                this.formatParserError(WorkoutDate.MESSAGE_CONSTRAINTS)); // invalid date (invalid letters)
        // One invalid input at a time: only the date length is invalid.
        assertParseFailure(parser, "1" + INVALID_DATE_DESC_LENGTH,
                this.formatParserError(WorkoutDate.MESSAGE_CONSTRAINTS)); // invalid date (too long)
        // One invalid input at a time: only the tag is invalid.
        assertParseFailure(parser, "1" + INVALID_TAG_DESC + CLEAR_TAGS,
                this.formatParserError(WorkoutPlanTag.MESSAGE_CONSTRAINTS)); // invalid tag

        // One invalid input at a time: description is invalid while date remains valid.
        assertParseFailure(parser, "1" + INVALID_PLAN_DESC + DATE_DESC_LEGS,
                this.formatParserError(WorkoutDescription.MESSAGE_CONSTRAINTS));

        // One invalid input at a time: only the tag is empty.
        assertParseFailure(parser, "1" + TAG_EMPTY,
                this.formatParserError(WorkoutPlanTag.MESSAGE_CONSTRAINTS));

        // Invalid inputs combined after separate tests: title and description are invalid.
        assertParseFailure(parser, "1" + INVALID_TITLE_DESC + INVALID_PLAN_DESC + DATE_DESC_LEGS,
                this.formatParserError(WorkoutTitle.MESSAGE_CONSTRAINTS));
    }

    // Each valid input at least once: all editable workout-plan fields parse successfully.
    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND;
        String userInput = targetIndex.getOneBased() + TITLE_DESC_LEGS + PLAN_DESC_LEGS
                + DATE_DESC_LEGS + TAG_DESC_STRENGTH + TAG_DESC_HIIT;

        EditPlanCommand.EditPlanDescriptor descriptor = new EditPlanDescriptorBuilder()
                .withTitle(VALID_WORKOUT_TITLE_LEGS)
                .withDescription(VALID_WORKOUT_PLAN_LEGS).withDate(VALID_WORKOUT_DATE_LEGS)
                .withTags(VALID_TAG_STRENGTH, VALID_TAG_HIIT).build();
        EditPlanCommand expectedCommand = new EditPlanCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    // EP: a subset of editable workout-plan fields (valid).
    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST;
        String userInput = targetIndex.getOneBased() + PLAN_DESC_LEGS + DATE_DESC_LEGS;

        EditPlanCommand.EditPlanDescriptor descriptor = new EditPlanDescriptorBuilder()
                .withDescription(VALID_WORKOUT_PLAN_LEGS)
                .withDate(VALID_WORKOUT_DATE_LEGS).build();
        EditPlanCommand expectedCommand = new EditPlanCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // Each valid input at least once: title succeeds in an edit test.
        Index targetIndex = INDEX_THIRD;
        String userInput = targetIndex.getOneBased() + TITLE_DESC_LEGS;
        EditPlanCommand.EditPlanDescriptor descriptor = new EditPlanDescriptorBuilder()
                .withTitle(VALID_WORKOUT_TITLE_LEGS).build();
        EditPlanCommand expectedCommand = new EditPlanCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // Each valid input at least once: description succeeds in an edit test.
        userInput = targetIndex.getOneBased() + PLAN_DESC_LEGS;
        descriptor = new EditPlanDescriptorBuilder().withDescription(VALID_WORKOUT_PLAN_LEGS).build();
        expectedCommand = new EditPlanCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // Each valid input at least once: date succeeds in an edit test.
        userInput = targetIndex.getOneBased() + DATE_DESC_LEGS;
        descriptor = new EditPlanDescriptorBuilder().withDate(VALID_WORKOUT_DATE_LEGS).build();
        expectedCommand = new EditPlanCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // Each valid input at least once: tags succeed in an edit test.
        userInput = targetIndex.getOneBased() + TAG_DESC_STRENGTH;
        descriptor = new EditPlanDescriptorBuilder().withTags(VALID_TAG_STRENGTH).build();
        expectedCommand = new EditPlanCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    // EP: repeated non-repeatable workout-plan field (invalid).
    @Test
    public void parse_multipleRepeatedFields_failure() {
        Index targetIndex = INDEX_FIRST;
        String userInput = targetIndex.getOneBased() + PLAN_DESC_LEGS + PLAN_DESC_CHEST;

        assertParseFailure(parser, userInput,
                this.formatParserError(Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PLAN)));
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

        EditPlanCommand.EditPlanDescriptor descriptor = new EditPlanDescriptorBuilder().withTags().build();
        EditPlanCommand expectedCommand = new EditPlanCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_clearTagsWithOtherField_success() {
        Index targetIndex = INDEX_THIRD;
        String userInput = targetIndex.getOneBased() + TITLE_DESC_LEGS + CLEAR_TAGS;

        EditPlanCommand.EditPlanDescriptor descriptor = new EditPlanDescriptorBuilder()
                .withTitle(VALID_WORKOUT_TITLE_LEGS).withTags().build();
        EditPlanCommand expectedCommand = new EditPlanCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_tagAndClearTags_success() {
        Index targetIndex = INDEX_THIRD;
        String userInput = targetIndex.getOneBased() + TAG_DESC_STRENGTH + CLEAR_TAGS;

        EditPlanCommand.EditPlanDescriptor descriptor = new EditPlanDescriptorBuilder().withTags().build();
        EditPlanCommand expectedCommand = new EditPlanCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

}
