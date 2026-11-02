package seedu.address.model.workoutplan;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents the name of a workout plan.
 * Guarantees: immutable and not blank.
 */
public final class WorkoutPlanName {

    public static final String MESSAGE_CONSTRAINTS = "Workout plan names should not be blank";

    private final String value;

    /**
     * Constructs a {@code WorkoutPlanName}.
     *
     * @param name A non-blank name.
     */
    public WorkoutPlanName(String name) {
        requireNonNull(name);
        checkArgument(isValidName(name), MESSAGE_CONSTRAINTS);
        value = name;
    }

    /**
     * Returns true if the name contains non-whitespace characters.
     */
    public static boolean isValidName(String test) {
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
        return other instanceof WorkoutPlanName otherName && value.equals(otherName.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
