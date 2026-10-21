package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PLAN;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddPlanCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.WorkoutPlanTag;
import seedu.address.model.workoutplan.WorkoutDate;
import seedu.address.model.workoutplan.WorkoutDescription;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.WorkoutTitle;

/**
 * Parses input arguments and creates a new AddPlanCommand object
 */
public class AddPlanCommandParser implements Parser<AddPlanCommand> {

    private static final Set<Prefix> VALID_PREFIXES = Set.of(PREFIX_PLAN, PREFIX_DATE, PREFIX_TAG);
    private static final Set<Prefix> REQUIRED_PREFIXES = Set.of(PREFIX_PLAN, PREFIX_DATE);
    private static final Set<Prefix> NO_REPEAT_PREFIXES = Set.of(PREFIX_PLAN, PREFIX_DATE);

    private static final Logger logger = LogsCenter.getLogger(AddPlanCommandParser.class);

    /**
     * Parses the given {@code String} of arguments in the context of the AddPlanCommand
     * and returns an AddPlanCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public AddPlanCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentValidator argValidator = new ArgumentValidator(VALID_PREFIXES, NO_REPEAT_PREFIXES, REQUIRED_PREFIXES);
        WorkoutPlan workout;

        ParsingStage stage = ParsingStage.TOKENIZATION;
        try {
            ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args);

            stage = ParsingStage.ARGUMENT_VALIDATION;
            argValidator.verifyArgs(argMultimap);

            stage = ParsingStage.FIELD_PARSING;
            workout = this.parseValidFields(argMultimap);

        } catch (ParseException pe) {
            logger.finer("Parsing of addPlan command failed at " + stage.getMessage());
            throw new ParseException(Messages.formatParserError(pe.getMessage(), AddPlanCommand.MESSAGE_USAGE), pe);
        }

        return new AddPlanCommand(workout);
    }

    /**
     * Parses the validated {@code args} string to produce the WorkoutPlan object to be added,
     * wrapping any error messages thrown with the message usage.
     * @throws ParseException if any of the fields contain invalid contents
     */
    private WorkoutPlan parseValidFields(ArgumentMultimap argMultimap) throws ParseException {
        WorkoutTitle title = ParserUtil.parseTitle(argMultimap.getPreamble());
        WorkoutDescription desc = ParserUtil.parseDescription(argMultimap.getValue(PREFIX_PLAN).get());
        WorkoutDate date = ParserUtil.parseDate(argMultimap.getValue(PREFIX_DATE).get());
        Set<WorkoutPlanTag> tagList = ParserUtil.parseWorkoutPlanTags(argMultimap.getAllValues(PREFIX_TAG));

        return new WorkoutPlan(title, desc, date, tagList);
    }
}
