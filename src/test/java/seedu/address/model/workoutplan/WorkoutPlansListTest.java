package seedu.address.model.workoutplan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.exercise.Exercise;
import seedu.address.model.exercise.ExerciseDescription;
import seedu.address.model.exercise.ExerciseName;
import seedu.address.model.exercise.ExercisesList;
import seedu.address.model.tag.WorkoutPlanTag;
import seedu.address.model.workoutplan.exceptions.AlreadyMarkedException;
import seedu.address.model.workoutplan.exceptions.AlreadyUnmarkedException;
import seedu.address.model.workoutplan.exceptions.DuplicateWorkoutPlanException;
import seedu.address.model.workoutplan.exceptions.WorkoutPlanIndexOutOfBoundsException;
import seedu.address.model.workoutplan.exceptions.WorkoutPlanNotFoundException;

public class WorkoutPlansListTest {

    private static final WorkoutPlan FIRST = new WorkoutPlan(
            new WorkoutTitle("Strength"), new WorkoutDescription("Squats"), new WorkoutDate(new Date(0)));
    private static final WorkoutPlan SECOND = new WorkoutPlan(
            new WorkoutTitle("Cardio"), new WorkoutDescription("Running"), new WorkoutDate(new Date(1)));
    private static final WorkoutPlan FIRST_WITH_EXERCISES = new WorkoutPlan(
            FIRST.getTitle(), FIRST.getWorkouts(), FIRST.getDate(), FIRST.getTags(), FIRST.getStatus(),
            new ExercisesList(List.of(new Exercise(new ExerciseName("Squats"),
                    new ExerciseDescription("Barbell back squat")))));

    @Test
    public void updateState_replacesChangedPlansAndPreservesUnchangedPlans() {
        WorkoutPlan completed = new WorkoutPlan(SECOND.getTitle(), SECOND.getWorkouts(), SECOND.getDate(),
                java.util.Set.of(), WorkoutPlanStatus.COMPLETED);
        WorkoutPlansList original = new WorkoutPlansList(List.of(FIRST, completed));

        WorkoutPlansList updated = original.updateState();

        assertNotSame(original, updated);
        assertEquals(WorkoutPlanStatus.PENDING, FIRST.getStatus());
        assertEquals(WorkoutPlanStatus.OVERDUE, updated.getWorkoutList().get(0).getStatus());
        assertSame(completed, updated.getWorkoutList().get(1));
        assertSame(updated, updated.updateState());
    }

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
    public void getWorkoutPlan_target_returnsCanonicalStoredPlan() {
        WorkoutPlan equalFirst = new WorkoutPlan(
                new WorkoutTitle("Strength"), new WorkoutDescription("Squats"), new WorkoutDate(new Date(0)));
        WorkoutPlan equalExternal = new WorkoutPlan(
                new WorkoutTitle("Strength"), new WorkoutDescription("Squats"), new WorkoutDate(new Date(0)));
        WorkoutPlansList plans = new WorkoutPlansList(List.of(FIRST, SECOND, equalFirst));

        assertSame(equalFirst, plans.getWorkoutPlan(equalFirst));
        assertSame(FIRST, plans.getWorkoutPlan(equalExternal));
        assertThrows(NullPointerException.class, () -> plans.getWorkoutPlan(null));
        assertThrows(WorkoutPlanNotFoundException.class, () -> plans.getWorkoutPlan(
                new WorkoutPlan(new WorkoutTitle("Missing"), new WorkoutDescription("Missing"),
                        new WorkoutDate(new Date(3)))));
    }

    @Test
    public void addWorkoutPlan_appendsInNewList() {
        WorkoutPlansList original = new WorkoutPlansList(List.of(FIRST));
        WorkoutPlan differentTitle = new WorkoutPlan(new WorkoutTitle("Cardio"), FIRST.getWorkouts(),
                FIRST.getDate(), FIRST.getTags(), WorkoutPlanStatus.OVERDUE);
        WorkoutPlansList updated = original.addWorkoutPlan(differentTitle);

        assertEquals(List.of(FIRST), original.getWorkoutList());
        assertEquals(List.of(FIRST, differentTitle), updated.getWorkoutList());
        assertThrows(NullPointerException.class, () -> original.addWorkoutPlan(null));
        assertThrows(DuplicateWorkoutPlanException.class, () -> original.addWorkoutPlan(FIRST));
        assertThrows(DuplicateWorkoutPlanException.class, () -> original.addWorkoutPlan(FIRST_WITH_EXERCISES));
    }

