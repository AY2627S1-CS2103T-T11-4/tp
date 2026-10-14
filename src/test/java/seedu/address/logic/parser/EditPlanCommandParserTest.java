package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.DATE_DESC_CHEST;
import static seedu.address.logic.commands.CommandTestUtil.DATE_DESC_LEGS;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DATE_DESC_LENGTH;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DATE_DESC_LETTERS;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PLAN_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TITLE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.PLAN_DESC_CHEST;
import static seedu.address.logic.commands.CommandTestUtil.PLAN_DESC_LEGS;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HIIT;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_STRENGTH;
import static seedu.address.logic.commands.CommandTestUtil.TITLE_DESC_CHEST;
import static seedu.address.logic.commands.CommandTestUtil.TITLE_DESC_LEGS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HIIT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_STRENGTH;
import static seedu.address.logic.commands.CommandTestUtil.VALID_WORKOUT_DATE_LEGS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_WORKOUT_PLAN_LEGS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_WORKOUT_TITLE_LEGS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PLAN;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD;

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

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditPlanCommand.MESSAGE_USAGE);

    private EditPlanCommandParser parser = new EditPlanCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_WORKOUT_TITLE_LEGS, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", EditPlanCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + TITLE_DESC_LEGS, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + TITLE_DESC_LEGS, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_TITLE_DESC,
                WorkoutTitle.MESSAGE_CONSTRAINTS); // invalid title
        assertParseFailure(parser, "1" + INVALID_PLAN_DESC,
                WorkoutDescription.MESSAGE_CONSTRAINTS); // invalid description
        assertParseFailure(parser, "1" + INVALID_DATE_DESC_LETTERS,
                WorkoutDate.MESSAGE_CONSTRAINTS); // invalid date (invalid letters)
        assertParseFailure(parser, "1" + INVALID_DATE_DESC_LENGTH,
                WorkoutDate.MESSAGE_CONSTRAINTS); // invalid date (too long)
        assertParseFailure(parser, "1" + INVALID_TAG_DESC,
                WorkoutPlanTag.MESSAGE_CONSTRAINTS); // invalid tag

        // invalid plan followed by valid date
        assertParseFailure(parser, "1" + INVALID_PLAN_DESC + DATE_DESC_LEGS,
                WorkoutDescription.MESSAGE_CONSTRAINTS);

        // while parsing {@code PREFIX_TAG} alone will reset the tags of the {@code WorkoutPlan} being edited,
        // parsing it together with a valid tag results in error
        assertParseFailure(parser, "1" + TAG_DESC_STRENGTH + TAG_DESC_HIIT + TAG_EMPTY,
                WorkoutPlanTag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + TAG_DESC_STRENGTH + TAG_EMPTY + TAG_DESC_HIIT,
                WorkoutPlanTag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + TAG_EMPTY + TAG_DESC_STRENGTH + TAG_DESC_HIIT,
                WorkoutPlanTag.MESSAGE_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, "1" + INVALID_TITLE_DESC + INVALID_PLAN_DESC + VALID_WORKOUT_DATE_LEGS,
                WorkoutTitle.MESSAGE_CONSTRAINTS);
    }

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
        // title
        Index targetIndex = INDEX_THIRD;
        String userInput = targetIndex.getOneBased() + TITLE_DESC_LEGS;
        EditPlanCommand.EditPlanDescriptor descriptor = new EditPlanDescriptorBuilder()
                .withTitle(VALID_WORKOUT_TITLE_LEGS).build();
        EditPlanCommand expectedCommand = new EditPlanCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // description
        userInput = targetIndex.getOneBased() + PLAN_DESC_LEGS;
        descriptor = new EditPlanDescriptorBuilder().withDescription(VALID_WORKOUT_PLAN_LEGS).build();
        expectedCommand = new EditPlanCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // date
        userInput = targetIndex.getOneBased() + DATE_DESC_LEGS;
        descriptor = new EditPlanDescriptorBuilder().withDate(VALID_WORKOUT_DATE_LEGS).build();
        expectedCommand = new EditPlanCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // tags
        userInput = targetIndex.getOneBased() + TAG_DESC_STRENGTH;
        descriptor = new EditPlanDescriptorBuilder().withTags(VALID_TAG_STRENGTH).build();
        expectedCommand = new EditPlanCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_failure() {
        // More extensive testing of duplicate parameter detections is done in
        // AddPlanCommandParserTest#parse_repeatedNonTagValue_failure()

        // valid followed by invalid
        Index targetIndex = INDEX_FIRST;
        String userInput = targetIndex.getOneBased() + INVALID_PLAN_DESC + PLAN_DESC_LEGS;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PLAN));

        // invalid followed by valid
        userInput = targetIndex.getOneBased() + PLAN_DESC_LEGS + INVALID_PLAN_DESC;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PLAN));

        // multiple valid fields repeated
        userInput = targetIndex.getOneBased() + TITLE_DESC_LEGS + PLAN_DESC_LEGS + DATE_DESC_LEGS
                + TAG_DESC_STRENGTH + TITLE_DESC_LEGS + PLAN_DESC_LEGS + DATE_DESC_LEGS + TAG_DESC_STRENGTH
                + TITLE_DESC_CHEST + PLAN_DESC_CHEST + DATE_DESC_CHEST + TAG_DESC_HIIT;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME, PREFIX_PLAN, PREFIX_DATE));

        // multiple invalid values
        userInput = targetIndex.getOneBased() + INVALID_TITLE_DESC + INVALID_PLAN_DESC + INVALID_DATE_DESC_LETTERS
                + INVALID_TITLE_DESC + INVALID_PLAN_DESC + INVALID_DATE_DESC_LENGTH + INVALID_TAG_DESC;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME, PREFIX_PLAN, PREFIX_DATE));
    }

    @Test
    public void parse_resetTags_success() {
        Index targetIndex = INDEX_THIRD;
        String userInput = targetIndex.getOneBased() + TAG_EMPTY;

        EditPlanCommand.EditPlanDescriptor descriptor = new EditPlanDescriptorBuilder().withTags().build();
        EditPlanCommand expectedCommand = new EditPlanCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
