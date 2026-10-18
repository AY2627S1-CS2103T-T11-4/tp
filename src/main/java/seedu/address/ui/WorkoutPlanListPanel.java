package seedu.address.ui;

import java.util.Locale;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import seedu.address.model.student.Student;
import seedu.address.model.workoutplan.WorkoutPlan;

/** Panel containing the workout plans for the currently selected student. */
public class WorkoutPlanListPanel extends UiPart<Region> {

    private static final String FXML = "WorkoutPlanListPanel.fxml";

    @FXML
    private Label heading;

    @FXML
    private Label studentAvatar;

    @FXML
    private Label subtitle;

    @FXML
    private Label emptyMessage;

    @FXML
    private ScrollPane workoutPlanScrollPane;

    @FXML
    private FlowPane workoutPlanFlowPane;

    /** Creates a panel in its initial state, before a student has been selected. */
    public WorkoutPlanListPanel() {
        super(FXML);
        showStudent(Optional.empty());
    }

    /** Displays the selected student's workout plans, or the initial prompt when no student is selected. */
    public void showStudent(Optional<Student> student) {
        if (student.isEmpty()) {
            heading.setText("Workout Plans");
            subtitle.setManaged(false);
            subtitle.setVisible(false);
            studentAvatar.setManaged(false);
            studentAvatar.setVisible(false);
            workoutPlanFlowPane.getChildren().clear();
            showEmptyMessage("Select a student to view workout plans.");
            return;
        }

        Student selectedStudent = student.get();
        String studentName = selectedStudent.getName().toString();
        heading.setText(studentName);
        subtitle.setText("Workout plans");
        subtitle.setManaged(true);
        subtitle.setVisible(true);
        studentAvatar.setText(getInitials(studentName));
        studentAvatar.setManaged(true);
        studentAvatar.setVisible(true);
        var workoutPlans = selectedStudent.getWorkoutPlansList().getWorkoutList();
        workoutPlanFlowPane.getChildren().clear();
        for (int index = 0; index < workoutPlans.size(); index++) {
            WorkoutPlan workoutPlan = workoutPlans.get(index);
            workoutPlanFlowPane.getChildren().add(new WorkoutPlanCard(workoutPlan, index + 1).getRoot());
        }

        if (workoutPlans.isEmpty()) {
            showEmptyMessage("No workout plans assigned.");
        } else {
            emptyMessage.setManaged(false);
            emptyMessage.setVisible(false);
            workoutPlanScrollPane.setManaged(true);
            workoutPlanScrollPane.setVisible(true);
        }
    }

    private String getInitials(String fullName) {
        String[] nameParts = fullName.trim().split("\\s+");
        StringBuilder initials = new StringBuilder(nameParts[0].substring(0, 1));
        if (nameParts.length > 1) {
            initials.append(nameParts[nameParts.length - 1].charAt(0));
        }
        return initials.toString().toUpperCase(Locale.ROOT);
    }

    private void showEmptyMessage(String message) {
        emptyMessage.setText(message);
        emptyMessage.setManaged(true);
        emptyMessage.setVisible(true);
        workoutPlanScrollPane.setManaged(false);
        workoutPlanScrollPane.setVisible(false);
    }
}
