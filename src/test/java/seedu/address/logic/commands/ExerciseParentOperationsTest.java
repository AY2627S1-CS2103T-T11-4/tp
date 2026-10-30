package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.ExerciseTestUtil.modelWith;
import static seedu.address.testutil.ExerciseTestUtil.withPlans;
import static seedu.address.testutil.TypicalExercises.PUSH_UPS;
import static seedu.address.testutil.TypicalExercises.SQUATS;
import static seedu.address.testutil.TypicalStudents.ALICE;
import static seedu.address.testutil.TypicalStudents.BENSON;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditPlanCommand.EditPlanDescriptor;
import seedu.address.logic.commands.EditStudentCommand.EditStudentDescriptor;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ModelManager;
import seedu.address.model.exercise.Exercise;
import seedu.address.model.student.Phone;
import seedu.address.model.student.Student;
import seedu.address.model.workoutplan.WorkoutDate;
import seedu.address.model.workoutplan.WorkoutDescription;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.WorkoutPlanStatus;
import seedu.address.testutil.ExerciseTestUtil;

/** Regression tests for nested exercises during existing parent operations. */
public class ExerciseParentOperationsTest {

    // EP: parent edits preserve nested exercises (valid).
    @Test
    public void editAndStatusOperations_preserveExercisesAndSelection() throws CommandException {
        WorkoutPlan selected = plan("Selected", List.of(SQUATS, PUSH_UPS));
        WorkoutPlan other = plan("Other", List.of(PUSH_UPS));
        Student alice = withPlans(ALICE, selected, other);
        Student benson = withPlans(BENSON, plan("Benson", List.of(SQUATS)));
        ModelManager model = modelWith(alice, benson);
        model.setCurrentStudent(alice);
        model.setCurrentWorkoutPlan(selected);

        EditStudentDescriptor studentChanges = new EditStudentDescriptor();
        studentChanges.setPhone(new Phone("91234567"));
        new EditStudentCommand(Index.fromOneBased(1), studentChanges).execute(model);
        assertExercisesAndIsolation(model, benson);

        EditPlanDescriptor planChanges = new EditPlanDescriptor();
        planChanges.setDescription(new WorkoutDescription("Updated plan notes"));
        new EditPlanCommand(Index.fromOneBased(1), planChanges).execute(model);
        assertExercisesAndIsolation(model, benson);

        new MarkPlanCommand(Index.fromOneBased(1)).execute(model);
        assertEquals(WorkoutPlanStatus.COMPLETED, model.getCurrentWorkoutPlan().orElseThrow().getStatus());
        assertExercisesAndIsolation(model, benson);

        new UnmarkPlanCommand(Index.fromOneBased(1)).execute(model);
        assertExercisesAndIsolation(model, benson);

        model.updateStudentWorkoutsState(model.getCurrentStudent().orElseThrow());
        assertExercisesAndIsolation(model, benson);
    }

    // EP: date refresh preserves nested exercises (valid).
    @Test
    public void dateRefresh_preservesExercisesAndSelection() {
        WorkoutPlan overdue = ExerciseTestUtil.plan("Past plan", "Plan notes",
                WorkoutDate.fromLocalDate(LocalDate.now().minusDays(1)), List.of(SQUATS, PUSH_UPS));
        Student alice = withPlans(ALICE, overdue);
        ModelManager model = modelWith(alice);
        model.setCurrentStudent(alice);
        model.setCurrentWorkoutPlan(overdue);

        model.updateStudentWorkoutsState(alice);

        assertEquals(WorkoutPlanStatus.OVERDUE, model.getCurrentWorkoutPlan().orElseThrow().getStatus());
        assertEquals(List.of(SQUATS, PUSH_UPS), model.getCurrentWorkoutPlan().orElseThrow()
                .getExercisesList().getExerciseList());
    }

    // EP: descendant selection becomes stale (removed parent).
    @Test
    public void parentDeletionAndClear_removeOnlyDescendantsAndClearStaleSelection() throws CommandException {
        WorkoutPlan first = plan("First", List.of(SQUATS));
        WorkoutPlan second = plan("Second", List.of(PUSH_UPS));
        Student alice = withPlans(ALICE, first, second);
        Student benson = withPlans(BENSON, plan("Benson", List.of(SQUATS)));
        ModelManager model = modelWith(alice, benson);
        model.setCurrentStudent(alice);
        model.setCurrentWorkoutPlan(second);

        new DeletePlanCommand(Index.fromOneBased(1)).execute(model);
        assertSame(second, model.getCurrentWorkoutPlan().orElseThrow());
        assertEquals(List.of(PUSH_UPS), model.getCurrentWorkoutPlan().orElseThrow()
                .getExercisesList().getExerciseList());

        new DeletePlanCommand(Index.fromOneBased(1)).execute(model);
        assertTrue(model.getCurrentWorkoutPlan().isEmpty());
        assertEquals(DeleteExerciseCommand.MESSAGE_NO_PLAN_SELECTED,
                assertThrows(CommandException.class, () ->
                        new DeleteExerciseCommand(Index.fromOneBased(1)).execute(model)).getMessage());
        assertSame(benson, model.getBrotherGym().getStudentList().get(1));

        model.setCurrentStudent(benson);
        model.setCurrentWorkoutPlan(firstPlanOf(benson));
        new DeleteStudentCommand(Index.fromOneBased(2)).execute(model);
        assertTrue(model.getCurrentStudent().isEmpty());
        assertTrue(model.getCurrentWorkoutPlan().isEmpty());

        Student remaining = model.getBrotherGym().getStudentList().get(0);
        model.addWorkoutPlanForStudent(remaining, plan("Before clear", List.of(SQUATS)));
        remaining = model.getBrotherGym().getStudentList().get(0);
        model.setCurrentStudent(remaining);
        model.setCurrentWorkoutPlan(firstPlanOf(remaining));
        new ClearCommand().execute(model);
        assertTrue(model.getCurrentStudent().isEmpty());
        assertTrue(model.getCurrentWorkoutPlan().isEmpty());
        assertEquals(DeleteExerciseCommand.MESSAGE_NO_STUDENT_SELECTED,
                assertThrows(CommandException.class, () ->
                        new DeleteExerciseCommand(Index.fromOneBased(1)).execute(model)).getMessage());
    }

    private static void assertExercisesAndIsolation(ModelManager model, Student untouchedStudent) {
        assertEquals(List.of(SQUATS, PUSH_UPS), model.getCurrentWorkoutPlan().orElseThrow()
                .getExercisesList().getExerciseList());
        assertEquals(List.of(PUSH_UPS), model.getCurrentStudent().orElseThrow()
                .getWorkoutPlansList().getWorkoutPlan(1).getExercisesList().getExerciseList());
        assertSame(untouchedStudent, model.getBrotherGym().getStudentList().get(1));
    }

    private static WorkoutPlan firstPlanOf(Student student) {
        return student.getWorkoutPlansList().getWorkoutPlan(0);
    }

    private static WorkoutPlan plan(String title, List<Exercise> exercises) {
        return ExerciseTestUtil.plan(title, "Plan notes", WorkoutDate.fromLocalDate(LocalDate.of(2026, 10, 31)),
                exercises);
    }
}
