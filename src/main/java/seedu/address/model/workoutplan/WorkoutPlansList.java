package seedu.address.model.workoutplan;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Represents an immutable, ordered list of workout plans.
 */
public final class WorkoutPlansList {

    private final List<WorkoutPlan> plans;

    /**
     * Constructs an empty list of workout plans.
     */
    public WorkoutPlansList() {
        this(List.of());
    }

    /**
     * Constructs a list containing the given plans in order.
     * Rejects a null list or any null plan.
     */
    public WorkoutPlansList(List<WorkoutPlan> plans) {
        this.plans = List.copyOf(plans);
    }

    /**
     * Returns the unmodifiable list of plans.
     */
    public List<WorkoutPlan> asList() {
        return plans;
    }

    /**
     * Returns a new list with {@code plan} appended.
     */
    public WorkoutPlansList withAdded(WorkoutPlan plan) {
        Objects.requireNonNull(plan);
        List<WorkoutPlan> updated = new ArrayList<>(plans);
        updated.add(plan);
        return new WorkoutPlansList(updated);
    }

    /**
     * Returns a new list with one plan equal to {@code plan} removed.
     *
     * @throws NoSuchElementException If no matching plan is present.
     */
    public WorkoutPlansList without(WorkoutPlan plan) {
        Objects.requireNonNull(plan);
        List<WorkoutPlan> updated = new ArrayList<>(plans);
        if (!updated.remove(plan)) {
            throw new NoSuchElementException("Workout plan not found: " + plan);
        }
        return new WorkoutPlansList(updated);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof WorkoutPlansList otherList)) {
            return false;
        }
        return plans.equals(otherList.plans);
    }

    @Override
    public int hashCode() {
        return plans.hashCode();
    }
}
