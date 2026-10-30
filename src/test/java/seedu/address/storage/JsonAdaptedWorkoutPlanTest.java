package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.exercise.Exercise;
import seedu.address.model.exercise.ExerciseDescription;
import seedu.address.model.exercise.ExerciseName;
import seedu.address.model.exercise.ExercisesList;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.WorkoutPlanTag;
import seedu.address.model.workoutplan.WorkoutDate;
import seedu.address.model.workoutplan.WorkoutDescription;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.WorkoutPlanStatus;
import seedu.address.model.workoutplan.WorkoutTitle;

public class JsonAdaptedWorkoutPlanTest {

    // EP: multiple ordered exercises survive a JSON round trip (valid).
    @Test
    public void modelToJsonAndBack_preservesExerciseOrder() throws Exception {
        Exercise squat = new Exercise(new ExerciseName("Squat"), new ExerciseDescription("Barbell"));
        Exercise press = new Exercise(new ExerciseName("Press"), new ExerciseDescription("Dumbbell"));
        Exercise alternateSquat = new Exercise(new ExerciseName("Squat"),
                new ExerciseDescription("Bodyweight"));
        WorkoutPlan original = new WorkoutPlan(new WorkoutTitle("Strength"), new WorkoutDescription("Leg day"),
                WorkoutDate.fromLocalDate(LocalDate.of(2023, 11, 15)), Set.of(), WorkoutPlanStatus.COMPLETED,
                new ExercisesList(List.of(squat, press, alternateSquat)));

        String json = JsonUtil.toJsonString(new JsonAdaptedWorkoutPlan(original));
        WorkoutPlan restored = JsonUtil.fromJsonString(json, JsonAdaptedWorkoutPlan.class).toModelType();

        assertEquals(List.of(squat, press, alternateSquat), restored.getExercisesList().getExerciseList());
        assertEquals(WorkoutPlanStatus.COMPLETED, restored.getStatus());
    }

    // EP: exercises are missing or null (legacy).
    @Test
    public void fromJson_missingOrNullExercises_loadsLegacyPlan() throws Exception {
        for (String exercisesField : List.of("", ",\"exercises\":null")) {
            JsonAdaptedWorkoutPlan adapted = JsonUtil.fromJsonString(
                    "{\"title\":\"Strength\",\"workoutDescription\":\"Squats\",\"date\":\"2023-11-15\""
                            + exercisesField + "}", JsonAdaptedWorkoutPlan.class);
            // EP: missing or null exercises become an empty list (legacy).
            assertEquals(new ExercisesList(), adapted.toModelType().getExercisesList());
        }
    }

    // One invalid input at a time: each invalid exercise-list form is checked.
    @Test
    public void fromJson_invalidExerciseList_throwsIllegalValueException() throws Exception {
        String base = "{\"title\":\"Strength\",\"workoutDescription\":\"Squats\","
                + "\"date\":\"2023-11-15\",\"exercises\":";
        for (String exercises : List.of("[null]", "[{\"name\":\"Squat\",\"description\":\" \"}]",
                "[{\"name\":\"Squat\",\"description\":\"Barbell\"},"
                        + "{\"name\":\"Squat\",\"description\":\"Barbell\"}]")) {
            JsonAdaptedWorkoutPlan adapted = JsonUtil.fromJsonString(base + exercises + "}",
                    JsonAdaptedWorkoutPlan.class);
            // One invalid input at a time: list contains a null, invalid, or duplicate exercise.
            assertThrows(IllegalValueException.class, adapted::toModelType);
        }
    }

    // BV: count > MAX_EXERCISES (invalid).
    @Test
    public void fromJson_moreThanHundredExercises_throwsIllegalValueException() throws Exception {
        String exercises = IntStream.range(0, 101)
                .mapToObj(index -> "{\"name\":\"Exercise " + index + "\",\"description\":\"Valid\"}")
                .collect(Collectors.joining(",", "[", "]"));
        JsonAdaptedWorkoutPlan adapted = JsonUtil.fromJsonString(
                "{\"title\":\"Strength\",\"workoutDescription\":\"Squats\",\"date\":\"2023-11-15\","
                        + "\"exercises\":" + exercises + "}", JsonAdaptedWorkoutPlan.class);
        assertThrows(IllegalValueException.class, adapted::toModelType);
    }

