package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalStudents.getTypicalBrotherGym;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.exercise.Exercise;
import seedu.address.model.exercise.ExerciseDescription;
import seedu.address.model.exercise.ExerciseName;
import seedu.address.model.exercise.ExercisesList;
import seedu.address.model.student.Student;
import seedu.address.model.workoutplan.WorkoutDate;
import seedu.address.model.workoutplan.WorkoutDescription;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.WorkoutTitle;

/** Tests adding an exercise to the selected workout plan. */
public class AddExerciseCommandTest {
    private static final Exercise EXERCISE = new Exercise(new ExerciseName("Squats"),
            new ExerciseDescription("Barbell back squat"));
    private static final WorkoutPlan PLAN = new WorkoutPlan(new WorkoutTitle("Leg day"),
            new WorkoutDescription("Lower body"), WorkoutDate.fromLocalDate(LocalDate.of(2026, 10, 29)));

    @Test
    public void constructor_nullExercise_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddExerciseCommand(null));
    }

    @Test
    public void execute_validSelectedPlan_addsExercise() {
        Model model = createModelWithSelectedPlan();
        Model expectedModel = new ModelManager(model.getBrotherGym(), new UserPrefs());
        Student expectedStudent = expectedModel.getFilteredStudentList().get(0);
        WorkoutPlan expectedPlan = expectedStudent.getWorkoutPlansList().getWorkoutList().get(0);
        expectedModel.addExerciseForWorkoutPlan(expectedStudent, expectedPlan, EXERCISE);

        assertCommandSuccess(new AddExerciseCommand(EXERCISE), model,
                String.format(AddExerciseCommand.MESSAGE_SUCCESS, EXERCISE), expectedModel);
        assertEquals(List.of(EXERCISE), model.getCurrentWorkoutPlan().orElseThrow()
                .getExercisesList().getExerciseList());
    }

    @Test
    public void execute_noSelectedPlan_throwsCommandException() {
        Model model = new ModelManager(getTypicalBrotherGym(), new UserPrefs());
        assertCommandFailure(new AddExerciseCommand(EXERCISE), model,
                AddExerciseCommand.MESSAGE_NO_PLAN_SELECTED);
        Student student = model.getFilteredStudentList().get(0);
        model.setCurrentStudent(student);
        model.addWorkoutPlanForStudent(student, PLAN);
        assertCommandFailure(new AddExerciseCommand(EXERCISE), model,
                AddExerciseCommand.MESSAGE_NO_PLAN_SELECTED);
    }

    @Test
    public void execute_duplicateExercise_throwsCommandException() throws Exception {
        Model model = createModelWithSelectedPlan();
        AddExerciseCommand command = new AddExerciseCommand(EXERCISE);
        command.execute(model);
        assertCommandFailure(command, model, AddExerciseCommand.MESSAGE_DUPLICATE_EXERCISE);
    }

    @Test
    public void execute_fullExerciseList_throwsCommandException() {
        Model model = createModelWithSelectedPlan();
        for (int i = 0; i < ExercisesList.MAX_EXERCISES; i++) {
            Student student = model.getCurrentStudent().orElseThrow();
            WorkoutPlan plan = model.getCurrentWorkoutPlan().orElseThrow();
            model.addExerciseForWorkoutPlan(student, plan,
                    new Exercise(new ExerciseName("Exercise " + i), new ExerciseDescription("Description " + i)));
        }
        assertCommandFailure(new AddExerciseCommand(EXERCISE), model,
                AddExerciseCommand.MESSAGE_EXERCISE_LIST_FULL);
    }

    @Test
    public void equals() {
        AddExerciseCommand first = new AddExerciseCommand(EXERCISE);
        AddExerciseCommand copy = new AddExerciseCommand(new Exercise(new ExerciseName("Squats"),
                new ExerciseDescription("Barbell back squat")));
        AddExerciseCommand different = new AddExerciseCommand(new Exercise(new ExerciseName("Deadlift"),
                new ExerciseDescription("Barbell deadlift")));
        assertTrue(first.equals(first));
        assertTrue(first.equals(copy));
        assertFalse(first.equals(different));
        assertFalse(first.equals(null));
        assertFalse(first.equals(1));
    }

    @Test
    public void toStringMethod() {
        AddExerciseCommand command = new AddExerciseCommand(EXERCISE);
        assertEquals(AddExerciseCommand.class.getCanonicalName() + "{toAdd=" + EXERCISE + "}",
                command.toString());
    }

    private static Model createModelWithSelectedPlan() {
        Model model = new ModelManager(getTypicalBrotherGym(), new UserPrefs());
        Student student = model.getFilteredStudentList().get(0);
        model.setCurrentStudent(student);
        model.addWorkoutPlanForStudent(student, PLAN);
        model.setCurrentWorkoutPlan(model.getCurrentStudent().orElseThrow()
                .getWorkoutPlansList().getWorkoutList().get(0));
        return model;
    }
}
