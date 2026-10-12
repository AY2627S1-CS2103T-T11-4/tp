package seedu.address.model.workoutplan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.junit.jupiter.api.Test;

public class WorkoutPlanTest {

    private static final String TITLE_TEXT_VALID = "Strength training";
    private static final String TITLE_TEXT_OTHER = "Cardio";
    private static final WorkoutTitle TITLE_VALID = new WorkoutTitle(TITLE_TEXT_VALID);

    private static final String DESCRIPTION_TEXT_VALID = "Squats, push-ups";
    private static final String DESCRIPTION_TEXT_OTHER = "Running";
    private static final WorkoutDescription DESCRIPTION_VALID = new WorkoutDescription(DESCRIPTION_TEXT_VALID);

    private static final long DATE_TIME_VALID = 1_700_000_000_000L;
    private static final long DATE_TIME_OTHER = 0L;
    private static final WorkoutDate DATE_VALID = new WorkoutDate(new Date(DATE_TIME_VALID));

    @Test
    public void constructor_nullTitle_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new WorkoutPlan(null, DESCRIPTION_VALID, DATE_VALID));
    }

    @Test
    public void constructor_nullDescription_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new WorkoutPlan(TITLE_VALID, null, DATE_VALID));
    }

    @Test
    public void constructor_nullDate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID, null));
    }

    @Test
    public void constructor_validFields_storesValues() {
        WorkoutPlan plan = new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID, DATE_VALID);
        assertEquals(TITLE_VALID, plan.getTitle());
        assertEquals(DESCRIPTION_VALID, plan.getWorkouts());
        assertEquals(DATE_VALID, plan.getDate());
    }

    @Test
    public void equals() {
        WorkoutPlan plan = new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID, DATE_VALID);

        // same values -> returns true
        WorkoutPlan copy = new WorkoutPlan(new WorkoutTitle(TITLE_TEXT_VALID),
                new WorkoutDescription(DESCRIPTION_TEXT_VALID), new WorkoutDate(DATE_VALID.getValue()));
        assertTrue(plan.equals(copy));

        // same object -> returns true
        assertTrue(plan.equals(plan));

        // null -> returns false
        assertFalse(plan.equals(null));

        // different type -> returns false
        assertFalse(plan.equals(TITLE_TEXT_VALID));

        // different title -> returns false
        assertFalse(plan.equals(new WorkoutPlan(new WorkoutTitle(TITLE_TEXT_OTHER),
                DESCRIPTION_VALID, DATE_VALID)));

        // different workouts -> returns false
        assertFalse(plan.equals(new WorkoutPlan(TITLE_VALID,
                new WorkoutDescription(DESCRIPTION_TEXT_OTHER), DATE_VALID)));

        // different date -> returns false
        assertFalse(plan.equals(new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID,
                new WorkoutDate(new Date(DATE_TIME_OTHER)))));
    }

    @Test
    public void hashCode_equalPlans_haveSameHashCode() {
        WorkoutPlan first = new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID, DATE_VALID);
        WorkoutPlan second = new WorkoutPlan(new WorkoutTitle(TITLE_TEXT_VALID),
                new WorkoutDescription(DESCRIPTION_TEXT_VALID), new WorkoutDate(DATE_VALID.getValue()));
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    public void toStringMethod() {
        WorkoutPlan plan = new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID, DATE_VALID);
        String formattedDate = DATE_VALID.getValue().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
        String expected = TITLE_TEXT_VALID + " - " + DESCRIPTION_TEXT_VALID + " (" + formattedDate + ")";
        assertEquals(expected, plan.toString());
    }
}
