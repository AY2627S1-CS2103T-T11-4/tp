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
 * Contains unit and integration test structure for {@link AddPlanCommand}.
 *
 * Contains execution, duplicate-plan, equality, and formatting tests.
 */
public class AddPlanCommandTest {

    private static final WorkoutPlan PLAN = new WorkoutPlan(
            new WorkoutTitle("Strength"),
            new WorkoutDescription("Upper body"),
            new WorkoutDate(new Date(0)));

    @Test
    public void constructor_nullWorkoutPlan_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddPlanCommand(null));
    }

    @Test
    public void execute_validCurrentStudent_addsPlan() {
        Model model = new ModelManager(getTypicalBrotherGym(), new UserPrefs());
        Student currentStudent = model.getFilteredStudentList().get(0);
        AddPlanCommand command = new AddPlanCommand(PLAN);
        command.setCurrentStudent(currentStudent);

        Student updatedStudent = new Student(currentStudent.getName(), currentStudent.getPhone(),
                currentStudent.getEmail(), currentStudent.getAddress(), currentStudent.getTags(),
                new WorkoutPlansList(List.of(PLAN)));
        Model expectedModel = new ModelManager(model.getBrotherGym(), new UserPrefs());
        expectedModel.setStudent(currentStudent, updatedStudent);

        assertCommandSuccess(command, model, String.format(AddPlanCommand.MESSAGE_SUCCESS, PLAN), expectedModel);
    }

    @Test
    public void execute_noCurrentStudent_throwsCommandException() {
        Model model = new ModelManager(getTypicalBrotherGym(), new UserPrefs());
        AddPlanCommand command = new AddPlanCommand(PLAN);

        assertCommandFailure(command, model, AddPlanCommand.MESSAGE_NO_STUDENT_SELECTED);
    }

    @Test
    public void execute_duplicateWorkoutPlan_throwsCommandException() {
        Student baseStudent = new ModelManager(getTypicalBrotherGym(), new UserPrefs())
                .getFilteredStudentList().get(0);
        Student studentWithPlan = new Student(baseStudent.getName(), baseStudent.getPhone(), baseStudent.getEmail(),
                baseStudent.getAddress(), baseStudent.getTags(), new WorkoutPlansList(List.of(PLAN)));
        BrotherGym brotherGym = new BrotherGym();
        brotherGym.addStudent(studentWithPlan);
        Model model = new ModelManager(brotherGym, new UserPrefs());
        AddPlanCommand command = new AddPlanCommand(PLAN);
        command.setCurrentStudent(studentWithPlan);

        assertCommandFailure(command, model, AddPlanCommand.MESSAGE_DUPLICATE_PLAN);
    }

    @Test
    public void equals() {
        AddPlanCommand first = new AddPlanCommand(PLAN);
        AddPlanCommand copy = new AddPlanCommand(new WorkoutPlan(
                new WorkoutTitle("Strength"), new WorkoutDescription("Upper body"), new WorkoutDate(new Date(0))));
        AddPlanCommand different = new AddPlanCommand(new WorkoutPlan(
                new WorkoutTitle("Cardio"), new WorkoutDescription("Running"), new WorkoutDate(new Date(0))));

        assertTrue(first.equals(first));
        assertTrue(first.equals(copy));
        assertFalse(first.equals(different));
        assertFalse(first.equals(null));
        assertFalse(first.equals(1));
    }

    @Test
    public void toStringMethod() {
        AddPlanCommand command = new AddPlanCommand(PLAN);
        String expected = AddPlanCommand.class.getCanonicalName() + "{toAdd=" + PLAN + "}";

        assertEquals(expected, command.toString());
    }
}
