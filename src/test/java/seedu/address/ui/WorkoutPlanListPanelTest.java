package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalStudents.ALICE;

import java.awt.GraphicsEnvironment;
import java.time.LocalDate;
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
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
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
            assertEquals("Plan #1: Strength", label(root, ".selected-student-subtitle").getText());
            assertNotNull(root.lookup(".workout-plan-selected"));
            assertEquals("DONE", label(root, ".workout-status-badge").getText());
            assertEquals("Plan notes", label(root, ".workout-plan-description").getText());
            assertEquals(2, root.lookupAll(".exercise-entry").size());
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
            assertEquals(1, panel.getRoot().lookupAll(".exercise-entry").size());

            panel.showStudent(Optional.of(student), Optional.of(empty));
            refresh(panel.getRoot());
            assertTrue(panel.getRoot().lookupAll(".exercise-entry").isEmpty());
            assertEquals("Plan #2: Mobility", label(panel.getRoot(), ".selected-student-subtitle").getText());
            assertTrue(panel.getRoot().lookupAll(".empty-message").stream()
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

    private static Label label(Region root, String selector) {
        return (Label) root.lookup(selector);
    }

    private static void refresh(Region root) {
        root.applyCss();
        root.layout();
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
        return new WorkoutPlan(new WorkoutTitle("Strength"), new WorkoutDescription("Plan notes"),
                WorkoutDate.fromLocalDate(LocalDate.now().plusDays(1)),
                Set.of(new WorkoutPlanTag("strength")), WorkoutPlanStatus.COMPLETED, new ExercisesList(exercises));
    }

    private static Student withPlan(WorkoutPlan plan) {
        return new Student(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(), ALICE.getAddress(),
                ALICE.getTags(), new WorkoutPlansList(List.of(plan)));
    }
}
