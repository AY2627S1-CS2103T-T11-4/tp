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

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

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

    @Disabled("Enable after Backend adds WorkoutPlan test fixtures")
    @Test
    public void execute_validIndexWithWorkoutPlans_success() {
        // Arrange: create a Student with one or more WorkoutPlan objects.
        // Act: execute new ViewStudentCommand(INDEX_FIRST_STUDENT).
        // Assert: the result contains the plan title and exercises.
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
