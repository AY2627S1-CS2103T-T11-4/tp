package seedu.address.ui;

import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.model.student.Student;
import seedu.address.model.workoutplan.WorkoutPlan;

/** Panel containing the workout plans for the currently selected student. */
public class WorkoutPlanListPanel extends UiPart<Region> {

    private static final String FXML = "WorkoutPlanListPanel.fxml";

    @FXML
    private Label heading;

    @FXML
    private Label emptyMessage;

    @FXML
    private ListView<WorkoutPlan> workoutPlanListView;

    /** Creates a panel in its initial state, before a student has been selected. */
    public WorkoutPlanListPanel() {
        super(FXML);
        workoutPlanListView.setCellFactory(listView -> new WorkoutPlanListViewCell());
        showStudent(Optional.empty());
    }

    /** Displays the selected student's workout plans, or the initial prompt when no student is selected. */
    public void showStudent(Optional<Student> student) {
        if (student.isEmpty()) {
            heading.setText("Workout Plans");
            workoutPlanListView.setItems(FXCollections.emptyObservableList());
            showEmptyMessage("Select a student to view workout plans.");
            return;
        }

        Student selectedStudent = student.get();
        heading.setText(selectedStudent.getName() + "'s Workout Plans");
        var workoutPlans = selectedStudent.getWorkoutPlansList().getWorkoutList();
        workoutPlanListView.setItems(FXCollections.observableArrayList(workoutPlans));

        if (workoutPlans.isEmpty()) {
            showEmptyMessage("No workout plans assigned.");
        } else {
            emptyMessage.setManaged(false);
            emptyMessage.setVisible(false);
            workoutPlanListView.setManaged(true);
            workoutPlanListView.setVisible(true);
        }
    }

    private void showEmptyMessage(String message) {
        emptyMessage.setText(message);
        emptyMessage.setManaged(true);
        emptyMessage.setVisible(true);
        workoutPlanListView.setManaged(false);
        workoutPlanListView.setVisible(false);
    }

    /** Custom list cell that renders a workout plan using {@link WorkoutPlanCard}. */
    static class WorkoutPlanListViewCell extends ListCell<WorkoutPlan> {
        @Override
        protected void updateItem(WorkoutPlan workoutPlan, boolean empty) {
            super.updateItem(workoutPlan, empty);

            if (empty || workoutPlan == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new WorkoutPlanCard(workoutPlan, getIndex() + 1).getRoot());
            }
        }
    }
}
