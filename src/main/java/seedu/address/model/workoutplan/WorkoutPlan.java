package seedu.address.model.workoutplan;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.exercise.ExercisesList;
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
    private final ExercisesList exercises;

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
        this(title, workouts, date, tags, status, new ExercisesList());
    }

    /**
     * Constructs a workout plan with validated, non-null fields and the given status and exercises.
     */
    public WorkoutPlan(WorkoutTitle title, WorkoutDescription workouts, WorkoutDate date,
            Set<WorkoutPlanTag> tags, WorkoutPlanStatus status, ExercisesList exercises) {
        requireAllNonNull(title, workouts, date, tags, status, exercises);
        this.title = title;
        this.workouts = workouts;
        this.date = date;
        this.tags = Set.copyOf(tags);
        this.status = status;
        this.exercises = exercises;
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

    public ExercisesList getExercises() {
        return exercises;
    }

    /** Returns this plan marked as completed. */
    public WorkoutPlan mark() {
        return withStatus(WorkoutPlanStatus.COMPLETED);
    }

    /**
     * Returns this plan unmarked, with its status determined by today's date.
     * Uses the same date rule as {@link #updateState(LocalDate)}.
     */
    public WorkoutPlan unmark() {
        return withStatus(statusForDate(LocalDate.now()));
    }

    private WorkoutPlanStatus statusForDate(LocalDate today) {
        LocalDate deadline = date.getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return today.isAfter(deadline)
                ? WorkoutPlanStatus.OVERDUE : WorkoutPlanStatus.PENDING;
    }

    private WorkoutPlan withStatus(WorkoutPlanStatus updatedStatus) {
        return status == updatedStatus
                ? this
                : new WorkoutPlan(title, workouts, date, tags, updatedStatus, exercises);
    }

    /**
     * Returns a plan with its status refreshed for {@code today}.
     * Preserves plans marked by {@link #mark()}, while {@link #unmark()} clears that mark.
     */
    public WorkoutPlan updateState(LocalDate today) {
        Objects.requireNonNull(today);
        return status == WorkoutPlanStatus.COMPLETED ? this : withStatus(statusForDate(today));
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
                && tags.equals(otherPlan.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, workouts, date, tags);
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
