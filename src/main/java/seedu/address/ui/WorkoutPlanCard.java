package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.util.Duration;
import seedu.address.model.tag.WorkoutPlanTag;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.WorkoutPlanStatus;

/** A UI component that displays one {@link WorkoutPlan}. */
public class WorkoutPlanCard extends UiPart<Region> {

    private static final String FXML = "WorkoutPlanCard.fxml";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy");
    private static final int DESCRIPTION_PREVIEW_LENGTH = 72;
    private static final int MAX_VISIBLE_TAGS = 3;

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
        requireNonNull(workoutPlan);
        assert displayedIndex > 0 : "displayed index must be positive";

        id.setText("#" + displayedIndex);
        setTitle(workoutPlan);
        setDate(workoutPlan);
        setStatus(workoutPlan);
        setDescription(workoutPlan);
        setTags(workoutPlan);
    }

    private void setTitle(WorkoutPlan workoutPlan) {
        String fullTitle = workoutPlan.getTitle().getValue();
        title.setText(fullTitle);
        title.setTooltip(createTooltip(fullTitle));
    }

    private void setDate(WorkoutPlan workoutPlan) {
        date.setText(workoutPlan.getDate().getValue().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .format(DATE_FORMATTER).toUpperCase(Locale.ROOT));
    }

    private void setStatus(WorkoutPlan workoutPlan) {
        status.setText(getStatusText(workoutPlan.getStatus()));
        status.getStyleClass().add(getStatusStyleClass(workoutPlan.getStatus()));
        id.getStyleClass().add(getStatusStyleClass(workoutPlan.getStatus()));
        getRoot().getStyleClass().add(getCardStyleClass(workoutPlan.getStatus()));
    }

    private void setDescription(WorkoutPlan workoutPlan) {
        String fullDescription = workoutPlan.getWorkouts().getValue();
        description.setText(getDescriptionPreview(fullDescription));
        description.setTooltip(createTooltip(fullDescription));
    }

    private void setTags(WorkoutPlan workoutPlan) {
        List<WorkoutPlanTag> sortedTags = getSortedTags(workoutPlan);
        String allTagNames = sortedTags.stream()
                .map(tag -> tag.tagName)
                .collect(Collectors.joining(", "));
        sortedTags.stream().limit(MAX_VISIBLE_TAGS)
                .forEach(tag -> addTagLabel(tag.tagName, allTagNames));
        int hiddenTagCount = sortedTags.size() - MAX_VISIBLE_TAGS;
        if (hiddenTagCount > 0) {
            addTagLabel("+" + hiddenTagCount, allTagNames);
        }
        if (!sortedTags.isEmpty()) {
            Tooltip.install(tags, createTooltip(allTagNames));
        }
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

    /** Returns a shortened description suitable for a fixed-height workout plan card. */
    static String getDescriptionPreview(String description) {
        requireNonNull(description);
        String normalizedDescription = description.replaceAll("\\s+", " ").trim();
        if (normalizedDescription.length() <= DESCRIPTION_PREVIEW_LENGTH) {
            return normalizedDescription;
        }

        int maximumContentLength = DESCRIPTION_PREVIEW_LENGTH - 3;
        int breakPosition = normalizedDescription.lastIndexOf(' ', maximumContentLength);
        if (breakPosition < DESCRIPTION_PREVIEW_LENGTH / 2) {
            breakPosition = maximumContentLength;
        }
        return normalizedDescription.substring(0, breakPosition).stripTrailing() + "...";
    }

    private static Tooltip createTooltip(String text) {
        Tooltip tooltip = new Tooltip(text);
        tooltip.setWrapText(true);
        tooltip.setMaxWidth(360);
        tooltip.setShowDelay(Duration.millis(100));
        tooltip.setHideDelay(Duration.millis(100));
        tooltip.setShowDuration(Duration.seconds(30));
        return tooltip;
    }

    private void addTagLabel(String text, String tooltipText) {
        Label tagLabel = new Label(text);
        tagLabel.setTooltip(createTooltip(tooltipText));
        tags.getChildren().add(tagLabel);
    }
}
