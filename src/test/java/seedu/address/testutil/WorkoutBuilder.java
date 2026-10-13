package seedu.address.testutil;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import seedu.address.model.tag.Tag;
import seedu.address.model.workoutplan.WorkoutDate;
import seedu.address.model.workoutplan.WorkoutDescription;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.WorkoutTitle;

/**
 * A utility class to help with building Workout objects.
 */
public class WorkoutBuilder {
    public static final String DEFAULT_TITLE = "Leg Day";
    public static final String DEFAULT_DESC = "Leg Press 3x 180kg, Leg Extension 3x 60kg, RDLs 3x 40kg";
    public static final LocalDate DEFAULT_DATE = LocalDate.of(2026, 10, 9);

    private WorkoutTitle title;
    private WorkoutDescription desc;
    private WorkoutDate date;
    private Set<Tag> tags;

    /**
     * Creates a {@code WorkoutBuilder}  with the default details
     */
    public WorkoutBuilder() {
        title = new WorkoutTitle(DEFAULT_TITLE);
        desc = new WorkoutDescription(DEFAULT_DESC);
        date = new WorkoutDate(DEFAULT_DATE);
        tags = new HashSet<>();
        date = WorkoutDate.fromLocalDate(DEFAULT_DATE);
    }

    /**
     * Initializes the WorkoutBuilder with the data of {@code workoutToCopy}.
     */
    public WorkoutBuilder(WorkoutPlan workoutToCopy) {
        title = workoutToCopy.getTitle();
        desc = workoutToCopy.getWorkouts();
        date = workoutToCopy.getDate();
        tags = new HashSet<>(); // TODO: tags = new HashSet<>(workoutToCopy.getTags());
    }

    /**
     * Sets the {@code title} of the workout we are building
     */
    public WorkoutBuilder withTitle(String title) {
        this.title = new WorkoutTitle(title);
        return this;
    }

    /**
     * Sets the {@code description} of the workout we are building
     */
    public WorkoutBuilder withDescription(String desc) {
        this.desc = new WorkoutDescription(desc);
        return this;
    }

    /**
     * Sets the {@code date} of the workout we are building
     */
    public WorkoutBuilder withDate(LocalDate date) {
        this.date = WorkoutDate.fromLocalDate(date);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and sets it to the {@code Student} that we are building.
     */
    public WorkoutBuilder withTags(String ... tags) {
        // TODO: this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Builds the {@code WorkoutPlan} object given the fields provided
     */
    public WorkoutPlan build() {
        return new WorkoutPlan(this.title, this.desc, this.date);
        // TODO: return new WorkoutPlan(this.title, this.desc, this.date, this.tags);
    }
}
