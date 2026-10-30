package seedu.address.logic.parser;

import static seedu.address.logic.commands.CommandTestUtil.DATE_DESC_LEGS;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DATE_DESC_LENGTH;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DATE_DESC_LETTERS;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PLAN_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PREFIX_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.PLAN_DESC_CHEST;
import static seedu.address.logic.commands.CommandTestUtil.PLAN_DESC_LEGS;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HIIT;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_STRENGTH;
import static seedu.address.logic.commands.CommandTestUtil.TITLE_PREAMBLE_LEGS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HIIT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_STRENGTH;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLEAR_TAGS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INVALID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PLAN;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalWorkouts.LEG_DAY;

import java.time.LocalDate;

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

        // Each valid input at least once: all compulsory fields and one tag parse successfully.
        assertParseSuccess(parser, TITLE_PREAMBLE_LEGS + PLAN_DESC_LEGS + DATE_DESC_LEGS + TAG_DESC_STRENGTH,
                new AddPlanCommand(expectedPlan));

        // EP: count >= 2 (valid).
        WorkoutPlan expectedPlanMultipleTags = new WorkoutBuilder(LEG_DAY).withTags(VALID_TAG_STRENGTH, VALID_TAG_HIIT)
                .build();
        assertParseSuccess(parser,
                TITLE_PREAMBLE_LEGS + PLAN_DESC_LEGS + DATE_DESC_LEGS + TAG_DESC_STRENGTH + TAG_DESC_HIIT,
                new AddPlanCommand(expectedPlanMultipleTags));

        // BV: 0 <= count <= MAX_TAG_COUNT (valid).
        WorkoutPlan expectedPlanNoTags = new WorkoutBuilder(LEG_DAY).build();
        assertParseSuccess(parser, TITLE_PREAMBLE_LEGS + PLAN_DESC_LEGS + DATE_DESC_LEGS,
                new AddPlanCommand(expectedPlanNoTags));
    }

    @Test
    public void parse_repeatedValue_failure() {
        String validExpectePlanString = TITLE_PREAMBLE_LEGS + PLAN_DESC_LEGS + DATE_DESC_LEGS + TAG_DESC_STRENGTH;

        // EP: repeated non-repeatable workout-plan field (invalid).
        assertParseFailure(parser, validExpectePlanString + PLAN_DESC_CHEST,
                this.formatParserError(Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PLAN)));

    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        // One invalid input at a time: only the compulsory description is missing.
        assertParseFailure(parser, TITLE_PREAMBLE_LEGS + DATE_DESC_LEGS,
                this.formatParserError(Messages.getErrorMessageForMissingPrefixes(PREFIX_PLAN)));
    }

    @Test
    public void parse_invalidField_failure() {
        // One invalid input at a time: only the unknown prefix is invalid.
        // One invalid input at a time: only --clearTags is invalid for add.
        assertParseFailure(parser,
                TITLE_PREAMBLE_LEGS + PLAN_DESC_LEGS + DATE_DESC_LEGS + TAG_DESC_STRENGTH + INVALID_PREFIX_DESC,
                this.formatParserError(Messages.getErrorMessageForInvalidPrefixes(PREFIX_INVALID)));

        assertParseFailure(parser,
                TITLE_PREAMBLE_LEGS + PLAN_DESC_LEGS + DATE_DESC_LEGS + " " + PREFIX_CLEAR_TAGS,
                this.formatParserError(Messages.getErrorMessageForInvalidPrefixes(PREFIX_CLEAR_TAGS)));

    }

    @Test
    public void parse_invalidValue_failure() {
        // One invalid input at a time: only the workout description is invalid.
        assertParseFailure(parser, TITLE_PREAMBLE_LEGS + INVALID_PLAN_DESC + DATE_DESC_LEGS,
                this.formatParserError(WorkoutDescription.MESSAGE_CONSTRAINTS));

        // One invalid input at a time: only the date length is invalid.
        assertParseFailure(parser, TITLE_PREAMBLE_LEGS + PLAN_DESC_LEGS + INVALID_DATE_DESC_LENGTH,
                this.formatParserError(WorkoutDate.MESSAGE_CONSTRAINTS));
        // One invalid input at a time: only the date characters are invalid.
        assertParseFailure(parser, TITLE_PREAMBLE_LEGS + PLAN_DESC_LEGS + INVALID_DATE_DESC_LETTERS,
                this.formatParserError(WorkoutDate.MESSAGE_CONSTRAINTS));

        // One invalid input at a time: only the tag is invalid.
        assertParseFailure(parser, TITLE_PREAMBLE_LEGS + PLAN_DESC_LEGS + DATE_DESC_LEGS + INVALID_TAG_DESC,
                this.formatParserError(Tag.MESSAGE_CONSTRAINTS));

        // Invalid inputs combined after separate tests: description and date are invalid.
        assertParseFailure(parser, TITLE_PREAMBLE_LEGS + INVALID_PLAN_DESC + INVALID_DATE_DESC_LENGTH,
                this.formatParserError(WorkoutDescription.MESSAGE_CONSTRAINTS));
    }

    @Test
    public void parse_invalidCalendarDate_reportsGenericDateError() {
        String input = TITLE_PREAMBLE_LEGS + PLAN_DESC_LEGS + " --date ";
        // EP: month exceeds 12 (invalid).
        assertParseFailure(parser, input + "01132026",
                this.formatParserError(WorkoutDate.MESSAGE_CONSTRAINTS));
        // EP: day exceeds 31 (invalid).
        assertParseFailure(parser, input + "32122026",
                this.formatParserError(WorkoutDate.MESSAGE_CONSTRAINTS));
        // EP: day is invalid for February (invalid).
        assertParseFailure(parser, input + "31022026",
                this.formatParserError(WorkoutDate.MESSAGE_CONSTRAINTS));
        // BV: 29 February in a non-leap year (invalid).
        assertParseFailure(parser, input + "29022026",
                this.formatParserError(WorkoutDate.MESSAGE_CONSTRAINTS));
    }

    // BV: 29 February in a leap year (valid).
    @Test
    public void parse_validLeapDay_success() {
        WorkoutPlan expectedPlan = new WorkoutBuilder(LEG_DAY).withDate(LocalDate.of(2024, 2, 29)).build();
        assertParseSuccess(parser, TITLE_PREAMBLE_LEGS + PLAN_DESC_LEGS + " --date 29022024",
                new AddPlanCommand(expectedPlan));
    }

    // BV: year divisible by 400 (valid).
    @Test
    public void parse_leapDayInYearDivisibleBy400_success() {
        WorkoutPlan expectedPlan = new WorkoutBuilder(LEG_DAY).withDate(LocalDate.of(2000, 2, 29)).build();
        assertParseSuccess(parser, TITLE_PREAMBLE_LEGS + PLAN_DESC_LEGS + " --date 29022000",
                new AddPlanCommand(expectedPlan));
    }

    // BV: century year not divisible by 400 (invalid).
    @Test
    public void parse_leapDayInCenturyNotDivisibleBy400_failure() {
        assertParseFailure(parser, TITLE_PREAMBLE_LEGS + PLAN_DESC_LEGS + " --date 29021900",
                this.formatParserError(WorkoutDate.MESSAGE_CONSTRAINTS));
    }
}