    // Each valid input at least once: every workout-plan status survives a round trip.
    @Test
    public void modelToJsonAndBack_preservesEveryStatusAndTags() throws Exception {
        for (WorkoutPlanStatus status : WorkoutPlanStatus.values()) {
            WorkoutPlan original = new WorkoutPlan(new WorkoutTitle("Strength"), new WorkoutDescription("Squats"),
                    WorkoutDate.fromLocalDate(LocalDate.of(2023, 11, 15)), Set.of(new WorkoutPlanTag("legs")), status);
            JsonAdaptedWorkoutPlan adapted = new JsonAdaptedWorkoutPlan(original);

            String json = JsonUtil.toJsonString(adapted);
            JsonAdaptedWorkoutPlan restored = JsonUtil.fromJsonString(json, JsonAdaptedWorkoutPlan.class);

            assertEquals(status.name(), adapted.getStatus());
            assertEquals(status.name(), restored.getStatus());
            assertEquals(original, restored.toModelType());
        }
    }

    // EP: status is missing or null (legacy).
    @Test
    public void fromJson_missingOrNullStatus_defaultsToPendingWithoutRefreshingDeadline() throws Exception {
        for (String statusField : List.of("", ",\"status\":null")) {
            JsonAdaptedWorkoutPlan adapted = JsonUtil.fromJsonString(
                    "{\"title\":\"Strength\",\"workoutDescription\":\"Squats\",\"date\":\"2023-11-15\","
                            + "\"tags\":[\"legs\"]" + statusField + "}", JsonAdaptedWorkoutPlan.class);

            assertEquals(WorkoutPlanStatus.PENDING, adapted.toModelType().getStatus());
            assertEquals(Set.of(new WorkoutPlanTag("legs")), adapted.toModelType().getTags());
        }
    }

    // EP: status is blank, unknown, mis-cased, or padded (invalid).
    @Test
    public void fromJson_invalidStatus_throwsIllegalValueException() throws Exception {
        for (String status : List.of("", "DONE", "completed", " COMPLETED ")) {
            JsonAdaptedWorkoutPlan adapted = JsonUtil.fromJsonString(
                    "{\"title\":\"Strength\",\"workoutDescription\":\"Squats\",\"date\":\"2023-11-15\","
                            + "\"status\":\"" + status + "\"}", JsonAdaptedWorkoutPlan.class);

            // EP: status is blank, unknown, mis-cased, or padded (invalid).
            IllegalValueException exception = assertThrows(IllegalValueException.class, adapted::toModelType);
            assertEquals(JsonAdaptedWorkoutPlan.INVALID_STATUS_MESSAGE, exception.getMessage());
        }
    }

    // BV: count >= 0 (valid).
    @Test
    public void fromJson_missingTags_loadsLegacyPlanWithEmptyTags() throws Exception {
        JsonAdaptedWorkoutPlan adapted = JsonUtil.fromJsonString(
                "{\"title\":\"Strength\",\"workoutDescription\":\"Squats\",\"date\":\"2023-11-15\"}",
                JsonAdaptedWorkoutPlan.class);

        assertEquals(Set.of(), adapted.toModelType().getTags());
        assertEquals(WorkoutPlanStatus.PENDING, adapted.toModelType().getStatus());
    }

    // EP: duplicate serialized tags collapse into a typed set (valid).
    @Test
    public void toModelType_duplicateTags_returnsTypedSet() throws Exception {
        JsonAdaptedWorkoutPlan adapted = new JsonAdaptedWorkoutPlan("Strength", "Squats", "2023-11-15",
                List.of(new JsonAdaptedTag("legs"), new JsonAdaptedTag("legs")));

        assertEquals(Set.of(new WorkoutPlanTag("legs")), adapted.toModelType().getTags());
    }

