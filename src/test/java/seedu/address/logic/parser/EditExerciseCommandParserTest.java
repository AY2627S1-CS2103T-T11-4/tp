package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditExerciseCommand;
import seedu.address.logic.commands.EditExerciseCommand.EditExerciseDescriptor;
import seedu.address.model.exercise.ExerciseDescription;
import seedu.address.model.exercise.ExerciseName;

public class EditExerciseCommandParserTest {

    private final EditExerciseCommandParser parser = new EditExerciseCommandParser();

    @Test
    public void parse_partialAndCompleteEdits_success() {
        EditExerciseDescriptor nameOnly = descriptor(new ExerciseName("Front Squat"), null);
        // Each valid input at least once: a name-only edit parses successfully.
        assertParseSuccess(parser, "1 " + PREFIX_NAME + " Front Squat",
                new EditExerciseCommand(INDEX_FIRST, nameOnly));

        EditExerciseDescriptor descriptionOnly = descriptor(null,
                new ExerciseDescription("Four controlled sets"));
        // Each valid input at least once: a description-only edit parses successfully.
        assertParseSuccess(parser, "1 " + PREFIX_DESCRIPTION + " Four controlled sets",
                new EditExerciseCommand(INDEX_FIRST, descriptionOnly));

        EditExerciseDescriptor complete = descriptor(new ExerciseName("Front Squat"),
                new ExerciseDescription("Four controlled sets"));
        // EP: name and description edited together (valid).
        assertParseSuccess(parser, "1 " + PREFIX_NAME + " Front Squat "
                + PREFIX_DESCRIPTION + " Four controlled sets",
                new EditExerciseCommand(INDEX_FIRST, complete));
    }

    @Test
    public void parse_missingOrInvalidIndex_failure() {
        // One invalid input at a time: only the edit fields are missing.
        assertParseFailure(parser, "1", EditExerciseCommand.MESSAGE_NOT_EDITED);
        // BV: index < 1 (invalid).
        assertParseFailure(parser, "0 " + PREFIX_NAME + " Squats",
                formatted(MESSAGE_INVALID_INDEX));
        // EP: non-numeric index (invalid).
        assertParseFailure(parser, "abc " + PREFIX_NAME + " Squats",
                formatted(MESSAGE_INVALID_INDEX));
    }

    @Test
    public void parse_invalidFields_failure() {
        // One invalid input at a time: only the exercise name is blank.
        assertParseFailure(parser, "1 " + PREFIX_NAME,
                formatted(ExerciseName.MESSAGE_CONSTRAINTS));
        // One invalid input at a time: only the exercise description is blank.
        assertParseFailure(parser, "1 " + PREFIX_DESCRIPTION,
                formatted(ExerciseDescription.MESSAGE_CONSTRAINTS));
        // One invalid input at a time: only the --reps prefix is unknown.
        assertParseFailure(parser, "1 --reps 10",
                formatted(Messages.getErrorMessageForInvalidPrefixes(new Prefix("--reps"))));
    }

    @Test
    public void parse_repeatedFields_failure() {
        // EP: repeated non-repeatable name field (invalid).
        assertParseFailure(parser, "1 " + PREFIX_NAME + " Squats " + PREFIX_NAME + " Lunges",
                formatted(Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME)));
        // EP: repeated non-repeatable description field (invalid).
        assertParseFailure(parser, "1 " + PREFIX_DESCRIPTION + " First "
                        + PREFIX_DESCRIPTION + " Second",
                formatted(Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DESCRIPTION)));
    }

    private static EditExerciseDescriptor descriptor(ExerciseName name, ExerciseDescription description) {
        EditExerciseDescriptor descriptor = new EditExerciseDescriptor();
        if (name != null) {
            descriptor.setName(name);
        }
        if (description != null) {
            descriptor.setDescription(description);
        }
        return descriptor;
    }

    private static String formatted(String message) {
        return Messages.formatParserError(message, EditExerciseCommand.MESSAGE_USAGE);
    }
}
