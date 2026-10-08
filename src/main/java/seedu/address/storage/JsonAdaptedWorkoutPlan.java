package seedu.address.storage;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.workoutplan.WorkoutDate;
import seedu.address.model.workoutplan.WorkoutDescription;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.WorkoutTitle;

/**
 * Jackson-friendly version of {@link WorkoutPlan}.
 */
class JsonAdaptedWorkoutPlan {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Workout plan's %s field is missing!";
    private static final DateTimeFormatter STORAGE_DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    private final String title;
    private final String workoutDescription;
    private final String date;

    @JsonCreator
    public JsonAdaptedWorkoutPlan(@JsonProperty("title") String title,
                                  @JsonProperty("workoutDescription") String workoutDescription,
                                  @JsonProperty("date") String date) {
        this.title = title;
        this.workoutDescription = workoutDescription;
        this.date = date;
    }

    public JsonAdaptedWorkoutPlan(WorkoutPlan source) {
        title = source.getTitle().getValue();
        workoutDescription = source.getWorkouts().getValue();
        date = formatDateForStorage(source.getDate().getValue());
    }

    public static String formatDateForStorage(Date modelDate) {
        if (modelDate == null) {
            throw new IllegalArgumentException("Model date cannot be null");
        }
        return modelDate.toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate().format(STORAGE_DATE_FORMAT);
    }

    public static WorkoutDate formatDateForModel(String storageDate) {
        try {
            LocalDate localDate = LocalDate.parse(storageDate, STORAGE_DATE_FORMAT);
            Date modelDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            return new WorkoutDate(modelDate);
        } catch (DateTimeParseException | NullPointerException e) {
            throw new IllegalArgumentException("Storage date must be in yyyy-MM-dd format", e);
        }
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("workoutDescription")
    public String getWorkoutDescription() {
        return workoutDescription;
    }

    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    public WorkoutPlan toModelType() throws IllegalValueException {
        if (title == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    WorkoutTitle.class.getSimpleName()));
        }
        if (workoutDescription == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    WorkoutDescription.class.getSimpleName()));
        }
        if (date == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    WorkoutDate.class.getSimpleName()));
        }

        final WorkoutDate modelDate;
        try {
            modelDate = formatDateForModel(date);
        } catch (IllegalArgumentException e) {
            throw new IllegalValueException(e.getMessage());
        }

        return new WorkoutPlan(new WorkoutTitle(title), new WorkoutDescription(workoutDescription), modelDate);
    }
}
