package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import seedu.address.model.student.Address;
import seedu.address.model.student.Email;
import seedu.address.model.student.Name;
import seedu.address.model.student.Phone;
import seedu.address.model.student.Student;
import seedu.address.model.workoutplan.WorkoutDate;
import seedu.address.model.workoutplan.WorkoutDescription;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.WorkoutPlansList;
import seedu.address.model.workoutplan.WorkoutTitle;

@ExtendWith(ApplicationExtension.class)
public class WorkoutPlanUiTest {

    private static final LocalDate PLAN_DATE = LocalDate.of(2025, 10, 8);
    private static final WorkoutPlan PLAN = new WorkoutPlan(
            new WorkoutTitle("Strength"),
            new WorkoutDescription("Squats and push-ups"),
            new WorkoutDate(Date.from(PLAN_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant())));

    @Test
    public void workoutPlanCard_validPlan_displaysPlanDetails() {
        WorkoutPlanCard card = new WorkoutPlanCard(PLAN, 2);
        Parent root = card.getRoot();

        assertEquals("2. ", getLabel(root, "#id").getText());
        assertEquals("Strength", getLabel(root, "#title").getText());
        assertEquals("08 Oct 2025", getLabel(root, "#date").getText());
        assertEquals("Squats and push-ups", getLabel(root, "#description").getText());
    }

    @Test
    public void workoutPlanListPanel_noStudentSelected_displaysInitialPrompt() {
        WorkoutPlanListPanel panel = new WorkoutPlanListPanel();
        Parent root = panel.getRoot();
        Label emptyMessage = getLabel(root, "#emptyMessage");
        ListView<WorkoutPlan> listView = getListView(root);

        assertEquals("Workout Plans", getLabel(root, "#heading").getText());
        assertEquals("Select a student to view workout plans.", emptyMessage.getText());
        assertTrue(emptyMessage.isVisible());
        assertFalse(listView.isVisible());
        assertTrue(listView.getItems().isEmpty());
    }

    @Test
    public void showStudent_studentWithoutPlans_displaysEmptyMessage() {
        WorkoutPlanListPanel panel = new WorkoutPlanListPanel();
        Parent root = panel.getRoot();

        panel.showStudent(Optional.of(createStudent(List.of())));

        assertEquals("Alex's Workout Plans", getLabel(root, "#heading").getText());
        assertEquals("No workout plans assigned.", getLabel(root, "#emptyMessage").getText());
        assertFalse(getListView(root).isVisible());
    }

    @Test
    public void showStudent_studentWithPlans_displaysWorkoutPlans() {
        WorkoutPlanListPanel panel = new WorkoutPlanListPanel();
        Parent root = panel.getRoot();
        Label emptyMessage = getLabel(root, "#emptyMessage");
        ListView<WorkoutPlan> listView = getListView(root);

        panel.showStudent(Optional.of(createStudent(List.of(PLAN))));

        assertEquals("Alex's Workout Plans", getLabel(root, "#heading").getText());
        assertFalse(emptyMessage.isVisible());
        assertFalse(emptyMessage.isManaged());
        assertTrue(listView.isVisible());
        assertTrue(listView.isManaged());
        assertEquals(List.of(PLAN), listView.getItems());
    }

    @Test
    public void workoutPlanListViewCell_updateItem_setsExpectedGraphic() {
        WorkoutPlanListPanel.WorkoutPlanListViewCell cell =
                new WorkoutPlanListPanel.WorkoutPlanListViewCell();

        cell.updateItem(null, true);
        assertNull(cell.getGraphic());
        assertNull(cell.getText());

        cell.updateItem(PLAN, false);
        assertNotNull(cell.getGraphic());
    }

    private static Student createStudent(List<WorkoutPlan> workoutPlans) {
        return new Student(new Name("Alex"), new Phone("87438807"),
                new Email("alex@example.com"), new Address("123 Clementi Road"), Set.of(),
                new WorkoutPlansList(workoutPlans));
    }

    private static Label getLabel(Parent root, String selector) {
        return (Label) root.lookup(selector);
    }

    @SuppressWarnings("unchecked")
    private static ListView<WorkoutPlan> getListView(Parent root) {
        return (ListView<WorkoutPlan>) root.lookup("#workoutPlanListView");
    }
}
