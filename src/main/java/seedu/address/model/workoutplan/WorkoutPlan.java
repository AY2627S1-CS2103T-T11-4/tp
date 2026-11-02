package seedu.address.model.workoutplan;

import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.exercise.ExercisesList;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.WorkoutPlanTag;
import seedu.address.model.workoutplan.exceptions.AlreadyMarkedException;
import seedu.address.model.workoutplan.exceptions.AlreadyUnmarkedException;

/**
 * Represents a workout plan in BrotherGym.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public final class WorkoutPlan {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy");

    private final WorkoutPlanName name;
    private final WorkoutDescription description;
    private final WorkoutDate date;
    private final Set<WorkoutPlanTag> tags;
    private final WorkoutPlanStatus status;
    private final ExercisesList exercises;

    /**
     * Constructs a workout plan with validated, non-null fields.
     */
    @Deprecated
    public WorkoutPlan(WorkoutPlanName name, WorkoutDescription description, WorkoutDate date) {
        this(name, description, date, Set.of());
    }

    /**
     * Constructs a workout plan with validated, non-null fields.
     */
    public WorkoutPlan(WorkoutPlanName name, WorkoutDescription description, WorkoutDate date,
            Set<WorkoutPlanTag> tags) {
        this(name, description, date, tags, WorkoutPlanStatus.PENDING);
    }

    /**
     * Constructs a workout plan with validated, non-null fields and the given status.
     */
    public WorkoutPlan(WorkoutPlanName name, WorkoutDescription description, WorkoutDate date,
            Set<WorkoutPlanTag> tags, WorkoutPlanStatus status) {
        this(name, description, date, tags, status, new ExercisesList());
    }

    /**
     * Constructs a workout plan with validated, non-null fields and the given status and exercises.
     */
    public WorkoutPlan(WorkoutPlanName name, WorkoutDescription description, WorkoutDate date,
            Set<WorkoutPlanTag> tags, WorkoutPlanStatus status, ExercisesList exercises) {
        requireAllNonNull(name, description, date, tags, status, exercises);
        checkArgument(Tag.isValidTagCount(tags.size()), Tag.MESSAGE_TAG_LIMIT);
        this.name = name;
        this.description = description;
        this.date = date;
        this.tags = Set.copyOf(tags);
        this.status = status;
        this.exercises = exercises;
    }

    public WorkoutPlanName getName() {
        return name;
    }

    public WorkoutDescription getDescription() {
        return description;
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

    public ExercisesList getExercisesList() {
        return exercises;
    }

    /** Returns a new plan with the given exercises and all other fields unchanged. */
    public WorkoutPlan withExercises(ExercisesList exercises) {
        return new WorkoutPlan(name, description, date, tags, status, exercises);
    }

    /**
     * Returns this plan marked as completed.
     *
     * @throws AlreadyMarkedException If this plan is already completed.
     */
    public WorkoutPlan mark() {
        if (status == WorkoutPlanStatus.COMPLETED) {
            throw new AlreadyMarkedException();
        }
        return withStatus(WorkoutPlanStatus.COMPLETED);
    }

    /**
     * Returns this plan unmarked, with its status determined by today's date.
     * Uses the same date rule as {@link #updateState(LocalDate)}.
     *
     * @throws AlreadyUnmarkedException If this plan is pending or overdue.
     */
    public WorkoutPlan unmark() {
        if (status != WorkoutPlanStatus.COMPLETED) {
            throw new AlreadyUnmarkedException();
        }
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
                : new WorkoutPlan(name, description, date, tags, updatedStatus, exercises);
    }

    /**
     * Returns a plan with its status refreshed for {@code today}.
     * Preserves plans marked by {@link #mark()}, while {@link #unmark()} clears that mark.
     */
    public WorkoutPlan updateState(LocalDate today) {
        Objects.requireNonNull(today);
        return status == WorkoutPlanStatus.COMPLETED ? this : withStatus(statusForDate(today));
    }

    /**
     * Returns true if both plans have the same name, description, and date,
     * regardless of tags, status, or exercises.
     * This defines the identity used to prevent duplicate plans for a student.
     */
    public boolean isSamePlan(WorkoutPlan otherPlan) {
        if (otherPlan == this) {
            return true;
        }
        return otherPlan != null
                && name.equals(otherPlan.name)
                && description.equals(otherPlan.description)
                && date.equals(otherPlan.date);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof WorkoutPlan otherPlan)) {
            return false;
        }
        return name.equals(otherPlan.name)
                && description.equals(otherPlan.description)
                && date.equals(otherPlan.date)
                && tags.equals(otherPlan.tags)
                && status.equals(otherPlan.status)
                && exercises.equals(otherPlan.exercises);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, date, tags, status, exercises);
    }

    @Override
    public String toString() {
        String formattedDate = date.getValue().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .format(DATE_FORMATTER);
        String planDetails = name.getValue() + " - " + description.getValue() + " (" + formattedDate + ")";
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
