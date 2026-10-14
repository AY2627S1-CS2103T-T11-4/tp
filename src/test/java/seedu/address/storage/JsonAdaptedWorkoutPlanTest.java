package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.tag.WorkoutPlanTag;
import seedu.address.model.workoutplan.WorkoutDate;
import seedu.address.model.workoutplan.WorkoutDescription;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.WorkoutTitle;

public class JsonAdaptedWorkoutPlanTest {

    @Test
    public void fromJson_missingTags_loadsLegacyPlanWithEmptyTags() throws Exception {
        JsonAdaptedWorkoutPlan adapted = JsonUtil.fromJsonString(
                "{\"title\":\"Strength\",\"workoutDescription\":\"Squats\",\"date\":\"2023-11-15\"}",
                JsonAdaptedWorkoutPlan.class);

        assertEquals(Set.of(), adapted.toModelType().getTags());
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
