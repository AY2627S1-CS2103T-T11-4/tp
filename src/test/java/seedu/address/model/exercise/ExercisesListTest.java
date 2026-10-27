package seedu.address.model.exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.exercise.exceptions.DuplicateExerciseException;
import seedu.address.model.exercise.exceptions.ExerciseIndexOutOfBoundsException;
import seedu.address.model.exercise.exceptions.ExerciseNotFoundException;

public class ExercisesListTest {

    private static final Exercise FIRST = new Exercise("Squats", "Barbell back squat", 10);
    private static final Exercise SECOND = new Exercise("Push-ups", "Standard push-up", 15);
    private static final Exercise THIRD = new Exercise("Lunges", "Alternating forward lunges", 12);

    @Test
    public void constructors_createEmptyListAndDefensivelyCopyInput() {
        assertEquals(List.of(), new ExercisesList().getExerciseList());

        List<Exercise> input = new ArrayList<>(List.of(FIRST));
        ExercisesList exercises = new ExercisesList(input);
        input.add(SECOND);

        assertEquals(List.of(FIRST), exercises.getExerciseList());
        assertThrows(UnsupportedOperationException.class, () ->
                exercises.getExerciseList().add(SECOND));
    }

    @Test
    public void constructor_nullOrDuplicateValues_throwsException() {
        assertThrows(NullPointerException.class, () -> new ExercisesList(null));

        List<Exercise> inputWithNull = new ArrayList<>(List.of(FIRST));
        inputWithNull.add(null);
        assertThrows(NullPointerException.class, () -> new ExercisesList(inputWithNull));

        Exercise equalFirst = new Exercise("Squats", "Barbell back squat", 10);
        assertThrows(DuplicateExerciseException.class, () ->
                new ExercisesList(List.of(FIRST, equalFirst)));
    }

    @Test
    public void getExercise_returnsExerciseInOrder() {
        ExercisesList exercises = new ExercisesList(List.of(FIRST, SECOND));

        assertSame(FIRST, exercises.getExercise(0));
        assertSame(SECOND, exercises.getExercise(1));
    }

    @Test
    public void getExercise_invalidIndex_throwsException() {
        ExercisesList exercises = new ExercisesList(List.of(FIRST));

        assertThrows(ExerciseIndexOutOfBoundsException.class, () -> exercises.getExercise(-1));
        assertThrows(ExerciseIndexOutOfBoundsException.class, () -> exercises.getExercise(1));
    }

    @Test
    public void addExercise_appendsWithoutChangingOriginal() {
        ExercisesList original = new ExercisesList(List.of(FIRST));

        ExercisesList updated = original.addExercise(SECOND);

        assertEquals(List.of(FIRST), original.getExerciseList());
        assertEquals(List.of(FIRST, SECOND), updated.getExerciseList());
    }

    @Test
    public void addExercise_nullOrDuplicate_throwsException() {
        ExercisesList exercises = new ExercisesList(List.of(FIRST));

        assertThrows(NullPointerException.class, () -> exercises.addExercise(null));
        assertThrows(DuplicateExerciseException.class, () -> exercises.addExercise(FIRST));
    }

    @Test
    public void setExercise_replacesTargetAndPreservesPosition() {
        ExercisesList original = new ExercisesList(List.of(FIRST, SECOND));

        ExercisesList updated = original.setExercise(FIRST, THIRD);

        assertEquals(List.of(FIRST, SECOND), original.getExerciseList());
        assertEquals(List.of(THIRD, SECOND), updated.getExerciseList());
    }

    @Test
    public void setExercise_equalTargetAndReplacement_returnsOriginal() {
        ExercisesList original = new ExercisesList(List.of(FIRST, SECOND));
        Exercise equalFirst = new Exercise("Squats", "Barbell back squat", 10);

        assertSame(original, original.setExercise(equalFirst, equalFirst));
    }

    @Test
    public void setExercise_nullMissingOrDuplicateReplacement_throwsException() {
        ExercisesList exercises = new ExercisesList(List.of(FIRST, SECOND));
        Exercise missing = new Exercise("Deadlift", "Conventional deadlift", 8);

        assertThrows(NullPointerException.class, () -> exercises.setExercise(null, THIRD));
        assertThrows(NullPointerException.class, () -> exercises.setExercise(FIRST, null));
        assertThrows(ExerciseNotFoundException.class, () -> exercises.setExercise(missing, THIRD));
        assertThrows(DuplicateExerciseException.class, () -> exercises.setExercise(FIRST, SECOND));
    }

    @Test
    public void removeExercise_removesTargetAndPreservesRemainingOrder() {
        ExercisesList original = new ExercisesList(List.of(FIRST, SECOND, THIRD));

        ExercisesList updated = original.removeExercise(SECOND);

        assertEquals(List.of(FIRST, SECOND, THIRD), original.getExerciseList());
        assertEquals(List.of(FIRST, THIRD), updated.getExerciseList());
    }

    @Test
    public void removeExercise_nullOrMissing_throwsException() {
        ExercisesList exercises = new ExercisesList(List.of(FIRST));
        Exercise missing = new Exercise("Deadlift", "Conventional deadlift", 8);

        assertThrows(NullPointerException.class, () -> exercises.removeExercise(null));
        assertThrows(ExerciseNotFoundException.class, () -> exercises.removeExercise(missing));
    }

    @Test
    public void equalsAndHashCode() {
        ExercisesList first = new ExercisesList(List.of(FIRST, SECOND));
        ExercisesList sameValues = new ExercisesList(List.of(FIRST, SECOND));
        ExercisesList reversed = new ExercisesList(List.of(SECOND, FIRST));

        assertTrue(first.equals(first));
        assertTrue(first.equals(sameValues));
        assertFalse(first.equals(reversed));
        assertFalse(first.equals(null));
        assertFalse(first.equals(FIRST));
        assertEquals(first.hashCode(), sameValues.hashCode());
    }

    @Test
    public void toStringMethod_returnsUnderlyingExerciseValues() {
        ExercisesList exercises = new ExercisesList(List.of(FIRST, SECOND));

        assertEquals(List.of(FIRST, SECOND).toString(), exercises.toString());
    }
}
