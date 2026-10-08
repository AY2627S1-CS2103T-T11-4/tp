package seedu.address.ui;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.address.model.workoutplan.WorkoutPlan;

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
    private Label description;

    /** Creates a card for the given workout plan and displayed index. */
    public WorkoutPlanCard(WorkoutPlan workoutPlan, int displayedIndex) {
        super(FXML);
        id.setText(displayedIndex + ". ");
        title.setText(workoutPlan.getTitle().getValue());
        date.setText(workoutPlan.getDate().getValue().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .format(DATE_FORMATTER));
        description.setText(workoutPlan.getWorkouts().getValue());
    }
}
