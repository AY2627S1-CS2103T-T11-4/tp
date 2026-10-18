package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;

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

    /**
     * Parses the given {@code String} of arguments in the context of the AddStudentCommand
     * and returns an AddStudentCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public AddStudentCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args);

        if (!argMultimap.arePrefixesPresent(PREFIX_ADDRESS, PREFIX_PHONE, PREFIX_EMAIL)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddStudentCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoInvalidPrefixesFor(PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS);

        Student student = this.parseValidated(argMultimap);
        return new AddStudentCommand(student);
    }

    /**
     * Parses the validated {@code args} string to produce the Student object to be added,
     * wrapping any error messages thrown with the message usage.
     * @throws ParseException if any of the fields contain invalid contents
     */
    private Student parseValidated(ArgumentMultimap argMultimap) throws ParseException {
        Name name;
        Phone phone;
        Email email;
        Address address;
        Set<StudentTag> tagList;

        try {
            name = ParserUtil.parseName(argMultimap.getPreamble());
            phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
            email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
            address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get());
            tagList = ParserUtil.parseStudentTags(argMultimap.getAllValues(PREFIX_TAG));
        } catch (ParseException pe) {
            throw new ParseException(Messages.formatParserError(pe.getMessage(), AddStudentCommand.MESSAGE_USAGE), pe);
        }
        return new Student(name, phone, email, address, tagList);
    }
}
