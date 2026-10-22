package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.testutil.WorkoutBuilder;

/**
 * Contains unit tests for {@link Messages}.
 */
public class MessagesTest {

    @Test
    public void format_workoutPlanWithoutTags_formatsDetails() {
        WorkoutPlan plan = new WorkoutBuilder()
                .withTitle("Leg Day")
                .withDescription("Squats and lunges")
                .withDate(LocalDate.of(2026, 10, 9))
                .build();

        assertEquals("Leg Day - Squats and lunges (09 Oct 2026)", Messages.format(plan));
    }

    @Test
    public void format_workoutPlanWithTags_formatsSortedTags() {
        WorkoutPlan plan = new WorkoutBuilder()
                .withTitle("Leg Day")
                .withDescription("Squats and lunges")
                .withDate(LocalDate.of(2026, 10, 9))
                .withTags("upperbody", "strength")
                .build();

        assertEquals("Leg Day - Squats and lunges (09 Oct 2026) Tags: [strength] [upperbody]",
                Messages.format(plan));
    }
}
