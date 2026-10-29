package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalStudents.getTypicalBrotherGym;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.model.BrotherGym;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.exercise.Exercise;
import seedu.address.model.exercise.ExerciseDescription;
import seedu.address.model.exercise.ExerciseName;
import seedu.address.model.exercise.ExercisesList;
import seedu.address.model.student.Student;
import seedu.address.model.tag.WorkoutPlanTag;
import seedu.address.model.workoutplan.WorkoutDate;
import seedu.address.model.workoutplan.WorkoutDescription;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.WorkoutPlanStatus;
import seedu.address.model.workoutplan.WorkoutPlansList;
import seedu.address.model.workoutplan.WorkoutTitle;

/** Contains unit and integration tests for {@link EditPlanCommand}. */
public class EditPlanCommandTest {

    private static final ExercisesList EXERCISES = new ExercisesList(List.of(new Exercise(
            new ExerciseName("Squats"), new ExerciseDescription("Barbell back squat"))));

    private static final WorkoutPlan FIRST_PLAN = new WorkoutPlan(
            new WorkoutTitle("Strength"), new WorkoutDescription("Upper body"),
            WorkoutDate.fromLocalDate(LocalDate.now().plusDays(1)),
            Set.of(new WorkoutPlanTag("old")), WorkoutPlanStatus.PENDING, EXERCISES);
  
    private static final WorkoutPlan SECOND_PLAN = new WorkoutPlan(
            new WorkoutTitle("Cardio"), new WorkoutDescription("Running"),
            WorkoutDate.fromLocalDate(LocalDate.now().plusDays(2)));

