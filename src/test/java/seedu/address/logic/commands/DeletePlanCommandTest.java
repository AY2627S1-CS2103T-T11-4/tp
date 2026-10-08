package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.WorkoutPlansList;
import seedu.address.model.workoutplan.WorkoutTitle;

/**
 * Contains unit and integration test structure for {@link DeletePlanCommand}.
 *
 * Contains execution, validation, equality, and formatting tests.
 */
public class DeletePlanCommandTest {

    private static final WorkoutPlan FIRST_PLAN = new WorkoutPlan(
            new WorkoutTitle("Strength"), new WorkoutDescription("Upper body"), new WorkoutDate(new Date(0)));
    private static final WorkoutPlan SECOND_PLAN = new WorkoutPlan(
            new WorkoutTitle("Cardio"), new WorkoutDescription("Running"), new WorkoutDate(new Date(1)));

    @Test
    public void constructor_nullIndex_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new DeletePlanCommand(null));
    }

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
        DeletePlanCommand command = new DeletePlanCommand(Index.fromOneBased(1));
        command.setCurrentStudent(studentWithPlans);

        Student updatedStudent = new Student(baseStudent.getName(), baseStudent.getPhone(), baseStudent.getEmail(),
                baseStudent.getAddress(), baseStudent.getTags(), new WorkoutPlansList(List.of(SECOND_PLAN)));
        Model expectedModel = new ModelManager(model.getBrotherGym(), new UserPrefs());
        expectedModel.setStudent(studentWithPlans, updatedStudent);

        assertCommandSuccess(command, model, String.format(DeletePlanCommand.MESSAGE_SUCCESS, FIRST_PLAN),
                expectedModel);
    }

    @Test
    public void execute_noCurrentStudent_throwsCommandException() {
        Model model = new ModelManager(getTypicalBrotherGym(), new UserPrefs());
        DeletePlanCommand command = new DeletePlanCommand(Index.fromOneBased(1));

        assertCommandFailure(command, model, DeletePlanCommand.MESSAGE_NO_STUDENT_SELECTED);
    }

    @Test
    public void execute_invalidWorkoutId_throwsCommandException() {
        Student baseStudent = new ModelManager(getTypicalBrotherGym(), new UserPrefs())
                .getFilteredStudentList().get(0);
        Student studentWithPlan = new Student(baseStudent.getName(), baseStudent.getPhone(), baseStudent.getEmail(),
                baseStudent.getAddress(), baseStudent.getTags(), new WorkoutPlansList(List.of(FIRST_PLAN)));
        BrotherGym brotherGym = new BrotherGym();
        brotherGym.addStudent(studentWithPlan);
        Model model = new ModelManager(brotherGym, new UserPrefs());
        DeletePlanCommand command = new DeletePlanCommand(Index.fromOneBased(2));
        command.setCurrentStudent(studentWithPlan);

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
