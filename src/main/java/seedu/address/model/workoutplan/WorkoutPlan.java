package seedu.address.model.workoutplan;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.tag.WorkoutPlanTag;

/**
 * Represents a workout plan in BrotherGym.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public final class WorkoutPlan {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy");

    private final WorkoutTitle title;
    private final WorkoutDescription workouts;
    private final WorkoutDate date;
    private final Set<WorkoutPlanTag> tags;
    private final WorkoutPlanStatus status;

    /**
     * Constructs a workout plan with validated, non-null fields.
     */
    @Deprecated
    public WorkoutPlan(WorkoutTitle title, WorkoutDescription workouts, WorkoutDate date) {
        this(title, workouts, date, Set.of());
    }

    /**
     * Constructs a workout plan with validated, non-null fields.
     */
    public WorkoutPlan(WorkoutTitle title, WorkoutDescription workouts, WorkoutDate date,
            Set<WorkoutPlanTag> tags) {
        this(title, workouts, date, tags, WorkoutPlanStatus.PENDING);
    }

    /**
     * Constructs a workout plan with validated, non-null fields and the given status.
     */
    public WorkoutPlan(WorkoutTitle title, WorkoutDescription workouts, WorkoutDate date,
            Set<WorkoutPlanTag> tags, WorkoutPlanStatus status) {
        requireAllNonNull(title, workouts, date, tags, status);
        this.title = title;
        this.workouts = workouts;
        this.date = date;
        this.tags = Set.copyOf(tags);
        this.status = status;
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

    /**
     * Returns an unmodifiable set of tags belonging to this workout plan.
     */
    public Set<WorkoutPlanTag> getTags() {
        return tags;
    }

    public WorkoutPlanStatus getStatus() {
        return status;
    }

    /** Returns a plan with its status refreshed for {@code today}. */
    public WorkoutPlan updateState(LocalDate today) {
        Objects.requireNonNull(today);
        LocalDate deadline = date.getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        WorkoutPlanStatus updatedStatus = status == WorkoutPlanStatus.COMPLETED
                ? WorkoutPlanStatus.COMPLETED
                : today.isAfter(deadline) ? WorkoutPlanStatus.OVERDUE : WorkoutPlanStatus.PENDING;
        return status == updatedStatus ? this : new WorkoutPlan(title, workouts, date, tags, updatedStatus);
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
                && date.equals(otherPlan.date)
                && tags.equals(otherPlan.tags)
                && status == otherPlan.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, workouts, date, tags, status);
    }

    @Override
    public String toString() {
        String formattedDate = date.getValue().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .format(DATE_FORMATTER);
        String planDetails = title.getValue() + " - " + workouts.getValue() + " (" + formattedDate + ")";
        if (tags.isEmpty()) {
            return planDetails;
        }

        String formattedTags = tags.stream()
                .map(WorkoutPlanTag::toString)
                .sorted()
                .collect(Collectors.joining(" "));
        return planDetails + " Tags: " + formattedTags;
    }
}
