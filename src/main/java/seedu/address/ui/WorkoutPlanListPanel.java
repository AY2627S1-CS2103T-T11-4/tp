package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.exercise.Exercise;
import seedu.address.model.student.Student;
import seedu.address.model.workoutplan.WorkoutPlan;

/** Panel containing the workout plans for the currently selected student. */
public class WorkoutPlanListPanel extends UiPart<Region> {

    private static final String FXML = "WorkoutPlanListPanel.fxml";

    @FXML
    private Label heading;

    @FXML
    private Label subtitle;

    @FXML
    private Label emptyMessage;

    @FXML
    private ScrollPane workoutPlanScrollPane;

    @FXML
    private VBox workoutPlanList;

    /** Creates a panel in its initial state, before a student has been selected. */
    public WorkoutPlanListPanel() {
        super(FXML);
        showStudent(Optional.empty());
    }

    /** Displays the selected student's workout plans, or the initial prompt when no student is selected. */
    public void showStudent(Optional<Student> student) {
        showStudent(student, Optional.empty());
    }

    /** Displays the selected plan's ordered exercises when one is selected. */
    public void showStudent(Optional<Student> student, Optional<WorkoutPlan> selectedPlan) {
        requireNonNull(student);
        requireNonNull(selectedPlan);
        if (student.isEmpty()) {
            heading.setText("Workout Plans");
            subtitle.setManaged(false);
            subtitle.setVisible(false);
            workoutPlanList.getChildren().clear();
            showEmptyMessage("Select a student to view workout plans.");
            return;
        }

        Student selectedStudent = student.get();
        String studentName = selectedStudent.getName().toString();
        heading.setText(studentName);
        subtitle.setText(selectedPlan.map(workoutPlan -> "Workout plan: " + workoutPlan.getTitle())
                .orElse("Workout plans"));
        subtitle.setManaged(true);
        subtitle.setVisible(true);
        if (selectedPlan.isPresent()) {
            showSelectedPlan(selectedPlan.get());
            return;
        }
        List<WorkoutPlan> workoutPlans = selectedStudent.getWorkoutPlansList().getWorkoutList();
        workoutPlanList.getChildren().clear();
        for (int index = 0; index < workoutPlans.size(); index++) {
            WorkoutPlan workoutPlan = workoutPlans.get(index);
            workoutPlanList.getChildren().add(new WorkoutPlanCard(workoutPlan, index + 1).getRoot());
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

    private void showSelectedPlan(WorkoutPlan selectedPlan) {
        workoutPlanList.getChildren().clear();
        VBox selectedPlanDetails = new VBox(10);
        selectedPlanDetails.getStyleClass().add("exercise-list");
        selectedPlanDetails.prefWidthProperty().bind(workoutPlanScrollPane.widthProperty().subtract(40));
        Label planDescription = new Label(selectedPlan.getWorkouts().getValue());
        planDescription.setWrapText(true);
        planDescription.getStyleClass().add("workout-plan-description");
        selectedPlanDetails.getChildren().add(planDescription);

        List<Exercise> exercises = selectedPlan.getExercisesList().getExerciseList();
        if (exercises.isEmpty()) {
            Label emptyExercisesMessage = new Label("No exercises in this workout plan.");
            emptyExercisesMessage.getStyleClass().add("empty-message");
            selectedPlanDetails.getChildren().add(emptyExercisesMessage);
        } else {
            for (int index = 0; index < exercises.size(); index++) {
                Exercise exercise = exercises.get(index);
                Label exerciseLabel = new Label((index + 1) + ". " + exercise.getName()
                        + "\n" + exercise.getDescription());
                exerciseLabel.setWrapText(true);
                exerciseLabel.setMaxWidth(Double.MAX_VALUE);
                exerciseLabel.getStyleClass().add("exercise-card");
                selectedPlanDetails.getChildren().add(exerciseLabel);
            }
        }
        workoutPlanList.getChildren().add(selectedPlanDetails);
        emptyMessage.setManaged(false);
        emptyMessage.setVisible(false);
        workoutPlanScrollPane.setManaged(true);
        workoutPlanScrollPane.setVisible(true);
    }

    private void showEmptyMessage(String message) {
        emptyMessage.setText(message);
        emptyMessage.setManaged(true);
        emptyMessage.setVisible(true);
        workoutPlanScrollPane.setManaged(false);
        workoutPlanScrollPane.setVisible(false);
    }
}
