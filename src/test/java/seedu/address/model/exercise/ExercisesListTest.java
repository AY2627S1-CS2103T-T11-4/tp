package seedu.address.model.exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import seedu.address.model.exercise.exceptions.DuplicateExerciseException;
import seedu.address.model.exercise.exceptions.ExerciseIndexOutOfBoundsException;
import seedu.address.model.exercise.exceptions.ExerciseListFullException;
import seedu.address.model.exercise.exceptions.ExerciseNotFoundException;

public class ExercisesListTest {

    private static final Exercise FIRST = new Exercise(new ExerciseName("Squats"),
            new ExerciseDescription("Barbell back squat"));
    private static final Exercise SECOND = new Exercise(new ExerciseName("Push Ups"),
            new ExerciseDescription("Bodyweight push-up"));
    private static final Exercise THIRD = new Exercise(new ExerciseName("Lunges"),
            new ExerciseDescription("Forward lunges"));
    private static final Exercise REPLACEMENT = new Exercise(new ExerciseName("Deadlift"),
            new ExerciseDescription("Conventional deadlift"));

    // EP: non-null replacement exercise list (valid).
    @Test
    public void withExercises_newExercises_returnsNewListWithoutChangingOriginal() {
        ExercisesList original = new ExercisesList(List.of(FIRST));

        ExercisesList copy = original.withExercises(List.of(SECOND));

        assertNotSame(original, copy);
        assertEquals(List.of(FIRST), original.getExerciseList());
        assertEquals(List.of(SECOND), copy.getExerciseList());
    }

    // One invalid input at a time: only the replacement exercise list is null.
    @Test
    public void withExercises_nullExercises_throwsNullPointerException() {
        ExercisesList original = new ExercisesList(List.of(FIRST));

        assertThrows(NullPointerException.class, () -> original.withExercises(null));
    }

    @Test
    public void constructors_rejectNullsAndCopyInput() {
        // BV: 0 <= count <= MAX_EXERCISES (valid).
        assertEquals(List.of(), new ExercisesList().getExerciseList());
        // EP: exercise-list reference is null (invalid).
        assertThrows(NullPointerException.class, () -> new ExercisesList(null));

        List<Exercise> inputWithNull = new ArrayList<>(List.of(FIRST));
        inputWithNull.add(null);
        // One invalid input at a time: the exercise list contains one null exercise.
        assertThrows(NullPointerException.class, () -> new ExercisesList(inputWithNull));

        List<Exercise> input = new ArrayList<>(List.of(FIRST));
        ExercisesList exercises = new ExercisesList(input);
        input.add(SECOND);

        assertEquals(List.of(FIRST), exercises.getExerciseList());
        assertThrows(UnsupportedOperationException.class, () ->
                exercises.getExerciseList().add(SECOND));

        Exercise equalFirst = new Exercise(new ExerciseName("Squats"),
                new ExerciseDescription("Barbell back squat"));
        // EP: two equivalent exercises in one list (invalid).
        assertThrows(DuplicateExerciseException.class, () ->
                new ExercisesList(List.of(FIRST, equalFirst)));

        List<Exercise> exercisesAtCapacity = createNumberedExercises(ExercisesList.MAX_EXERCISES);
        // BV: 0 <= count <= MAX_EXERCISES (valid).
        assertEquals(ExercisesList.MAX_EXERCISES,
                new ExercisesList(exercisesAtCapacity).getExerciseList().size());

        List<Exercise> exercisesOverCapacity = createNumberedExercises(ExercisesList.MAX_EXERCISES + 1);
        // BV: count > MAX_EXERCISES (invalid).
        assertThrows(ExerciseListFullException.class, () -> new ExercisesList(exercisesOverCapacity));
    }

    @Test
    public void addExercise_appendsInNewList() {
        ExercisesList original = new ExercisesList(List.of(FIRST));
        Exercise equalFirst = new Exercise(new ExerciseName("Squats"),
                new ExerciseDescription("Barbell back squat"));

        ExercisesList updated = original.addExercise(SECOND);

        assertNotSame(original, updated);
        assertEquals(List.of(FIRST), original.getExerciseList());
        assertEquals(List.of(FIRST, SECOND), updated.getExerciseList());

        // EP: exercise input is null (invalid).
        assertThrows(NullPointerException.class, () -> original.addExercise(null));
        // EP: the same exercise object is already present (invalid).
        assertThrows(DuplicateExerciseException.class, () -> original.addExercise(FIRST));
        // EP: an equivalent exercise is already present (invalid).
        assertThrows(DuplicateExerciseException.class, () -> original.addExercise(equalFirst));

        ExercisesList nearlyFull = new ExercisesList(
                createNumberedExercises(ExercisesList.MAX_EXERCISES - 1));
        // BV: result <= MAX_EXERCISES (valid).
        assertEquals(ExercisesList.MAX_EXERCISES,
                nearlyFull.addExercise(REPLACEMENT).getExerciseList().size());

        ExercisesList full = new ExercisesList(createNumberedExercises(ExercisesList.MAX_EXERCISES));
        // BV: result > MAX_EXERCISES (invalid).
        assertThrows(ExerciseListFullException.class, () -> full.addExercise(REPLACEMENT));
        // EP: adding a duplicate to a full list (invalid).
        assertThrows(DuplicateExerciseException.class, () -> full.addExercise(full.getExercise(0)));
        assertEquals(ExercisesList.MAX_EXERCISES, full.getExerciseList().size());

        Exercise sameNameDifferentDescription = new Exercise(new ExerciseName("Squats"),
                new ExerciseDescription("Goblet squat"));
        // EP: same name with a different description is unique (valid).
        assertEquals(List.of(FIRST, sameNameDifferentDescription),
                original.addExercise(sameNameDifferentDescription).getExerciseList());
    }

