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
    public void showStudent(Optional<Student> student, Optional<WorkoutPlan> plan) {
        requireNonNull(student);
        requireNonNull(plan);
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
        subtitle.setText(plan.map(value -> "Workout plan: " + value.getTitle()).orElse("Workout plans"));
        subtitle.setManaged(true);
        subtitle.setVisible(true);
        if (plan.isPresent()) {
            showSelectedPlan(plan.get());
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

    private void showSelectedPlan(WorkoutPlan plan) {
        workoutPlanList.getChildren().clear();
        VBox details = new VBox(10);
        details.getStyleClass().add("exercise-list");
        details.prefWidthProperty().bind(workoutPlanScrollPane.widthProperty().subtract(40));
        Label planDetails = new Label(plan.getWorkouts().getValue());
        planDetails.setWrapText(true);
        planDetails.getStyleClass().add("exercise-plan-description");
        details.getChildren().add(planDetails);

        List<Exercise> exercises = plan.getExercisesList().getExerciseList();
        if (exercises.isEmpty()) {
            Label emptyExercises = new Label("No exercises in this workout plan.");
            emptyExercises.getStyleClass().add("empty-message");
            details.getChildren().add(emptyExercises);
        } else {
            for (int index = 0; index < exercises.size(); index++) {
                Exercise exercise = exercises.get(index);
                Label exerciseCard = new Label((index + 1) + ". " + exercise.getName()
                        + "\n" + exercise.getDescription());
                exerciseCard.setWrapText(true);
                exerciseCard.setMaxWidth(Double.MAX_VALUE);
                exerciseCard.getStyleClass().add("exercise-card");
                details.getChildren().add(exerciseCard);
            }
        }
        workoutPlanList.getChildren().add(details);
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
