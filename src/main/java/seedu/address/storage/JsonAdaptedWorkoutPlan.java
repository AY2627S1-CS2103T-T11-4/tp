package seedu.address.storage;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.WorkoutPlanTag;
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
    private final List<JsonAdaptedTag> tags = new ArrayList<>();

    @JsonCreator
    public JsonAdaptedWorkoutPlan(@JsonProperty("title") String title,
                                  @JsonProperty("workoutDescription") String workoutDescription,
                                  @JsonProperty("date") String date,
                                  @JsonProperty("tags") List<JsonAdaptedTag> tags) {
        this.title = title;
        this.workoutDescription = workoutDescription;
        this.date = date;
        if (tags != null) {
            this.tags.addAll(tags);
        }
    }

    /**
     * Creates an adapted workout plan without tags.
     */
    public JsonAdaptedWorkoutPlan(String title, String workoutDescription, String date) {
        this(title, workoutDescription, date, List.of());
    }

    public JsonAdaptedWorkoutPlan(WorkoutPlan source) {
        title = source.getTitle().getValue();
        workoutDescription = source.getWorkouts().getValue();
        date = formatDateForStorage(source.getDate().getValue());
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
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

        Set<WorkoutPlanTag> modelTags = new HashSet<>();
        for (JsonAdaptedTag tag : tags) {
            if (tag == null) {
                throw new IllegalValueException(Tag.MESSAGE_CONSTRAINTS);
            }
            modelTags.add(tag.toWorkoutPlanTag());
        }

        return new WorkoutPlan(new WorkoutTitle(title), new WorkoutDescription(workoutDescription),
                modelDate, modelTags);
    }
}
