package seedu.address.model.workoutplan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.junit.jupiter.api.Test;

public class WorkoutDateTest {

    private static final long TIME_VALID = 1_700_000_000_000L;
    private static final long TIME_OTHER = 1_800_000_000_000L;
    private static final long TIME_EPOCH = 0L;

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new WorkoutDate(null));
    }

    @Test
    public void constructor_validDate_storesValue() {
        WorkoutDate date = new WorkoutDate(new Date(TIME_VALID));
        assertEquals(new Date(TIME_VALID), date.getValue());
    }

    @Test
    public void constructor_epochDate_storesValue() {
        WorkoutDate date = new WorkoutDate(new Date(TIME_EPOCH));
        assertEquals(new Date(TIME_EPOCH), date.getValue());
    }

    @Test
    public void constructor_inputChangedAfterConstruction_doesNotChangeValue() {
        Date input = new Date(TIME_VALID);
        WorkoutDate date = new WorkoutDate(input);

        input.setTime(TIME_OTHER);

        assertEquals(new Date(TIME_VALID), date.getValue());
    }

    @Test
    public void getValue_returnedDateChanged_doesNotChangeValue() {
        WorkoutDate date = new WorkoutDate(new Date(TIME_VALID));

        date.getValue().setTime(TIME_OTHER);

        assertEquals(new Date(TIME_VALID), date.getValue());
    }

    @Test
    public void equals() {
        WorkoutDate date = new WorkoutDate(new Date(TIME_VALID));

        // same values -> returns true
        assertTrue(date.equals(new WorkoutDate(new Date(TIME_VALID))));

        // same object -> returns true
        assertTrue(date.equals(date));

        // null -> returns false
        assertFalse(date.equals(null));

        // different type -> returns false
        assertFalse(date.equals(new Date(TIME_VALID)));

        // different values -> returns false
        assertFalse(date.equals(new WorkoutDate(new Date(TIME_OTHER))));
    }

    @Test
    public void hashCode_equalDates_haveSameHashCode() {
        assertEquals(new WorkoutDate(new Date(TIME_VALID)).hashCode(),
                new WorkoutDate(new Date(TIME_VALID)).hashCode());
    }

    @Test
    public void toStringMethod() {
        Date input = new Date(TIME_VALID);
        assertEquals(input.toString(), new WorkoutDate(input).toString());
    }

    @Test
    public void toDisplayString_returnsFormattedDate() {
        Date input = Date.from(LocalDate.of(2023, 11, 15)
                .atStartOfDay(ZoneId.systemDefault()).toInstant());

        assertEquals("15-11-2023", new WorkoutDate(input).toDisplayString());
    }
}
