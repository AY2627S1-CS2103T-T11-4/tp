package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalExercises.LUNGES;
import static seedu.address.testutil.TypicalExercises.PUSH_UPS;
import static seedu.address.testutil.TypicalExercises.SQUATS;
import static seedu.address.testutil.TypicalStudents.ALICE;
import static seedu.address.testutil.TypicalStudents.BENSON;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.BrotherGym;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.exercise.Exercise;
import seedu.address.model.exercise.ExercisesList;
import seedu.address.model.student.Student;
import seedu.address.model.workoutplan.WorkoutDate;
import seedu.address.model.workoutplan.WorkoutDescription;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.WorkoutPlanStatus;
import seedu.address.model.workoutplan.WorkoutPlansList;
import seedu.address.model.workoutplan.WorkoutTitle;

/** Tests exercise deletion through the selected student and workout plan. */
public class DeleteExerciseCommandTest {

    @Test
    public void execute_firstMiddleLastExercise_removesOnlyRequestedExercise() throws CommandException {
        List<Exercise> original = List.of(SQUATS, PUSH_UPS, LUNGES);
        for (int index = 0; index < original.size(); index++) {
            WorkoutPlan selectedPlan = plan("Selected", original);
            WorkoutPlan otherPlan = plan("Other", List.of(SQUATS));
            Student alice = withPlans(ALICE, selectedPlan, otherPlan);
            Student benson = withPlans(BENSON, plan("Benson", List.of(LUNGES)));
            ModelManager model = modelWith(alice, benson);
            model.setCurrentStudent(alice);
            model.setCurrentWorkoutPlan(selectedPlan);

            Exercise deleted = original.get(index);
            CommandResult result = new DeleteExerciseCommand(Index.fromOneBased(index + 1)).execute(model);
            List<Exercise> expected = new ArrayList<>(original);
            expected.remove(index);

            assertEquals(String.format(DeleteExerciseCommand.MESSAGE_SUCCESS, deleted), result.getFeedbackToUser());
            assertEquals(expected, model.getCurrentWorkoutPlan().orElseThrow().getExercisesList().getExerciseList());
            assertSame(otherPlan, model.getCurrentStudent().orElseThrow()
                    .getWorkoutPlansList().getWorkoutPlan(1));
            assertSame(benson, model.getBrotherGym().getStudentList().get(1));
            assertEquals(original, selectedPlan.getExercisesList().getExerciseList());
        }
    }

    @Test
    public void execute_onlyExercise_leavesEmptySelectedPlan() throws CommandException {
        WorkoutPlan plan = plan("Selected", List.of(SQUATS));
        Student student = withPlans(ALICE, plan);
        ModelManager model = modelWith(student);
        model.setCurrentStudent(student);
        model.setCurrentWorkoutPlan(plan);

        new DeleteExerciseCommand(Index.fromOneBased(1)).execute(model);

        assertTrue(model.getCurrentWorkoutPlan().orElseThrow().getExercisesList().getExerciseList().isEmpty());
        assertEquals(1, model.getCurrentStudent().orElseThrow().getWorkoutPlansList().getWorkoutList().size());
    }

    @Test
    public void execute_sequentialDeletions_useUpdatedNumbering() throws CommandException {
        WorkoutPlan plan = plan("Selected", List.of(SQUATS, PUSH_UPS, LUNGES));
        Student student = withPlans(ALICE, plan);
        ModelManager model = modelWith(student);
        model.setCurrentStudent(student);
        model.setCurrentWorkoutPlan(plan);

        new DeleteExerciseCommand(Index.fromOneBased(1)).execute(model);
        new DeleteExerciseCommand(Index.fromOneBased(2)).execute(model);

        assertEquals(List.of(PUSH_UPS), model.getCurrentWorkoutPlan().orElseThrow()
                .getExercisesList().getExerciseList());
    }

    @Test
    public void execute_afterViewPlan_updatesDisplayedIndexes() throws CommandException {
        WorkoutPlan plan = plan("Selected", List.of(SQUATS, PUSH_UPS, LUNGES));
        ModelManager model = modelWith(withPlans(ALICE, plan));
        new ViewStudentCommand(Index.fromOneBased(1)).execute(model);
        new ViewPlanCommand(Index.fromOneBased(1)).execute(model);

        new DeleteExerciseCommand(Index.fromOneBased(2)).execute(model);

        assertEquals(List.of(SQUATS, LUNGES), model.getCurrentWorkoutPlan().orElseThrow()
                .getExercisesList().getExerciseList());
        String feedback = new ViewPlanCommand(Index.fromOneBased(1)).execute(model).getFeedbackToUser();
        assertTrue(feedback.contains("1. " + SQUATS));
        assertTrue(feedback.contains("2. " + LUNGES));
        assertFalse(feedback.contains("3. "));
    }

