package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalStudents.ALICE;

import java.awt.GraphicsEnvironment;
import java.util.List;
import java.util.Optional;
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
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.exercise.Exercise;
import seedu.address.model.exercise.ExerciseDescription;
import seedu.address.model.exercise.ExerciseName;
import seedu.address.model.exercise.ExercisesList;
import seedu.address.model.student.Student;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.WorkoutPlansList;
import seedu.address.testutil.WorkoutBuilder;

/** Tests the exercise display in the workout plan panel on hosts with a graphical display. */
public class WorkoutPlanListPanelExerciseTest {

    private static boolean startedToolkit;

    @BeforeAll
    public static void startToolkit() {
        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless());
        try {
            Platform.startup(() -> { });
            startedToolkit = true;
        } catch (IllegalStateException e) {
            // The toolkit may already have been started by another UI test.
        }
    }

    @AfterAll
    public static void stopToolkit() {
        if (startedToolkit) {
            Platform.exit();
        }
    }

    @Test
    public void showStudent_noSelectionOrPlans_showsEmptyMessages() throws Exception {
        onFxThread(() -> {
            WorkoutPlanListPanel panel = new WorkoutPlanListPanel();
            Region root = panel.getRoot();
            new Scene(root, 500, 600);
            refresh(root);
            assertEquals("Select a student to view workout plans.", label(root, ".empty-message").getText());

            panel.showStudent(Optional.of(ALICE));
            refresh(root);
            assertEquals("No workout plans assigned.", label(root, ".empty-message").getText());
            assertFalse(root.lookup(".workout-plan-scroll").isVisible());
            return null;
        });
    }

    @Test
    public void showStudent_planListAndSelectedPlan_showsOrderedExercises() throws Exception {
        Exercise first = new Exercise(new ExerciseName("Squats"),
                new ExerciseDescription("Barbell back squat"));
        Exercise second = new Exercise(new ExerciseName("Push ups"),
                new ExerciseDescription("Bodyweight exercise"));
        WorkoutPlan plan = withExercises(List.of(first, second));
        Student student = withPlan(plan);

        onFxThread(() -> {
            WorkoutPlanListPanel panel = new WorkoutPlanListPanel();
            Region root = panel.getRoot();
            new Scene(root, 500, 600);
            panel.showStudent(Optional.of(student));
            refresh(root);
            assertEquals(1, root.lookupAll(".workout-plan-card").size());
            assertTrue(root.lookup(".workout-plan-scroll").isVisible());

            panel.showStudent(Optional.of(student), Optional.of(plan));
            refresh(root);
            VBox exerciseList = (VBox) root.lookup(".exercise-list");
            assertEquals("Workout plan: " + plan.getTitle(), label(root, ".selected-student-subtitle").getText());
            assertEquals(plan.getWorkouts().getValue(), ((Label) exerciseList.getChildren().get(0)).getText());
            assertEquals("1. Squats\nBarbell back squat", ((Label) exerciseList.getChildren().get(1)).getText());
            assertEquals("2. Push ups\nBodyweight exercise", ((Label) exerciseList.getChildren().get(2)).getText());
            assertTrue(root.lookupAll(".workout-plan-card").isEmpty());
            return null;
        });
    }

    @Test
    public void showStudent_emptySelectedPlan_clearsPreviousExercises() throws Exception {
        WorkoutPlan populated = withExercises(List.of(new Exercise(new ExerciseName("Squats"),
                new ExerciseDescription("Barbell back squat"))));
        WorkoutPlan empty = new WorkoutBuilder().withTitle("Stretching").build();
        Student student = withPlans(List.of(populated, empty));

        onFxThread(() -> {
            WorkoutPlanListPanel panel = new WorkoutPlanListPanel();
            Region root = panel.getRoot();
            new Scene(root, 500, 600);
            panel.showStudent(Optional.of(student), Optional.of(populated));
            refresh(root);
            assertEquals(1, root.lookupAll(".exercise-card").size());

            panel.showStudent(Optional.of(student), Optional.of(empty));
            refresh(root);
            assertTrue(root.lookupAll(".exercise-card").isEmpty());
            VBox exerciseList = (VBox) root.lookup(".exercise-list");
            Label emptyExercisesMessage = (Label) exerciseList.getChildren().get(1);
            assertEquals("No exercises in this workout plan.", emptyExercisesMessage.getText());

            panel.showStudent(Optional.empty());
            refresh(root);
            assertEquals("Select a student to view workout plans.", label(root, ".empty-message").getText());
            return null;
        });
    }

    private static WorkoutPlan withExercises(List<Exercise> exercises) {
        WorkoutPlan plan = new WorkoutBuilder().build();
        return new WorkoutPlan(plan.getTitle(), plan.getWorkouts(), plan.getDate(), plan.getTags(),
                plan.getStatus(), new ExercisesList(exercises));
    }

    private static Student withPlan(WorkoutPlan plan) {
        return withPlans(List.of(plan));
    }

    private static Student withPlans(List<WorkoutPlan> plans) {
        return new Student(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(), ALICE.getAddress(),
                ALICE.getTags(), new WorkoutPlansList(plans));
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
}
