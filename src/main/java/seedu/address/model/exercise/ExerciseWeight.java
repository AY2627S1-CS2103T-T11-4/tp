package seedu.address.model.exercise;

import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents the weight of an exercise in kilograms.
 * Guarantees: immutable, finite, and non-negative.
 */
public final class ExerciseWeight {

    public static final double MIN_WEIGHT = 0.0;
    public static final String MESSAGE_CONSTRAINTS =
            "Exercise weights should be finite and non-negative kilograms";

    private final double value;

    /**
     * Constructs an {@code ExerciseWeight} with a validated value.
     *
     * @param weight the exercise weight in kilograms
     */
    public ExerciseWeight(double weight) {
        checkArgument(isValidWeight(weight), MESSAGE_CONSTRAINTS);
        value = weight == 0.0 ? MIN_WEIGHT : weight;
    }

    /** Returns true if {@code weight} is finite and non-negative. */
    public static boolean isValidWeight(double weight) {
        return Double.isFinite(weight) && weight >= MIN_WEIGHT;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        return other instanceof ExerciseWeight otherWeight
                && Double.compare(value, otherWeight.value) == 0;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }
}
