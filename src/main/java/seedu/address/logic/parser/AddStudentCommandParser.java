package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddStudentCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.student.Address;
import seedu.address.model.student.Email;
import seedu.address.model.student.Name;
import seedu.address.model.student.Phone;
import seedu.address.model.student.Student;
import seedu.address.model.tag.StudentTag;

/**
 * Parses input arguments and creates a new AddStudentCommand object
 */
public class AddStudentCommandParser implements Parser<AddStudentCommand> {

    private static final Set<Prefix> VALID_PREFIXES = Set.of(PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG);
    private static final Set<Prefix> REQUIRED_PREFIXES = Set.of(PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS);
    private static final Set<Prefix> NO_REPEAT_PREFIXES = Set.of(PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS);

    private static final Logger logger = LogsCenter.getLogger(AddStudentCommandParser.class);

    /**
     * Parses the given {@code String} of arguments in the context of the AddStudentCommand
     * and returns an AddStudentCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public AddStudentCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentValidator argValidator = new ArgumentValidator(VALID_PREFIXES, NO_REPEAT_PREFIXES, REQUIRED_PREFIXES);
        Student student;

        ParsingStage stage = ParsingStage.TOKENIZATION;
        try {
            ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args);

            stage = ParsingStage.ARGUMENT_VALIDATION;
            argValidator.verifyArgs(argMultimap);

            stage = ParsingStage.FIELD_PARSING;
            student = this.parseValidFields(argMultimap);

        } catch (ParseException pe) {
            logger.finer("Parsing of addStudent command failed at " + stage.getMessage());
            throw pe.withMessage(Messages.formatParserError(pe.getMessage(), AddStudentCommand.MESSAGE_USAGE));
        }

        return new AddStudentCommand(student);
    }

    /**
     * Parses the validated {@code args} string to produce the Student object to be added,
     * wrapping any error messages thrown with the message usage.
     * @throws ParseException if any of the fields contain invalid contents
     */
    private Student parseValidFields(ArgumentMultimap argMultimap) throws ParseException {
        Name name = ParserUtil.parseName(argMultimap.getPreamble());
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
        Address address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get());
        Set<StudentTag> tagList = ParserUtil.parseStudentTags(argMultimap.getAllValues(PREFIX_TAG));

        return new Student(name, phone, email, address, tagList);
    }
}
