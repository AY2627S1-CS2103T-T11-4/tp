package seedu.address.testutil;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

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
    public static final Date DEFAULT_DATE = Date.from(LocalDate.of(2026, 10, 9)
            .atStartOfDay(ZoneId.systemDefault()).toInstant());

    private WorkoutTitle title;
    private WorkoutDescription desc;
    private WorkoutDate date;

    /**
     * Creates a {@code WorkoutBuilder}  with the default details
     */
    public WorkoutBuilder() {
        title = new WorkoutTitle(DEFAULT_TITLE);
        desc = new WorkoutDescription(DEFAULT_DESC);
        date = new WorkoutDate(DEFAULT_DATE);
    }

    /**
     * Initializes the WorkoutBuilder with the data of {@code workoutToCopy}.
     */
    public WorkoutBuilder(WorkoutPlan workoutToCopy) {
        title = workoutToCopy.getTitle();
        desc = workoutToCopy.getWorkouts();
        date = workoutToCopy.getDate();
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
    public WorkoutBuilder withDate(Date date) {
        this.date = new WorkoutDate(date);
        return this;
    }

    public WorkoutPlan build() {
        return new WorkoutPlan(this.title, this.desc, this.date);
    }
}
