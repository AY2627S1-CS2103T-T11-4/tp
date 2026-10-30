package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
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

    private Student renderedStudent;
    private WorkoutPlan renderedSelectedPlan;
    private int renderedSelectedPlanIndex = -1;
    private int renderedWorkoutPlanCount;
    private long renderGeneration;

    /** Creates a panel in its initial state, before a student has been selected. */
    public WorkoutPlanListPanel() {
        super(FXML);
        showStudent(Optional.empty());
    }

    /** Displays the selected student's workout plans, or the initial prompt when no student is selected. */
    public void showStudent(Optional<Student> student) {
        showStudent(student, Optional.empty());
    }

    /** Displays all of the student's plans and expands the selected plan's exercises, if any. */
    public void showStudent(Optional<Student> student, Optional<WorkoutPlan> selectedPlan) {
        requireNonNull(student);
        requireNonNull(selectedPlan);
        long currentRenderGeneration = ++renderGeneration;
        if (student.isEmpty()) {
            heading.setText("Workout Plans");
            subtitle.setManaged(false);
            subtitle.setVisible(false);
            workoutPlanList.getChildren().clear();
            showEmptyMessage("Select a student to view workout plans.");
            clearRenderedState();
            return;
        }

        double previousScrollOffset = getScrollOffset();
        Student previousStudent = renderedStudent;
        WorkoutPlan previousSelectedPlan = renderedSelectedPlan;
        int previousSelectedPlanIndex = renderedSelectedPlanIndex;
        int previousWorkoutPlanCount = renderedWorkoutPlanCount;
        Student selectedStudent = student.get();
        String studentName = selectedStudent.getName().toString();
        List<WorkoutPlan> workoutPlans = selectedStudent.getWorkoutPlansList().getWorkoutList();
        int selectedPlanIndex = selectedPlan.map(value -> indexOfInstance(workoutPlans, value)).orElse(-1);
        boolean sameStudentContext = hasSameStudentDetails(previousStudent, selectedStudent);
        boolean retainedSelection = selectedPlanIndex >= 0
                && (selectedPlan.get() == previousSelectedPlan
                        || sameStudentContext && selectedPlanIndex == previousSelectedPlanIndex);
        boolean selectedPlanWasDeleted = selectedPlanIndex < 0 && previousSelectedPlan != null
                && sameStudentContext && workoutPlans.size() < previousWorkoutPlanCount
                && indexOfInstance(workoutPlans, previousSelectedPlan) < 0;

        heading.setText(studentName);
        subtitle.setText(selectedPlanIndex < 0 ? "Workout plans"
                : "Workout plans · #" + (selectedPlanIndex + 1) + " selected");
        subtitle.setManaged(true);
        subtitle.setVisible(true);
        workoutPlanList.getChildren().clear();
        Region selectedCard = null;
        for (int index = 0; index < workoutPlans.size(); index++) {
            WorkoutPlan workoutPlan = workoutPlans.get(index);
            boolean isExpanded = index == selectedPlanIndex;
            Region planCard = new WorkoutPlanCard(workoutPlan, index + 1, isExpanded).getRoot();
            workoutPlanList.getChildren().add(planCard);
            if (isExpanded) {
                selectedCard = planCard;
            }
        }

        if (workoutPlans.isEmpty()) {
            showEmptyMessage("No workout plans assigned.");
        } else {
            emptyMessage.setManaged(false);
            emptyMessage.setVisible(false);
            workoutPlanScrollPane.setManaged(true);
            workoutPlanScrollPane.setVisible(true);
        }

        renderedStudent = selectedStudent;
        renderedSelectedPlan = selectedPlanIndex < 0 ? null : selectedPlan.get();
        renderedSelectedPlanIndex = selectedPlanIndex;
        renderedWorkoutPlanCount = workoutPlans.size();
        if (selectedCard != null && !retainedSelection) {
            revealCard(selectedCard, currentRenderGeneration);
        } else if (retainedSelection || selectedPlanWasDeleted
                || previousSelectedPlan == null && sameStudentContext) {
            restoreScrollOffset(previousScrollOffset, currentRenderGeneration);
        } else {
            workoutPlanScrollPane.setVvalue(0);
        }
    }

    /** Resets the outer workout-list viewport before navigation refreshes the panel. */
    void resetScrollPosition() {
        renderGeneration++;
        workoutPlanScrollPane.setVvalue(0);
    }

    private static int indexOfInstance(List<WorkoutPlan> workoutPlans, WorkoutPlan selectedPlan) {
        for (int index = 0; index < workoutPlans.size(); index++) {
            if (workoutPlans.get(index) == selectedPlan) {
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

    private void clearRenderedState() {
        renderedStudent = null;
        renderedSelectedPlan = null;
        renderedSelectedPlanIndex = -1;
        renderedWorkoutPlanCount = 0;
        workoutPlanScrollPane.setVvalue(0);
    }

    private double getScrollOffset() {
        double scrollableHeight = Math.max(0,
                workoutPlanList.getBoundsInLocal().getHeight()
                        - workoutPlanScrollPane.getViewportBounds().getHeight());
        return workoutPlanScrollPane.getVvalue() * scrollableHeight;
    }

    private void restoreScrollOffset(double scrollOffset, long scheduledGeneration) {
        Platform.runLater(() -> {
            if (scheduledGeneration != renderGeneration) {
                return;
            }
            refreshLayout();
            double scrollableHeight = Math.max(0,
                    workoutPlanList.getBoundsInLocal().getHeight()
                            - workoutPlanScrollPane.getViewportBounds().getHeight());
            workoutPlanScrollPane.setVvalue(scrollableHeight == 0
                    ? 0 : clamp(scrollOffset / scrollableHeight));
        });
    }

    private void revealCard(Region card, long scheduledGeneration) {
        Platform.runLater(() -> {
            if (scheduledGeneration != renderGeneration) {
                return;
            }
            refreshLayout();
            double viewportHeight = workoutPlanScrollPane.getViewportBounds().getHeight();
            double contentHeight = workoutPlanList.getBoundsInLocal().getHeight();
            double scrollableHeight = Math.max(0, contentHeight - viewportHeight);
            if (scrollableHeight == 0) {
                workoutPlanScrollPane.setVvalue(0);
                return;
            }

            Bounds cardBounds = card.getBoundsInParent();
            double currentOffset = workoutPlanScrollPane.getVvalue() * scrollableHeight;
            double targetOffset = currentOffset;
            if (cardBounds.getHeight() >= viewportHeight || cardBounds.getMinY() < currentOffset) {
                targetOffset = cardBounds.getMinY();
            } else if (cardBounds.getMaxY() > currentOffset + viewportHeight) {
                targetOffset = cardBounds.getMaxY() - viewportHeight;
            }
            workoutPlanScrollPane.setVvalue(clamp(targetOffset / scrollableHeight));
        });
    }

    private void refreshLayout() {
        getRoot().applyCss();
        getRoot().layout();
    }

    private static boolean hasSameStudentDetails(Student first, Student second) {
        return first != null
                && first.getName().equals(second.getName())
                && first.getPhone().equals(second.getPhone())
                && first.getEmail().equals(second.getEmail())
                && first.getAddress().equals(second.getAddress())
                && first.getTags().equals(second.getTags());
    }

    private static double clamp(double value) {
        return Math.max(0, Math.min(1, value));
    }
}
