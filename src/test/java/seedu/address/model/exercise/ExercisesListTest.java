package seedu.address.model.exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.exercise.exceptions.DuplicateExerciseException;
import seedu.address.model.exercise.exceptions.ExerciseIndexOutOfBoundsException;
import seedu.address.model.exercise.exceptions.ExerciseNotFoundException;

public class ExercisesListTest {

    private static final Exercise FIRST = new Exercise(new ExerciseName("Squats"),
            new ExerciseWeight(20.5), new ExerciseReps(10));
    private static final Exercise SECOND = new Exercise(new ExerciseName("Push-ups"),
            new ExerciseWeight(0.0), new ExerciseReps(15));
    private static final Exercise THIRD = new Exercise(new ExerciseName("Lunges"),
            new ExerciseWeight(10.0), new ExerciseReps(12));
    private static final Exercise REPLACEMENT = new Exercise(new ExerciseName("Deadlift"),
            new ExerciseWeight(40.0), new ExerciseReps(8));

    @Test
    public void constructors_rejectNullsAndCopyInput() {
        assertEquals(List.of(), new ExercisesList().getExerciseList());
        assertThrows(NullPointerException.class, () -> new ExercisesList(null));

        List<Exercise> inputWithNull = new ArrayList<>(List.of(FIRST));
        inputWithNull.add(null);
        assertThrows(NullPointerException.class, () -> new ExercisesList(inputWithNull));

        List<Exercise> input = new ArrayList<>(List.of(FIRST));
        ExercisesList exercises = new ExercisesList(input);
        input.add(SECOND);

        assertEquals(List.of(FIRST), exercises.getExerciseList());
        assertThrows(UnsupportedOperationException.class, () ->
                exercises.getExerciseList().add(SECOND));

        Exercise equalFirst = new Exercise(new ExerciseName("Squats"),
                new ExerciseWeight(20.5), new ExerciseReps(10));
        assertThrows(DuplicateExerciseException.class, () ->
                new ExercisesList(List.of(FIRST, equalFirst)));
    }

    @Test
    public void addExercise_appendsInNewList() {
        ExercisesList original = new ExercisesList(List.of(FIRST));

        ExercisesList updated = original.addExercise(SECOND);

        assertNotSame(original, updated);
        assertEquals(List.of(FIRST), original.getExerciseList());
        assertEquals(List.of(FIRST, SECOND), updated.getExerciseList());

        assertThrows(NullPointerException.class, () -> original.addExercise(null));
        assertThrows(DuplicateExerciseException.class, () -> original.addExercise(FIRST));
    }

    @Test
    public void setExercise_replacesExactTargetAndPreservesOrder() {
        Exercise equalFirst = new Exercise(new ExerciseName("Squats"),
                new ExerciseWeight(20.5), new ExerciseReps(10));
        ExercisesList original = new ExercisesList(List.of(FIRST, SECOND, THIRD));

        ExercisesList updated = original.setExercise(equalFirst, REPLACEMENT);

        assertNotSame(original, updated);
        assertEquals(List.of(FIRST, SECOND, THIRD), original.getExerciseList());
        assertEquals(List.of(REPLACEMENT, SECOND, THIRD), updated.getExerciseList());
        assertSame(SECOND, updated.getExerciseList().get(1));

        assertThrows(DuplicateExerciseException.class, () -> original.setExercise(FIRST, SECOND));
        assertThrows(NullPointerException.class, () -> original.setExercise(null, REPLACEMENT));
        assertThrows(NullPointerException.class, () -> original.setExercise(FIRST, null));
        assertThrows(ExerciseNotFoundException.class, () ->
                original.setExercise(REPLACEMENT, FIRST));
    }

    @Test
    public void setExercise_equalTargetAndReplacement_returnsOriginal() {
        ExercisesList original = new ExercisesList(List.of(FIRST, SECOND));
        Exercise equalFirst = new Exercise(new ExerciseName("Squats"),
                new ExerciseWeight(20.5), new ExerciseReps(10));

        assertSame(original, original.setExercise(equalFirst, equalFirst));
    }

    @Test
    public void removeExercise_removesSpecifiedExerciseInNewList() {
        ExercisesList original = new ExercisesList(List.of(FIRST, SECOND, THIRD));

        ExercisesList updated = original.removeExercise(SECOND);

        assertNotSame(original, updated);
        assertEquals(List.of(FIRST, SECOND, THIRD), original.getExerciseList());
        assertEquals(List.of(FIRST, THIRD), updated.getExerciseList());

        assertThrows(NullPointerException.class, () -> original.removeExercise(null));
        assertThrows(ExerciseNotFoundException.class, () -> original.removeExercise(REPLACEMENT));
    }

    @Test
    public void indexedAccess_invalidIndex_throwsExerciseIndexOutOfBoundsException() {
        ExercisesList exercises = new ExercisesList(List.of(FIRST, SECOND));

        assertSame(SECOND, exercises.getExercise(1));
        assertThrows(ExerciseIndexOutOfBoundsException.class, () -> exercises.getExercise(-1));
        assertThrows(ExerciseIndexOutOfBoundsException.class, () -> exercises.getExercise(2));
    }

    @Test
    public void equals() {
        ExercisesList first = new ExercisesList(List.of(FIRST, SECOND));
        ExercisesList sameValues = new ExercisesList(List.of(FIRST, SECOND));
        ExercisesList reversed = new ExercisesList(List.of(SECOND, FIRST));

        assertTrue(first.equals(first));
        assertTrue(first.equals(sameValues));
        assertFalse(first.equals(reversed));
        assertFalse(first.equals(null));
        assertFalse(first.equals(FIRST));
    }

    @Test
    public void hashCode_equalLists_haveSameHashCode() {
        ExercisesList first = new ExercisesList(List.of(FIRST, SECOND));
        ExercisesList sameValues = new ExercisesList(List.of(FIRST, SECOND));

        assertEquals(first.hashCode(), sameValues.hashCode());
    }

    @Test
    public void toStringMethod() {
        ExercisesList exercises = new ExercisesList(List.of(FIRST, SECOND));

        assertEquals(List.of(FIRST, SECOND).toString(), exercises.toString());
    }
}
