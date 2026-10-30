package seedu.address.model.workoutplan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import seedu.address.model.exercise.Exercise;
import seedu.address.model.exercise.ExerciseDescription;
import seedu.address.model.exercise.ExerciseName;
import seedu.address.model.exercise.ExercisesList;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.WorkoutPlanTag;
import seedu.address.model.workoutplan.exceptions.AlreadyMarkedException;
import seedu.address.model.workoutplan.exceptions.AlreadyUnmarkedException;

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

    // EP: non-null replacement exercises (valid).
    @Test
    public void withExercises_newExercises_returnsCopyWithOtherFieldsUnchanged() {
        WorkoutPlan original = new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID, DATE_VALID,
                Set.of(new WorkoutPlanTag("strength")), WorkoutPlanStatus.COMPLETED);

        WorkoutPlan copy = original.withExercises(EXERCISES);

        assertNotSame(original, copy);
        assertEquals(EXERCISES, copy.getExercisesList());
        assertEquals(new ExercisesList(), original.getExercisesList());
        assertEquals(original.getTitle(), copy.getTitle());
        assertEquals(original.getDescription(), copy.getDescription());
        assertEquals(original.getDate(), copy.getDate());
        assertEquals(original.getTags(), copy.getTags());
        assertEquals(original.getStatus(), copy.getStatus());
    }

    // One invalid input at a time: only the exercise list is null.
    @Test
    public void withExercises_nullExercises_throwsNullPointerException() {
        WorkoutPlan original = new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID, DATE_VALID);

        assertThrows(NullPointerException.class, () -> original.withExercises(null));
    }

    // One invalid input at a time: only the workout title is null.
    @Test
    public void constructor_nullTitle_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new WorkoutPlan(null, DESCRIPTION_VALID, DATE_VALID));
    }

    // One invalid input at a time: only the workout description is null.
    @Test
    public void constructor_nullDescription_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new WorkoutPlan(TITLE_VALID, null, DATE_VALID));
    }

    // One invalid input at a time: only the workout date is null.
    @Test
    public void constructor_nullDate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID, null));
    }

    @Test
    public void constructor_validFields_storesValues() {
        WorkoutPlan plan = new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID, DATE_VALID);
        // Each valid input at least once: a valid title is stored.
        assertEquals(TITLE_VALID, plan.getTitle());
        // Each valid input at least once: a valid description is stored.
        assertEquals(DESCRIPTION_VALID, plan.getDescription());
        // Each valid input at least once: a valid date is stored.
        assertEquals(DATE_VALID, plan.getDate());
        // BV: 0 <= count <= MAX_TAG_COUNT (valid).
        assertTrue(plan.getTags().isEmpty());
        // EP: a newly created plan has pending status (valid).
        assertEquals(WorkoutPlanStatus.PENDING, plan.getStatus());
        // BV: 0 <= count <= MAX_EXERCISES (valid).
        assertEquals(new ExercisesList(), plan.getExercisesList());
    }

    @Test
    public void constructor_withStatus_storesStatus() {
        WorkoutPlan plan = new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID, DATE_VALID,
                Set.of(), WorkoutPlanStatus.COMPLETED);

        // Each valid input at least once: completed status is stored.
        assertEquals(WorkoutPlanStatus.COMPLETED, plan.getStatus());
        // One invalid input at a time: only the workout-plan status is null.
        assertThrows(NullPointerException.class, () -> new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID,
                DATE_VALID, Set.of(), null));

        // One invalid input at a time: only the exercise list is null.
        assertThrows(NullPointerException.class, () -> new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID,
                DATE_VALID, Set.of(), WorkoutPlanStatus.PENDING, null));
    }

    @Test
    public void constructor_withExercises_storesExercises() {
        WorkoutPlan plan = new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID, DATE_VALID,
                Set.of(), WorkoutPlanStatus.PENDING, EXERCISES);

        assertEquals(EXERCISES, plan.getExercisesList());
    }

    // BV: 0 <= count <= MAX_TAG_COUNT (valid).
    @Test
    public void constructor_tagsAtLimit_success() {
        Set<WorkoutPlanTag> tags = createWorkoutPlanTags(Tag.MAX_TAG_COUNT);

        WorkoutPlan plan = new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID, DATE_VALID, tags);

        assertEquals(tags, plan.getTags());
    }

    // BV: count > MAX_TAG_COUNT (invalid).
    @Test
    public void constructor_tooManyTags_throwsIllegalArgumentException() {
        Set<WorkoutPlanTag> tags = createWorkoutPlanTags(Tag.MAX_TAG_COUNT + 1);

        assertThrows(IllegalArgumentException.class, Tag.MESSAGE_TAG_LIMIT, ()
            -> new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID, DATE_VALID, tags));
    }

    // EP: status = COMPLETED; date changes preserve status (valid).
    @Test
    public void updateState_completedRemainsCompletedRegardlessOfDate() {
        WorkoutPlan completed = planForDate(LocalDate.of(2026, 10, 17), WorkoutPlanStatus.COMPLETED);

        assertEquals(completed, completed.updateState(LocalDate.of(2026, 10, 18)));
    }

    // BV: date < today (valid).
    @Test
    public void updateState_pastDateBecomesOverdueWithoutMutatingOriginal() {
        WorkoutPlan pending = planForDate(LocalDate.of(2026, 10, 17), WorkoutPlanStatus.PENDING);

        WorkoutPlan updated = pending.updateState(LocalDate.of(2026, 10, 18));

        assertNotSame(pending, updated);
        assertEquals(WorkoutPlanStatus.OVERDUE, updated.getStatus());
        assertEquals(WorkoutPlanStatus.PENDING, pending.getStatus());
        assertEquals(pending.getTitle(), updated.getTitle());
        assertEquals(pending.getDate(), updated.getDate());
        assertEquals(EXERCISES, updated.getExercisesList());
    }

    @Test
    public void updateState_exactDateAndFutureDateRemainPending() {
        WorkoutPlan today = planForDate(LocalDate.of(2026, 10, 18), WorkoutPlanStatus.OVERDUE);
        WorkoutPlan future = planForDate(LocalDate.of(2026, 10, 19), WorkoutPlanStatus.PENDING);
        LocalDate todayDate = LocalDate.of(2026, 10, 18);

        // BV: date = today (valid).
        assertEquals(WorkoutPlanStatus.PENDING, today.updateState(todayDate).getStatus());
        // BV: date > today (valid).
        assertEquals(future, future.updateState(todayDate));
    }

    @Test
    public void mark_completesPlanWithoutChangingOriginal() {
        WorkoutPlan pending = planForDate(LocalDate.of(2026, 10, 18), WorkoutPlanStatus.PENDING);

        WorkoutPlan marked = pending.mark();

        assertEquals(WorkoutPlanStatus.COMPLETED, marked.getStatus());
        assertEquals(WorkoutPlanStatus.PENDING, pending.getStatus());
        assertEquals(pending.getDate(), marked.getDate());
        assertEquals(EXERCISES, marked.getExercisesList());
        assertThrows(AlreadyMarkedException.class, marked::mark);
        assertEquals(WorkoutPlanStatus.COMPLETED, marked.getStatus());
    }

    @Test
    public void unmark_restoresPendingOrOverdueStatus() {
        LocalDate today = LocalDate.now();
        WorkoutPlan past = planForDate(today.minusDays(1), WorkoutPlanStatus.COMPLETED);
        WorkoutPlan dueToday = planForDate(today, WorkoutPlanStatus.COMPLETED);
        WorkoutPlan future = planForDate(today.plusDays(1), WorkoutPlanStatus.COMPLETED);

        // BV: date < today (valid).
        assertEquals(WorkoutPlanStatus.OVERDUE, past.unmark().getStatus());
        // BV: date = today (valid).
        assertEquals(WorkoutPlanStatus.PENDING, dueToday.unmark().getStatus());
        // BV: date > today (valid).
        assertEquals(WorkoutPlanStatus.PENDING, future.unmark().getStatus());
        assertEquals(WorkoutPlanStatus.COMPLETED, past.getStatus());
        assertEquals(EXERCISES, past.unmark().getExercisesList());

        WorkoutPlan unmarked = past.unmark();
        assertThrows(AlreadyUnmarkedException.class, unmarked::unmark);
        assertEquals(WorkoutPlanStatus.OVERDUE, unmarked.getStatus());
    }

    @Test
    public void unmark_pendingPlan_throwsAlreadyUnmarkedException() {
        WorkoutPlan pending = planForDate(LocalDate.now().minusDays(1), WorkoutPlanStatus.PENDING);

        assertThrows(AlreadyUnmarkedException.class, pending::unmark);
        assertEquals(WorkoutPlanStatus.PENDING, pending.getStatus());
    }

    private static WorkoutPlan planForDate(LocalDate deadline, WorkoutPlanStatus status) {
        return new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID, WorkoutDate.fromLocalDate(deadline), Set.of(), status,
                EXERCISES);
    }

    @Test
    public void equals() {
        WorkoutPlan plan = new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID, DATE_VALID);

        // EP: Another plan with equal identifying fields (equivalent).
        WorkoutPlan copy = new WorkoutPlan(new WorkoutTitle(TITLE_TEXT_VALID),
                new WorkoutDescription(DESCRIPTION_TEXT_VALID), new WorkoutDate(DATE_VALID.getValue()));
        assertTrue(plan.equals(copy));

        // EP: The same workout-plan object (equivalent).
        assertTrue(plan.equals(plan));

        // EP: Null (non-equivalent).
        assertFalse(plan.equals(null));

        // EP: An object of another type (non-equivalent).
        assertFalse(plan.equals(TITLE_TEXT_VALID));

        // EP: Only the title differs (non-equivalent).
        assertFalse(plan.equals(new WorkoutPlan(new WorkoutTitle(TITLE_TEXT_OTHER),
                DESCRIPTION_VALID, DATE_VALID)));

        // EP: Only the description differs (non-equivalent).
        assertFalse(plan.equals(new WorkoutPlan(TITLE_VALID,
                new WorkoutDescription(DESCRIPTION_TEXT_OTHER), DATE_VALID)));

        // EP: Only the date differs (non-equivalent).
        assertFalse(plan.equals(new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID,
                new WorkoutDate(new Date(DATE_TIME_OTHER)))));

        // EP: Only the tags differ (non-equivalent).
        WorkoutPlan strength = new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID, DATE_VALID,
                Set.of(new WorkoutPlanTag("strength")));
        WorkoutPlan cardio = new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID, DATE_VALID,
                Set.of(new WorkoutPlanTag("cardio")));
        assertFalse(strength.equals(cardio));

        // EP: Only the status differs (non-equivalent).
        WorkoutPlan overdue = new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID, DATE_VALID,
                Set.of(), WorkoutPlanStatus.OVERDUE);
        assertFalse(plan.equals(overdue));

        // EP: Only the exercises differ (non-equivalent).
        WorkoutPlan withExercises = new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID, DATE_VALID,
                Set.of(), WorkoutPlanStatus.PENDING, EXERCISES);
        assertFalse(plan.equals(withExercises));
    }

    @Test
    public void hashCode_equalPlans_haveSameHashCode() {
        WorkoutPlan first = new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID, DATE_VALID);
        WorkoutPlan second = new WorkoutPlan(new WorkoutTitle(TITLE_TEXT_VALID),
                new WorkoutDescription(DESCRIPTION_TEXT_VALID), new WorkoutDate(DATE_VALID.getValue()));
        assertEquals(first.hashCode(), second.hashCode());

        WorkoutPlan sameValuesWithExercises = new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID, DATE_VALID,
                Set.of(), WorkoutPlanStatus.PENDING, EXERCISES);
        WorkoutPlan sameValuesWithExercisesCopy = new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID, DATE_VALID,
                Set.of(), WorkoutPlanStatus.PENDING, new ExercisesList(List.of(EXERCISE)));
        assertEquals(sameValuesWithExercises.hashCode(), sameValuesWithExercisesCopy.hashCode());
    }

    @Test
    public void isSamePlan() {
        WorkoutPlan plan = new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID, DATE_VALID);
        for (WorkoutPlanStatus status : WorkoutPlanStatus.values()) {
            WorkoutPlan other = new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID, DATE_VALID, Set.of(), status);
            // Each valid input at least once: every status preserves the same plan identity.
            assertTrue(plan.isSamePlan(other));
            // Each valid input at least once: every status is symmetric for plan identity.
            assertTrue(other.isSamePlan(plan));
        }

        // EP: null (different identity).
        assertFalse(plan.isSamePlan(null));
        // EP: only the title differs (different identity).
        assertFalse(plan.isSamePlan(new WorkoutPlan(new WorkoutTitle(TITLE_TEXT_OTHER),
                DESCRIPTION_VALID, DATE_VALID)));
        // EP: only the description differs (different identity).
        assertFalse(plan.isSamePlan(new WorkoutPlan(TITLE_VALID,
                new WorkoutDescription(DESCRIPTION_TEXT_OTHER), DATE_VALID)));
        // EP: only the date differs (different identity).
        assertFalse(plan.isSamePlan(new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID,
                new WorkoutDate(new Date(DATE_TIME_OTHER)))));
        WorkoutPlan tagged = new WorkoutPlan(TITLE_VALID, DESCRIPTION_VALID, DATE_VALID,
                Set.of(new WorkoutPlanTag("strength")));
        // EP: tags do not affect plan identity (same identity).
        assertTrue(plan.isSamePlan(tagged));
        // EP: tag differences are symmetric (same identity).
        assertTrue(tagged.isSamePlan(plan));

        WorkoutPlan withExercises = plan.withExercises(EXERCISES);
        assertTrue(plan.isSamePlan(withExercises));
        assertTrue(withExercises.isSamePlan(plan));
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

    private static Set<WorkoutPlanTag> createWorkoutPlanTags(int count) {
        return IntStream.range(0, count)
                .mapToObj(index -> new WorkoutPlanTag("tag" + index))
                .collect(Collectors.toSet());
    }
}
