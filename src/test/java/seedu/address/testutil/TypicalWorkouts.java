package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_WORKOUT_DATE_CHEST;
import static seedu.address.logic.commands.CommandTestUtil.VALID_WORKOUT_DATE_LEGS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_WORKOUT_PLAN_CHEST;
import static seedu.address.logic.commands.CommandTestUtil.VALID_WORKOUT_PLAN_LEGS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_WORKOUT_TITLE_CHEST;
import static seedu.address.logic.commands.CommandTestUtil.VALID_WORKOUT_TITLE_LEGS;

import seedu.address.model.workoutplan.WorkoutPlan;

/**
 * A utility class containing a list of {@code Student} objects to be used in tests.
 */
public class TypicalWorkouts {
    // Workout details found in {@code CommandTestUtil}
    public static final WorkoutPlan LEG_DAY = new WorkoutBuilder()
            .withTitle(VALID_WORKOUT_TITLE_LEGS).withDescription(VALID_WORKOUT_PLAN_LEGS)
            .withDate(VALID_WORKOUT_DATE_LEGS).build();
    public static final WorkoutPlan CHEST_DAY = new WorkoutBuilder()
            .withTitle(VALID_WORKOUT_TITLE_CHEST).withDescription(VALID_WORKOUT_PLAN_CHEST)
            .withDate(VALID_WORKOUT_DATE_CHEST).build();

    private TypicalWorkouts() {} // prevents instantiation
}
