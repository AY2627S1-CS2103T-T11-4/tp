package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalStudents.ALICE;

import java.awt.GraphicsEnvironment;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.exercise.Exercise;
import seedu.address.model.exercise.ExerciseDescription;
import seedu.address.model.exercise.ExerciseName;
import seedu.address.model.exercise.ExercisesList;
import seedu.address.model.student.Student;
import seedu.address.model.tag.WorkoutPlanTag;
import seedu.address.model.workoutplan.WorkoutDate;
import seedu.address.model.workoutplan.WorkoutDescription;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.WorkoutPlanStatus;
import seedu.address.model.workoutplan.WorkoutPlansList;
import seedu.address.model.workoutplan.WorkoutTitle;

/** Verifies the selected workout plan view on hosts with a graphical display. */
public class WorkoutPlanListPanelTest {

    private static boolean startedToolkit;

    @BeforeAll
    public static void startToolkit() {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless());
        try {
            Platform.startup(() -> { });
            startedToolkit = true;
        } catch (IllegalStateException e) {
            // Another UI test already started the toolkit.
        }
    }

    @AfterAll
    public static void stopToolkit() {
        if (startedToolkit) {
            Platform.exit();
        }
    }

    // EP: populated plan renders ordered exercises (selected plan).
    @Test
    public void selectedPlan_showsIdentityDetailsAndOrderedExercises() throws Exception {
        Exercise first = exercise("Squats", "Barbell back squat");
        Exercise second = exercise("Push ups", "Bodyweight exercise");
        WorkoutPlan plan = plan(List.of(first, second));
        Student student = withPlan(plan);

        onFxThread(() -> {
            WorkoutPlanListPanel panel = new WorkoutPlanListPanel();
            new Scene(panel.getRoot(), 500, 600);
            panel.showStudent(Optional.of(student), Optional.of(plan));
            Region root = panel.getRoot();
            refresh(root);

            assertEquals(student.getName().toString(), label(root, ".selected-student-name").getText());
            assertEquals("Workout plans · #1 selected", label(root, ".selected-student-subtitle").getText());
            Region selectedCard = (Region) root.lookup(".workout-plan-selected");
            assertNotNull(selectedCard);
            assertEquals(1, root.lookupAll(".workout-plan-card").size());
            assertEquals("DONE", label(root, ".workout-status-badge").getText());
            assertEquals("Plan notes", label(root, ".workout-plan-description").getText());
            assertEquals("Exercises (2)", label(selectedCard, ".exercise-section-heading").getText());
            assertEquals(2, selectedCard.lookupAll(".exercise-entry").size());
            assertEquals(List.of("1.", "2."), root.lookupAll(".exercise-number").stream()
                    .map(node -> ((Label) node).getText()).sorted().toList());
            assertEquals(List.of("Push ups", "Squats"), root.lookupAll(".exercise-name").stream()
                    .map(node -> ((Label) node).getText()).sorted().toList());
            return null;
        });
    }

    // BV: 0 <= count <= MAX_EXERCISES (valid).
    @Test
    public void emptyPlanAndNavigation_clearOldExerciseEntries() throws Exception {
        WorkoutPlan populated = plan(List.of(exercise("Squats", "Barbell back squat")));
        WorkoutPlan empty = new WorkoutPlan(new WorkoutTitle("Mobility"), new WorkoutDescription("Plan notes"),
                WorkoutDate.fromLocalDate(LocalDate.now().plusDays(1)),
                Set.of(new WorkoutPlanTag("mobility")), WorkoutPlanStatus.COMPLETED, new ExercisesList());
        Student student = new Student(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(), ALICE.getAddress(),
                ALICE.getTags(), new WorkoutPlansList(List.of(populated, empty)));

        onFxThread(() -> {
            WorkoutPlanListPanel panel = new WorkoutPlanListPanel();
            new Scene(panel.getRoot(), 500, 600);
            panel.showStudent(Optional.of(student), Optional.of(populated));
            refresh(panel.getRoot());
            assertEquals(2, panel.getRoot().lookupAll(".workout-plan-card").size());
            assertEquals(1, panel.getRoot().lookupAll(".workout-plan-selected").size());
            assertEquals(1, panel.getRoot().lookupAll(".exercise-entry").size());

            panel.showStudent(Optional.of(student), Optional.of(empty));
            refresh(panel.getRoot());
            assertEquals(2, panel.getRoot().lookupAll(".workout-plan-card").size());
            assertEquals(1, panel.getRoot().lookupAll(".workout-plan-selected").size());
            assertTrue(panel.getRoot().lookupAll(".exercise-entry").isEmpty());
            assertEquals("Workout plans · #2 selected",
                    label(panel.getRoot(), ".selected-student-subtitle").getText());
            Region selectedCard = (Region) panel.getRoot().lookup(".workout-plan-selected");
            assertEquals("Exercises (0)", label(selectedCard, ".exercise-section-heading").getText());
            assertTrue(panel.getRoot().lookupAll(".exercise-empty-message").stream()
                    .anyMatch(node -> ((Label) node).getText().contains("No exercises")));

            panel.showStudent(Optional.empty(), Optional.empty());
            refresh(panel.getRoot());
            assertNull(panel.getRoot().lookup(".workout-plan-selected"));
            assertEquals("Select a student to view workout plans.",
                    label(panel.getRoot(), ".empty-message").getText());
            return null;
        });
    }

    // EP: long description in a narrow panel (valid).
    @Test
    public void selectedMiddlePlan_retainsCardsInOriginalOrder() throws Exception {
        WorkoutPlan first = plan(List.of(exercise("Squats", "Barbell back squat")));
        WorkoutPlan second = new WorkoutPlan(new WorkoutTitle("Cardio"), new WorkoutDescription("Intervals"),
                WorkoutDate.fromLocalDate(LocalDate.now().plusDays(2)),
                Set.of(new WorkoutPlanTag("cardio")), WorkoutPlanStatus.PENDING,
                new ExercisesList(List.of(exercise("Sprints", "Six rounds"))));
        WorkoutPlan third = new WorkoutPlan(new WorkoutTitle("Mobility"), new WorkoutDescription("Recovery"),
                WorkoutDate.fromLocalDate(LocalDate.now().plusDays(3)),
                Set.of(new WorkoutPlanTag("mobility")), WorkoutPlanStatus.PENDING, new ExercisesList());
        Student student = new Student(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(), ALICE.getAddress(),
                ALICE.getTags(), new WorkoutPlansList(List.of(first, second, third)));

        onFxThread(() -> {
            WorkoutPlanListPanel panel = new WorkoutPlanListPanel();
            new Scene(panel.getRoot(), 500, 600);
            panel.showStudent(Optional.of(student), Optional.of(second));
            refresh(panel.getRoot());

            VBox list = (VBox) panel.getRoot().lookup(".workout-plan-list");
            assertEquals(List.of("Strength", "Cardio", "Mobility"), list.getChildren().stream()
                    .map(node -> ((Label) node.lookup(".workout-plan-title")).getText())
                    .toList());
            assertEquals(3, list.getChildren().size());
            assertEquals(1, list.getChildren().stream()
                    .filter(node -> node.getStyleClass().contains("workout-plan-selected"))
                    .count());
            Node selectedCard = list.getChildren().get(1);
            assertEquals(1, selectedCard.lookupAll(".exercise-entry").size());
            assertTrue(list.getChildren().get(0).lookupAll(".exercise-entry").isEmpty());
            assertTrue(list.getChildren().get(2).lookupAll(".exercise-entry").isEmpty());
            return null;
        });
    }

    @Test
    public void longDescription_wrapsWithinNarrowScrollablePanel() throws Exception {
        WorkoutPlan plan = plan(List.of(exercise("Squats", "Long description ".repeat(20))));
        Student student = withPlan(plan);

        onFxThread(() -> {
            WorkoutPlanListPanel panel = new WorkoutPlanListPanel();
            panel.showStudent(Optional.of(student), Optional.of(plan));
            Scene scene = new Scene(panel.getRoot(), 360, 480);
            scene.getRoot().applyCss();
            scene.getRoot().layout();

            Label description = label(panel.getRoot(), ".exercise-description");
            ScrollPane scrollPane = (ScrollPane) panel.getRoot().lookup(".workout-plan-scroll");
            assertTrue(description.isWrapText());
            assertTrue(scrollPane.isFitToWidth());
            assertTrue(description.getWidth() <= scrollPane.getViewportBounds().getWidth());
            return null;
        });
    }

    @Test
    public void selectedOffscreenPlan_isRevealedAndSiblingCardsRemain() throws Exception {
        List<WorkoutPlan> plans = new ArrayList<>();
        for (int index = 1; index <= 8; index++) {
            plans.add(namedPlan("Plan " + index, List.of(exercise("Exercise " + index, "Description"))));
        }
        Student student = withPlans(plans);
        WorkoutPlanListPanel panel = onFxThread(() -> {
            WorkoutPlanListPanel createdPanel = new WorkoutPlanListPanel();
            new Scene(createdPanel.getRoot(), 360, 300);
            createdPanel.showStudent(Optional.of(student), Optional.empty());
            refresh(createdPanel.getRoot());
            return createdPanel;
        });

        onFxThread(() -> {
            panel.showStudent(Optional.of(student), Optional.of(plans.get(7)));
            refresh(panel.getRoot());
            return null;
        });
        onFxThread(() -> null);

        onFxThread(() -> {
            refresh(panel.getRoot());
            VBox list = (VBox) panel.getRoot().lookup(".workout-plan-list");
            ScrollPane scrollPane = (ScrollPane) panel.getRoot().lookup(".workout-plan-scroll");
            Bounds selectedBounds = list.getChildren().get(7).getBoundsInParent();
            double viewportTop = scrollOffset(list, scrollPane);
            double viewportBottom = viewportTop + scrollPane.getViewportBounds().getHeight();

            assertEquals(8, list.getChildren().size());
            assertTrue(selectedBounds.getMaxY() > viewportTop);
            assertTrue(selectedBounds.getMinY() < viewportBottom);
            assertTrue(scrollPane.getVvalue() > 0);
            panel.resetScrollPosition();
            assertEquals(0, scrollPane.getVvalue());
            return null;
        });
    }

    @Test
    public void retainedSelectionAfterReplacement_preservesScrollOffset() throws Exception {
        List<WorkoutPlan> originalPlans = new ArrayList<>();
        for (int index = 1; index <= 8; index++) {
            originalPlans.add(namedPlan("Plan " + index,
                    List.of(exercise("Exercise " + index, "Description"))));
        }
        Student originalStudent = withPlans(originalPlans);
        WorkoutPlanListPanel panel = onFxThread(() -> {
            WorkoutPlanListPanel createdPanel = new WorkoutPlanListPanel();
            new Scene(createdPanel.getRoot(), 360, 300);
            createdPanel.showStudent(Optional.of(originalStudent), Optional.of(originalPlans.get(3)));
            refresh(createdPanel.getRoot());
            return createdPanel;
        });
        onFxThread(() -> null);

        double oldScrollOffset = onFxThread(() -> {
            VBox list = (VBox) panel.getRoot().lookup(".workout-plan-list");
            ScrollPane scrollPane = (ScrollPane) panel.getRoot().lookup(".workout-plan-scroll");
            scrollPane.setVvalue(0.55);
            refresh(panel.getRoot());
            return scrollOffset(list, scrollPane);
        });

        WorkoutPlan replacement = namedPlan("Plan 4 updated",
                List.of(exercise("Exercise 4", "Updated description")));
        List<WorkoutPlan> updatedPlans = new ArrayList<>(originalPlans);
        updatedPlans.set(3, replacement);
        Student updatedStudent = withPlans(updatedPlans);
        onFxThread(() -> {
            panel.showStudent(Optional.of(updatedStudent), Optional.of(replacement));
            refresh(panel.getRoot());
            return null;
        });
        onFxThread(() -> null);

        onFxThread(() -> {
            refresh(panel.getRoot());
            VBox list = (VBox) panel.getRoot().lookup(".workout-plan-list");
            ScrollPane scrollPane = (ScrollPane) panel.getRoot().lookup(".workout-plan-scroll");
            assertEquals(oldScrollOffset, scrollOffset(list, scrollPane), 1.5);
            assertEquals("Workout plans · #4 selected",
                    label(panel.getRoot(), ".selected-student-subtitle").getText());
            return null;
        });
    }

    @Test
    public void planDeletion_reindexesRetainedSelectionAndCollapsesDeletedSelection() throws Exception {
        WorkoutPlan first = namedPlan("First", List.of(exercise("One", "First exercise")));
        WorkoutPlan selected = namedPlan("Selected", List.of(exercise("Two", "Second exercise")));
        WorkoutPlan third = namedPlan("Third", List.of(exercise("Three", "Third exercise")));
        WorkoutPlanListPanel panel = onFxThread(() -> {
            WorkoutPlanListPanel createdPanel = new WorkoutPlanListPanel();
            new Scene(createdPanel.getRoot(), 500, 600);
            return createdPanel;
        });

        onFxThread(() -> {
            panel.showStudent(Optional.of(withPlans(List.of(first, selected, third))), Optional.of(selected));
            panel.showStudent(Optional.of(withPlans(List.of(selected, third))), Optional.of(selected));
            refresh(panel.getRoot());
            assertEquals("Workout plans · #1 selected",
                    label(panel.getRoot(), ".selected-student-subtitle").getText());
            assertEquals(2, panel.getRoot().lookupAll(".workout-plan-card").size());
            assertEquals(1, panel.getRoot().lookupAll(".workout-plan-selected").size());

            panel.showStudent(Optional.of(withPlans(List.of(third))), Optional.empty());
            refresh(panel.getRoot());
            assertEquals("Workout plans", label(panel.getRoot(), ".selected-student-subtitle").getText());
            assertTrue(panel.getRoot().lookupAll(".workout-plan-selected").isEmpty());
            assertEquals(1, panel.getRoot().lookupAll(".workout-plan-card").size());
            return null;
        });
    }

    private static Label label(Region root, String selector) {
        return (Label) root.lookup(selector);
    }

    private static void refresh(Region root) {
        root.applyCss();
        root.layout();
    }

    private static double scrollOffset(VBox list, ScrollPane scrollPane) {
        double scrollableHeight = Math.max(0,
                list.getBoundsInLocal().getHeight() - scrollPane.getViewportBounds().getHeight());
        return scrollPane.getVvalue() * scrollableHeight;
    }

    private static <T> T onFxThread(Callable<T> action) throws Exception {
        FutureTask<T> task = new FutureTask<>(action);
        Platform.runLater(task);
        return task.get(10, TimeUnit.SECONDS);
    }

    private static Exercise exercise(String name, String description) {
        return new Exercise(new ExerciseName(name), new ExerciseDescription(description));
    }

    private static WorkoutPlan plan(List<Exercise> exercises) {
        return namedPlan("Strength", exercises);
    }

    private static WorkoutPlan namedPlan(String title, List<Exercise> exercises) {
        return new WorkoutPlan(new WorkoutTitle(title), new WorkoutDescription("Plan notes"),
                WorkoutDate.fromLocalDate(LocalDate.now().plusDays(1)),
                Set.of(new WorkoutPlanTag("strength")), WorkoutPlanStatus.COMPLETED, new ExercisesList(exercises));
    }

    private static Student withPlan(WorkoutPlan plan) {
        return withPlans(List.of(plan));
    }

    private static Student withPlans(List<WorkoutPlan> plans) {
        return new Student(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(), ALICE.getAddress(),
                ALICE.getTags(), new WorkoutPlansList(plans));
    }
}
