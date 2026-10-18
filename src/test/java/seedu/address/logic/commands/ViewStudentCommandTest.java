package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND;
import static seedu.address.testutil.TypicalStudents.getTypicalBrotherGym;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.BrotherGym;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.student.Student;
import seedu.address.model.workoutplan.WorkoutDate;
import seedu.address.model.workoutplan.WorkoutDescription;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.WorkoutPlanStatus;
import seedu.address.model.workoutplan.WorkoutPlansList;
import seedu.address.model.workoutplan.WorkoutTitle;
import seedu.address.testutil.TypicalStudents;

/**
 * Contains integration tests and unit tests for {@link ViewStudentCommand}.
 */
public class ViewStudentCommandTest {

    private final Model model = new ModelManager(getTypicalBrotherGym(), new UserPrefs());

    @Test
    public void execute_validIndexNoWorkoutPlans_success() {
        ViewStudentCommand command = new ViewStudentCommand(INDEX_FIRST);
        String expectedMessage = "Viewing plans for Alice Pauline:\n"
                + ViewStudentCommand.MESSAGE_NO_WORKOUTS;

        Model expectedModel = new ModelManager(model.getBrotherGym(), new UserPrefs());
        expectedModel.setCurrentStudent(TypicalStudents.ALICE);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(TypicalStudents.ALICE, model.getCurrentStudent().orElseThrow());
    }

    @Test
    public void execute_validIndexWithWorkoutPlans_success() {
        WorkoutPlan plan = new WorkoutPlan(new WorkoutTitle("Strength"),
                new WorkoutDescription("Upper body"), new WorkoutDate(new Date(0)));
        Student baseStudent = TypicalStudents.ALICE;
        Student studentWithPlan = new Student(baseStudent.getName(), baseStudent.getPhone(), baseStudent.getEmail(),
                baseStudent.getAddress(), baseStudent.getTags(), new WorkoutPlansList(List.of(plan)));
        BrotherGym brotherGym = new BrotherGym();
        brotherGym.addStudent(studentWithPlan);
        Model modelWithPlan = new ModelManager(brotherGym, new UserPrefs());

        ViewStudentCommand command = new ViewStudentCommand(INDEX_FIRST);
        String expectedMessage = "Viewing plans for Alice Pauline:\n1. " + plan;
        Model expectedModel = new ModelManager(modelWithPlan.getBrotherGym(), new UserPrefs());
        WorkoutPlan overduePlan = new WorkoutPlan(plan.getTitle(), plan.getWorkouts(), plan.getDate(),
                plan.getTags(), WorkoutPlanStatus.OVERDUE);
        Student refreshedStudent = withPlans(baseStudent, List.of(overduePlan));
        expectedModel.setStudent(studentWithPlan, refreshedStudent);
        expectedModel.setCurrentStudent(refreshedStudent);

        assertCommandSuccess(command, modelWithPlan, expectedMessage, expectedModel);
        assertEquals(refreshedStudent, modelWithPlan.getCurrentStudent().orElseThrow());
    }

    @Test
    public void execute_filteredStudent_refreshesOnlySelectedStudentAndPreservesCompletedPlans() throws Exception {
        LocalDate today = LocalDate.now();
        WorkoutPlan past = plan("Past", today.minusDays(1), WorkoutPlanStatus.PENDING);
        WorkoutPlan completed = plan("Done", today.minusDays(1), WorkoutPlanStatus.COMPLETED);
        WorkoutPlan dueToday = plan("Today", today, WorkoutPlanStatus.OVERDUE);
        WorkoutPlan future = plan("Future", today.plusDays(1), WorkoutPlanStatus.OVERDUE);
        Student selected = withPlans(TypicalStudents.BENSON, List.of(past, completed, dueToday, future));
        Student other = withPlans(TypicalStudents.ALICE, List.of(past));
        Model refreshModel = new ModelManager();
        refreshModel.addStudent(other);
        refreshModel.addStudent(selected);
        refreshModel.setCurrentStudent(other);
        refreshModel.updateFilteredStudentList(student -> student.isSameStudent(selected));
        ViewStudentCommand command = new ViewStudentCommand(INDEX_FIRST);

        CommandResult result = command.execute(refreshModel);

        Student refreshed = refreshModel.getCurrentStudent().orElseThrow();
        List<WorkoutPlan> refreshedPlans = refreshed.getWorkoutPlansList().getWorkoutList();
        assertEquals(withPlans(TypicalStudents.BENSON, List.of(
                plan("Past", today.minusDays(1), WorkoutPlanStatus.OVERDUE), completed,
                plan("Today", today, WorkoutPlanStatus.PENDING),
                plan("Future", today.plusDays(1), WorkoutPlanStatus.PENDING))), refreshed);
        assertSame(refreshed, refreshModel.getBrotherGym().getStudentList().get(1));
        assertSame(refreshed, refreshModel.getFilteredStudentList().get(0));
        assertSame(other, refreshModel.getBrotherGym().getStudentList().get(0));
        assertSame(completed, refreshedPlans.get(1));
        assertEquals(WorkoutPlanStatus.PENDING, past.getStatus());
        assertEquals(String.format(ViewStudentCommand.MESSAGE_SUCCESS, refreshed.getName(),
                "1. " + refreshedPlans.get(0) + "\n2. " + refreshedPlans.get(1)
                        + "\n3. " + refreshedPlans.get(2) + "\n4. " + refreshedPlans.get(3)),
                result.getFeedbackToUser());

        command.execute(refreshModel);
        assertSame(refreshed, refreshModel.getCurrentStudent().orElseThrow());
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
        ViewStudentCommand firstCommand = new ViewStudentCommand(INDEX_FIRST);
        ViewStudentCommand secondCommand = new ViewStudentCommand(INDEX_SECOND);

        assertTrue(firstCommand.equals(firstCommand));
        assertTrue(firstCommand.equals(new ViewStudentCommand(INDEX_FIRST)));
        assertFalse(firstCommand.equals(secondCommand));
        assertFalse(firstCommand.equals(null));
        assertFalse(firstCommand.equals(1));
    }

    @Test
    public void toStringMethod() {
        ViewStudentCommand command = new ViewStudentCommand(INDEX_FIRST);
        String expected = ViewStudentCommand.class.getCanonicalName()
                + "{targetIndex=" + INDEX_FIRST + "}";

        assertEquals(expected, command.toString());
    }

    private static Student withPlans(Student student, List<WorkoutPlan> plans) {
        return new Student(student.getName(), student.getPhone(), student.getEmail(),
                student.getAddress(), student.getTags(), new WorkoutPlansList(plans));
    }

    private static WorkoutPlan plan(String title, LocalDate deadline, WorkoutPlanStatus status) {
        return new WorkoutPlan(new WorkoutTitle(title), new WorkoutDescription("Exercise"),
                WorkoutDate.fromLocalDate(deadline), Set.of(), status);
    }
}