    @Test
    public void setExercise_replacesExactTargetAndPreservesOrder() {
        Exercise equalFirst = new Exercise(new ExerciseName("Squats"),
                new ExerciseDescription("Barbell back squat"));
        ExercisesList original = new ExercisesList(List.of(FIRST, SECOND, THIRD));

        ExercisesList updated = original.setExercise(equalFirst, REPLACEMENT);

        assertNotSame(original, updated);
        assertEquals(List.of(FIRST, SECOND, THIRD), original.getExerciseList());
        assertEquals(List.of(REPLACEMENT, SECOND, THIRD), updated.getExerciseList());
        assertSame(SECOND, updated.getExerciseList().get(1));

        // EP: replacement duplicates another stored exercise (invalid).
        assertThrows(DuplicateExerciseException.class, () -> original.setExercise(FIRST, SECOND));
        // One invalid input at a time: only the target exercise is null.
        assertThrows(NullPointerException.class, () -> original.setExercise(null, REPLACEMENT));
        // One invalid input at a time: only the replacement exercise is null.
        assertThrows(NullPointerException.class, () -> original.setExercise(FIRST, null));
        // EP: target exercise is absent from the list (invalid).
        assertThrows(ExerciseNotFoundException.class, () ->
                original.setExercise(REPLACEMENT, FIRST));
    }

    // EP: Target and replacement exercises are equal (equivalent).
    @Test
    public void setExercise_equalTargetAndReplacement_returnsOriginal() {
        ExercisesList original = new ExercisesList(List.of(FIRST, SECOND));
        Exercise equalFirst = new Exercise(new ExerciseName("Squats"),
                new ExerciseDescription("Barbell back squat"));
        Exercise equalReplacement = new Exercise(new ExerciseName("Squats"),
                new ExerciseDescription("Barbell back squat"));

        assertSame(original, original.setExercise(equalFirst, equalReplacement));
    }

    @Test
    public void removeExercise_removesSpecifiedExerciseInNewList() {
        ExercisesList original = new ExercisesList(List.of(FIRST, SECOND, THIRD));
        Exercise equalSecond = new Exercise(new ExerciseName("Push Ups"),
                new ExerciseDescription("Bodyweight push-up"));

        ExercisesList updated = original.removeExercise(equalSecond);

        assertNotSame(original, updated);
        assertEquals(List.of(FIRST, SECOND, THIRD), original.getExerciseList());
        assertEquals(List.of(FIRST, THIRD), updated.getExerciseList());

        // EP: exercise removal target is null (invalid).
        assertThrows(NullPointerException.class, () -> original.removeExercise(null));
        // EP: exercise is absent from the list (invalid).
        assertThrows(ExerciseNotFoundException.class, () -> original.removeExercise(REPLACEMENT));
    }

    @Test
    public void indexedAccess_invalidIndex_throwsExerciseIndexOutOfBoundsException() {
        ExercisesList exercises = new ExercisesList(List.of(FIRST, SECOND));

        // BV: 0 <= index < size (valid).
        assertSame(SECOND, exercises.getExercise(1));
        // BV: index < 0 (invalid).
        assertThrows(ExerciseIndexOutOfBoundsException.class, () -> exercises.getExercise(-1));
        // BV: index >= size (invalid).
        assertThrows(ExerciseIndexOutOfBoundsException.class, () -> exercises.getExercise(2));
    }

    @Test
    public void equals() {
        ExercisesList first = new ExercisesList(List.of(FIRST, SECOND));
        ExercisesList sameValues = new ExercisesList(List.of(FIRST, SECOND));
        ExercisesList reversed = new ExercisesList(List.of(SECOND, FIRST));

        // EP: The same exercise-list object (equivalent).
        assertTrue(first.equals(first));
        // EP: Another list with equal ordered elements (equivalent).
        assertTrue(first.equals(sameValues));
        // EP: Equal elements in a different order (non-equivalent).
        assertFalse(first.equals(reversed));
        // EP: Null (non-equivalent).
        assertFalse(first.equals(null));
        // EP: An object of another type (non-equivalent).
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

    private static List<Exercise> createNumberedExercises(int count) {
        return IntStream.range(0, count)
                .mapToObj(index -> new Exercise(new ExerciseName("Exercise" + index),
                        new ExerciseDescription("Description " + index)))
                .toList();
    }
}
