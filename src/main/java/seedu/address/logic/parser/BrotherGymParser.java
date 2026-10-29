package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNEXPECTED_PARAMETERS;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddExerciseCommand;
import seedu.address.logic.commands.AddPlanCommand;
import seedu.address.logic.commands.AddStudentCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteExerciseCommand;
import seedu.address.logic.commands.DeletePlanCommand;
import seedu.address.logic.commands.DeleteStudentCommand;
import seedu.address.logic.commands.EditExerciseCommand;
import seedu.address.logic.commands.EditPlanCommand;
import seedu.address.logic.commands.EditStudentCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HomeCommand;
import seedu.address.logic.commands.MarkPlanCommand;
import seedu.address.logic.commands.UnmarkPlanCommand;
import seedu.address.logic.commands.ViewPlanCommand;
import seedu.address.logic.commands.ViewStudentCommand;
import seedu.address.logic.parser.exceptions.FieldValueInvalidException;
import seedu.address.logic.parser.exceptions.InputFormatInvalidException;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class BrotherGymParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");
    private static final Logger logger = LogsCenter.getLogger(BrotherGymParser.class);

    /**
     * Parser objects
     */
    private static final AddStudentCommandParser ADD_STUDENT_PARSER = new AddStudentCommandParser();
    private static final AddPlanCommandParser ADD_PLAN_PARSER = new AddPlanCommandParser();
    private static final AddExerciseCommandParser ADD_EXERCISE_PARSER = new AddExerciseCommandParser();
    private static final EditStudentCommandParser EDIT_STUDENT_PARSER = new EditStudentCommandParser();
    private static final EditPlanCommandParser EDIT_PLAN_PARSER = new EditPlanCommandParser();
    private static final EditExerciseCommandParser EDIT_EXERCISE_PARSER = new EditExerciseCommandParser();
    private static final FindCommandParser FIND_COMMAND_PARSER = new FindCommandParser();
    private static final SingleIndexCommandParser<MarkPlanCommand> MARK_PLAN_PARSER =
            new SingleIndexCommandParser<>(
                    MarkPlanCommand::new, MarkPlanCommand.MESSAGE_USAGE, MarkPlanCommand.COMMAND_WORD);
    private static final SingleIndexCommandParser<UnmarkPlanCommand> UNMARK_PLAN_PARSER =
            new SingleIndexCommandParser<>(
                    UnmarkPlanCommand::new, UnmarkPlanCommand.MESSAGE_USAGE, UnmarkPlanCommand.COMMAND_WORD);
    private static final SingleIndexCommandParser<ViewStudentCommand> VIEW_STUDENT_PARSER =
            new SingleIndexCommandParser<>(
                    ViewStudentCommand::new, ViewStudentCommand.MESSAGE_USAGE, ViewStudentCommand.COMMAND_WORD);
    private static final SingleIndexCommandParser<ViewPlanCommand> VIEW_PLAN_PARSER =
            new SingleIndexCommandParser<>(
                    ViewPlanCommand::new, ViewPlanCommand.MESSAGE_USAGE, ViewPlanCommand.COMMAND_WORD);
    private static final SingleIndexCommandParser<DeleteStudentCommand> DELETE_STUDENT_PARSER =
            new SingleIndexCommandParser<>(
                    DeleteStudentCommand::new, DeleteStudentCommand.MESSAGE_USAGE, DeleteStudentCommand.COMMAND_WORD);
    private static final SingleIndexCommandParser<DeletePlanCommand> DELETE_PLAN_PARSER =
            new SingleIndexCommandParser<>(
                    DeletePlanCommand::new, DeletePlanCommand.MESSAGE_USAGE, DeletePlanCommand.COMMAND_WORD);
    private static final SingleIndexCommandParser<DeleteExerciseCommand> DELETE_EXERCISE_PARSER =
            new SingleIndexCommandParser<>(DeleteExerciseCommand::new,
                    DeleteExerciseCommand.MESSAGE_USAGE, DeleteExerciseCommand.COMMAND_WORD);

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform to the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        requireNonNull(userInput);

        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new InputFormatInvalidException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");

        // Note to developers: Change LOG_LEVEL in LogsCenter to enable lower level (i.e., FINE, FINER and lower)
        // log messages such as the one below.
        // Lower level log messages are used sparingly to minimize noise in the code.
        logger.fine("Parsing command with command word: " + commandWord + "; Arguments: " + arguments);

        Command parsedCommand = switch (commandWord) {
            case AddStudentCommand.COMMAND_WORD -> ADD_STUDENT_PARSER.parse(arguments);
            case EditStudentCommand.COMMAND_WORD -> EDIT_STUDENT_PARSER.parse(arguments);
            case DeleteStudentCommand.COMMAND_WORD -> DELETE_STUDENT_PARSER.parse(arguments);
            case ViewStudentCommand.COMMAND_WORD -> VIEW_STUDENT_PARSER.parse(arguments);
            case AddPlanCommand.COMMAND_WORD -> ADD_PLAN_PARSER.parse(arguments);
            case AddExerciseCommand.COMMAND_WORD -> ADD_EXERCISE_PARSER.parse(arguments);
            case EditPlanCommand.COMMAND_WORD -> EDIT_PLAN_PARSER.parse(arguments);
            case MarkPlanCommand.COMMAND_WORD -> MARK_PLAN_PARSER.parse(arguments);
            case UnmarkPlanCommand.COMMAND_WORD -> UNMARK_PLAN_PARSER.parse(arguments);
            case DeletePlanCommand.COMMAND_WORD -> DELETE_PLAN_PARSER.parse(arguments);
            case DeleteExerciseCommand.COMMAND_WORD -> DELETE_EXERCISE_PARSER.parse(arguments);
            case EditExerciseCommand.COMMAND_WORD -> EDIT_EXERCISE_PARSER.parse(arguments);
            case ViewPlanCommand.COMMAND_WORD -> VIEW_PLAN_PARSER.parse(arguments);
            case ClearCommand.COMMAND_WORD ->
                parseCommandWithoutArguments(arguments, ClearCommand.MESSAGE_USAGE, new ClearCommand());
            case FindCommand.COMMAND_WORD -> FIND_COMMAND_PARSER.parse(arguments);
            case HomeCommand.COMMAND_WORD ->
                parseCommandWithoutArguments(arguments, HomeCommand.MESSAGE_USAGE, new HomeCommand());
            case ExitCommand.COMMAND_WORD ->
                parseCommandWithoutArguments(arguments, ExitCommand.MESSAGE_USAGE, new ExitCommand());
            case HelpCommand.COMMAND_WORD ->
                parseCommandWithoutArguments(arguments, HelpCommand.MESSAGE_USAGE, new HelpCommand());
            default -> {
                logger.finer("Unknown command word for input: " + userInput);
                throw new InputFormatInvalidException(MESSAGE_UNKNOWN_COMMAND);
            }
        };

        logger.info("Parsed user input to generate: " + parsedCommand);

        return parsedCommand;
    }

    /**
     * Rejects arguments for a command that does not accept parameters.
     *
     * @throws FieldValueInvalidException If non-whitespace arguments are present.
     */
    private Command parseCommandWithoutArguments(String arguments, String usage, Command command)
            throws FieldValueInvalidException {
        if (!arguments.isBlank()) {
            throw new FieldValueInvalidException(Messages.formatParserError(MESSAGE_UNEXPECTED_PARAMETERS, usage));
        }
        return command;
    }

}
