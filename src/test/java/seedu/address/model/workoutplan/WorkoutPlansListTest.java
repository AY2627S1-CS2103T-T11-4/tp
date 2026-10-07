package seedu.address.model.workoutplan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        assertEquals(List.of(), new WorkoutPlansList().getWorkoutList());
        assertThrows(NullPointerException.class, () -> new WorkoutPlansList(null));
        List<WorkoutPlan> inputWithNull = new ArrayList<>(List.of(FIRST));
        inputWithNull.add(null);
        assertThrows(NullPointerException.class, () -> new WorkoutPlansList(inputWithNull));

        List<WorkoutPlan> input = new ArrayList<>(List.of(FIRST));
        WorkoutPlansList plans = new WorkoutPlansList(input);
        input.add(SECOND);

        assertEquals(List.of(FIRST), plans.getWorkoutList());
        assertThrows(UnsupportedOperationException.class, () -> plans.getWorkoutList().add(SECOND));
    }

    @Test
    public void addWorkoutPlan_appendsInNewList() {
        WorkoutPlansList original = new WorkoutPlansList(List.of(FIRST));
        WorkoutPlansList updated = original.addWorkoutPlan(SECOND);

        assertEquals(List.of(FIRST), original.getWorkoutList());
        assertEquals(List.of(FIRST, SECOND), updated.getWorkoutList());
        assertThrows(NullPointerException.class, () -> original.addWorkoutPlan(null));
    }

    @Test
    public void removeWorkoutPlan_removesSpecifiedWorkoutPlanInNewList() {
        WorkoutPlansList original = new WorkoutPlansList(List.of(FIRST, SECOND, FIRST));
        WorkoutPlan equalFirst = new WorkoutPlan(
                new WorkoutTitle("Strength"), new WorkoutDescription("Squats"), new WorkoutDate(new Date(0)));

        WorkoutPlansList updated = original.removeWorkoutPlan(equalFirst);

        assertEquals(List.of(FIRST, SECOND, FIRST), original.getWorkoutList());
        assertEquals(List.of(SECOND, FIRST), updated.getWorkoutList());
        assertThrows(NullPointerException.class, () -> original.removeWorkoutPlan(null));
        assertThrows(NoSuchElementException.class, () -> new WorkoutPlansList().removeWorkoutPlan(FIRST));
    }

    @Test
    public void equals() {
        WorkoutPlansList first = new WorkoutPlansList(List.of(FIRST, SECOND));
        WorkoutPlansList sameValues = new WorkoutPlansList(List.of(FIRST, SECOND));
        WorkoutPlansList reversed = new WorkoutPlansList(List.of(SECOND, FIRST));

        assertTrue(first.equals(first));
        assertTrue(first.equals(sameValues));
        assertFalse(first.equals(reversed));
        assertFalse(first.equals(null));
        assertFalse(first.equals(FIRST));
    }

    @Test
    public void hashCode_equalLists_haveSameHashCode() {
        WorkoutPlansList first = new WorkoutPlansList(List.of(FIRST, SECOND));
        WorkoutPlansList sameValues = new WorkoutPlansList(List.of(FIRST, SECOND));

        assertEquals(first.hashCode(), sameValues.hashCode());
    }
}
