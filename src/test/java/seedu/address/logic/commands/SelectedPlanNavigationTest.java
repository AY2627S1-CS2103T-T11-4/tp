package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalExercises.PUSH_UPS;
import static seedu.address.testutil.TypicalExercises.SQUATS;
import static seedu.address.testutil.TypicalStudents.ALICE;
import static seedu.address.testutil.TypicalStudents.BENSON;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditPlanCommand.EditPlanDescriptor;
import seedu.address.logic.commands.EditStudentCommand.EditStudentDescriptor;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.BrotherGym;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.exercise.Exercise;
import seedu.address.model.exercise.ExerciseDescription;
import seedu.address.model.exercise.ExercisesList;
import seedu.address.model.student.Phone;
import seedu.address.model.student.Student;
import seedu.address.model.workoutplan.WorkoutDate;
import seedu.address.model.workoutplan.WorkoutDescription;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.WorkoutPlanStatus;
import seedu.address.model.workoutplan.WorkoutPlansList;
import seedu.address.model.workoutplan.WorkoutTitle;

/** Tests selected-plan navigation and refresh across parent and exercise changes. */
public class SelectedPlanNavigationTest {

    // EP: commands target the newly selected plan (selected plan).
    @Test
    public void viewPlan_switchesPlanAndUsesDisplayedExerciseIndexes() throws CommandException {
        WorkoutPlan first = plan("First", List.of(SQUATS));
        WorkoutPlan second = plan("Second", List.of(PUSH_UPS, SQUATS));
        Student alice = withPlans(ALICE, first, second);
        ModelManager model = modelWith(alice);
        model.setCurrentStudent(alice);

        String feedback = new ViewPlanCommand(Index.fromOneBased(2)).execute(model).getFeedbackToUser();
        assertSame(second, model.getCurrentWorkoutPlan().orElseThrow());
        assertTrue(feedback.contains("1. " + PUSH_UPS));
        assertTrue(feedback.contains("2. " + SQUATS));

        new ViewPlanCommand(Index.fromOneBased(1)).execute(model);
        assertSame(first, model.getCurrentWorkoutPlan().orElseThrow());
        assertThrows(CommandException.class, () -> new ViewPlanCommand(Index.fromOneBased(3)).execute(model));
        assertSame(first, model.getCurrentWorkoutPlan().orElseThrow());
    }

    // EP: selected plan rebinds to its replacement (updated parent).
    @Test
    public void exerciseAndParentUpdates_refreshSelectedPlan() throws CommandException {
        WorkoutPlan first = plan("First", List.of(SQUATS));
        Student alice = withPlans(ALICE, first);
        ModelManager model = modelWith(alice);
        model.setCurrentStudent(alice);
        model.setCurrentWorkoutPlan(first);
        AtomicInteger selectionChanges = new AtomicInteger();
        model.currentWorkoutPlanProperty().addListener((observable, oldPlan, newPlan) ->
                selectionChanges.incrementAndGet());

        model.addExerciseForWorkoutPlan(alice, first, PUSH_UPS);
        assertEquals(List.of(SQUATS, PUSH_UPS), selectedExercises(model));
        WorkoutPlan afterAdd = model.getCurrentWorkoutPlan().orElseThrow();
        Exercise edited = new Exercise(PUSH_UPS.getName(), new ExerciseDescription("Updated description"));
        model.setExerciseForWorkoutPlan(model.getCurrentStudent().orElseThrow(), afterAdd, PUSH_UPS, edited);
        assertEquals(List.of(SQUATS, edited), selectedExercises(model));
        WorkoutPlan afterEdit = model.getCurrentWorkoutPlan().orElseThrow();
        model.removeExerciseForWorkoutPlan(model.getCurrentStudent().orElseThrow(), afterEdit, SQUATS);
        model.removeExerciseForWorkoutPlan(model.getCurrentStudent().orElseThrow(),
                model.getCurrentWorkoutPlan().orElseThrow(), edited);
        assertTrue(selectedExercises(model).isEmpty());

        EditPlanDescriptor planChanges = new EditPlanDescriptor();
        planChanges.setDescription(new WorkoutDescription("Updated notes"));
        new EditPlanCommand(Index.fromOneBased(1), planChanges).execute(model);
        assertEquals("Updated notes", model.getCurrentWorkoutPlan().orElseThrow().getWorkouts().getValue());

        EditStudentDescriptor studentChanges = new EditStudentDescriptor();
        studentChanges.setPhone(new Phone("91234567"));
        new EditStudentCommand(Index.fromOneBased(1), studentChanges).execute(model);
        assertTrue(model.getCurrentWorkoutPlan().isPresent());

        new MarkPlanCommand(Index.fromOneBased(1)).execute(model);
        assertEquals(WorkoutPlanStatus.COMPLETED, model.getCurrentWorkoutPlan().orElseThrow().getStatus());
        new UnmarkPlanCommand(Index.fromOneBased(1)).execute(model);
        assertTrue(model.getCurrentWorkoutPlan().isPresent());
        assertTrue(selectionChanges.get() >= 6);
    }

