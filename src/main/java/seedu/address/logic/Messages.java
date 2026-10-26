package seedu.address.logic;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.student.Student;
import seedu.address.model.tag.WorkoutPlanTag;
import seedu.address.model.workoutplan.WorkoutPlan;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command.";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format!\n%1$s";
    public static final String MESSAGE_UNEXPECTED_PARAMETERS =
            "This command does not accept parameters. Enter only the command word.";
    public static final String MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX = "The student index provided is invalid.";
    public static final String MESSAGE_STUDENTS_LISTED_OVERVIEW = "%1$d student(s) listed!";
    public static final String MESSAGE_MISSING_PREAMBLE = "No preamble specified";
    public static final String MESSAGE_MISSING_PREFIXES = "Missing required field(s): %1$s";
    public static final String MESSAGE_DUPLICATE_FIELDS = "Multiple values specified for single-valued field(s): %1$s";
    public static final String MESSAGE_INVALID_PREFIX = "Invalid prefix(es) included: %1$s";
    public static final String MESSAGE_EMPTY_PREFIX = "Empty prefix detected."
            + " Ensure all prefixes are in the form '--prefix' and isolated by spaces";

    private static final DateTimeFormatter WORKOUT_PLAN_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy");

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
     * Retuns an error message indicating the missing prefixes
     */
    public static String getErrorMessageForMissingPrefixes(Prefix... missingPrefixes) {
        return String.format(MESSAGE_MISSING_PREFIXES, getErrorMessageForPrefixes(missingPrefixes));
    }

    /**
     * Return an error message for multiple prefixes
     */
    private static String getErrorMessageForPrefixes(Prefix... prefixes) {
        assert prefixes.length > 0;
        Set<String> fields = Stream.of(prefixes).map(Prefix::toString).collect(Collectors.toSet());
        return String.join(" ", fields);
    }

    /**
     * Returns an error string properly formatted to
     *
     * <Specific errors in input String>
     * =================================
     * <Command usage>
     */
    public static String formatParserError(String errorMessage, String commandUsage) {
        StringBuilder sb = new StringBuilder();
        sb.append(errorMessage).append("\n==========COMMAND USAGE==========\n").append(commandUsage);
        return sb.toString();
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

    /**
     * Formats the {@code workoutPlan} for display to the user.
     */
    public static String format(WorkoutPlan workoutPlan) {
        String formattedDate = workoutPlan.getDate().getValue().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .format(WORKOUT_PLAN_DATE_FORMATTER);
        StringBuilder builder = new StringBuilder()
                .append(workoutPlan.getTitle().getValue())
                .append(" - ")
                .append(workoutPlan.getWorkouts().getValue())
                .append(" (")
                .append(formattedDate)
                .append(")");

        if (!workoutPlan.getTags().isEmpty()) {
            String formattedTags = workoutPlan.getTags().stream()
                    .map(WorkoutPlanTag::toString)
                    .sorted()
                    .collect(Collectors.joining(" "));
            builder.append(" Tags: ").append(formattedTags);
        }
        return builder.toString();
    }

}
