package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showStudentAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST;
import static seedu.address.testutil.TypicalStudents.getTypicalBrotherGym;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for HomeCommand.
 */
public class HomeCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalBrotherGym(), new UserPrefs());
        expectedModel = new ModelManager(model.getBrotherGym(), new UserPrefs());
    }

    // EP: unfiltered list.
    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        assertCommandSuccess(new HomeCommand(), model, HomeCommand.MESSAGE_SUCCESS, expectedModel);
    }

    // EP: filtered list.
    @Test
    public void execute_listIsFiltered_showsEverything() {
        showStudentAtIndex(model, INDEX_FIRST);
        assertCommandSuccess(new HomeCommand(), model, HomeCommand.MESSAGE_SUCCESS, expectedModel);
    }

    // EP: selected student.
    @Test
    public void execute_currentStudentSelected_clearsCurrentStudent() {
        model.setCurrentStudent(model.getFilteredStudentList().get(0));

        new HomeCommand().execute(model);

        assertTrue(model.getCurrentStudent().isEmpty());
    }
}
