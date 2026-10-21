package seedu.address.ui;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
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

    /** Creates a card for the given workout plan and displayed index. */
    public WorkoutPlanCard(WorkoutPlan workoutPlan, int displayedIndex) {
        super(FXML);
        id.setText("#" + displayedIndex);
        title.setText(workoutPlan.getTitle().getValue());
        date.setText(workoutPlan.getDate().getValue().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .format(DATE_FORMATTER).toUpperCase(Locale.ROOT));
        status.setText(getStatusText(workoutPlan.getStatus()));
        status.getStyleClass().add(getStatusStyleClass(workoutPlan.getStatus()));
        description.setText(workoutPlan.getWorkouts().getValue());
        getSortedTags(workoutPlan).forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }

    /** Returns the display text for a workout plan status. */
    static String getStatusText(WorkoutPlanStatus status) {
        return switch (status) {
            case COMPLETED -> "DONE";
            case OVERDUE -> "OVERDUE";
            case PENDING -> "PENDING";
        };
    }

    /** Returns the CSS class for a workout plan status. */
    static String getStatusStyleClass(WorkoutPlanStatus status) {
        return switch (status) {
            case COMPLETED -> "workout-status-completed";
            case OVERDUE -> "workout-status-overdue";
            case PENDING -> "workout-status-pending";
        };
    }

    /** Returns the workout plan's tags sorted by name for consistent display. */
    static List<WorkoutPlanTag> getSortedTags(WorkoutPlan workoutPlan) {
        return workoutPlan.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .toList();
    }
}
