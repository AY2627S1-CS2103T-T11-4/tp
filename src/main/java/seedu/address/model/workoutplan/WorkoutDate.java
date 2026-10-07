package seedu.address.model.workoutplan;

import static java.util.Objects.requireNonNull;

import java.text.SimpleDateFormat;
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
    public static final SimpleDateFormat INPUT_FORMATTER = new SimpleDateFormat("ddMMyyyy");

    private final Date value;

    /**
     * Constructs a {@code WorkoutDate} from a non-null date.
     */
    public WorkoutDate(Date date) {
        requireNonNull(date);
        value = new Date(date.getTime());
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
