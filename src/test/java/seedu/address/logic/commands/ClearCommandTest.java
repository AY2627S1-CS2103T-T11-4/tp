package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalStudents.getTypicalBrotherGym;

import org.junit.jupiter.api.Test;

import seedu.address.model.BrotherGym;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class ClearCommandTest {

    @Test
    public void execute_emptyBrotherGym_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyBrotherGym_success() {
        Model model = new ModelManager(getTypicalBrotherGym(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalBrotherGym(), new UserPrefs());
        expectedModel.setBrotherGym(new BrotherGym());

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

}
