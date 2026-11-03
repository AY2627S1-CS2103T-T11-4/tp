package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalStudents.getTypicalBrotherGym;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.model.BrotherGym;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.student.Student;
import seedu.address.model.workoutplan.WorkoutDate;
import seedu.address.model.workoutplan.WorkoutDescription;
import seedu.address.model.workoutplan.WorkoutName;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.WorkoutPlansList;

/**
 * Contains unit and integration test structure for {@link DeletePlanCommand}.
 *
 * Contains execution, validation, equality, and formatting tests.
 */
public class DeletePlanCommandTest {

    private static final WorkoutPlan FIRST_PLAN = new WorkoutPlan(
            new WorkoutName("Strength"), new WorkoutDescription("Upper body"), new WorkoutDate(new Date(0)));
    private static final WorkoutPlan SECOND_PLAN = new WorkoutPlan(
            new WorkoutName("Cardio"), new WorkoutDescription("Running"), new WorkoutDate(new Date(1)));

    // EP: workout-plan index is null (invalid).
    @Test
    public void constructor_nullIndex_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new DeletePlanCommand(null));
    }

    // EP: workout-plan index exists (valid).
    @Test
    public void execute_validWorkoutId_deletesPlan() {
        Student baseStudent = new ModelManager(getTypicalBrotherGym(), new UserPrefs())
                .getFilteredStudentList().get(0);
        Student studentWithPlans = new Student(baseStudent.getName(), baseStudent.getPhone(), baseStudent.getEmail(),
                baseStudent.getAddress(), baseStudent.getTags(),
                new WorkoutPlansList(List.of(FIRST_PLAN, SECOND_PLAN)));
        BrotherGym brotherGym = new BrotherGym();
        brotherGym.addStudent(studentWithPlans);
        Model model = new ModelManager(brotherGym, new UserPrefs());
        model.setCurrentStudent(studentWithPlans);
        model.setCurrentWorkoutPlan(FIRST_PLAN);
        DeletePlanCommand command = new DeletePlanCommand(Index.fromOneBased(1));

        Student updatedStudent = new Student(baseStudent.getName(), baseStudent.getPhone(), baseStudent.getEmail(),
                baseStudent.getAddress(), baseStudent.getTags(), new WorkoutPlansList(List.of(SECOND_PLAN)));
        Model expectedModel = new ModelManager(model.getBrotherGym(), new UserPrefs());
        expectedModel.setStudent(studentWithPlans, updatedStudent);

        assertCommandSuccess(command, model, String.format(DeletePlanCommand.MESSAGE_SUCCESS, FIRST_PLAN),
                expectedModel);
        assertEquals(updatedStudent, model.getCurrentStudent().orElseThrow());
        assertTrue(model.getCurrentWorkoutPlan().isEmpty());
    }

    // EP: deleting another plan keeps the selected workout plan (valid).
    @Test
    public void execute_otherPlanDeleted_keepsSelectedPlan() throws Exception {
        Student baseStudent = new ModelManager(getTypicalBrotherGym(), new UserPrefs())
                .getFilteredStudentList().get(0);
        Student studentWithPlans = new Student(baseStudent.getName(), baseStudent.getPhone(), baseStudent.getEmail(),
                baseStudent.getAddress(), baseStudent.getTags(),
                new WorkoutPlansList(List.of(FIRST_PLAN, SECOND_PLAN)));
        BrotherGym brotherGym = new BrotherGym();
        brotherGym.addStudent(studentWithPlans);
        Model model = new ModelManager(brotherGym, new UserPrefs());
        model.setCurrentStudent(studentWithPlans);
        model.setCurrentWorkoutPlan(SECOND_PLAN);

        new DeletePlanCommand(Index.fromOneBased(1)).execute(model);

        assertEquals(List.of(SECOND_PLAN),
                model.getCurrentStudent().orElseThrow().getWorkoutPlansList().getWorkoutList());
        assertSame(SECOND_PLAN, model.getCurrentWorkoutPlan().orElseThrow());
    }

    // EP: no current student is selected (invalid).
    @Test
    public void execute_noCurrentStudent_throwsCommandException() {
        Model model = new ModelManager(getTypicalBrotherGym(), new UserPrefs());
        DeletePlanCommand command = new DeletePlanCommand(Index.fromOneBased(1));

        assertCommandFailure(command, model, DeletePlanCommand.MESSAGE_NO_STUDENT_SELECTED);
    }

    // BV: index > size (invalid).
    @Test
    public void execute_invalidWorkoutId_throwsCommandException() {
        Student baseStudent = new ModelManager(getTypicalBrotherGym(), new UserPrefs())
                .getFilteredStudentList().get(0);
        Student studentWithPlan = new Student(baseStudent.getName(), baseStudent.getPhone(), baseStudent.getEmail(),
                baseStudent.getAddress(), baseStudent.getTags(), new WorkoutPlansList(List.of(FIRST_PLAN)));
        BrotherGym brotherGym = new BrotherGym();
        brotherGym.addStudent(studentWithPlan);
        Model model = new ModelManager(brotherGym, new UserPrefs());
        model.setCurrentStudent(studentWithPlan);
        DeletePlanCommand command = new DeletePlanCommand(Index.fromOneBased(2));

        assertCommandFailure(command, model, DeletePlanCommand.MESSAGE_INVALID_WORKOUT_ID);
    }

    @Test
    public void equals() {
        DeletePlanCommand first = new DeletePlanCommand(Index.fromOneBased(1));
        DeletePlanCommand copy = new DeletePlanCommand(Index.fromOneBased(1));
        DeletePlanCommand different = new DeletePlanCommand(Index.fromOneBased(2));

        assertTrue(first.equals(first));
        assertTrue(first.equals(copy));
        assertFalse(first.equals(different));
        assertFalse(first.equals(null));
        assertFalse(first.equals(1));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeletePlanCommand command = new DeletePlanCommand(targetIndex);
        String expected = DeletePlanCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";

        assertEquals(expected, command.toString());
    }
}
