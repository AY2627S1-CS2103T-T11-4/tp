package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_STUDENTS;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalStudents.ALICE;
import static seedu.address.testutil.TypicalStudents.BENSON;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.student.NameContainsKeywordsPredicate;
import seedu.address.model.student.Student;
import seedu.address.model.student.exceptions.StudentNotFoundException;
import seedu.address.model.workoutplan.WorkoutDate;
import seedu.address.model.workoutplan.WorkoutDescription;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.WorkoutPlanStatus;
import seedu.address.model.workoutplan.WorkoutPlansList;
import seedu.address.model.workoutplan.WorkoutTitle;
import seedu.address.model.workoutplan.exceptions.DuplicateWorkoutPlanException;
import seedu.address.model.workoutplan.exceptions.WorkoutPlanNotFoundException;
import seedu.address.testutil.BrotherGymBuilder;
import seedu.address.testutil.StudentBuilder;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    // At least once: cover each date boundary and status without testing every combination.
    @Test
    public void addWorkoutPlanForStudent_pastPending_storesExpectedStatus() {
        assertAddRefresh(-1, WorkoutPlanStatus.PENDING, WorkoutPlanStatus.OVERDUE);
    }

    @Test
    public void addWorkoutPlanForStudent_dueTodayOverdue_storesExpectedStatus() {
        assertAddRefresh(0, WorkoutPlanStatus.OVERDUE, WorkoutPlanStatus.PENDING);
    }

    @Test
    public void addWorkoutPlanForStudent_futurePending_storesExpectedStatus() {
        assertAddRefresh(1, WorkoutPlanStatus.PENDING, WorkoutPlanStatus.PENDING);
    }

    @Test
    public void addWorkoutPlanForStudent_pastCompleted_storesExpectedStatus() {
        assertAddRefresh(-1, WorkoutPlanStatus.COMPLETED, WorkoutPlanStatus.COMPLETED);
    }

    private void assertAddRefresh(int days, WorkoutPlanStatus status, WorkoutPlanStatus expectedStatus) {
        LocalDate today = LocalDate.now();
        ModelManager model = new ModelManager();
        WorkoutPlan unchanged = plan("Other", today.minusDays(1), WorkoutPlanStatus.PENDING);
        Student student = withPlans(ALICE, unchanged);
        model.addStudent(student);
        model.addStudent(BENSON);
        model.setCurrentStudent(student);
        WorkoutPlan added = plan("Added", today.plusDays(days), status);

        model.addWorkoutPlanForStudent(student, added);

        Student updated = model.getCurrentStudent().orElseThrow();
        assertEquals(plan("Added", today.plusDays(days), expectedStatus),
                updated.getWorkoutPlansList().getWorkoutPlan(1));
        assertSame(unchanged, updated.getWorkoutPlansList().getWorkoutPlan(0));
        assertSame(updated, model.getBrotherGym().getStudentList().get(0));
        assertSame(BENSON, model.getBrotherGym().getStudentList().get(1));
        assertEquals(List.of(unchanged), student.getWorkoutPlansList().getWorkoutList());
        assertEquals(status, added.getStatus());
    }

    @Test
    public void setWorkoutPlanForStudent_pastPending_storesExpectedStatus() {
        assertSetRefresh(-1, WorkoutPlanStatus.PENDING, WorkoutPlanStatus.OVERDUE);
    }

    @Test
    public void setWorkoutPlanForStudent_dueTodayOverdue_storesExpectedStatus() {
        assertSetRefresh(0, WorkoutPlanStatus.OVERDUE, WorkoutPlanStatus.PENDING);
    }

    @Test
    public void setWorkoutPlanForStudent_futurePending_storesExpectedStatus() {
        assertSetRefresh(1, WorkoutPlanStatus.PENDING, WorkoutPlanStatus.PENDING);
    }

    @Test
    public void setWorkoutPlanForStudent_pastCompleted_storesExpectedStatus() {
        assertSetRefresh(-1, WorkoutPlanStatus.COMPLETED, WorkoutPlanStatus.COMPLETED);
    }

    private void assertSetRefresh(int days, WorkoutPlanStatus status, WorkoutPlanStatus expectedStatus) {
        LocalDate today = LocalDate.now();
        ModelManager model = new ModelManager();
        WorkoutPlan unchanged = plan("Other", today.minusDays(1), WorkoutPlanStatus.PENDING);
        WorkoutPlan target = plan("Target", today, status);
        Student student = withPlans(ALICE, unchanged, target);
        model.addStudent(student);
        model.addStudent(BENSON);
        model.setCurrentStudent(student);
        WorkoutPlan edited = plan("Edited", today.plusDays(days), status);

        model.setWorkoutPlanForStudent(student, target, edited);

        Student updated = model.getCurrentStudent().orElseThrow();
        assertEquals(plan("Edited", today.plusDays(days), expectedStatus),
                updated.getWorkoutPlansList().getWorkoutPlan(1));
        assertSame(unchanged, updated.getWorkoutPlansList().getWorkoutPlan(0));
        assertSame(updated, model.getBrotherGym().getStudentList().get(0));
        assertSame(BENSON, model.getBrotherGym().getStudentList().get(1));
        assertEquals(List.of(unchanged, target), student.getWorkoutPlansList().getWorkoutList());
        assertEquals(status, edited.getStatus());
    }

    @Test
    public void workoutPlanOperations_duplicateAfterStatusRefresh_doesNotChangeModel() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        WorkoutPlan existing = plan("Same", yesterday, WorkoutPlanStatus.OVERDUE);
        WorkoutPlan candidate = plan("Same", yesterday, WorkoutPlanStatus.PENDING);
        WorkoutPlan target = plan("Target", yesterday, WorkoutPlanStatus.PENDING);
        Student student = withPlans(ALICE, existing, target);
        modelManager.addStudent(student);
        modelManager.setCurrentStudent(student);

        assertThrows(DuplicateWorkoutPlanException.class, () ->
                modelManager.addWorkoutPlanForStudent(student, candidate));
        assertThrows(DuplicateWorkoutPlanException.class, () ->
                modelManager.setWorkoutPlanForStudent(student, target, candidate));
        assertSame(student, modelManager.getCurrentStudent().orElseThrow());
        assertSame(student, modelManager.getBrotherGym().getStudentList().get(0));
        assertEquals(WorkoutPlanStatus.PENDING, candidate.getStatus());
    }

    @Test
    public void workoutPlanOperations_replaceStudentWithoutChangingOriginalValues() {
        WorkoutPlan first = plan("First", LocalDate.now(), WorkoutPlanStatus.PENDING);
        WorkoutPlan second = plan("Second", LocalDate.now(), WorkoutPlanStatus.PENDING);
        WorkoutPlan replacement = plan("Replacement", LocalDate.now(), WorkoutPlanStatus.PENDING);
        Student original = withPlans(ALICE, first, second);
        WorkoutPlansList originalPlans = original.getWorkoutPlansList();
        modelManager.addStudent(original);
        modelManager.setCurrentStudent(original);

        modelManager.setWorkoutPlanForStudent(original, first, replacement);
        Student replaced = modelManager.getCurrentStudent().orElseThrow();
        assertNotSame(original, replaced);
        assertEquals(List.of(replacement, second), replaced.getWorkoutPlansList().getWorkoutList());
        assertEquals(List.of(first, second), originalPlans.getWorkoutList());
        assertEquals(original.getName(), replaced.getName());
        assertEquals(original.getPhone(), replaced.getPhone());
        assertEquals(original.getEmail(), replaced.getEmail());
        assertEquals(original.getAddress(), replaced.getAddress());
        assertEquals(original.getTags(), replaced.getTags());

        modelManager.removeWorkoutPlanForStudent(replaced, replacement);
        Student removed = modelManager.getCurrentStudent().orElseThrow();
        assertEquals(List.of(second), removed.getWorkoutPlansList().getWorkoutList());
        assertEquals(List.of(replacement, second), replaced.getWorkoutPlansList().getWorkoutList());

        modelManager.addWorkoutPlanForStudent(removed, first);
        Student added = modelManager.getCurrentStudent().orElseThrow();
        assertEquals(List.of(second, first), added.getWorkoutPlansList().getWorkoutList());
        assertSame(added, modelManager.getBrotherGym().getStudentList().get(0));
        assertEquals(List.of(second), removed.getWorkoutPlansList().getWorkoutList());
    }

    @Test
    public void workoutPlanOperations_missingTargetsDoNotChangeModel() {
        WorkoutPlan plan = plan("Plan", LocalDate.now(), WorkoutPlanStatus.PENDING);
        Student studentNotInModel = withPlans(BENSON, plan);
        modelManager.addStudent(ALICE);

        assertThrows(StudentNotFoundException.class, () ->
                modelManager.addWorkoutPlanForStudent(studentNotInModel, plan));
        assertThrows(StudentNotFoundException.class, () ->
                modelManager.removeWorkoutPlanForStudent(studentNotInModel, plan));
        assertThrows(StudentNotFoundException.class, () ->
                modelManager.setWorkoutPlanForStudent(studentNotInModel, plan, plan));
        assertThrows(WorkoutPlanNotFoundException.class, () ->
                modelManager.removeWorkoutPlanForStudent(ALICE, plan));
        assertThrows(WorkoutPlanNotFoundException.class, () ->
                modelManager.setWorkoutPlanForStudent(ALICE, plan, plan));
        assertSame(ALICE, modelManager.getBrotherGym().getStudentList().get(0));
    }

    @Test
    public void setWorkoutPlanForStudent_equalPlans_replacesOnlySpecifiedPosition() {
        WorkoutPlan first = plan("Same", LocalDate.now(), WorkoutPlanStatus.PENDING);
        WorkoutPlan second = plan("Same", LocalDate.now(), WorkoutPlanStatus.PENDING);
        WorkoutPlan replacement = plan("Different", LocalDate.now(), WorkoutPlanStatus.PENDING);
        Student student = withPlans(BENSON, first, second);
        modelManager.addStudent(ALICE);
        modelManager.addStudent(student);
        modelManager.setCurrentStudent(ALICE);

        modelManager.setWorkoutPlanForStudent(student, second, replacement);

        assertEquals(List.of(first, replacement),
                modelManager.getBrotherGym().getStudentList().get(1).getWorkoutPlansList().getWorkoutList());
        assertSame(first, modelManager.getBrotherGym().getStudentList().get(1).getWorkoutPlansList()
                .getWorkoutList().get(0));
        assertEquals(List.of(first, second), student.getWorkoutPlansList().getWorkoutList());
        assertSame(ALICE, modelManager.getCurrentStudent().orElseThrow());
    }

    @Test
    public void updateStudentWorkoutsState_storesAndReturnsOnlyTargetStudent() {
        LocalDate today = LocalDate.now();
        ModelManager model = new ModelManager(new BrotherGym(), new UserPrefs());
        Student alice = withPlans(ALICE,
                plan("Completed", today.minusDays(1), WorkoutPlanStatus.COMPLETED),
                plan("Overdue", today.minusDays(1), WorkoutPlanStatus.PENDING),
                plan("Today", today, WorkoutPlanStatus.OVERDUE),
                plan("Future", today.plusDays(1), WorkoutPlanStatus.PENDING));
        Student benson = withPlans(BENSON,
                plan("Other", today.minusDays(1), WorkoutPlanStatus.PENDING));
        model.addStudent(alice);
        model.addStudent(benson);
        model.setCurrentStudent(alice);

        Student updated = model.updateStudentWorkoutsState(alice);

        assertNotSame(alice, updated);
        assertSame(updated, model.getBrotherGym().getStudentList().get(0));
        assertSame(updated, model.getCurrentStudent().orElseThrow());
        assertSame(benson, model.getBrotherGym().getStudentList().get(1));
        assertEquals(List.of(WorkoutPlanStatus.COMPLETED, WorkoutPlanStatus.OVERDUE,
                WorkoutPlanStatus.PENDING, WorkoutPlanStatus.PENDING),
                updated.getWorkoutPlansList().getWorkoutList().stream().map(WorkoutPlan::getStatus).toList());
        assertSame(updated, model.updateStudentWorkoutsState(updated));
    }

    @Test
    public void updateStudentWorkoutsState_nullStudent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.updateStudentWorkoutsState(null));
    }

    @Test
    public void updateStudentWorkoutsState_studentNotInModel_throwsStudentNotFoundException() {
        assertThrows(StudentNotFoundException.class, () -> modelManager.updateStudentWorkoutsState(ALICE));
    }

    private static Student withPlans(Student student, WorkoutPlan... plans) {
        return new Student(student.getName(), student.getPhone(), student.getEmail(), student.getAddress(),
                student.getTags(), new WorkoutPlansList(List.of(plans)));
    }

    private static WorkoutPlan plan(String title, LocalDate deadline, WorkoutPlanStatus status) {
        WorkoutDate date = WorkoutDate.fromLocalDate(deadline);
        return new WorkoutPlan(new WorkoutTitle(title), new WorkoutDescription("Exercise"), date, Set.of(), status);
    }

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
        assertThrows(UnsupportedOperationException.class, () ->
                modelManager.getFilteredStudentList().remove(0));
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
    public void setCurrentStudent_missingStudent_throwsStudentNotFoundException() {
        assertThrows(StudentNotFoundException.class, () -> modelManager.setCurrentStudent(ALICE));
        assertTrue(modelManager.getCurrentStudent().isEmpty());
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
