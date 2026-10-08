package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.DATE_DESC_CHEST;
import static seedu.address.logic.commands.CommandTestUtil.DATE_DESC_LEGS;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DATE_DESC_LENGTH;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DATE_DESC_LETTERS;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PLAN_DESC;
import static seedu.address.logic.commands.CommandTestUtil.PLAN_DESC_CHEST;
import static seedu.address.logic.commands.CommandTestUtil.PLAN_DESC_LEGS;
import static seedu.address.logic.commands.CommandTestUtil.TITLE_PREAMBLE_LEGS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PLAN;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalWorkouts.LEG_DAY;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddPlanCommand;
import seedu.address.model.workoutplan.WorkoutDate;
import seedu.address.model.workoutplan.WorkoutDescription;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.testutil.WorkoutBuilder;

public class AddPlanCommandParserTest {
    private AddPlanCommandParser parser = new AddPlanCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        WorkoutPlan expectedPlan = new WorkoutBuilder(LEG_DAY).build();

        assertParseSuccess(parser, TITLE_PREAMBLE_LEGS + PLAN_DESC_LEGS + DATE_DESC_LEGS,
                new AddPlanCommand(expectedPlan));
    }

    @Test
    public void parse_repeatedValue_failure() {
        String validExpectePlanString = TITLE_PREAMBLE_LEGS + PLAN_DESC_LEGS + DATE_DESC_LEGS;

        // repeated workout plans
        assertParseFailure(parser, validExpectePlanString + PLAN_DESC_CHEST,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PLAN));

        // repeated workout dates
        assertParseFailure(parser, validExpectePlanString + DATE_DESC_CHEST,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DATE));

        // multiple repeated fields
        assertParseFailure(parser, validExpectePlanString + PLAN_DESC_CHEST + DATE_DESC_CHEST,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PLAN, PREFIX_DATE));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPlanCommand.MESSAGE_USAGE);

        // missing workout title
        assertParseFailure(parser, PLAN_DESC_LEGS + DATE_DESC_LEGS, expectedMessage);

        // missing workout description
        assertParseFailure(parser, TITLE_PREAMBLE_LEGS + DATE_DESC_LEGS, expectedMessage);

        // missing workout date
        assertParseFailure(parser, TITLE_PREAMBLE_LEGS + PLAN_DESC_LEGS, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid workout description
        assertParseFailure(parser, TITLE_PREAMBLE_LEGS + INVALID_PLAN_DESC + DATE_DESC_LEGS,
                WorkoutDescription.MESSAGE_CONSTRAINTS);

        // invalid workout date
        assertParseFailure(parser, TITLE_PREAMBLE_LEGS + PLAN_DESC_LEGS + INVALID_DATE_DESC_LENGTH,
                WorkoutDate.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, TITLE_PREAMBLE_LEGS + PLAN_DESC_LEGS + INVALID_DATE_DESC_LETTERS,
                WorkoutDate.MESSAGE_CONSTRAINTS);

        // both invalid values, first invalid value reported
        assertParseFailure(parser, TITLE_PREAMBLE_LEGS + INVALID_PLAN_DESC + INVALID_DATE_DESC_LENGTH,
                WorkoutDescription.MESSAGE_CONSTRAINTS);
    }
}
