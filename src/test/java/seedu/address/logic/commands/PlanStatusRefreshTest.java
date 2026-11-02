package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.TypicalStudents.ALICE;

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

/** Tests refreshing stale plans when commands operate on an already open student. */
public class PlanStatusRefreshTest {

    @Test
    public void execute_addPlan_refreshesExistingAndAddedPlans() throws Exception {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        Model model = modelWithPlans(plan("First", yesterday, WorkoutPlanStatus.PENDING));

        new AddPlanCommand(plan("Second", yesterday, WorkoutPlanStatus.PENDING)).execute(model);

        assertBothPlansOverdue(model);
    }

    @Test
    public void execute_editPlan_refreshesExistingAndEditedPlans() throws Exception {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        Model model = modelWithPlans(
                plan("First", yesterday, WorkoutPlanStatus.PENDING),
                plan("Second", LocalDate.now().plusDays(1), WorkoutPlanStatus.PENDING));
        EditPlanCommand.EditPlanDescriptor descriptor = new EditPlanCommand.EditPlanDescriptor();
        descriptor.setDate(WorkoutDate.fromLocalDate(yesterday));

        new EditPlanCommand(Index.fromOneBased(2), descriptor).execute(model);

        assertBothPlansOverdue(model);
    }

    private static void assertBothPlansOverdue(Model model) {
        List<WorkoutPlan> plans = model.getCurrentStudent().orElseThrow().getWorkoutPlansList().getWorkoutList();
        assertEquals(2, plans.size());
        assertEquals(WorkoutPlanStatus.OVERDUE, plans.get(0).getStatus());
        assertEquals(WorkoutPlanStatus.OVERDUE, plans.get(1).getStatus());
        assertEquals(plans.get(0).getDate(), plans.get(1).getDate());
    }

    private static Model modelWithPlans(WorkoutPlan... plans) {
        Student student = ALICE.withWorkoutPlans(new WorkoutPlansList(List.of(plans)));
        Model model = new ModelManager();
        model.addStudent(student);
        model.setCurrentStudent(student);
        return model;
    }

    private static WorkoutPlan plan(String title, LocalDate date, WorkoutPlanStatus status) {
        return new WorkoutPlan(new WorkoutTitle(title), new WorkoutDescription("Test"),
                WorkoutDate.fromLocalDate(date), Set.of(), status);
    }
}