    @Test
    public void constructor_nullArguments_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new EditPlanCommand(null, new EditPlanCommand.EditPlanDescriptor()));
        assertThrows(NullPointerException.class, () ->
                new EditPlanCommand(Index.fromOneBased(1), null));
    }

    @Test
    public void execute_fieldSpecified_preservesOtherFieldsAndTags() throws Exception {
        Model model = createModelWithPlans(FIRST_PLAN, SECOND_PLAN);
        Student currentStudent = model.getFilteredStudentList().get(0);
        model.setCurrentStudent(currentStudent);
        EditPlanCommand.EditPlanDescriptor descriptor = new EditPlanCommand.EditPlanDescriptor();
        descriptor.setTitle(new WorkoutTitle("Updated strength"));

        WorkoutPlan expectedPlan = new WorkoutPlan(new WorkoutTitle("Updated strength"),
                FIRST_PLAN.getWorkouts(), FIRST_PLAN.getDate(), FIRST_PLAN.getTags(),
                FIRST_PLAN.getStatus(), FIRST_PLAN.getExercisesList());
        Student expectedStudent = withPlans(currentStudent, expectedPlan, SECOND_PLAN);
        Model expectedModel = modelWithStudent(expectedStudent);

        CommandTestUtil.assertCommandSuccess(new EditPlanCommand(Index.fromOneBased(1), descriptor), model,
                String.format(EditPlanCommand.MESSAGE_SUCCESS, expectedPlan), expectedModel);
        assertEquals(expectedStudent, model.getCurrentStudent().orElseThrow());
    }

    @Test
    public void execute_fieldSpecified_preservesCompletedStatus() throws Exception {
        WorkoutPlan completedPlan = new WorkoutPlan(FIRST_PLAN.getTitle(), FIRST_PLAN.getWorkouts(),
                FIRST_PLAN.getDate(), FIRST_PLAN.getTags(), WorkoutPlanStatus.COMPLETED,
                FIRST_PLAN.getExercisesList());
        Model model = createModelWithPlans(completedPlan);
        Student currentStudent = model.getFilteredStudentList().get(0);
        model.setCurrentStudent(currentStudent);
        EditPlanCommand.EditPlanDescriptor descriptor = new EditPlanCommand.EditPlanDescriptor();
        WorkoutTitle updatedTitle = new WorkoutTitle("Updated strength");
        descriptor.setTitle(updatedTitle);

        WorkoutPlan expectedPlan = new WorkoutPlan(updatedTitle, completedPlan.getWorkouts(),
                completedPlan.getDate(), completedPlan.getTags(), WorkoutPlanStatus.COMPLETED,
                completedPlan.getExercisesList());
        Student expectedStudent = withPlans(currentStudent, expectedPlan);
        Model expectedModel = modelWithStudent(expectedStudent);

        CommandTestUtil.assertCommandSuccess(new EditPlanCommand(Index.fromOneBased(1), descriptor), model,
                String.format(EditPlanCommand.MESSAGE_SUCCESS, expectedPlan), expectedModel);
        assertEquals(WorkoutPlanStatus.COMPLETED,
                model.getCurrentStudent().orElseThrow().getWorkoutPlansList().getWorkoutList().get(0).getStatus());
    }

    // At least once: cover each date boundary and status without testing every combination.
    @Test
    public void execute_dateEditedPastPending_refreshesStatus() throws Exception {
        assertDateEdit(-1, WorkoutPlanStatus.PENDING, WorkoutPlanStatus.OVERDUE);
    }

    @Test
    public void execute_dateEditedDueTodayOverdue_refreshesStatus() throws Exception {
        assertDateEdit(0, WorkoutPlanStatus.OVERDUE, WorkoutPlanStatus.PENDING);
    }

    @Test
    public void execute_dateEditedFuturePending_refreshesStatus() throws Exception {
        assertDateEdit(1, WorkoutPlanStatus.PENDING, WorkoutPlanStatus.PENDING);
    }

    @Test
    public void execute_dateEditedPastCompleted_refreshesStatus() throws Exception {
        assertDateEdit(-1, WorkoutPlanStatus.COMPLETED, WorkoutPlanStatus.COMPLETED);
    }

    private void assertDateEdit(int days, WorkoutPlanStatus status, WorkoutPlanStatus expectedStatus) throws Exception {
        LocalDate dueDate = LocalDate.now().plusDays(days);
        WorkoutPlan original = new WorkoutPlan(FIRST_PLAN.getTitle(), FIRST_PLAN.getWorkouts(),
                FIRST_PLAN.getDate(), FIRST_PLAN.getTags(), status);
        Model model = createModelWithPlans(original, SECOND_PLAN);
        model.setCurrentStudent(model.getFilteredStudentList().get(0));
        EditPlanCommand.EditPlanDescriptor descriptor = new EditPlanCommand.EditPlanDescriptor();
        descriptor.setDate(WorkoutDate.fromLocalDate(dueDate));

        new EditPlanCommand(Index.fromOneBased(1), descriptor).execute(model);

        WorkoutPlan edited = model.getCurrentStudent().orElseThrow().getWorkoutPlansList().getWorkoutPlan(0);
        assertEquals(expectedStatus, edited.getStatus());
        assertEquals(WorkoutDate.fromLocalDate(dueDate), edited.getDate());
        assertEquals(SECOND_PLAN,
                model.getCurrentStudent().orElseThrow().getWorkoutPlansList().getWorkoutPlan(1));
    }

    @Test
    public void execute_tagsSpecified_replacesTags() throws Exception {
        Model model = createModelWithPlans(FIRST_PLAN);
        Student currentStudent = model.getFilteredStudentList().get(0);
        model.setCurrentStudent(currentStudent);
        EditPlanCommand.EditPlanDescriptor descriptor = new EditPlanCommand.EditPlanDescriptor();
        descriptor.setWorkoutPlanTags(Set.of(new WorkoutPlanTag("new")));

        WorkoutPlan expectedPlan = new WorkoutPlan(FIRST_PLAN.getTitle(), FIRST_PLAN.getWorkouts(),
                FIRST_PLAN.getDate(), Set.of(new WorkoutPlanTag("new")), FIRST_PLAN.getStatus(),
                FIRST_PLAN.getExercisesList());
        Student expectedStudent = withPlans(currentStudent, expectedPlan);
        Model expectedModel = modelWithStudent(expectedStudent);

        CommandTestUtil.assertCommandSuccess(new EditPlanCommand(Index.fromOneBased(1), descriptor), model,
                String.format(EditPlanCommand.MESSAGE_SUCCESS, expectedPlan), expectedModel);
    }

    @Test
    public void execute_explicitEmptyTagSet_clearsTags() throws Exception {
        Model model = createModelWithPlans(FIRST_PLAN);
        Student currentStudent = model.getFilteredStudentList().get(0);
        model.setCurrentStudent(currentStudent);
        EditPlanCommand.EditPlanDescriptor descriptor = new EditPlanCommand.EditPlanDescriptor();
        descriptor.setWorkoutPlanTags(Set.of());

        WorkoutPlan expectedPlan = new WorkoutPlan(FIRST_PLAN.getTitle(), FIRST_PLAN.getWorkouts(),
                FIRST_PLAN.getDate(), Set.of(), FIRST_PLAN.getStatus(), FIRST_PLAN.getExercisesList());
        Student expectedStudent = withPlans(currentStudent, expectedPlan);
        Model expectedModel = modelWithStudent(expectedStudent);

        CommandTestUtil.assertCommandSuccess(new EditPlanCommand(Index.fromOneBased(1), descriptor), model,
                String.format(EditPlanCommand.MESSAGE_SUCCESS, expectedPlan), expectedModel);
    }

    @Test
    public void execute_noCurrentStudent_throwsCommandException() {
        Model model = new ModelManager(getTypicalBrotherGym(), new UserPrefs());
        EditPlanCommand.EditPlanDescriptor descriptor = new EditPlanCommand.EditPlanDescriptor();
        descriptor.setTitle(new WorkoutTitle("Updated"));

        CommandTestUtil.assertCommandFailure(new EditPlanCommand(Index.fromOneBased(1), descriptor), model,
                EditPlanCommand.MESSAGE_NO_STUDENT_SELECTED);
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        EditPlanCommand.EditPlanDescriptor descriptor = new EditPlanCommand.EditPlanDescriptor();
        descriptor.setTitle(new WorkoutTitle("Updated"));
        EditPlanCommand command = new EditPlanCommand(Index.fromOneBased(1), descriptor);

        assertThrows(NullPointerException.class, () -> command.execute(null));
    }

    @Test
    public void execute_invalidWorkoutId_throwsCommandException() {
        Model model = createModelWithPlans(FIRST_PLAN);
        model.setCurrentStudent(model.getFilteredStudentList().get(0));
        EditPlanCommand.EditPlanDescriptor descriptor = new EditPlanCommand.EditPlanDescriptor();
        descriptor.setTitle(new WorkoutTitle("Updated"));

        CommandTestUtil.assertCommandFailure(new EditPlanCommand(Index.fromOneBased(2), descriptor), model,
                EditPlanCommand.MESSAGE_INVALID_WORKOUT_ID);
    }

    @Test
    public void execute_noFieldsSpecified_throwsCommandException() {
        Model model = createModelWithPlans(FIRST_PLAN);
        model.setCurrentStudent(model.getFilteredStudentList().get(0));

        CommandTestUtil.assertCommandFailure(new EditPlanCommand(Index.fromOneBased(1),
                new EditPlanCommand.EditPlanDescriptor()), model, EditPlanCommand.MESSAGE_NOT_EDITED);
    }

    @Test
    public void execute_duplicatePlan_throwsCommandException() {
        for (WorkoutPlanStatus status : WorkoutPlanStatus.values()) {
            WorkoutPlan existing = new WorkoutPlan(SECOND_PLAN.getTitle(), SECOND_PLAN.getWorkouts(),
                    SECOND_PLAN.getDate(), SECOND_PLAN.getTags(), status);
            Model model = createModelWithPlans(FIRST_PLAN, existing);
            model.setCurrentStudent(model.getFilteredStudentList().get(0));
            EditPlanCommand.EditPlanDescriptor descriptor = new EditPlanCommand.EditPlanDescriptor();
            descriptor.setTitle(existing.getTitle());
            descriptor.setDescription(existing.getWorkouts());
            descriptor.setDate(existing.getDate());
            Set<WorkoutPlanTag> editedTags = status == WorkoutPlanStatus.PENDING
                    ? existing.getTags() : Set.of(new WorkoutPlanTag("different"));
            descriptor.setWorkoutPlanTags(editedTags);

            CommandTestUtil.assertCommandFailure(new EditPlanCommand(Index.fromOneBased(1), descriptor), model,
                    EditPlanCommand.MESSAGE_DUPLICATE_PLAN);
        }
    }

    @Test
    public void equals() {
        EditPlanCommand.EditPlanDescriptor descriptor = new EditPlanCommand.EditPlanDescriptor();
        descriptor.setTitle(new WorkoutTitle("Updated"));
        EditPlanCommand first = new EditPlanCommand(Index.fromOneBased(1), descriptor);
        EditPlanCommand copy = new EditPlanCommand(Index.fromOneBased(1), descriptor);
        EditPlanCommand different = new EditPlanCommand(Index.fromOneBased(2), descriptor);

        assertTrue(first.equals(first));
        assertTrue(first.equals(copy));
        assertFalse(first.equals(different));
        assertFalse(first.equals(null));
        assertFalse(first.equals(1));
    }

    private static Model createModelWithPlans(WorkoutPlan... plans) {
        Student baseStudent = new ModelManager(getTypicalBrotherGym(), new UserPrefs())
                .getFilteredStudentList().get(0);
        Student studentWithPlans = withPlans(baseStudent, plans);
        BrotherGym brotherGym = new BrotherGym();
        brotherGym.addStudent(studentWithPlans);
        return new ModelManager(brotherGym, new UserPrefs());
    }

    private static Student withPlans(Student student, WorkoutPlan... plans) {
        return new Student(student.getName(), student.getPhone(), student.getEmail(), student.getAddress(),
                student.getTags(), new WorkoutPlansList(List.of(plans)));
    }

    private static Model modelWithStudent(Student student) {
        BrotherGym brotherGym = new BrotherGym();
        brotherGym.addStudent(student);
        Model model = new ModelManager(brotherGym, new UserPrefs());
        model.setCurrentStudent(student);
        return model;
    }
}
