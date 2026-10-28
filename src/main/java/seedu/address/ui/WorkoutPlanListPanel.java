package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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

    /** Displays either the student's plans or the selected plan's exercises. */
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
        List<WorkoutPlan> workoutPlans = selectedStudent.getWorkoutPlansList().getWorkoutList();
        int selectedPlanIndex = plan.map(value -> indexOfInstance(workoutPlans, value)).orElse(-1);
        heading.setText(studentName);
        subtitle.setText(selectedPlanIndex < 0 ? "Workout plans"
                : "Plan #" + (selectedPlanIndex + 1) + ": " + plan.get().getTitle());
        subtitle.setManaged(true);
        subtitle.setVisible(true);
        if (selectedPlanIndex >= 0) {
            showSelectedPlan(workoutPlans.get(selectedPlanIndex), selectedPlanIndex + 1);
            return;
        }
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

    private void showSelectedPlan(WorkoutPlan plan, int displayedIndex) {
        workoutPlanList.getChildren().clear();
        Region planCard = new WorkoutPlanCard(plan, displayedIndex).getRoot();
        planCard.getStyleClass().add("workout-plan-selected");
        workoutPlanList.getChildren().add(planCard);

        Label exercisesHeading = new Label("Exercises");
        exercisesHeading.getStyleClass().add("exercise-section-heading");
        workoutPlanList.getChildren().add(exercisesHeading);
        List<Exercise> exercises = plan.getExercisesList().getExerciseList();
        if (exercises.isEmpty()) {
            Label noExercises = new Label("No exercises in this workout plan.");
            noExercises.getStyleClass().add("empty-message");
            workoutPlanList.getChildren().add(noExercises);
        } else {
            for (int index = 0; index < exercises.size(); index++) {
                workoutPlanList.getChildren().add(createExerciseEntry(exercises.get(index), index + 1));
            }
        }
        emptyMessage.setManaged(false);
        emptyMessage.setVisible(false);
        workoutPlanScrollPane.setManaged(true);
        workoutPlanScrollPane.setVisible(true);
    }

    private static HBox createExerciseEntry(Exercise exercise, int displayedIndex) {
        HBox entry = new HBox(10);
        entry.setMaxWidth(Double.MAX_VALUE);
        entry.getStyleClass().add("exercise-entry");

        Label number = new Label(displayedIndex + ".");
        number.getStyleClass().add("exercise-number");
        Label name = new Label(exercise.getName().getValue());
        name.setWrapText(true);
        name.getStyleClass().add("exercise-name");
        Label description = new Label(exercise.getDescription().getValue());
        description.setWrapText(true);
        description.getStyleClass().add("exercise-description");

        VBox text = new VBox(3, name, description);
        text.setMinWidth(0);
        HBox.setHgrow(text, Priority.ALWAYS);
        entry.getChildren().addAll(number, text);
        return entry;
    }

    private static int indexOfInstance(List<WorkoutPlan> plans, WorkoutPlan selectedPlan) {
        for (int index = 0; index < plans.size(); index++) {
            if (plans.get(index) == selectedPlan) {
                return index;
            }
        }
        return -1;
    }

    private void showEmptyMessage(String message) {
        emptyMessage.setText(message);
        emptyMessage.setManaged(true);
        emptyMessage.setVisible(true);
        workoutPlanScrollPane.setManaged(false);
        workoutPlanScrollPane.setVisible(false);
    }
}
