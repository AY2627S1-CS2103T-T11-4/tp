package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;

import java.util.Set;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddExerciseCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.exercise.Exercise;
import seedu.address.model.exercise.ExerciseDescription;
import seedu.address.model.exercise.ExerciseName;

/**
 * Parses an addExercise command.
 */
public class AddExerciseCommandParser implements Parser<AddExerciseCommand> {
    private static final Set<Prefix> VALID_PREFIXES = Set.of(PREFIX_DESCRIPTION);
    private static final Set<Prefix> NO_REPEAT_PREFIXES = Set.of(PREFIX_DESCRIPTION);
    private static final Set<Prefix> REQUIRED_PREFIXES = Set.of(PREFIX_DESCRIPTION);
    private static final Logger logger = LogsCenter.getLogger(AddExerciseCommandParser.class);

    @Override
    public AddExerciseCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentValidator argValidator = new ArgumentValidator(VALID_PREFIXES, NO_REPEAT_PREFIXES, REQUIRED_PREFIXES);
        Exercise exercise;

        ParsingStage stage = ParsingStage.TOKENIZATION;
        try {
            ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args);

            stage = ParsingStage.ARGUMENT_VALIDATION;
            argValidator.verifyArgs(argMultimap);

            stage = ParsingStage.FIELD_PARSING;
            exercise = parseValidFields(argMultimap);
        } catch (ParseException pe) {
            logger.finer("Parsing of addExercise command failed at " + stage.getMessage());
            throw pe.withMessage(Messages.formatParserError(pe.getMessage(), AddExerciseCommand.MESSAGE_USAGE));
        }

        return new AddExerciseCommand(exercise);
    }

    private Exercise parseValidFields(ArgumentMultimap argMultimap) throws ParseException {
        ExerciseName name = ParserUtil.parseExerciseName(argMultimap.getPreamble());
        ExerciseDescription desc = ParserUtil.parseExerciseDescription(argMultimap.getValue(PREFIX_DESCRIPTION).get());
        return new Exercise(name, desc);
    }
}
