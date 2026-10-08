package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_STUDENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_STUDENT;
import static seedu.address.testutil.TypicalStudents.getTypicalBrotherGym;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
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
import seedu.address.testutil.TypicalStudents;

/**
 * Contains integration tests and unit tests for {@link ViewStudentCommand}.
 */
public class ViewStudentCommandTest {

    private final Model model = new ModelManager(getTypicalBrotherGym(), new UserPrefs());

    @Test
    public void execute_validIndexNoWorkoutPlans_success() {
        ViewStudentCommand command = new ViewStudentCommand(INDEX_FIRST_STUDENT);
        String expectedMessage = "Viewing plans for Alice Pauline:\n"
                + ViewStudentCommand.MESSAGE_NO_WORKOUTS;

        Model expectedModel = new ModelManager(model.getBrotherGym(), new UserPrefs());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndexWithWorkoutPlans_success() {
        WorkoutPlan plan = new WorkoutPlan(new WorkoutTitle("Strength"),
                new WorkoutDescription("Upper body"), new WorkoutDate(new Date(0)));
        Student baseStudent = TypicalStudents.ALICE;
        Student studentWithPlan = new Student(baseStudent.getName(), baseStudent.getPhone(), baseStudent.getEmail(),
                baseStudent.getAddress(), baseStudent.getTags(), new WorkoutPlansList(List.of(plan)));
        BrotherGym brotherGym = new BrotherGym();
        brotherGym.addStudent(studentWithPlan);
        Model modelWithPlan = new ModelManager(brotherGym, new UserPrefs());

        ViewStudentCommand command = new ViewStudentCommand(INDEX_FIRST_STUDENT);
        String expectedMessage = "Viewing plans for Alice Pauline:\n1. " + plan;
        Model expectedModel = new ModelManager(modelWithPlan.getBrotherGym(), new UserPrefs());

        assertCommandSuccess(command, modelWithPlan, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredStudentList().size() + 1);
        ViewStudentCommand command = new ViewStudentCommand(outOfBoundIndex);

        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
    }

    @Test
    public void constructor_nullIndex_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ViewStudentCommand(null));
    }

    @Test
    public void equals() {
        ViewStudentCommand firstCommand = new ViewStudentCommand(INDEX_FIRST_STUDENT);
        ViewStudentCommand secondCommand = new ViewStudentCommand(INDEX_SECOND_STUDENT);

        assertTrue(firstCommand.equals(firstCommand));
        assertTrue(firstCommand.equals(new ViewStudentCommand(INDEX_FIRST_STUDENT)));
        assertFalse(firstCommand.equals(secondCommand));
        assertFalse(firstCommand.equals(null));
        assertFalse(firstCommand.equals(1));
    }

    @Test
    public void toStringMethod() {
        ViewStudentCommand command = new ViewStudentCommand(INDEX_FIRST_STUDENT);
        String expected = ViewStudentCommand.class.getCanonicalName()
                + "{targetIndex=" + INDEX_FIRST_STUDENT + "}";

        assertEquals(expected, command.toString());
    }
}
