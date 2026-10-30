package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.testutil.TypicalExercises.LUNGES;
import static seedu.address.testutil.TypicalExercises.PUSH_UPS;
import static seedu.address.testutil.TypicalExercises.SQUATS;
import static seedu.address.testutil.TypicalStudents.ALICE;
import static seedu.address.testutil.TypicalStudents.BENSON;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditExerciseCommand.EditExerciseDescriptor;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.BrotherGym;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.exercise.Exercise;
import seedu.address.model.exercise.ExerciseDescription;
import seedu.address.model.exercise.ExerciseName;
import seedu.address.model.exercise.ExercisesList;
import seedu.address.model.exercise.exceptions.ExerciseNotFoundException;
import seedu.address.model.student.Student;
import seedu.address.model.student.exceptions.StudentNotFoundException;
import seedu.address.model.workoutplan.WorkoutDate;
import seedu.address.model.workoutplan.WorkoutDescription;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.WorkoutPlanStatus;
import seedu.address.model.workoutplan.WorkoutPlansList;
import seedu.address.model.workoutplan.WorkoutTitle;
import seedu.address.model.workoutplan.exceptions.WorkoutPlanNotFoundException;

/** Tests exercise editing through the selected student and workout plan. */
public class EditExerciseCommandTest {

    @Test
    public void execute_partialEdits_preserveOmittedFieldAndExercisePosition() throws CommandException {
        ExerciseName editedName = new ExerciseName("Front Squats");
        ExerciseDescription editedDescription = new ExerciseDescription("Four controlled sets");

        // Each valid input at least once: a name-only edit succeeds.
        assertPartialEdit(editedName, null,
                new Exercise(editedName, SQUATS.getDescription()));
        // Each valid input at least once: a description-only edit succeeds.
        assertPartialEdit(null, editedDescription,
                new Exercise(SQUATS.getName(), editedDescription));
    }

    @Test
    public void execute_allFieldsEdited_updatesOnlySelectedPlan() throws CommandException {
        WorkoutPlan selectedPlan = plan("Selected", List.of(SQUATS, PUSH_UPS));
        WorkoutPlan otherPlan = plan("Other", List.of(LUNGES));
        Student alice = withPlans(ALICE, selectedPlan, otherPlan);
        Student benson = withPlans(BENSON, plan("Benson", List.of(PUSH_UPS)));
        ModelManager model = modelWith(alice, benson);
        model.setCurrentStudent(alice);
        model.setCurrentWorkoutPlan(selectedPlan);
        EditExerciseDescriptor descriptor = descriptor(
                new ExerciseName("Goblet Squat"), new ExerciseDescription("3 sets of 10 reps"));

        CommandResult result = new EditExerciseCommand(Index.fromOneBased(1), descriptor).execute(model);

        Exercise expected = new Exercise(new ExerciseName("Goblet Squat"),
                new ExerciseDescription("3 sets of 10 reps"));
        WorkoutPlan updatedPlan = model.getCurrentWorkoutPlan().orElseThrow();
        assertEquals(String.format(EditExerciseCommand.MESSAGE_SUCCESS, expected), result.getFeedbackToUser());
        assertEquals(List.of(expected, PUSH_UPS), updatedPlan.getExercisesList().getExerciseList());
        assertEquals(selectedPlan.getTitle(), updatedPlan.getTitle());
        assertEquals(selectedPlan.getDescription(), updatedPlan.getDescription());
        assertEquals(selectedPlan.getDate(), updatedPlan.getDate());
        assertEquals(selectedPlan.getTags(), updatedPlan.getTags());
        assertEquals(selectedPlan.getStatus(), updatedPlan.getStatus());
        assertSame(otherPlan, model.getCurrentStudent().orElseThrow()
                .getWorkoutPlansList().getWorkoutPlan(1));
        assertSame(benson, model.getBrotherGym().getStudentList().get(1));
        assertEquals(List.of(SQUATS, PUSH_UPS), selectedPlan.getExercisesList().getExerciseList());
    }

