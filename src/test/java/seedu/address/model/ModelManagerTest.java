package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_STUDENTS;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalStudents.ALICE;
import static seedu.address.testutil.TypicalStudents.BENSON;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.student.NameContainsKeywordsPredicate;
import seedu.address.model.student.Student;
import seedu.address.testutil.BrotherGymBuilder;
import seedu.address.testutil.StudentBuilder;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new BrotherGym(), new BrotherGym(modelManager.getBrotherGym()));
    }

    @Test
    public void constructor_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager = new ModelManager(new BrotherGym(), userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setGuiSettings(new GuiSettings(5, 6, 7, 8));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void hasStudent_nullStudent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasStudent(null));
    }

    @Test
    public void hasStudent_studentNotInBrotherGym_returnsFalse() {
        assertFalse(modelManager.hasStudent(ALICE));
    }

    @Test
    public void hasStudent_studentInBrotherGym_returnsTrue() {
        modelManager.addStudent(ALICE);
        assertTrue(modelManager.hasStudent(ALICE));
    }

    @Test
    public void getFilteredStudentList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredStudentList().remove(0));
    }

    @Test
    public void currentStudent_selectionChanges_notifiesObservers() {
        modelManager.addStudent(ALICE);
        modelManager.addStudent(BENSON);
        List<Optional<Student>> observedSelections = new ArrayList<>();
        modelManager.currentStudentProperty().addListener((observable, oldStudent, newStudent) ->
                observedSelections.add(Optional.ofNullable(newStudent)));

        assertTrue(modelManager.getCurrentStudent().isEmpty());
        modelManager.setCurrentStudent(ALICE);
        assertSame(ALICE, modelManager.getCurrentStudent().orElseThrow());
        assertSame(ALICE, modelManager.currentStudentProperty().get());

        modelManager.setCurrentStudent(BENSON);
        modelManager.clearCurrentStudent();
        assertTrue(modelManager.getCurrentStudent().isEmpty());
        assertEquals(List.of(Optional.of(ALICE), Optional.of(BENSON), Optional.empty()), observedSelections);
    }

    @Test
    public void setCurrentStudent_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setCurrentStudent(null));
    }

    @Test
    public void setStudent_selectedStudentReplaced_updatesCurrentStudent() {
        modelManager.addStudent(ALICE);
        modelManager.setCurrentStudent(ALICE);
        var editedAlice = new StudentBuilder(ALICE).withAddress("New address").build();
        List<Optional<Student>> observedSelections = new ArrayList<>();
        modelManager.currentStudentProperty().addListener((observable, oldStudent, newStudent) ->
                observedSelections.add(Optional.ofNullable(newStudent)));

        modelManager.setStudent(ALICE, editedAlice);

        assertSame(editedAlice, modelManager.getCurrentStudent().orElseThrow());
        assertEquals(List.of(Optional.of(editedAlice)), observedSelections);
    }

    @Test
    public void deleteStudent_selectedStudentDeleted_clearsCurrentStudent() {
        modelManager.addStudent(ALICE);
        modelManager.setCurrentStudent(ALICE);

        modelManager.deleteStudent(ALICE);

        assertTrue(modelManager.getCurrentStudent().isEmpty());
        assertNull(modelManager.currentStudentProperty().get());
    }

    @Test
    public void changeOtherStudent_keepsCurrentStudent() {
        modelManager.addStudent(ALICE);
        modelManager.addStudent(BENSON);
        modelManager.setCurrentStudent(ALICE);
        var editedBenson = new StudentBuilder(BENSON).withAddress("New address").build();

        modelManager.setStudent(BENSON, editedBenson);
        modelManager.deleteStudent(editedBenson);

        assertSame(ALICE, modelManager.getCurrentStudent().orElseThrow());
    }

    @Test
    public void setBrotherGym_replacesData_clearsCurrentStudent() {
        modelManager.addStudent(ALICE);
        modelManager.setCurrentStudent(ALICE);

        modelManager.setBrotherGym(new BrotherGymBuilder().withStudent(BENSON).build());

        assertTrue(modelManager.getCurrentStudent().isEmpty());
    }

    @Test
    public void equals() {
        BrotherGym brotherGym = new BrotherGymBuilder().withStudent(ALICE).withStudent(BENSON).build();
        BrotherGym differentBrotherGym = new BrotherGym();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(brotherGym, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(brotherGym, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different brotherGym -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentBrotherGym, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredStudentList(new NameContainsKeywordsPredicate(List.of(keywords)));
        assertFalse(modelManager.equals(new ModelManager(brotherGym, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        assertFalse(modelManager.equals(new ModelManager(brotherGym, differentUserPrefs)));
    }
}
