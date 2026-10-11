package seedu.address.model.workoutplan;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Represents the date of a workout plan.
 * Guarantees: immutable and not null.
 */
public final class WorkoutDate {

    public static final String MESSAGE_CONSTRAINTS =
            "Dates should be formatted only in DDMMYYYY format";
    /*
     * Date strings have to be 8 numeric characters long
     */
    public static final String VALIDATION_REGEX = "^\\d{8}$";

    public static final DateTimeFormatter INPUT_DATE_FORMAT = DateTimeFormatter.ofPattern("ddMMyyyy");

    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-uuuu");

    private final Date value;

    /**
     * Constructs a {@code WorkoutDate} from a non-null date.
     */
    public WorkoutDate(Date date) {
        requireNonNull(date);
        value = new Date(date.getTime());
    }

    /**
     * Constructs a {@code WorkoutDate} from a LocalDate object
     */
    public static WorkoutDate fromLocalDate(LocalDate localDate) {
        return new WorkoutDate(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }

    /**
     * Returns a copy of the date.
     */
    public Date getValue() {
        return new Date(value.getTime());
    }

    /**
     * Returns true if date is a string of 8 numerical digits
     */
    public static boolean isValidDateString(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /** Returns this date formatted for display to the user. */
    public String toDisplayString() {
        return value.toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate().format(DISPLAY_DATE_FORMAT);
    }

    /** Returns this date formatted as the intended user input */
    public String toInputString() {
        return value.toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate().format(INPUT_DATE_FORMAT);
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        return other instanceof WorkoutDate otherDate && value.equals(otherDate.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