    // EP: edited exercise duplicates another stored exercise (invalid).
    @Test
    public void execute_duplicateEdit_failsWithoutChangingModel() {
        WorkoutPlan selectedPlan = plan("Selected", List.of(SQUATS, PUSH_UPS));
        Student student = withPlans(ALICE, selectedPlan);
        ModelManager model = modelWith(student);
        model.setCurrentStudent(student);
        model.setCurrentWorkoutPlan(selectedPlan);
        EditExerciseDescriptor descriptor = descriptor(PUSH_UPS.getName(), PUSH_UPS.getDescription());

        CommandException exception = assertThrows(CommandException.class, ()
            -> new EditExerciseCommand(Index.fromOneBased(1), descriptor).execute(model));

        assertEquals(EditExerciseCommand.MESSAGE_DUPLICATE_EXERCISE, exception.getMessage());
        assertSame(student, model.getCurrentStudent().orElseThrow());
        assertSame(selectedPlan, model.getCurrentWorkoutPlan().orElseThrow());
        assertEquals(List.of(SQUATS, PUSH_UPS), selectedPlan.getExercisesList().getExerciseList());
    }

    @Test
    public void execute_missingSelectionInvalidIndexOrEmptyEdit_failsWithoutChanges() {
        WorkoutPlan selectedPlan = plan("Selected", List.of(SQUATS));
        Student student = withPlans(ALICE, selectedPlan);
        ModelManager model = modelWith(student);
        EditExerciseDescriptor descriptor = descriptor(new ExerciseName("Edited"), null);
        EditExerciseCommand command = new EditExerciseCommand(Index.fromOneBased(1), descriptor);

        // EP: no current student is selected (invalid).
        assertEquals(EditExerciseCommand.MESSAGE_NO_STUDENT_SELECTED,
                assertThrows(CommandException.class, () -> command.execute(model)).getMessage());
        model.setCurrentStudent(student);
        // EP: no workout plan is selected (invalid).
        assertEquals(EditExerciseCommand.MESSAGE_NO_PLAN_SELECTED,
                assertThrows(CommandException.class, () -> command.execute(model)).getMessage());
        model.setCurrentWorkoutPlan(selectedPlan);
        // BV: index > size (invalid).
        assertEquals(EditExerciseCommand.MESSAGE_INVALID_EXERCISE_ID,
                assertThrows(CommandException.class, () -> new EditExerciseCommand(
                        Index.fromOneBased(2), descriptor).execute(model)).getMessage());
        // BV: count < 1 (invalid).
        assertEquals(EditExerciseCommand.MESSAGE_NOT_EDITED,
                assertThrows(CommandException.class, () -> new EditExerciseCommand(
                        Index.fromOneBased(1), new EditExerciseDescriptor()).execute(model)).getMessage());
        assertSame(selectedPlan, model.getCurrentWorkoutPlan().orElseThrow());
        assertEquals(List.of(SQUATS), selectedPlan.getExercisesList().getExerciseList());
    }

    @Test
    public void execute_modelRejectsStaleReferences_returnsUsefulError() {
        WorkoutPlan selectedPlan = plan("Selected", List.of(SQUATS));
        Student student = withPlans(ALICE, selectedPlan);
        EditExerciseCommand command = new EditExerciseCommand(Index.fromOneBased(1),
                descriptor(new ExerciseName("Edited"), null));

        // One invalid input at a time: only the student reference is stale.
        assertSetExerciseFailure(student, selectedPlan, command, new StudentNotFoundException(),
                EditExerciseCommand.MESSAGE_PLAN_NO_LONGER_AVAILABLE);
        // One invalid input at a time: only the workout-plan reference is stale.
        assertSetExerciseFailure(student, selectedPlan, command,
                new WorkoutPlanNotFoundException(selectedPlan),
                EditExerciseCommand.MESSAGE_PLAN_NO_LONGER_AVAILABLE);
        // One invalid input at a time: only the exercise reference is stale.
        assertSetExerciseFailure(student, selectedPlan, command,
                new ExerciseNotFoundException(SQUATS),
                EditExerciseCommand.MESSAGE_INVALID_EXERCISE_ID);
    }

