package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INVALID;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddExerciseCommand;
import seedu.address.model.exercise.Exercise;
import seedu.address.model.exercise.ExerciseDescription;
import seedu.address.model.exercise.ExerciseName;

public class AddExerciseCommandParserTest {
    private final AddExerciseCommandParser parser = new AddExerciseCommandParser();

    private String formatParserError(String message) {
        return Messages.formatParserError(message, AddExerciseCommand.MESSAGE_USAGE);
    }

    // Each valid input at least once: valid name and description parse successfully.
    @Test
    public void parse_allFieldsPresent_success() {
        Exercise exercise = new Exercise(new ExerciseName("Squats"),
                new ExerciseDescription("Barbell back squat"));
        assertParseSuccess(parser, " Squats --description Barbell back squat", new AddExerciseCommand(exercise));
    }

    // EP: repeated non-repeatable description field (invalid).
    @Test
    public void parse_repeatedValue_failure() {
        assertParseFailure(parser, " Squats --description First --description Second",
                formatParserError(Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DESCRIPTION)));
    }

    // One invalid input at a time: only the compulsory description is missing.
    @Test
    public void parse_compulsoryFieldMissing_failure() {
        assertParseFailure(parser, " Squats",
                formatParserError(Messages.getErrorMessageForMissingPrefixes(PREFIX_DESCRIPTION)));
    }

    // One invalid input at a time: only the unknown prefix is invalid.
    @Test
    public void parse_invalidField_failure() {
        assertParseFailure(parser, " Squats --description Back squat --invalid value",
                formatParserError(Messages.getErrorMessageForInvalidPrefixes(PREFIX_INVALID)));
    }

    @Test
    public void parse_invalidValue_failure() {
        // One invalid input at a time: only the exercise name is blank.
        assertParseFailure(parser, "  --description Back squat",
                formatParserError(Messages.MESSAGE_MISSING_PREAMBLE));
        // One invalid input at a time: only the exercise description is blank.
        assertParseFailure(parser, " Squats --description  ",
                formatParserError(ExerciseDescription.MESSAGE_CONSTRAINTS));
    }
}
