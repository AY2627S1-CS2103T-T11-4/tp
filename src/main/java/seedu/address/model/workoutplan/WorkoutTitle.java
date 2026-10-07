package seedu.address.model.workoutplan;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents the title of a workout plan.
 * Guarantees: immutable and not blank.
 */
public final class WorkoutTitle {

    public static final String MESSAGE_CONSTRAINTS = "Workout titles should not be blank";

    private final String value;

    /**
     * Constructs a {@code WorkoutTitle}.
     *
     * @param title A non-blank title.
     */
    public WorkoutTitle(String title) {
        requireNonNull(title);
        checkArgument(isValidWorkoutTitle(title), MESSAGE_CONSTRAINTS);
        value = title;
    }

    /**
     * Returns true if the title contains non-whitespace characters.
     */
    public static boolean isValidWorkoutTitle(String test) {
        return !test.isBlank();
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        return other instanceof WorkoutTitle otherTitle && value.equals(otherTitle.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
