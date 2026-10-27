package seedu.address.model.workoutplan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.exercise.Exercise;
import seedu.address.model.exercise.ExerciseDescription;
import seedu.address.model.exercise.ExerciseName;
import seedu.address.model.exercise.ExercisesList;
import seedu.address.model.tag.WorkoutPlanTag;

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

    private static final Exercise EXERCISE = new Exercise(new ExerciseName("Squats"),
            new ExerciseDescription("Barbell back squat"));
    private static final ExercisesList EXERCISES = new ExercisesList(List.of(EXERCISE));

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
        assertTrue(plan.getTags().isEmpty());
        assertEquals(WorkoutPlanStatus.PENDING, plan.getStatus());
        assertEquals(new ExercisesList(), plan.getExercises());
    }

    @Test
    public void constructor_withStatus_storesStatus() {
        WorkoutPlan plan = new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID, DATE_VALID,
                Set.of(), WorkoutPlanStatus.COMPLETED);

        assertEquals(WorkoutPlanStatus.COMPLETED, plan.getStatus());
        assertThrows(NullPointerException.class, () -> new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID,
                DATE_VALID, Set.of(), null));

        assertThrows(NullPointerException.class, () -> new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID,
                DATE_VALID, Set.of(), WorkoutPlanStatus.PENDING, null));
    }

    @Test
    public void constructor_withExercises_storesExercises() {
        WorkoutPlan plan = new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID, DATE_VALID,
                Set.of(), WorkoutPlanStatus.PENDING, EXERCISES);

        assertSame(EXERCISES, plan.getExercises());
    }

    @Test
    public void updateState_completedRemainsCompletedRegardlessOfDate() {
        WorkoutPlan completed = planForDate(LocalDate.of(2026, 10, 17), WorkoutPlanStatus.COMPLETED);

        assertSame(completed, completed.updateState(LocalDate.of(2026, 10, 18)));
    }

    @Test
    public void updateState_pastDateBecomesOverdueWithoutMutatingOriginal() {
        WorkoutPlan pending = planForDate(LocalDate.of(2026, 10, 17), WorkoutPlanStatus.PENDING);

        WorkoutPlan updated = pending.updateState(LocalDate.of(2026, 10, 18));

        assertNotSame(pending, updated);
        assertEquals(WorkoutPlanStatus.OVERDUE, updated.getStatus());
        assertEquals(WorkoutPlanStatus.PENDING, pending.getStatus());
        assertEquals(pending.getTitle(), updated.getTitle());
        assertEquals(pending.getDate(), updated.getDate());
        assertEquals(EXERCISES, updated.getExercises());
    }

    @Test
    public void updateState_exactDateAndFutureDateRemainPending() {
        WorkoutPlan today = planForDate(LocalDate.of(2026, 10, 18), WorkoutPlanStatus.OVERDUE);
        WorkoutPlan future = planForDate(LocalDate.of(2026, 10, 19), WorkoutPlanStatus.PENDING);
        LocalDate todayDate = LocalDate.of(2026, 10, 18);

        assertEquals(WorkoutPlanStatus.PENDING, today.updateState(todayDate).getStatus());
        assertSame(future, future.updateState(todayDate));
    }

    @Test
    public void mark_completesPlanWithoutChangingOriginal() {
        WorkoutPlan pending = planForDate(LocalDate.of(2026, 10, 18), WorkoutPlanStatus.PENDING);

        WorkoutPlan marked = pending.mark();

        assertEquals(WorkoutPlanStatus.COMPLETED, marked.getStatus());
        assertEquals(WorkoutPlanStatus.PENDING, pending.getStatus());
        assertEquals(pending.getDate(), marked.getDate());
        assertEquals(EXERCISES, marked.getExercises());
        assertSame(marked, marked.mark());
    }

    @Test
    public void unmark_restoresPendingOrOverdueStatus() {
        LocalDate today = LocalDate.now();
        WorkoutPlan past = planForDate(today.minusDays(1), WorkoutPlanStatus.COMPLETED);
        WorkoutPlan dueToday = planForDate(today, WorkoutPlanStatus.COMPLETED);
        WorkoutPlan future = planForDate(today.plusDays(1), WorkoutPlanStatus.COMPLETED);

        assertEquals(WorkoutPlanStatus.OVERDUE, past.unmark().getStatus());
        assertEquals(WorkoutPlanStatus.PENDING, dueToday.unmark().getStatus());
        assertEquals(WorkoutPlanStatus.PENDING, future.unmark().getStatus());
        assertEquals(WorkoutPlanStatus.COMPLETED, past.getStatus());
        assertEquals(EXERCISES, past.unmark().getExercises());

        WorkoutPlan unmarked = past.unmark();
        assertSame(unmarked, unmarked.unmark());
    }

    private static WorkoutPlan planForDate(LocalDate deadline, WorkoutPlanStatus status) {
        return new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID, WorkoutDate.fromLocalDate(deadline), Set.of(), status,
                EXERCISES);
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

        // different tags -> returns false
        WorkoutPlan strength = new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID, DATE_VALID,
                Set.of(new WorkoutPlanTag("strength")));
        WorkoutPlan cardio = new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID, DATE_VALID,
                Set.of(new WorkoutPlanTag("cardio")));
        assertFalse(strength.equals(cardio));

        // different status -> still returns true because status is not part of plan equality
        WorkoutPlan overdue = new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID, DATE_VALID,
                Set.of(), WorkoutPlanStatus.OVERDUE);
        assertTrue(plan.equals(overdue));

        // different exercises -> still returns true because exercises are nested data
        WorkoutPlan withExercises = new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID, DATE_VALID,
                Set.of(), WorkoutPlanStatus.PENDING, EXERCISES);
        assertTrue(plan.equals(withExercises));
    }

    @Test
    public void hashCode_equalPlans_haveSameHashCode() {
        WorkoutPlan first = new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID, DATE_VALID);
        WorkoutPlan second = new WorkoutPlan(new WorkoutTitle(TITLE_TEXT_VALID),
                new WorkoutDescription(DESCRIPTION_TEXT_VALID), new WorkoutDate(DATE_VALID.getValue()));
        assertEquals(first.hashCode(), second.hashCode());

        WorkoutPlan differentStatusAndExercises = new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID, DATE_VALID,
                Set.of(), WorkoutPlanStatus.OVERDUE, EXERCISES);
        assertEquals(first.hashCode(), differentStatusAndExercises.hashCode());
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

        WorkoutPlan taggedPlan = new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID, DATE_VALID,
                Set.of(new WorkoutPlanTag("strength"), new WorkoutPlanTag("cardio")));
        String taggedExpected = expected + " Tags: [cardio] [strength]";
        assertEquals(taggedExpected, taggedPlan.toString());
    }
}