    @Test
    public void equalsAndToString() {
        EditExerciseDescriptor descriptor = descriptor(new ExerciseName("Edited"), null);
        EditExerciseCommand command = new EditExerciseCommand(Index.fromOneBased(1), descriptor);

        // One invalid input at a time: only the command index is null.
        assertThrows(NullPointerException.class, () -> new EditExerciseCommand(null, descriptor));
        // One invalid input at a time: only the edit descriptor is null.
        assertThrows(NullPointerException.class, () -> new EditExerciseCommand(Index.fromOneBased(1), null));
        // EP: Another command with equal fields (equivalent).
        assertEquals(command, new EditExerciseCommand(Index.fromOneBased(1), descriptor));
        assertEquals(descriptor.hashCode(), new EditExerciseDescriptor(descriptor).hashCode());
        // EP: A different exercise index (non-equivalent).
        assertFalse(command.equals(new EditExerciseCommand(Index.fromOneBased(2), descriptor)));
        // EP: Null (non-equivalent).
        assertFalse(command.equals(null));
        assertEquals(EditExerciseCommand.class.getCanonicalName() + "{targetIndex="
                + Index.fromOneBased(1) + ", editExerciseDescriptor=" + descriptor + "}", command.toString());
    }

    private static void assertPartialEdit(ExerciseName name, ExerciseDescription description,
            Exercise expected) throws CommandException {
        WorkoutPlan selectedPlan = plan("Selected", List.of(SQUATS, PUSH_UPS));
        Student student = withPlans(ALICE, selectedPlan);
        ModelManager model = modelWith(student);
        model.setCurrentStudent(student);
        model.setCurrentWorkoutPlan(selectedPlan);

        new EditExerciseCommand(Index.fromOneBased(1), descriptor(name, description)).execute(model);

        assertEquals(List.of(expected, PUSH_UPS), model.getCurrentWorkoutPlan().orElseThrow()
                .getExercisesList().getExerciseList());
    }

    private static void assertSetExerciseFailure(Student student, WorkoutPlan selectedPlan,
            EditExerciseCommand command, RuntimeException failure, String expectedMessage) {
        BrotherGym data = new BrotherGym();
        data.addStudent(student);
        ModelManager model = new ModelManager(data, new UserPrefs()) {
            @Override
            public void setExerciseForWorkoutPlan(Student targetStudent, WorkoutPlan targetPlan,
                    Exercise target, Exercise editedExercise) {
                throw failure;
            }
        };
        model.setCurrentStudent(student);
        model.setCurrentWorkoutPlan(selectedPlan);

        assertEquals(expectedMessage,
                assertThrows(CommandException.class, () -> command.execute(model)).getMessage());
    }

    private static EditExerciseDescriptor descriptor(ExerciseName name, ExerciseDescription description) {
        EditExerciseDescriptor descriptor = new EditExerciseDescriptor();
        if (name != null) {
            descriptor.setName(name);
        }
        if (description != null) {
            descriptor.setDescription(description);
        }
        return descriptor;
    }

    private static WorkoutPlan plan(String title, List<Exercise> exercises) {
        return new WorkoutPlan(new WorkoutTitle(title), new WorkoutDescription("Plan notes"),
                new WorkoutDate(new Date(0)), Set.of(), WorkoutPlanStatus.PENDING,
                new ExercisesList(exercises));
    }

    private static Student withPlans(Student base, WorkoutPlan... plans) {
        return new Student(base.getName(), base.getPhone(), base.getEmail(), base.getAddress(),
                base.getTags(), new WorkoutPlansList(List.of(plans)));
    }

    private static ModelManager modelWith(Student... students) {
        BrotherGym data = new BrotherGym();
        for (Student student : students) {
            data.addStudent(student);
        }
        return new ModelManager(data, new UserPrefs());
    }
}