    @Test
    public void addWorkoutPlan_sameDetailsAcrossStatuses_throwsDuplicateWorkoutPlanException() {
        for (WorkoutPlanStatus existingStatus : List.of(WorkoutPlanStatus.OVERDUE, WorkoutPlanStatus.COMPLETED)) {
            WorkoutPlan existing = new WorkoutPlan(FIRST.getTitle(), FIRST.getWorkouts(), FIRST.getDate(),
                    FIRST.getTags(), existingStatus);
            WorkoutPlansList plans = new WorkoutPlansList(List.of(existing));
            for (WorkoutPlanStatus newStatus : List.of(WorkoutPlanStatus.PENDING, WorkoutPlanStatus.OVERDUE)) {
                WorkoutPlan newPlan = new WorkoutPlan(FIRST.getTitle(), FIRST.getWorkouts(), FIRST.getDate(),
                        Set.of(new WorkoutPlanTag("different")), newStatus);
                assertThrows(DuplicateWorkoutPlanException.class, () -> plans.addWorkoutPlan(newPlan));
            }
        }

        WorkoutPlansList plans = new WorkoutPlansList(List.of(FIRST));
        assertThrows(DuplicateWorkoutPlanException.class, () -> plans.markWorkoutPlan(0).addWorkoutPlan(FIRST));
        assertThrows(DuplicateWorkoutPlanException.class, () -> plans.updateState().addWorkoutPlan(FIRST));
    }

    @Test
    public void markAndUnmarkWorkoutPlan_changeOnlySelectedPlan() {
        WorkoutPlansList original = new WorkoutPlansList(List.of(FIRST, SECOND));

        WorkoutPlansList marked = original.markWorkoutPlan(0);
        WorkoutPlansList unmarked = marked.unmarkWorkoutPlan(0);

        assertEquals(WorkoutPlanStatus.PENDING, original.getWorkoutList().get(0).getStatus());
        assertEquals(WorkoutPlanStatus.COMPLETED, marked.getWorkoutList().get(0).getStatus());
        assertEquals(WorkoutPlanStatus.OVERDUE, unmarked.getWorkoutList().get(0).getStatus());
        assertSame(SECOND, marked.getWorkoutList().get(1));
        assertThrows(AlreadyMarkedException.class, () -> marked.markWorkoutPlan(0));
        assertThrows(AlreadyUnmarkedException.class, () -> unmarked.unmarkWorkoutPlan(0));
        assertThrows(AlreadyUnmarkedException.class, () -> original.unmarkWorkoutPlan(0));
        assertThrows(WorkoutPlanIndexOutOfBoundsException.class, () -> original.markWorkoutPlan(2));
        assertThrows(WorkoutPlanIndexOutOfBoundsException.class, () -> original.unmarkWorkoutPlan(-1));
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
        assertThrows(WorkoutPlanNotFoundException.class, () -> new WorkoutPlansList().removeWorkoutPlan(FIRST));
    }

    @Test
    public void setWorkoutPlan_replacesExactTargetAndPreservesOrder() {
        WorkoutPlan equalFirst = new WorkoutPlan(
                new WorkoutTitle("Strength"), new WorkoutDescription("Squats"), new WorkoutDate(new Date(0)));
        WorkoutPlansList original = new WorkoutPlansList(List.of(FIRST, SECOND, equalFirst));

        WorkoutPlan replacement = new WorkoutPlan(
                new WorkoutTitle("Mobility"), new WorkoutDescription("Stretching"), new WorkoutDate(new Date(2)));
        WorkoutPlansList updated = original.setWorkoutPlan(equalFirst, replacement);

        assertEquals(List.of(FIRST, SECOND, equalFirst), original.getWorkoutList());
        assertEquals(List.of(FIRST, SECOND, replacement), updated.getWorkoutList());
        assertSame(FIRST, updated.getWorkoutList().get(0));
        assertThrows(DuplicateWorkoutPlanException.class, () -> original.setWorkoutPlan(equalFirst, SECOND));
        assertThrows(NullPointerException.class, () -> original.setWorkoutPlan(null, SECOND));
        assertThrows(NullPointerException.class, () -> original.setWorkoutPlan(FIRST, null));
        assertThrows(WorkoutPlanNotFoundException.class, () -> new WorkoutPlansList().setWorkoutPlan(FIRST, SECOND));

        WorkoutPlan secondWithExercises = new WorkoutPlan(SECOND.getTitle(), SECOND.getWorkouts(), SECOND.getDate(),
                SECOND.getTags(), SECOND.getStatus(), FIRST_WITH_EXERCISES.getExercisesList());
        assertThrows(DuplicateWorkoutPlanException.class, () ->
                original.setWorkoutPlan(equalFirst, secondWithExercises));
    }

