package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class WorkoutPlanTagTest {

    @Test
    public void equals() {
        WorkoutPlanTag tag = new WorkoutPlanTag("strength");

        // EP: Same workout-plan tag name (equivalent).
        assertTrue(tag.equals(new WorkoutPlanTag("strength")));

        // EP: The same workout-plan tag object (equivalent).
        assertTrue(tag.equals(tag));

        // EP: Null (non-equivalent).
        assertFalse(tag.equals(null));

        // EP: A non-tag object (non-equivalent).
        assertFalse(tag.equals("strength"));

        // EP: A different workout-plan tag name (non-equivalent).
        assertFalse(tag.equals(new WorkoutPlanTag("cardio")));

        // EP: Same name on a student tag (non-equivalent).
        assertFalse(tag.equals(new StudentTag("strength")));
    }

    @Test
    public void hashCode_equalTags_haveSameHashCode() {
        WorkoutPlanTag first = new WorkoutPlanTag("strength");
        WorkoutPlanTag second = new WorkoutPlanTag("strength");

        assertEquals(first.hashCode(), second.hashCode());
    }
}
