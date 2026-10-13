package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

public class WorkoutPlanTagTest {

    @Test
    public void equals_sameTypeAndName_equalWithSameHashCode() {
        WorkoutPlanTag first = new WorkoutPlanTag("strength");
        WorkoutPlanTag second = new WorkoutPlanTag("strength");

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    public void equals_differentNameOrType_notEqual() {
        WorkoutPlanTag tag = new WorkoutPlanTag("strength");

        assertFalse(tag.equals(new WorkoutPlanTag("cardio")));
        assertFalse(tag.equals(new StudentTag("strength")));
        assertFalse(new StudentTag("strength").equals(tag));
        assertFalse(tag.equals(null));
    }
}
