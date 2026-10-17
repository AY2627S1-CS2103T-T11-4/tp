package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.student.Student;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command.";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format!\n%1$s";
    public static final String MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX = "The student index provided is invalid.";
    public static final String MESSAGE_STUDENTS_LISTED_OVERVIEW = "%1$d student(s) listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): %1$s";
    public static final String MESSAGE_INVALID_PREFIX = "Invalid prefix(es) included: %1$s";
    public static final String MESSAGE_EMPTY_PREFIX = "Empty prefix detected."
            + " Ensure all prefixes are in the form '--prefix' and isolated by spaces";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        return String.format(MESSAGE_DUPLICATE_FIELDS, getErrorMessageForPrefixes(duplicatePrefixes));
    }

    /**
     * Returns an error message indicating the invalid prefixes
     */
    public static String getErrorMessageForInvalidPrefixes(Prefix... invalidPrefixes) {
        return String.format(MESSAGE_INVALID_PREFIX, getErrorMessageForPrefixes(invalidPrefixes));
    }

    /**
     * Return an error message for multiple prefixes
     */
    public static String getErrorMessageForPrefixes(Prefix... prefixes) {
        assert prefixes.length > 0;
        Set<String> fields = Stream.of(prefixes).map(Prefix::toString).collect(Collectors.toSet());
        return String.join(" ", fields);
    }

    /**
     * Formats the {@code student} for display to the user.
     */
    public static String format(Student student) {
        final StringBuilder builder = new StringBuilder();
        builder.append(student.getName())
                .append("; Phone: ")
                .append(student.getPhone())
                .append("; Email: ")
                .append(student.getEmail())
                .append("; Address: ")
                .append(student.getAddress())
                .append("; Tags: ");
        student.getTags().forEach(builder::append);
        return builder.toString();
    }

}
