package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.workoutplan.WorkoutDate;
import seedu.address.model.workoutplan.WorkoutDescription;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.WorkoutTitle;

public class JsonAdaptedWorkoutPlanTest {

    @Test
    public void toModelType_validPlan_returnsPlan() throws Exception {
        JsonAdaptedWorkoutPlan adapted = new JsonAdaptedWorkoutPlan(
                "Strength", "Squats", "2023-11-15");

        WorkoutPlan plan = adapted.toModelType();

        assertEquals(new WorkoutTitle("Strength"), plan.getTitle());
        assertEquals(new WorkoutDescription("Squats"), plan.getWorkouts());
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
}
