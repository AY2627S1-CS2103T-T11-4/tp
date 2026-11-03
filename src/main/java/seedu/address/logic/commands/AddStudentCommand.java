package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.student.Student;
import seedu.address.model.student.exceptions.StudentListFullException;

/**
 * Adds a student to the student list.
 */
public class AddStudentCommand extends Command {

    public static final String COMMAND_WORD = "addStudent";

    public static final String MESSAGE_DESCRIPTION = "Adds a student to the student list.";
    public static final String MESSAGE_PARAMETERS = "NAME " + PREFIX_PHONE + " PHONE_NUMBER "
            + PREFIX_EMAIL + " EMAIL " + PREFIX_ADDRESS + " ADDRESS " + "[" + PREFIX_TAG + " TAG]...";
    public static final String MESSAGE_EXAMPLE = COMMAND_WORD + " James Ho " + PREFIX_PHONE + " 22224444 "
            + PREFIX_EMAIL + " jamesho@example.com " + PREFIX_ADDRESS + " 123, Clementi Rd, 1234665 "
            + PREFIX_TAG + " friend " + PREFIX_TAG + " colleague";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": " + MESSAGE_DESCRIPTION + " \n"
            + "Parameters: " + MESSAGE_PARAMETERS + "\n"
            + "Example: " + MESSAGE_EXAMPLE;

    public static final String MESSAGE_SUCCESS = "New student added: %1$s";
    public static final String MESSAGE_DUPLICATE_STUDENT = "This student already exists in the student list.";
    public static final String MESSAGE_STUDENT_LIST_FULL = "The student list is full. "
            + "Delete a student before adding another.";

    private final Student toAdd;

    /**
     * Creates an AddStudentCommand to add the specified {@code Student}
     */
    public AddStudentCommand(Student student) {
        requireNonNull(student);
        toAdd = student;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasStudent(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_STUDENT);
        }

        try {
            model.addStudent(toAdd);
        } catch (StudentListFullException e) {
            throw new CommandException(MESSAGE_STUDENT_LIST_FULL, e);
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddStudentCommand otherAddStudentCommand)) {
            return false;
        }

        return toAdd.equals(otherAddStudentCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
