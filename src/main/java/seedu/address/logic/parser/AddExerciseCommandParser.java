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

/**
 * Parses an addExercise command.
 */
public class AddExerciseCommandParser implements Parser<AddExerciseCommand> {
    private static final Set<Prefix> PREFIXES = Set.of(PREFIX_DESCRIPTION);
    private static final Logger logger = LogsCenter.getLogger(AddExerciseCommandParser.class);

    @Override
    public AddExerciseCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentValidator validator = new ArgumentValidator(PREFIXES, PREFIXES, PREFIXES);
        ParsingStage stage = ParsingStage.TOKENIZATION;
        try {
            ArgumentMultimap arguments = ArgumentTokenizer.tokenize(args);
            stage = ParsingStage.ARGUMENT_VALIDATION;
            validator.verifyArgs(arguments);
            stage = ParsingStage.FIELD_PARSING;
            Exercise exercise = new Exercise(ParserUtil.parseExerciseName(arguments.getPreamble()),
                    ParserUtil.parseExerciseDescription(arguments.getValue(PREFIX_DESCRIPTION).orElseThrow()));
            return new AddExerciseCommand(exercise);
        } catch (ParseException e) {
            logger.finer("Parsing of addExercise command failed at " + stage.getMessage());
            throw e.withMessage(Messages.formatParserError(e.getMessage(), AddExerciseCommand.MESSAGE_USAGE));
        }
    }
}
