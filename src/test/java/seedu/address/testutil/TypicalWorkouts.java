package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_WORKOUT_DATE_CHEST;
import static seedu.address.logic.commands.CommandTestUtil.VALID_WORKOUT_DATE_LEGS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_WORKOUT_NAME_CHEST;
import static seedu.address.logic.commands.CommandTestUtil.VALID_WORKOUT_NAME_LEGS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_WORKOUT_PLAN_CHEST;
import static seedu.address.logic.commands.CommandTestUtil.VALID_WORKOUT_PLAN_LEGS;

import seedu.address.model.workoutplan.WorkoutPlan;

/**
 * A utility class containing a list of {@code Student} objects to be used in tests.
 */
public class TypicalWorkouts {
    // Workout details found in {@code CommandTestUtil}
    public static final WorkoutPlan LEG_DAY = new WorkoutBuilder()
            .withName(VALID_WORKOUT_NAME_LEGS).withDescription(VALID_WORKOUT_PLAN_LEGS)
            .withDate(VALID_WORKOUT_DATE_LEGS).build();
    public static final WorkoutPlan CHEST_DAY = new WorkoutBuilder()
            .withName(VALID_WORKOUT_NAME_CHEST).withDescription(VALID_WORKOUT_PLAN_CHEST)
            .withDate(VALID_WORKOUT_DATE_CHEST).build();

    private TypicalWorkouts() {} // prevents instantiation
}
