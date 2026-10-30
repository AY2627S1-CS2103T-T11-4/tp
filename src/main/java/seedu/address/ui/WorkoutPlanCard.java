package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.exercise.Exercise;
import seedu.address.model.tag.WorkoutPlanTag;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.WorkoutPlanStatus;

/** A UI component that displays one {@link WorkoutPlan}. */
public class WorkoutPlanCard extends UiPart<Region> {

    private static final String FXML = "WorkoutPlanCard.fxml";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy");

    @FXML
    private Label id;

    @FXML
    private Label title;

    @FXML
    private Label date;

    @FXML
    private Label status;

    @FXML
    private Label description;

    @FXML
    private FlowPane tags;

    @FXML
    private VBox exerciseSection;

    @FXML
    private Label exercisesHeading;

    @FXML
    private VBox exerciseList;

    /** Creates a card for the given workout plan and displayed index. */
    public WorkoutPlanCard(WorkoutPlan workoutPlan, int displayedIndex) {
        this(workoutPlan, displayedIndex, false);
    }

    /** Creates a card for the given workout plan, expanding its exercises when requested. */
    public WorkoutPlanCard(WorkoutPlan workoutPlan, int displayedIndex, boolean isExpanded) {
        super(FXML);
        requireNonNull(workoutPlan);
        assert displayedIndex > 0 : "displayed index must be positive";
        id.setText("#" + displayedIndex);
        title.setText(workoutPlan.getTitle().getValue());
        date.setText(workoutPlan.getDate().getValue().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .format(DATE_FORMATTER).toUpperCase(Locale.ROOT));
        status.setText(getStatusText(workoutPlan.getStatus()));
        status.getStyleClass().add(getStatusStyleClass(workoutPlan.getStatus()));
        id.getStyleClass().add(getStatusStyleClass(workoutPlan.getStatus()));
        getRoot().getStyleClass().add(getCardStyleClass(workoutPlan.getStatus()));
        description.setText(workoutPlan.getWorkouts().getValue());
        getSortedTags(workoutPlan).forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
        configureExerciseSection(workoutPlan, isExpanded);
    }

    private void configureExerciseSection(WorkoutPlan workoutPlan, boolean isExpanded) {
        exerciseSection.setManaged(isExpanded);
        exerciseSection.setVisible(isExpanded);
        if (!isExpanded) {
            return;
        }

        getRoot().getStyleClass().add("workout-plan-selected");
        List<Exercise> exercises = workoutPlan.getExercisesList().getExerciseList();
        exercisesHeading.setText("Exercises (" + exercises.size() + ")");
        if (exercises.isEmpty()) {
            Label noExercises = new Label("No exercises in this workout plan.");
            noExercises.setWrapText(true);
            noExercises.getStyleClass().add("exercise-empty-message");
            exerciseList.getChildren().add(noExercises);
            return;
        }

        for (int index = 0; index < exercises.size(); index++) {
            exerciseList.getChildren().add(createExerciseEntry(exercises.get(index), index + 1));
        }
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
        Label exerciseDescription = new Label(exercise.getDescription().getValue());
        exerciseDescription.setWrapText(true);
        exerciseDescription.getStyleClass().add("exercise-description");

        VBox text = new VBox(3, name, exerciseDescription);
        text.setMinWidth(0);
        HBox.setHgrow(text, Priority.ALWAYS);
        entry.getChildren().addAll(number, text);
        return entry;
    }

    /** Returns the display text for a workout plan status. */
    static String getStatusText(WorkoutPlanStatus status) {
        return switch (status) {
            case COMPLETED -> "DONE";
            case OVERDUE -> "OVERDUE";
            case PENDING -> "PENDING";
        };
    }

    /** Returns the badge-level CSS class for a workout plan status. */
    static String getStatusStyleClass(WorkoutPlanStatus status) {
        return switch (status) {
            case COMPLETED -> "workout-status-completed";
            case OVERDUE -> "workout-status-overdue";
            case PENDING -> "workout-status-pending";
        };
    }

    /** Returns the card-level CSS class for a workout plan status. */
    static String getCardStyleClass(WorkoutPlanStatus status) {
        return switch (status) {
            case COMPLETED -> "workout-plan-completed";
            case OVERDUE -> "workout-plan-overdue";
            case PENDING -> "workout-plan-pending";
        };
    }

    /** Returns the workout plan's tags sorted by name for consistent display. */
    static List<WorkoutPlanTag> getSortedTags(WorkoutPlan workoutPlan) {
        requireNonNull(workoutPlan);
        return workoutPlan.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .toList();
    }
}
