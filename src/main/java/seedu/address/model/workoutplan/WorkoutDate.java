package seedu.address.model.workoutplan;

import static java.util.Objects.requireNonNull;

import java.util.Date;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Represents the date of a workout plan.
 * Guarantees: immutable and not null.
 */
public final class WorkoutDate {

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
     * Returns a copy of the date.
     */
    public Date getValue() {
        return new Date(value.getTime());
    }

    /** Returns this date formatted for display to the user. */
    public String toDisplayString() {
        return value.toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate().format(DISPLAY_DATE_FORMAT);
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
