package seedu.address.model.workoutplan;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents a workout plan in BrotherGym.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public final class WorkoutPlan {

    private final WorkoutTitle title;
    private final WorkoutDescription workouts;
    private final WorkoutDate date;

    /**
     * Constructs a workout plan with validated, non-null fields.
     */
    public WorkoutPlan(WorkoutTitle title, WorkoutDescription workouts, WorkoutDate date) {
        requireAllNonNull(title, workouts, date);
        this.title = title;
        this.workouts = workouts;
        this.date = date;
    }

    public WorkoutTitle getTitle() {
        return title;
    }

    public WorkoutDescription getWorkouts() {
        return workouts;
    }

    public WorkoutDate getDate() {
        return date;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof WorkoutPlan otherPlan)) {
            return false;
        }
        return title.equals(otherPlan.title)
                && workouts.equals(otherPlan.workouts)
                && date.equals(otherPlan.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, workouts, date);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("title", title)
                .add("workouts", workouts)
                .add("date", date)
                .toString();
    }
}
