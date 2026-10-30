package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalStudents.getTypicalBrotherGym;

import org.junit.jupiter.api.Test;

import seedu.address.model.BrotherGym;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class ClearCommandTest {

    // BV: count >= 0 (valid).
    @Test
    public void execute_emptyBrotherGym_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    // EP: clearing a non-empty data set (valid).
    @Test
    public void execute_nonEmptyBrotherGym_success() {
        Model model = new ModelManager(getTypicalBrotherGym(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalBrotherGym(), new UserPrefs());
        expectedModel.setBrotherGym(new BrotherGym());

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

}
