package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Contains unit and integration test structure for {@link DeletePlanCommand}.
 *
 * <p>The execution tests are disabled until Backend finalizes WorkoutPlan,
 * current Student handling, and the Model workout methods.</p>
 */
public class DeletePlanCommandTest {

    @Test
    public void constructor_nullIndex_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new DeletePlanCommand(null));
    }

    @Disabled("Enable after Backend finalizes the WorkoutPlan API")
    @Test
    public void execute_validWorkoutId_deletesPlan() {
        // Arrange: create a Model with a selected Student and workout plans.
        // Act: execute new DeletePlanCommand(validWorkoutId).
        // Assert: the plan is removed and the success CommandResult is returned.
    }

    @Disabled("Enable after Backend finalizes the WorkoutPlan API")
    @Test
    public void execute_noCurrentStudent_throwsCommandException() {
        // Arrange: create a Model without a currently selected Student.
        // Act and assert: execution throws MESSAGE_NO_STUDENT_SELECTED.
    }

    @Disabled("Enable after Backend finalizes the WorkoutPlan API")
    @Test
    public void execute_invalidWorkoutId_throwsCommandException() {
        // Arrange: create a Model with fewer plans than the requested ID.
        // Act and assert: execution throws MESSAGE_INVALID_WORKOUT_ID.
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
