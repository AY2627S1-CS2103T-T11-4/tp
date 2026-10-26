package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertSame;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalStudents.getTypicalBrotherGym;

import org.junit.jupiter.api.Test;

import seedu.address.model.BrotherGym;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.student.Student;

public class ClearCommandTest {

    @Test
    public void execute_unconfirmedEmptyBrotherGym_requestsConfirmation() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_CONFIRMATION, expectedModel);
    }

    @Test
    public void execute_unconfirmedNonEmptyBrotherGym_preservesDataAndSelection() {
        Model model = new ModelManager(getTypicalBrotherGym(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalBrotherGym(), new UserPrefs());
        Student selected = model.getBrotherGym().getStudentList().get(0);
        model.setCurrentStudent(selected);

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_CONFIRMATION, expectedModel);
        assertSame(selected, model.getCurrentStudent().orElseThrow());
        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_CONFIRMATION, expectedModel);
    }

    @Test
    public void execute_confirmedEmptyBrotherGym_success() {
        assertCommandSuccess(new ClearCommand(true), new ModelManager(),
                ClearCommand.MESSAGE_SUCCESS, new ModelManager());
    }

    @Test
    public void execute_confirmedNonEmptyBrotherGym_success() {
        Model model = new ModelManager(getTypicalBrotherGym(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalBrotherGym(), new UserPrefs());
        expectedModel.setBrotherGym(new BrotherGym());

        assertCommandSuccess(new ClearCommand(true), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

}
