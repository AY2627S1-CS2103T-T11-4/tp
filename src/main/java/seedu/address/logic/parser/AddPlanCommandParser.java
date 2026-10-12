package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PLAN;

import java.util.stream.Stream;

import seedu.address.logic.commands.AddPlanCommand;
import seedu.address.logic.parser.exceptions.ParseException;
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
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_PLAN, PREFIX_DATE);

        if (!arePrefixesPresent(argMultimap, PREFIX_PLAN, PREFIX_DATE) || argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPlanCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_PLAN, PREFIX_DATE);
        WorkoutTitle title = ParserUtil.parseTitle(argMultimap.getPreamble());
        WorkoutDescription desc = ParserUtil.parseDescription(argMultimap.getValue(PREFIX_PLAN).get());
        WorkoutDate date = ParserUtil.parseDate(argMultimap.getValue(PREFIX_DATE).get());

        WorkoutPlan workout = new WorkoutPlan(title, desc, date);

        return new AddPlanCommand(workout);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