    @Test
    public void execute_missingSelectionOrInvalidIndex_leavesModelUnchanged() {
        WorkoutPlan plan = plan("Selected", List.of(SQUATS));
        Student student = withPlans(ALICE, plan);
        ModelManager model = modelWith(student);
        DeleteExerciseCommand command = new DeleteExerciseCommand(Index.fromOneBased(1));

        assertEquals(DeleteExerciseCommand.MESSAGE_NO_STUDENT_SELECTED,
                assertThrows(CommandException.class, () -> command.execute(model)).getMessage());
        model.setCurrentStudent(student);
        assertEquals(DeleteExerciseCommand.MESSAGE_NO_PLAN_SELECTED,
                assertThrows(CommandException.class, () -> command.execute(model)).getMessage());
        model.setCurrentWorkoutPlan(plan);
        assertEquals(DeleteExerciseCommand.MESSAGE_INVALID_EXERCISE_ID,
                assertThrows(CommandException.class, () ->
                        new DeleteExerciseCommand(Index.fromOneBased(2)).execute(model)).getMessage());
        assertSame(student, model.getCurrentStudent().orElseThrow());
        assertSame(plan, model.getCurrentWorkoutPlan().orElseThrow());
        assertEquals(List.of(SQUATS), plan.getExercisesList().getExerciseList());
    }

    @Test
    public void execute_afterSelectingDifferentPlan_targetsNewPlanOnly() throws CommandException {
        WorkoutPlan first = plan("First", List.of(SQUATS));
        WorkoutPlan second = plan("Second", List.of(PUSH_UPS));
        Student student = withPlans(ALICE, first, second);
        ModelManager model = modelWith(student);
        model.setCurrentStudent(student);
        model.setCurrentWorkoutPlan(first);
        model.setCurrentWorkoutPlan(second);

        new DeleteExerciseCommand(Index.fromOneBased(1)).execute(model);

        assertSame(first, model.getCurrentStudent().orElseThrow().getWorkoutPlansList().getWorkoutPlan(0));
        assertTrue(model.getCurrentWorkoutPlan().orElseThrow().getExercisesList().getExerciseList().isEmpty());
    }

    @Test
    public void execute_afterSwitchingStudent_requiresNewPlanSelection() {
        WorkoutPlan first = plan("First", List.of(SQUATS));
        WorkoutPlan second = plan("Second", List.of(PUSH_UPS));
        Student alice = withPlans(ALICE, first);
        Student benson = withPlans(BENSON, second);
        ModelManager model = modelWith(alice, benson);
        model.setCurrentStudent(alice);
        model.setCurrentWorkoutPlan(first);
        model.setCurrentStudent(benson);

        assertEquals(DeleteExerciseCommand.MESSAGE_NO_PLAN_SELECTED,
                assertThrows(CommandException.class, () ->
                        new DeleteExerciseCommand(Index.fromOneBased(1)).execute(model)).getMessage());
        assertSame(first, alice.getWorkoutPlansList().getWorkoutPlan(0));
        assertSame(second, benson.getWorkoutPlansList().getWorkoutPlan(0));
    }

    @Test
    public void constructor_nullIndex_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new DeleteExerciseCommand(null));
    }

    @Test
    public void equals() {
        DeleteExerciseCommand first = new DeleteExerciseCommand(Index.fromOneBased(1));
        DeleteExerciseCommand copy = new DeleteExerciseCommand(Index.fromOneBased(1));
        DeleteExerciseCommand different = new DeleteExerciseCommand(Index.fromOneBased(2));

        assertTrue(first.equals(first));
        assertTrue(first.equals(copy));
        assertFalse(first.equals(different));
        assertFalse(first.equals(null));
        assertFalse(first.equals(1));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteExerciseCommand command = new DeleteExerciseCommand(targetIndex);
        String expected = DeleteExerciseCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";

        assertEquals(expected, command.toString());
    }

    private static WorkoutPlan plan(String title, List<Exercise> exercises) {
        return new WorkoutPlan(new WorkoutTitle(title), new WorkoutDescription("Plan notes"),
                new WorkoutDate(new Date(0)), Set.of(), WorkoutPlanStatus.PENDING,
                new ExercisesList(exercises));
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
