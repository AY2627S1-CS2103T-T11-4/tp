package seedu.address.model.workoutplan;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import seedu.address.model.workoutplan.exceptions.DuplicateWorkoutPlanException;
import seedu.address.model.workoutplan.exceptions.WorkoutPlanIndexOutOfBoundsException;
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

    /** Returns the plan at {@code index}. */
    public WorkoutPlan getWorkoutPlan(int index) {
        checkIndex(index);
        return plans.get(index);
    }

    /**
     * Returns the stored plan matching {@code target}.
     * Prefers the exact target instance when plans with matching details are present.
     *
     * @throws WorkoutPlanNotFoundException If no matching plan is present.
     */
    public WorkoutPlan getWorkoutPlan(WorkoutPlan target) {
        Objects.requireNonNull(target);
        int index = findTargetIndex(target);
        if (index < 0) {
            throw new WorkoutPlanNotFoundException(target);
        }
        return plans.get(index);
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= plans.size()) {
            throw new WorkoutPlanIndexOutOfBoundsException(index);
        }
    }

    /** Returns a list with each plan's status refreshed. */
    public WorkoutPlansList updateState() {
        LocalDate today = LocalDate.now();
        List<WorkoutPlan> updated = new ArrayList<>(plans.size());
        boolean changed = false;
        for (WorkoutPlan plan : plans) {
            WorkoutPlan updatedPlan = plan.updateState(today);
            updated.add(updatedPlan);
            changed |= updatedPlan != plan;
        }
        return changed ? new WorkoutPlansList(updated) : this;
    }

    /** Returns a list with the plan at {@code index} marked as completed. */
    public WorkoutPlansList markWorkoutPlan(int index) {
        WorkoutPlan marked = getWorkoutPlan(index).mark();
        return replaceWorkoutPlan(index, marked);
    }

    /** Returns a list with the plan at {@code index} unmarked and its status refreshed. */
    public WorkoutPlansList unmarkWorkoutPlan(int index) {
        WorkoutPlan unmarked = getWorkoutPlan(index).unmark();
        return replaceWorkoutPlan(index, unmarked);
    }

    private WorkoutPlansList replaceWorkoutPlan(int index, WorkoutPlan replacement) {
        if (getWorkoutPlan(index) == replacement) {
            return this;
        }
        List<WorkoutPlan> updated = new ArrayList<>(plans);
        updated.set(index, replacement);
        WorkoutPlansList result = new WorkoutPlansList(updated);
        assert result.plans.size() == plans.size() : "Replacing a plan changed the list size";
        assert result.plans.get(index) == replacement : "Plan was not replaced at the requested index";
        return result;
    }

    /**
     * Returns a list with {@code target} replaced by {@code editedPlan} at the same position.
     * Prefers the exact target instance. Otherwise, matches title, description, date, tags, and status,
     * ignoring exercises; the first matching plan is replaced.
     *
     * @throws WorkoutPlanNotFoundException If no matching plan is present.
     */
    public WorkoutPlansList setWorkoutPlan(WorkoutPlan target, WorkoutPlan editedPlan) {
        Objects.requireNonNull(target);
        Objects.requireNonNull(editedPlan);
        int index = findTargetIndex(target);
        if (index < 0) {
            throw new WorkoutPlanNotFoundException(target);
        }
        for (int i = 0; i < plans.size(); i++) {
            if (i != index && plans.get(i).isSamePlan(editedPlan)) {
                throw new DuplicateWorkoutPlanException();
            }
        }
        return replaceWorkoutPlan(index, editedPlan);
    }

    private int findTargetIndex(WorkoutPlan target) {
        for (int i = 0; i < plans.size(); i++) {
            if (plans.get(i) == target) {
                return i;
            }
        }
        for (int i = 0; i < plans.size(); i++) {
            WorkoutPlan candidate = plans.get(i);
            if (candidate.isSamePlan(target)
                    && candidate.getTags().equals(target.getTags())
                    && candidate.getStatus() == target.getStatus()) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns a new list with {@code plan} appended.
     */
    public WorkoutPlansList addWorkoutPlan(WorkoutPlan plan) {
        Objects.requireNonNull(plan);
        if (plans.stream().anyMatch(existingPlan -> existingPlan.isSamePlan(plan))) {
            throw new DuplicateWorkoutPlanException();
        }
        List<WorkoutPlan> updated = new ArrayList<>(plans);
        updated.add(plan);
        return new WorkoutPlansList(updated);
    }

    /**
     * Returns a new list with one plan removed.
     * Prefers the exact {@code plan} instance. Otherwise, matches title, description, date, tags, and status,
     * ignoring exercises; the first matching plan is removed.
     *
     * @throws WorkoutPlanNotFoundException If no matching plan is present.
     */
    public WorkoutPlansList removeWorkoutPlan(WorkoutPlan plan) {
        Objects.requireNonNull(plan);
        int index = findTargetIndex(plan);
        if (index < 0) {
            throw new WorkoutPlanNotFoundException(plan);
        }
        List<WorkoutPlan> updated = new ArrayList<>(plans);
        updated.remove(index);
        return new WorkoutPlansList(updated);
    }

    /** Returns a new list without the plan at {@code index}. */
    public WorkoutPlansList removeWorkoutPlan(int index) {
        checkIndex(index);
        List<WorkoutPlan> updated = new ArrayList<>(plans);
        updated.remove(index);
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

    @Override
    public String toString() {
        return "{" + String.join(", ", plans.stream().map(plan -> "[" + plan + "]").toList()) + "}";
    }
}
