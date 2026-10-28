package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.testutil.TypicalStudents.ALICE;
import static seedu.address.testutil.TypicalStudents.BENSON;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.student.Student;
import seedu.address.model.workoutplan.WorkoutDate;
import seedu.address.model.workoutplan.WorkoutDescription;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.WorkoutPlanStatus;
import seedu.address.model.workoutplan.WorkoutPlansList;
import seedu.address.model.workoutplan.WorkoutTitle;

/** Tests status changes and validation for {@link UnmarkPlanCommand}. */
public class UnmarkPlanCommandTest {

    @Test
    public void execute_validIndex_updatesOnlySelectedPlan() throws Exception {
        LocalDate today = LocalDate.now();
        assertTransition(today.minusDays(1), WorkoutPlanStatus.COMPLETED, WorkoutPlanStatus.OVERDUE);
        assertTransition(today, WorkoutPlanStatus.COMPLETED, WorkoutPlanStatus.PENDING);
        assertTransition(today.plusDays(1), WorkoutPlanStatus.COMPLETED, WorkoutPlanStatus.PENDING);
    }

    private void assertTransition(LocalDate deadline, WorkoutPlanStatus initialStatus,
            WorkoutPlanStatus expectedStatus) throws Exception {
        WorkoutPlan target = plan("Target", deadline, initialStatus);
        // Deliberately stale: this command must not refresh unrelated plans.
        WorkoutPlan unchanged = plan("Other", LocalDate.now().minusDays(1), WorkoutPlanStatus.PENDING);
        Student original = withPlans(ALICE, List.of(unchanged, target));
        Student otherStudent = withPlans(BENSON, List.of(unchanged));
        Model model = new ModelManager();
        model.addStudent(original);
        model.addStudent(otherStudent);
        model.setCurrentStudent(original);
        UnmarkPlanCommand command = new UnmarkPlanCommand(Index.fromOneBased(2));

        CommandResult result = command.execute(model);

        Student updated = model.getCurrentStudent().orElseThrow();
        List<WorkoutPlan> updatedPlans = updated.getWorkoutPlansList().getWorkoutList();
        WorkoutPlan expectedPlan = plan("Target", deadline, expectedStatus);
        assertEquals(withPlans(ALICE, List.of(unchanged, expectedPlan)), updated);
        assertSame(updated, model.getBrotherGym().getStudentList().get(0));
        assertSame(otherStudent, model.getBrotherGym().getStudentList().get(1));
        assertSame(unchanged, updatedPlans.get(0));
        assertEquals(initialStatus, target.getStatus());
        assertEquals(String.format(UnmarkPlanCommand.MESSAGE_SUCCESS, expectedPlan), result.getFeedbackToUser());

        assertCommandFailure(command, model, UnmarkPlanCommand.MESSAGE_ALREADY_UNMARKED);
        assertSame(updated, model.getCurrentStudent().orElseThrow());
    }

    @Test
    public void execute_unmarkedPlan_throwsCommandExceptionWithoutChangingSelection() {
        for (WorkoutPlanStatus status : List.of(WorkoutPlanStatus.PENDING, WorkoutPlanStatus.OVERDUE)) {
            Student student = withPlans(ALICE, List.of(plan("Target", LocalDate.now().minusDays(1), status)));
            Model model = new ModelManager();
            model.addStudent(student);
            model.setCurrentStudent(student);

            assertCommandFailure(new UnmarkPlanCommand(Index.fromOneBased(1)), model,
                    UnmarkPlanCommand.MESSAGE_ALREADY_UNMARKED);
            assertSame(student, model.getCurrentStudent().orElseThrow());
        }
    }

    @Test
    public void execute_noStudentSelected_throwsCommandException() {
        Model model = new ModelManager();
        model.addStudent(ALICE);
        assertCommandFailure(new UnmarkPlanCommand(Index.fromOneBased(1)), model,
                UnmarkPlanCommand.MESSAGE_NO_STUDENT_SELECTED);
    }

    @Test
    public void execute_invalidIndex_throwsCommandExceptionWithoutChangingSelection() {
        Model model = new ModelManager();
        Student student = withPlans(ALICE, List.of(
                plan("Target", LocalDate.now(), WorkoutPlanStatus.COMPLETED)));
        model.addStudent(student);
        model.setCurrentStudent(student);

        assertCommandFailure(new UnmarkPlanCommand(Index.fromOneBased(2)), model,
                UnmarkPlanCommand.MESSAGE_INVALID_WORKOUT_ID);
        assertSame(student, model.getCurrentStudent().orElseThrow());
    }

    @Test
    public void execute_emptyPlans_throwsCommandException() {
        Model model = new ModelManager();
        model.addStudent(ALICE);
        model.setCurrentStudent(ALICE);
        assertCommandFailure(new UnmarkPlanCommand(Index.fromOneBased(1)), model,
                UnmarkPlanCommand.MESSAGE_INVALID_WORKOUT_ID);
    }

    @Test
    public void nullArguments_throwNullPointerException() {
        assertThrows(NullPointerException.class, () -> new UnmarkPlanCommand(null));
        assertThrows(NullPointerException.class, () ->
                new UnmarkPlanCommand(Index.fromOneBased(1)).execute(null));
    }

    @Test
    public void equals_comparesCommandTypeAndIndex() {
        UnmarkPlanCommand command = new UnmarkPlanCommand(Index.fromOneBased(1));
        assertEquals(command, command);
        assertEquals(command, new UnmarkPlanCommand(Index.fromOneBased(1)));
        assertNotEquals(command, new UnmarkPlanCommand(Index.fromOneBased(2)));
        assertNotEquals(command, new MarkPlanCommand(Index.fromOneBased(1)));
        assertNotEquals(command, new DeletePlanCommand(Index.fromOneBased(1)));
        assertNotEquals(command, null);
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        UnmarkPlanCommand command = new UnmarkPlanCommand(targetIndex);
        String expected = UnmarkPlanCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";

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
