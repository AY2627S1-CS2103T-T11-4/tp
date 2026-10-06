package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Contains unit and integration test structure for {@link AddPlanCommand}.
 *
 * <p>The execution tests are disabled until Backend finalizes WorkoutPlan,
 * the current Student API, and the Model workout methods.</p>
 */
public class AddPlanCommandTest {

    @Test
    public void constructor_nullWorkoutPlan_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddPlanCommand(null));
    }

    @Disabled("Enable after Backend finalizes the WorkoutPlan API")
    @Test
    public void execute_validCurrentStudent_addsPlan() {
        // Arrange: create a Model with a selected Student and a valid WorkoutPlan.
        // Act: execute new AddPlanCommand(workoutPlan).
        // Assert: the plan is added and the success CommandResult is returned.
    }

    @Disabled("Enable after Backend finalizes the WorkoutPlan API")
    @Test
    public void execute_noCurrentStudent_throwsCommandException() {
        // Arrange: create a Model without a currently selected Student.
        // Act and assert: execution throws MESSAGE_NO_STUDENT_SELECTED.
    }

    @Disabled("Enable after Backend finalizes the WorkoutPlan API")
    @Test
    public void execute_duplicateWorkoutPlan_throwsCommandException() {
        // Arrange: create a Model where the current Student already has the plan.
        // Act and assert: execution throws MESSAGE_DUPLICATE_PLAN.
    }

    @Disabled("Enable after Backend finalizes the WorkoutPlan API")
    @Test
    public void equals() {
        // Add equality checks after the WorkoutPlan test fixture is available.
    }

    @Disabled("Enable after Backend finalizes the WorkoutPlan API")
    @Test
    public void toStringMethod() {
        // Add the expected string after the WorkoutPlan test fixture is available.
    }
}
