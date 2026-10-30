package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalStudents.getTypicalBrotherGym;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.BrotherGym;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.student.Student;
import seedu.address.model.tag.WorkoutPlanTag;
import seedu.address.model.workoutplan.WorkoutDate;
import seedu.address.model.workoutplan.WorkoutDescription;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.WorkoutPlanStatus;
import seedu.address.model.workoutplan.WorkoutPlansList;
import seedu.address.model.workoutplan.WorkoutTitle;

/**
 * Contains unit and integration test structure for {@link AddPlanCommand}.
 *
 * Contains execution, duplicate-plan, equality, and formatting tests.
 */
public class AddPlanCommandTest {

    private static final WorkoutPlan PLAN = new WorkoutPlan(
            new WorkoutTitle("Strength"),
            new WorkoutDescription("Upper body"),
            new WorkoutDate(new Date(0)));

    // EP: workout-plan input is null (invalid).
    @Test
    public void constructor_nullWorkoutPlan_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddPlanCommand(null));
    }

    // EP: a current student is selected (valid).
    @Test
    public void execute_validCurrentStudent_addsPlan() {
        Model model = new ModelManager(getTypicalBrotherGym(), new UserPrefs());
        Student currentStudent = model.getFilteredStudentList().get(0);
        model.setCurrentStudent(currentStudent);
        AddPlanCommand command = new AddPlanCommand(PLAN);

        Student updatedStudent = new Student(currentStudent.getName(), currentStudent.getPhone(),
                currentStudent.getEmail(), currentStudent.getAddress(), currentStudent.getTags(),
                new WorkoutPlansList(List.of(new WorkoutPlan(PLAN.getTitle(), PLAN.getWorkouts(), PLAN.getDate(),
                        PLAN.getTags(), WorkoutPlanStatus.OVERDUE))));
        Model expectedModel = new ModelManager(model.getBrotherGym(), new UserPrefs());
        expectedModel.setStudent(currentStudent, updatedStudent);

        assertCommandSuccess(command, model, String.format(AddPlanCommand.MESSAGE_SUCCESS, PLAN), expectedModel);
        assertEquals(updatedStudent, model.getCurrentStudent().orElseThrow());
    }

    // EP: no current student is selected (invalid).
    @Test
    public void execute_noCurrentStudent_throwsCommandException() {
        Model model = new ModelManager(getTypicalBrotherGym(), new UserPrefs());
        AddPlanCommand command = new AddPlanCommand(PLAN);

        assertCommandFailure(command, model, AddPlanCommand.MESSAGE_NO_STUDENT_SELECTED);
    }

    // Each valid input at least once: workout-plan tags are preserved.
    @Test
    public void execute_taggedPlan_preservesWorkoutPlanTags() throws Exception {
        Model model = new ModelManager(getTypicalBrotherGym(), new UserPrefs());
        Student currentStudent = model.getFilteredStudentList().get(0);
        model.setCurrentStudent(currentStudent);
        WorkoutPlan taggedPlan = new WorkoutPlan(new WorkoutTitle("Strength"),
                new WorkoutDescription("Upper body"), new WorkoutDate(new Date(0)),
                Set.of(new WorkoutPlanTag("strength"), new WorkoutPlanTag("upperbody")));

        new AddPlanCommand(taggedPlan).execute(model);

        WorkoutPlan storedPlan = model.getCurrentStudent().orElseThrow()
                .getWorkoutPlansList().getWorkoutList().get(0);
        assertEquals(WorkoutPlanStatus.OVERDUE, storedPlan.getStatus());
        assertEquals(taggedPlan.getTitle(), storedPlan.getTitle());
        assertEquals(taggedPlan.getWorkouts(), storedPlan.getWorkouts());
        assertEquals(taggedPlan.getDate(), storedPlan.getDate());
        assertEquals(Set.of(new WorkoutPlanTag("strength"), new WorkoutPlanTag("upperbody")),
                storedPlan.getTags());
    }

    // EP: workout plan duplicates a stored plan (invalid).
    @Test
    public void execute_duplicateWorkoutPlan_throwsCommandException() {
        Student baseStudent = new ModelManager(getTypicalBrotherGym(), new UserPrefs())
                .getFilteredStudentList().get(0);
        Student studentWithPlan = new Student(baseStudent.getName(), baseStudent.getPhone(), baseStudent.getEmail(),
                baseStudent.getAddress(), baseStudent.getTags(),
                new WorkoutPlansList(List.of(PLAN.updateState(LocalDate.now()))));
        BrotherGym brotherGym = new BrotherGym();
        brotherGym.addStudent(studentWithPlan);
        Model model = new ModelManager(brotherGym, new UserPrefs());
        model.setCurrentStudent(studentWithPlan);
        AddPlanCommand command = new AddPlanCommand(PLAN);

        assertCommandFailure(command, model, AddPlanCommand.MESSAGE_DUPLICATE_PLAN);
    }

    @Test
    public void equals() {
        AddPlanCommand first = new AddPlanCommand(PLAN);
        AddPlanCommand copy = new AddPlanCommand(new WorkoutPlan(
                new WorkoutTitle("Strength"), new WorkoutDescription("Upper body"), new WorkoutDate(new Date(0))));
        AddPlanCommand different = new AddPlanCommand(new WorkoutPlan(
                new WorkoutTitle("Cardio"), new WorkoutDescription("Running"), new WorkoutDate(new Date(0))));

        assertTrue(first.equals(first));
        assertTrue(first.equals(copy));
        assertFalse(first.equals(different));
        assertFalse(first.equals(null));
        assertFalse(first.equals(1));
    }

    @Test
    public void toStringMethod() {
        AddPlanCommand command = new AddPlanCommand(PLAN);
        String expected = AddPlanCommand.class.getCanonicalName() + "{toAdd=" + PLAN + "}";

        assertEquals(expected, command.toString());
    }
}
