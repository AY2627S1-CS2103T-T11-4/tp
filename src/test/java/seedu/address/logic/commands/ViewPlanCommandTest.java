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

/** Contains unit and integration tests for {@link ViewPlanCommand}. */
public class ViewPlanCommandTest {

    private static final WorkoutPlan FIRST_PLAN = new WorkoutPlan(
            new WorkoutTitle("Strength"), new WorkoutDescription("Upper body"), new WorkoutDate(new Date(0)));
    private static final WorkoutPlan SECOND_PLAN = new WorkoutPlan(
            new WorkoutTitle("Cardio"), new WorkoutDescription("Running"), new WorkoutDate(new Date(1)));

    @Test
    public void constructor_nullIndex_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ViewPlanCommand(null));
    }

    @Test
    public void execute_validWorkoutId_returnsSelectedPlan() {
        Student studentWithPlans = createStudentWithPlans();
        Model model = createModel(studentWithPlans);
        ViewPlanCommand command = new ViewPlanCommand(Index.fromOneBased(2));
        command.setCurrentStudent(studentWithPlans);

        assertCommandSuccess(command, model,
                String.format(ViewPlanCommand.MESSAGE_SUCCESS, SECOND_PLAN), model);
        assertEquals(studentWithPlans, command.getCurrentStudent().orElseThrow());
    }

    @Test
    public void execute_noCurrentStudent_throwsCommandException() {
        Model model = new ModelManager(getTypicalBrotherGym(), new UserPrefs());
        ViewPlanCommand command = new ViewPlanCommand(Index.fromOneBased(1));

        assertCommandFailure(command, model, ViewPlanCommand.MESSAGE_NO_STUDENT_SELECTED);
    }

    @Test
    public void execute_invalidWorkoutId_throwsCommandException() {
        Student studentWithPlans = createStudentWithPlans();
        Model model = createModel(studentWithPlans);
        ViewPlanCommand command = new ViewPlanCommand(Index.fromOneBased(3));
        command.setCurrentStudent(studentWithPlans);

        assertCommandFailure(command, model, ViewPlanCommand.MESSAGE_INVALID_WORKOUT_ID);
    }

    @Test
    public void equals() {
        ViewPlanCommand first = new ViewPlanCommand(Index.fromOneBased(1));
        ViewPlanCommand copy = new ViewPlanCommand(Index.fromOneBased(1));
        ViewPlanCommand different = new ViewPlanCommand(Index.fromOneBased(2));

        assertTrue(first.equals(first));
        assertTrue(first.equals(copy));
        assertFalse(first.equals(different));
        assertFalse(first.equals(null));
        assertFalse(first.equals(1));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        ViewPlanCommand command = new ViewPlanCommand(targetIndex);
        String expected = ViewPlanCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";

        assertEquals(expected, command.toString());
    }

    private static Student createStudentWithPlans() {
        Student baseStudent = new ModelManager(getTypicalBrotherGym(), new UserPrefs())
                .getFilteredStudentList().get(0);
        return new Student(baseStudent.getName(), baseStudent.getPhone(), baseStudent.getEmail(),
                baseStudent.getAddress(), baseStudent.getTags(),
                new WorkoutPlansList(List.of(FIRST_PLAN, SECOND_PLAN)));
    }

    private static Model createModel(Student student) {
        BrotherGym brotherGym = new BrotherGym();
        brotherGym.addStudent(student);
        return new ModelManager(brotherGym, new UserPrefs());
    }
}
