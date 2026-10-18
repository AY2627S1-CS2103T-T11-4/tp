package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PLAN;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;

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
    /**
     * Parses the given {@code String} of arguments in the context of the AddPlanCommand
     * and returns an AddPlanCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public AddPlanCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args);

        if (!argMultimap.arePrefixesPresent(PREFIX_PLAN, PREFIX_DATE) || argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPlanCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoInvalidPrefixesFor(PREFIX_PLAN, PREFIX_DATE, PREFIX_TAG);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_PLAN, PREFIX_DATE);

        WorkoutPlan workout = this.parseValidated(argMultimap);

        return new AddPlanCommand(workout);
    }

    /**
     * Parses the validated {@code args} string to produce the WorkoutPlan object to be added,
     * wrapping any error messages thrown with the message usage.
     * @throws ParseException if any of the fields contain invalid contents
     */
    private WorkoutPlan parseValidated(ArgumentMultimap argMultimap) throws ParseException {
        WorkoutTitle title;
        WorkoutDescription desc;
        WorkoutDate date;
        Set<WorkoutPlanTag> tagList;

        try {
            title = ParserUtil.parseTitle(argMultimap.getPreamble());
            desc = ParserUtil.parseDescription(argMultimap.getValue(PREFIX_PLAN).get());
            date = ParserUtil.parseDate(argMultimap.getValue(PREFIX_DATE).get());
            tagList = ParserUtil.parseWorkoutPlanTags(argMultimap.getAllValues(PREFIX_TAG));
        } catch (ParseException pe) {
            throw new ParseException(Messages.formatParserError(pe.getMessage(), AddPlanCommand.MESSAGE_USAGE), pe);
        }
        return new WorkoutPlan(title, desc, date, tagList);
    }
}
