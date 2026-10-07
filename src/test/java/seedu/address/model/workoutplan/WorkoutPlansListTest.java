package seedu.address.model.workoutplan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

public class WorkoutPlansListTest {

    private static final WorkoutPlan FIRST = new WorkoutPlan(
            new WorkoutTitle("Strength"), new WorkoutDescription("Squats"), new WorkoutDate(new Date(0)));
    private static final WorkoutPlan SECOND = new WorkoutPlan(
            new WorkoutTitle("Cardio"), new WorkoutDescription("Running"), new WorkoutDate(new Date(1)));

    @Test
    public void constructors_rejectNullsAndCopyInput() {
        assertEquals(List.of(), new WorkoutPlansList().asList());
        assertThrows(NullPointerException.class, () -> new WorkoutPlansList(null));
        List<WorkoutPlan> inputWithNull = new ArrayList<>(List.of(FIRST));
        inputWithNull.add(null);
        assertThrows(NullPointerException.class, () -> new WorkoutPlansList(inputWithNull));

        List<WorkoutPlan> input = new ArrayList<>(List.of(FIRST));
        WorkoutPlansList plans = new WorkoutPlansList(input);
        input.add(SECOND);

        assertEquals(List.of(FIRST), plans.asList());
        assertThrows(UnsupportedOperationException.class, () -> plans.asList().add(SECOND));
    }

    @Test
    public void withAdded_appendsInNewList() {
        WorkoutPlansList original = new WorkoutPlansList(List.of(FIRST));
        WorkoutPlansList updated = original.withAdded(SECOND);

        assertEquals(List.of(FIRST), original.asList());
        assertEquals(List.of(FIRST, SECOND), updated.asList());
        assertThrows(NullPointerException.class, () -> original.withAdded(null));
    }

    @Test
    public void without_removesSpecifiedWorkoutPlanInNewList() {
        WorkoutPlansList original = new WorkoutPlansList(List.of(FIRST, SECOND, FIRST));
        WorkoutPlan equalFirst = new WorkoutPlan(
                new WorkoutTitle("Strength"), new WorkoutDescription("Squats"), new WorkoutDate(new Date(0)));

        WorkoutPlansList updated = original.without(equalFirst);

        assertEquals(List.of(FIRST, SECOND, FIRST), original.asList());
        assertEquals(List.of(SECOND, FIRST), updated.asList());
        assertThrows(NullPointerException.class, () -> original.without(null));
        assertThrows(NoSuchElementException.class, () -> new WorkoutPlansList().without(FIRST));
    }

    @Test
    public void equalsAndHashCode_respectValuesAndOrder() {
        WorkoutPlansList first = new WorkoutPlansList(List.of(FIRST, SECOND));
        WorkoutPlansList sameValues = new WorkoutPlansList(List.of(FIRST, SECOND));
        WorkoutPlansList reversed = new WorkoutPlansList(List.of(SECOND, FIRST));

        assertEquals(first, sameValues);
        assertEquals(first.hashCode(), sameValues.hashCode());
        assertNotEquals(first, reversed);
        assertNotEquals(first, null);
        assertNotEquals(first, FIRST);
    }
}