    @Test
    public void setWorkoutPlan_equalTargetReplacesFirstMatchingPlan() {
        WorkoutPlansList original = new WorkoutPlansList(List.of(FIRST, SECOND));
        WorkoutPlan equalFirst = new WorkoutPlan(
                new WorkoutTitle("Strength"), new WorkoutDescription("Squats"), new WorkoutDate(new Date(0)));

        WorkoutPlan replacement = new WorkoutPlan(
                new WorkoutTitle("Mobility"), new WorkoutDescription("Stretching"), new WorkoutDate(new Date(2)));
        WorkoutPlansList updated = original.setWorkoutPlan(equalFirst, replacement);

        assertEquals(List.of(replacement, SECOND), updated.getWorkoutList());
        assertEquals(List.of(FIRST, SECOND), original.getWorkoutList());
    }

    @Test
    public void setWorkoutPlan_nestedExercisesChangeIsAllowedForTarget() {
        WorkoutPlansList original = new WorkoutPlansList(List.of(FIRST));

        WorkoutPlansList updated = original.setWorkoutPlan(FIRST, FIRST_WITH_EXERCISES);

        assertEquals(List.of(FIRST), original.getWorkoutList());
        assertEquals(List.of(FIRST_WITH_EXERCISES), updated.getWorkoutList());
    }
  
    @Test
    public void setWorkoutPlan_titleEditMatchesOverduePlan_throwsDuplicateWorkoutPlanException() {
        assertTitleEditMatchingPlanIsRejected(WorkoutPlanStatus.OVERDUE);
    }

    @Test
    public void setWorkoutPlan_titleEditMatchesCompletedPlan_throwsDuplicateWorkoutPlanException() {
        assertTitleEditMatchingPlanIsRejected(WorkoutPlanStatus.COMPLETED);
    }

    @Test
    public void setWorkoutPlan_titleEditRemainsUnique_replacesTarget() {
        WorkoutPlan existing = new WorkoutPlan(FIRST.getTitle(), FIRST.getWorkouts(), FIRST.getDate(),
                FIRST.getTags(), WorkoutPlanStatus.COMPLETED);
        WorkoutPlan target = new WorkoutPlan(new WorkoutTitle("Cardio"), FIRST.getWorkouts(), FIRST.getDate(),
                FIRST.getTags(), WorkoutPlanStatus.PENDING);
        WorkoutPlan edited = new WorkoutPlan(new WorkoutTitle("Mobility"), target.getWorkouts(), target.getDate(),
                target.getTags(), target.getStatus());
        WorkoutPlansList plans = new WorkoutPlansList(List.of(existing, target));

        WorkoutPlansList updated = plans.setWorkoutPlan(target, edited);

        assertEquals(List.of(existing, edited), updated.getWorkoutList());
        assertEquals(List.of(existing, target), plans.getWorkoutList());
    }

    private static void assertTitleEditMatchingPlanIsRejected(WorkoutPlanStatus existingStatus) {
        WorkoutPlan existing = new WorkoutPlan(FIRST.getTitle(), FIRST.getWorkouts(), FIRST.getDate(),
                FIRST.getTags(), existingStatus);
        WorkoutPlan target = new WorkoutPlan(new WorkoutTitle("Cardio"), existing.getWorkouts(), existing.getDate(),
                Set.of(new WorkoutPlanTag("different")), WorkoutPlanStatus.PENDING);
        WorkoutPlan edited = new WorkoutPlan(existing.getTitle(), target.getWorkouts(), target.getDate(),
                target.getTags(), target.getStatus());
        WorkoutPlansList plans = new WorkoutPlansList(List.of(existing, target));

        assertThrows(DuplicateWorkoutPlanException.class, () -> plans.setWorkoutPlan(target, edited));
        assertEquals(List.of(existing, target), plans.getWorkoutList());
    }

    @Test
    public void indexedAccessAndRemoval_invalidIndex_throwsWorkoutPlanIndexOutOfBoundsException() {
        WorkoutPlansList original = new WorkoutPlansList(List.of(FIRST, SECOND));
        assertSame(SECOND, original.getWorkoutPlan(1));
        assertEquals(List.of(FIRST), original.removeWorkoutPlan(1).getWorkoutList());
        assertThrows(WorkoutPlanIndexOutOfBoundsException.class, () -> original.getWorkoutPlan(2));
        assertThrows(WorkoutPlanIndexOutOfBoundsException.class, () -> original.removeWorkoutPlan(-1));
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
