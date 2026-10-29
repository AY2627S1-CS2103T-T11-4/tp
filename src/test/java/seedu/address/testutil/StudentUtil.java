package seedu.address.testutil;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADD_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLEAR_TAGS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DELETE_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import seedu.address.logic.commands.AddStudentCommand;
import seedu.address.logic.commands.EditStudentCommand;
import seedu.address.model.student.Student;

/**
 * A utility class for Student.
 */
public class StudentUtil {

    /**
     * Returns an addStudent command string for adding the {@code student}.
     */
    public static String getAddStudentCommand(Student student) {
        return AddStudentCommand.COMMAND_WORD + " " + getStudentDetails(student);
    }

    /**
     * Returns the part of command string for the given {@code student}'s details.
     */
    public static String getStudentDetails(Student student) {
        StringBuilder sb = new StringBuilder();
        sb.append(student.getName().fullName + " ");
        sb.append(PREFIX_PHONE + " " + student.getPhone().value + " ");
        sb.append(PREFIX_EMAIL + " " + student.getEmail().value + " ");
        sb.append(PREFIX_ADDRESS + " " + student.getAddress().value + " ");
        student.getTags().stream().forEach(
            s -> sb.append(PREFIX_ADD_TAG).append(" ").append(s.tagName).append(" ")
        );
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditStudentDescriptor}'s details.
     */
    public static String getEditStudentDescriptorDetails(EditStudentCommand.EditStudentDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getName().ifPresent(name -> sb.append(PREFIX_NAME).append(" ").append(name.fullName).append(" "));
        descriptor.getPhone().ifPresent(phone -> sb.append(PREFIX_PHONE).append(" ").append(phone.value).append(" "));
        descriptor.getEmail().ifPresent(email -> sb.append(PREFIX_EMAIL).append(" ").append(email.value).append(" "));
        descriptor.getAddress().ifPresent(addr -> sb.append(PREFIX_ADDRESS).append(" ").append(addr.value).append(" "));
        if (descriptor.isClearTags()) {
            sb.append(PREFIX_CLEAR_TAGS).append(" ");
        }
        descriptor.getTagsToDelete().ifPresent(tags ->
                tags.forEach(s -> sb.append(PREFIX_DELETE_TAG).append(" ").append(s.tagName).append(" ")));
        descriptor.getTagsToAdd().ifPresent(tags ->
                tags.forEach(s -> sb.append(PREFIX_ADD_TAG).append(" ").append(s.tagName).append(" ")));
        return sb.toString();
    }
}
