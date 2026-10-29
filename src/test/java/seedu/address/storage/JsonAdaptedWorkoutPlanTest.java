package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.WorkoutPlanTag;
import seedu.address.model.workoutplan.WorkoutDate;
import seedu.address.model.workoutplan.WorkoutDescription;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.WorkoutPlanStatus;
import seedu.address.model.workoutplan.WorkoutTitle;

public class JsonAdaptedWorkoutPlanTest {

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

    @Test
    public void fromJson_invalidStatus_throwsIllegalValueException() throws Exception {
        for (String status : List.of("", "DONE", "completed", " COMPLETED ")) {
            JsonAdaptedWorkoutPlan adapted = JsonUtil.fromJsonString(
                    "{\"title\":\"Strength\",\"workoutDescription\":\"Squats\",\"date\":\"2023-11-15\","
                            + "\"status\":\"" + status + "\"}", JsonAdaptedWorkoutPlan.class);

            IllegalValueException exception = assertThrows(IllegalValueException.class, adapted::toModelType);
            assertEquals(JsonAdaptedWorkoutPlan.INVALID_STATUS_MESSAGE, exception.getMessage());
        }
    }

    @Test
    public void fromJson_missingTags_loadsLegacyPlanWithEmptyTags() throws Exception {
        JsonAdaptedWorkoutPlan adapted = JsonUtil.fromJsonString(
                "{\"title\":\"Strength\",\"workoutDescription\":\"Squats\",\"date\":\"2023-11-15\"}",
                JsonAdaptedWorkoutPlan.class);

        assertEquals(Set.of(), adapted.toModelType().getTags());
        assertEquals(WorkoutPlanStatus.PENDING, adapted.toModelType().getStatus());
    }

    @Test
    public void toModelType_duplicateTags_returnsTypedSet() throws Exception {
        JsonAdaptedWorkoutPlan adapted = new JsonAdaptedWorkoutPlan("Strength", "Squats", "2023-11-15",
                List.of(new JsonAdaptedTag("legs"), new JsonAdaptedTag("legs")));

        assertEquals(Set.of(new WorkoutPlanTag("legs")), adapted.toModelType().getTags());
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        for (List<JsonAdaptedTag> tags : List.of(List.of(new JsonAdaptedTag("#legs")),
                List.of(new JsonAdaptedTag((String) null)), Arrays.asList((JsonAdaptedTag) null))) {
            JsonAdaptedWorkoutPlan adapted = new JsonAdaptedWorkoutPlan("Strength", "Squats", "2023-11-15", tags);
            assertThrows(IllegalValueException.class, adapted::toModelType);
        }
    }

    @Test
    public void toModelType_tooManyTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> tags = IntStream.range(0, Tag.MAX_TAG_COUNT + 1)
                .mapToObj(index -> new JsonAdaptedTag("tag" + index))
                .toList();
        JsonAdaptedWorkoutPlan adapted = new JsonAdaptedWorkoutPlan("Strength", "Squats", "2023-11-15", tags);

        IllegalValueException exception = assertThrows(IllegalValueException.class, adapted::toModelType);
        assertEquals(Tag.MESSAGE_TAG_LIMIT, exception.getMessage());
    }

    @Test
    public void toModelType_validPlan_returnsPlan() throws Exception {
        JsonAdaptedWorkoutPlan adapted = new JsonAdaptedWorkoutPlan(
                "Strength", "Squats", "2023-11-15");

        WorkoutPlan plan = adapted.toModelType();

        assertEquals(new WorkoutTitle("Strength"), plan.getTitle());
        assertEquals(new WorkoutDescription("Squats"), plan.getWorkouts());
        assertEquals("Strength", adapted.getTitle());
        assertEquals("Squats", adapted.getWorkoutDescription());
        assertEquals("2023-11-15", adapted.getDate());
    }

    @Test
    public void modelToJson_usesIsoStorageDate() {
        WorkoutPlan plan = new WorkoutPlan(new WorkoutTitle("Strength"),
                new WorkoutDescription("Squats"),
                new WorkoutDate(Date.from(LocalDate.of(2023, 11, 15)
                        .atStartOfDay(ZoneId.systemDefault()).toInstant())));

        JsonAdaptedWorkoutPlan adapted = new JsonAdaptedWorkoutPlan(plan);

        assertEquals("2023-11-15", adapted.getDate());
    }

    @Test
    public void toModelType_invalidDate_throwsIllegalValueException() {
        JsonAdaptedWorkoutPlan adapted = new JsonAdaptedWorkoutPlan(
                "Strength", "Squats", "15-11-2023");

        assertThrows(IllegalValueException.class, adapted::toModelType);
    }

    @Test
    public void toModelType_blankTitle_throwsIllegalValueException() {
        for (String title : List.of("", " ", "\t\n")) {
            JsonAdaptedWorkoutPlan adapted = new JsonAdaptedWorkoutPlan(title, "Squats", "2023-11-15");
            IllegalValueException exception = assertThrows(IllegalValueException.class, adapted::toModelType);
            assertEquals(WorkoutTitle.MESSAGE_CONSTRAINTS, exception.getMessage());
        }
    }

    @Test
    public void toModelType_blankDescription_throwsIllegalValueException() {
        for (String description : List.of("", " ", "\t\n")) {
            JsonAdaptedWorkoutPlan adapted = new JsonAdaptedWorkoutPlan("Strength", description, "2023-11-15");
            IllegalValueException exception = assertThrows(IllegalValueException.class, adapted::toModelType);
            assertEquals(WorkoutDescription.MESSAGE_CONSTRAINTS, exception.getMessage());
        }
    }

    @Test
    public void toModelType_tooLongDescription_throwsIllegalValueException() {
        String description = "a".repeat(WorkoutDescription.MAX_LENGTH + 1);
        JsonAdaptedWorkoutPlan adapted = new JsonAdaptedWorkoutPlan("Strength", description, "2023-11-15");

        IllegalValueException exception = assertThrows(IllegalValueException.class, adapted::toModelType);
        assertEquals(WorkoutDescription.MESSAGE_CONSTRAINTS, exception.getMessage());
    }

    @Test
    public void toModelType_missingFields_throwsIllegalValueException() {
        assertThrows(IllegalValueException.class, () -> new JsonAdaptedWorkoutPlan(null, "Squats", "2023-11-15")
                .toModelType());
        assertThrows(IllegalValueException.class, () -> new JsonAdaptedWorkoutPlan("Strength", null, "2023-11-15")
                .toModelType());
        assertThrows(IllegalValueException.class, () -> new JsonAdaptedWorkoutPlan("Strength", "Squats", null)
                .toModelType());
    }

    @Test
    public void formatDateForStorage_nullDate_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> JsonAdaptedWorkoutPlan.formatDateForStorage(null));
    }

    @Test
    public void formatDateForModel_invalidDate_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> JsonAdaptedWorkoutPlan.formatDateForModel("invalid"));
        assertThrows(IllegalArgumentException.class, () -> JsonAdaptedWorkoutPlan.formatDateForModel(null));
    }
}