    // EP: stale plan selection is cleared.
    @Test
    public void navigationAndFiltering_clearStaleSelection() throws CommandException {
        WorkoutPlan first = plan("First", List.of(SQUATS));
        WorkoutPlan second = plan("Second", List.of(PUSH_UPS));
        Student alice = withPlans(ALICE, first);
        Student benson = withPlans(BENSON, second);
        ModelManager model = modelWith(alice, benson);
        model.setCurrentStudent(alice);
        model.setCurrentWorkoutPlan(first);

        new ViewStudentCommand(Index.fromOneBased(2)).execute(model);
        assertSame(benson, model.getCurrentStudent().orElseThrow());
        assertTrue(model.getCurrentWorkoutPlan().isEmpty());
        model.setCurrentWorkoutPlan(second);
        model.updateFilteredStudentList(student -> student == alice);
        assertTrue(model.getCurrentStudent().isEmpty());
        assertTrue(model.getCurrentWorkoutPlan().isEmpty());

        model.updateFilteredStudentList(student -> true);
        model.setCurrentStudent(alice);
        model.setCurrentWorkoutPlan(first);
        new HomeCommand().execute(model);
        assertTrue(model.getCurrentStudent().isEmpty());
        assertTrue(model.getCurrentWorkoutPlan().isEmpty());

        model.setCurrentStudent(alice);
        model.setCurrentWorkoutPlan(first);
        new ClearCommand().execute(model);
        assertTrue(model.getCurrentStudent().isEmpty());
        assertTrue(model.getCurrentWorkoutPlan().isEmpty());
    }

    // EP: only affected selection is cleared (removed parent).
    @Test
    public void parentDeletionAndReplacement_preserveOrClearCorrectPlan() throws CommandException {
        WorkoutPlan first = plan("First", List.of(SQUATS));
        WorkoutPlan second = plan("Second", List.of(PUSH_UPS));
        Student alice = withPlans(ALICE, first, second);
        ModelManager model = modelWith(alice);
        model.setCurrentStudent(alice);
        model.setCurrentWorkoutPlan(second);

        new DeletePlanCommand(Index.fromOneBased(1)).execute(model);
        assertSame(second, model.getCurrentWorkoutPlan().orElseThrow());
        new DeletePlanCommand(Index.fromOneBased(1)).execute(model);
        assertTrue(model.getCurrentWorkoutPlan().isEmpty());

        Student current = model.getCurrentStudent().orElseThrow();
        WorkoutPlan replacement = plan("Replacement", List.of(SQUATS));
        model.addWorkoutPlanForStudent(current, replacement);
        model.setCurrentWorkoutPlan(replacement);
        Student stored = model.getCurrentStudent().orElseThrow();
        model.setStudent(stored, withPlans(stored, plan("Unrelated", List.of(PUSH_UPS))));
        assertTrue(model.getCurrentWorkoutPlan().isEmpty());
        assertThrows(IllegalArgumentException.class, () -> model.setCurrentWorkoutPlan(replacement));

        new DeleteStudentCommand(Index.fromOneBased(1)).execute(model);
        assertTrue(model.getCurrentStudent().isEmpty());
        assertTrue(model.getCurrentWorkoutPlan().isEmpty());
    }

    // EP: selection rebinds to the refreshed plan (refreshed parent).
    @Test
    public void dateRefresh_rebindsSelectedPlan() {
        WorkoutPlan overdue = new WorkoutPlan(new WorkoutTitle("Past plan"),
                new WorkoutDescription("Notes"), WorkoutDate.fromLocalDate(LocalDate.now().minusDays(1)),
                Set.of(), WorkoutPlanStatus.PENDING, new ExercisesList(List.of(SQUATS)));
        Student alice = withPlans(ALICE, overdue);
        ModelManager model = modelWith(alice);
        model.setCurrentStudent(alice);
        model.setCurrentWorkoutPlan(overdue);

        model.updateStudentWorkoutsState(alice);

        assertEquals(WorkoutPlanStatus.OVERDUE, model.getCurrentWorkoutPlan().orElseThrow().getStatus());
        assertEquals(List.of(SQUATS), selectedExercises(model));
    }

    private static List<Exercise> selectedExercises(ModelManager model) {
        return model.getCurrentWorkoutPlan().orElseThrow().getExercisesList().getExerciseList();
    }

    private static WorkoutPlan plan(String title, List<Exercise> exercises) {
        return new WorkoutPlan(new WorkoutTitle(title), new WorkoutDescription("Notes"),
                WorkoutDate.fromLocalDate(LocalDate.now().plusDays(1)), Set.of(),
                WorkoutPlanStatus.PENDING, new ExercisesList(exercises));
    }

    private static Student withPlans(Student base, WorkoutPlan... plans) {
        return new Student(base.getName(), base.getPhone(), base.getEmail(), base.getAddress(),
                base.getTags(), new WorkoutPlansList(List.of(plans)));
    }

    private static ModelManager modelWith(Student... students) {
        BrotherGym data = new BrotherGym();
        for (Student student : students) {
            data.addStudent(student);
        }
        return new ModelManager(data, new UserPrefs());
    }
}