    // One invalid input at a time: each invalid tag representation is checked.
    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        for (List<JsonAdaptedTag> tags : List.of(List.of(new JsonAdaptedTag("#legs")),
                List.of(new JsonAdaptedTag((String) null)), Arrays.asList((JsonAdaptedTag) null))) {
            JsonAdaptedWorkoutPlan adapted = new JsonAdaptedWorkoutPlan("Strength", "Squats", "2023-11-15", tags);
            // One invalid input at a time: only the serialized tags are invalid.
            assertThrows(IllegalValueException.class, adapted::toModelType);
        }
    }

    // BV: count > MAX_TAG_COUNT (invalid).
    @Test
    public void toModelType_tooManyTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> tags = IntStream.range(0, Tag.MAX_TAG_COUNT + 1)
                .mapToObj(index -> new JsonAdaptedTag("tag" + index))
                .toList();
        JsonAdaptedWorkoutPlan adapted = new JsonAdaptedWorkoutPlan("Strength", "Squats", "2023-11-15", tags);

        IllegalValueException exception = assertThrows(IllegalValueException.class, adapted::toModelType);
        assertEquals(Tag.MESSAGE_TAG_LIMIT, exception.getMessage());
    }

    // Each valid input at least once: all workout-plan fields deserialize successfully.
    @Test
    public void toModelType_validPlan_returnsPlan() throws Exception {
        JsonAdaptedWorkoutPlan adapted = new JsonAdaptedWorkoutPlan(
                "Strength", "Squats", "2023-11-15");

        WorkoutPlan plan = adapted.toModelType();

        assertEquals(new WorkoutTitle("Strength"), plan.getTitle());
        assertEquals(new WorkoutDescription("Squats"), plan.getDescription());
        assertEquals("Strength", adapted.getTitle());
        assertEquals("Squats", adapted.getWorkoutDescription());
        assertEquals("2023-11-15", adapted.getDate());
    }

    // EP: model date serializes in ISO format (valid).
    @Test
    public void modelToJson_usesIsoStorageDate() {
        WorkoutPlan plan = new WorkoutPlan(new WorkoutTitle("Strength"),
                new WorkoutDescription("Squats"),
                new WorkoutDate(Date.from(LocalDate.of(2023, 11, 15)
                        .atStartOfDay(ZoneId.systemDefault()).toInstant())));

        JsonAdaptedWorkoutPlan adapted = new JsonAdaptedWorkoutPlan(plan);

        assertEquals("2023-11-15", adapted.getDate());
    }

    // One invalid input at a time: only the stored date format is invalid.
    @Test
    public void toModelType_invalidDate_throwsIllegalValueException() {
        JsonAdaptedWorkoutPlan adapted = new JsonAdaptedWorkoutPlan(
                "Strength", "Squats", "15-11-2023");

        assertThrows(IllegalValueException.class, adapted::toModelType);
    }

    // EP: title is empty, spaced, or whitespace-only (invalid).
    @Test
    public void toModelType_blankTitle_throwsIllegalValueException() {
        for (String title : List.of("", " ", "\t\n")) {
            JsonAdaptedWorkoutPlan adapted = new JsonAdaptedWorkoutPlan(title, "Squats", "2023-11-15");
            // EP: empty, spaced, or whitespace-only (invalid).
            IllegalValueException exception = assertThrows(IllegalValueException.class, adapted::toModelType);
            assertEquals(WorkoutTitle.MESSAGE_CONSTRAINTS, exception.getMessage());
        }
    }

    // EP: description is empty, spaced, or whitespace-only (invalid).
    @Test
    public void toModelType_blankDescription_throwsIllegalValueException() {
        for (String description : List.of("", " ", "\t\n")) {
            JsonAdaptedWorkoutPlan adapted = new JsonAdaptedWorkoutPlan("Strength", description, "2023-11-15");
            // EP: empty, spaced, or whitespace-only (invalid).
            IllegalValueException exception = assertThrows(IllegalValueException.class, adapted::toModelType);
            assertEquals(WorkoutDescription.MESSAGE_CONSTRAINTS, exception.getMessage());
        }
    }

    // BV: length > MAX_LENGTH (invalid).
    @Test
    public void toModelType_tooLongDescription_throwsIllegalValueException() {
        String description = "a".repeat(WorkoutDescription.MAX_LENGTH + 1);
        JsonAdaptedWorkoutPlan adapted = new JsonAdaptedWorkoutPlan("Strength", description, "2023-11-15");

        IllegalValueException exception = assertThrows(IllegalValueException.class, adapted::toModelType);
        assertEquals(WorkoutDescription.MESSAGE_CONSTRAINTS, exception.getMessage());
    }

    @Test
    public void toModelType_missingFields_throwsIllegalValueException() {
        // One invalid input at a time: only the title is missing.
        assertThrows(IllegalValueException.class, () -> new JsonAdaptedWorkoutPlan(null, "Squats", "2023-11-15")
                .toModelType());
        // One invalid input at a time: only the description is missing.
        assertThrows(IllegalValueException.class, () -> new JsonAdaptedWorkoutPlan("Strength", null, "2023-11-15")
                .toModelType());
        // One invalid input at a time: only the date is missing.
        assertThrows(IllegalValueException.class, () -> new JsonAdaptedWorkoutPlan("Strength", "Squats", null)
                .toModelType());
    }

    // EP: model date is null (invalid).
    @Test
    public void formatDateForStorage_nullDate_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> JsonAdaptedWorkoutPlan.formatDateForStorage(null));
    }

    @Test
    public void formatDateForModel_invalidDate_throwsIllegalArgumentException() {
        // EP: malformed stored date (invalid).
        assertThrows(IllegalArgumentException.class, () -> JsonAdaptedWorkoutPlan.formatDateForModel("invalid"));
        // EP: stored date is null (invalid).
        assertThrows(IllegalArgumentException.class, () -> JsonAdaptedWorkoutPlan.formatDateForModel(null));
    }
}
