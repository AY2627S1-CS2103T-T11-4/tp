package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.WorkoutPlanStatus;
import seedu.address.testutil.WorkoutBuilder;

public class WorkoutPlanCardTest {

    // EP: workout plan has multiple unsorted tags (valid).
    @Test
    public void getSortedTags_multipleTags_returnsTagsSortedByName() {
        WorkoutPlan workoutPlan = new WorkoutBuilder()
                .withTags("strength", "cardio", "balance")
                .build();

        List<String> sortedTagNames = WorkoutPlanCard.getSortedTags(workoutPlan).stream()
                .map(tag -> tag.tagName)
                .toList();

        assertEquals(List.of("balance", "cardio", "strength"), sortedTagNames);
    }

    // BV: 0 <= count <= MAX_TAG_COUNT (valid).
    @Test
    public void getSortedTags_noTags_returnsEmptyList() {
        WorkoutPlan workoutPlan = new WorkoutBuilder().build();

        assertEquals(List.of(), WorkoutPlanCard.getSortedTags(workoutPlan));
    }

    // Each valid input at least once: every status has display text.
    @Test
    public void getStatusText_allStatuses_returnsDisplayText() {
        assertEquals("DONE", WorkoutPlanCard.getStatusText(WorkoutPlanStatus.COMPLETED));
        assertEquals("OVERDUE", WorkoutPlanCard.getStatusText(WorkoutPlanStatus.OVERDUE));
        assertEquals("PENDING", WorkoutPlanCard.getStatusText(WorkoutPlanStatus.PENDING));
    }

    // Each valid input at least once: every status has a status style.
    @Test
    public void getStatusStyleClass_allStatuses_returnsMatchingStyleClass() {
        assertEquals("workout-status-completed",
                WorkoutPlanCard.getStatusStyleClass(WorkoutPlanStatus.COMPLETED));
        assertEquals("workout-status-overdue", WorkoutPlanCard.getStatusStyleClass(WorkoutPlanStatus.OVERDUE));
        assertEquals("workout-status-pending", WorkoutPlanCard.getStatusStyleClass(WorkoutPlanStatus.PENDING));
    }

    // Each valid input at least once: every status has a card style.
    @Test
    public void getCardStyleClass_allStatuses_returnsMatchingStyleClass() {
        assertEquals("workout-plan-completed",
                WorkoutPlanCard.getCardStyleClass(WorkoutPlanStatus.COMPLETED));
        assertEquals("workout-plan-overdue", WorkoutPlanCard.getCardStyleClass(WorkoutPlanStatus.OVERDUE));
        assertEquals("workout-plan-pending", WorkoutPlanCard.getCardStyleClass(WorkoutPlanStatus.PENDING));
    }

    // EP: workout plan is null (invalid).
    @Test
    public void getSortedTags_nullWorkoutPlan_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> WorkoutPlanCard.getSortedTags(null));
    }

    // BV: length <= 72 (valid).
    @Test
    public void getDescriptionPreview_shortDescription_returnsUnchangedText() {
        assertEquals("Squats and lunges", WorkoutPlanCard.getDescriptionPreview("Squats and lunges"));
    }

    // BV: length > 72 (valid).
    @Test
    public void getDescriptionPreview_longDescription_returnsShortenedText() {
        String description = "Complete five rounds of squats, lunges, deadlifts, pull-ups, push-ups, "
                + "planks, mountain climbers, and stretching";

        String preview = WorkoutPlanCard.getDescriptionPreview(description);

        assertTrue(preview.length() <= 72);
        assertTrue(preview.endsWith("..."));
    }

    // EP: description is null (invalid).
    @Test
    public void getDescriptionPreview_nullDescription_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> WorkoutPlanCard.getDescriptionPreview(null));
    }
}
