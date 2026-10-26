package seedu.address.logic.parser;

import static seedu.address.logic.commands.CommandTestUtil.ADD_TAG_DESC_HIIT;
import static seedu.address.logic.commands.CommandTestUtil.ADD_TAG_DESC_STRENGTH;
import static seedu.address.logic.commands.CommandTestUtil.DATE_DESC_LEGS;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADD_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DATE_DESC_LENGTH;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DATE_DESC_LETTERS;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PLAN_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PREFIX_DESC;
import static seedu.address.logic.commands.CommandTestUtil.PLAN_DESC_CHEST;
import static seedu.address.logic.commands.CommandTestUtil.PLAN_DESC_LEGS;
import static seedu.address.logic.commands.CommandTestUtil.TITLE_PREAMBLE_LEGS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HIIT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_STRENGTH;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INVALID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PLAN;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalWorkouts.LEG_DAY;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddPlanCommand;
import seedu.address.model.tag.Tag;
import seedu.address.model.workoutplan.WorkoutDate;
import seedu.address.model.workoutplan.WorkoutDescription;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.testutil.WorkoutBuilder;

public class AddPlanCommandParserTest {
    private AddPlanCommandParser parser = new AddPlanCommandParser();

    private String formatParserError(String errorMessage) {
        return Messages.formatParserError(errorMessage, AddPlanCommand.MESSAGE_USAGE);
    }

    @Test
    public void parse_allFieldsPresent_success() {
        WorkoutPlan expectedPlan = new WorkoutBuilder(LEG_DAY).withTags(VALID_TAG_STRENGTH).build();

        // valid command format
        assertParseSuccess(parser, TITLE_PREAMBLE_LEGS + PLAN_DESC_LEGS + DATE_DESC_LEGS + ADD_TAG_DESC_STRENGTH,
                new AddPlanCommand(expectedPlan));

        // multiple tags - all accepted
        WorkoutPlan expectedPlanMultipleTags = new WorkoutBuilder(LEG_DAY).withTags(VALID_TAG_STRENGTH, VALID_TAG_HIIT)
                .build();
        assertParseSuccess(parser,
                TITLE_PREAMBLE_LEGS + PLAN_DESC_LEGS + DATE_DESC_LEGS + ADD_TAG_DESC_STRENGTH + ADD_TAG_DESC_HIIT,
                new AddPlanCommand(expectedPlanMultipleTags));

        // no tags - accepted
        WorkoutPlan expectedPlanNoTags = new WorkoutBuilder(LEG_DAY).build();
        assertParseSuccess(parser, TITLE_PREAMBLE_LEGS + PLAN_DESC_LEGS + DATE_DESC_LEGS,
                new AddPlanCommand(expectedPlanNoTags));
    }

    @Test
    public void parse_repeatedValue_failure() {
        String validExpectePlanString = TITLE_PREAMBLE_LEGS + PLAN_DESC_LEGS + DATE_DESC_LEGS + ADD_TAG_DESC_STRENGTH;

        // repeated workout plans
        assertParseFailure(parser, validExpectePlanString + PLAN_DESC_CHEST,
                this.formatParserError(Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PLAN)));

    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        // missing workout description
        assertParseFailure(parser, TITLE_PREAMBLE_LEGS + DATE_DESC_LEGS,
                this.formatParserError(Messages.getErrorMessageForMissingPrefixes(PREFIX_PLAN)));
    }

    @Test
    public void parse_invalidField_failure() {
        // single invalid field
        assertParseFailure(parser,
                TITLE_PREAMBLE_LEGS + PLAN_DESC_LEGS + DATE_DESC_LEGS + ADD_TAG_DESC_STRENGTH + INVALID_PREFIX_DESC,
                this.formatParserError(Messages.getErrorMessageForInvalidPrefixes(PREFIX_INVALID)));
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid workout description
        assertParseFailure(parser, TITLE_PREAMBLE_LEGS + INVALID_PLAN_DESC + DATE_DESC_LEGS,
                this.formatParserError(WorkoutDescription.MESSAGE_CONSTRAINTS));

        // invalid workout date
        assertParseFailure(parser, TITLE_PREAMBLE_LEGS + PLAN_DESC_LEGS + INVALID_DATE_DESC_LENGTH,
                this.formatParserError(WorkoutDate.MESSAGE_CONSTRAINTS));
        assertParseFailure(parser, TITLE_PREAMBLE_LEGS + PLAN_DESC_LEGS + INVALID_DATE_DESC_LETTERS,
                this.formatParserError(WorkoutDate.MESSAGE_CONSTRAINTS));

        // invalid tag
        assertParseFailure(parser, TITLE_PREAMBLE_LEGS + PLAN_DESC_LEGS + DATE_DESC_LEGS + INVALID_ADD_TAG_DESC,
                this.formatParserError(Tag.MESSAGE_CONSTRAINTS));

        // both invalid values, first invalid value reported
        assertParseFailure(parser, TITLE_PREAMBLE_LEGS + INVALID_PLAN_DESC + INVALID_DATE_DESC_LENGTH,
                this.formatParserError(WorkoutDescription.MESSAGE_CONSTRAINTS));
    }
}
