package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.WorkoutPlanStatus;
import seedu.address.testutil.WorkoutBuilder;

public class WorkoutPlanCardTest {

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

    @Test
    public void getSortedTags_noTags_returnsEmptyList() {
        WorkoutPlan workoutPlan = new WorkoutBuilder().build();

        assertEquals(List.of(), WorkoutPlanCard.getSortedTags(workoutPlan));
    }

    @Test
    public void getStatusText_allStatuses_returnsDisplayText() {
        assertEquals("DONE", WorkoutPlanCard.getStatusText(WorkoutPlanStatus.COMPLETED));
        assertEquals("OVERDUE", WorkoutPlanCard.getStatusText(WorkoutPlanStatus.OVERDUE));
        assertEquals("PENDING", WorkoutPlanCard.getStatusText(WorkoutPlanStatus.PENDING));
    }

    @Test
    public void getStatusStyleClass_allStatuses_returnsMatchingStyleClass() {
        assertEquals("workout-status-completed",
                WorkoutPlanCard.getStatusStyleClass(WorkoutPlanStatus.COMPLETED));
        assertEquals("workout-status-overdue", WorkoutPlanCard.getStatusStyleClass(WorkoutPlanStatus.OVERDUE));
        assertEquals("workout-status-pending", WorkoutPlanCard.getStatusStyleClass(WorkoutPlanStatus.PENDING));
    }
}
