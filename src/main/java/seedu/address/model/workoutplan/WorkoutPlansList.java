package seedu.address.model.workoutplan;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import seedu.address.model.workoutplan.exceptions.WorkoutPlanNotFoundException;

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
    public List<WorkoutPlan> getWorkoutList() {
        return plans;
    }

    /** Returns a list with each plan's status refreshed. */
    public WorkoutPlansList updateState() {
        LocalDate today = LocalDate.now();
        List<WorkoutPlan> updated = plans.stream().map(plan -> plan.updateState(today)).toList();
        return updated.equals(plans) ? this : new WorkoutPlansList(updated);
    }

    /** Returns a list with the plan at {@code index} marked as completed. */
    public WorkoutPlansList markWorkoutPlan(int index) {
        WorkoutPlan marked = plans.get(index).mark();
        return replaceWorkoutPlan(index, marked);
    }

    /** Returns a list with the plan at {@code index} unmarked and its status refreshed. */
    public WorkoutPlansList unmarkWorkoutPlan(int index) {
        WorkoutPlan unmarked = plans.get(index).unmark();
        return replaceWorkoutPlan(index, unmarked);
    }

    private WorkoutPlansList replaceWorkoutPlan(int index, WorkoutPlan replacement) {
        if (plans.get(index) == replacement) {
            return this;
        }
        List<WorkoutPlan> updated = new ArrayList<>(plans);
        updated.set(index, replacement);
        return new WorkoutPlansList(updated);
    }

    /**
     * Returns a list with {@code target} replaced by {@code editedPlan} at the same position.
     * Prefers the exact target instance when equal-valued plans are present.
     *
     * @throws WorkoutPlanNotFoundException If no matching plan is present.
     */
    public WorkoutPlansList setWorkoutPlan(WorkoutPlan target, WorkoutPlan editedPlan) {
        Objects.requireNonNull(target);
        Objects.requireNonNull(editedPlan);
        int index = -1;
        for (int i = 0; i < plans.size(); i++) {
            if (plans.get(i) == target) {
                index = i;
                break;
            }
        }
        if (index < 0) {
            index = plans.indexOf(target);
        }
        if (index < 0) {
            throw new WorkoutPlanNotFoundException(target);
        }
        return replaceWorkoutPlan(index, editedPlan);
    }

    /**
     * Returns a new list with {@code plan} appended.
     */
    public WorkoutPlansList addWorkoutPlan(WorkoutPlan plan) {
        Objects.requireNonNull(plan);
        List<WorkoutPlan> updated = new ArrayList<>(plans);
        updated.add(plan);
        return new WorkoutPlansList(updated);
    }

    /**
     * Returns a new list with one plan equal to {@code plan} removed.
     *
     * @throws WorkoutPlanNotFoundException If no matching plan is present.
     */
    public WorkoutPlansList removeWorkoutPlan(WorkoutPlan plan) {
        Objects.requireNonNull(plan);
        List<WorkoutPlan> updated = new ArrayList<>(plans);
        if (!updated.remove(plan)) {
            throw new WorkoutPlanNotFoundException(plan);
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
