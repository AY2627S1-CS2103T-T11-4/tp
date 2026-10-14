package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class WorkoutPlanTagTest {

    @Test
    public void equals() {
        WorkoutPlanTag tag = new WorkoutPlanTag("strength");

        // same values -> returns true
        assertTrue(tag.equals(new WorkoutPlanTag("strength")));

        // same object -> returns true
        assertTrue(tag.equals(tag));

        // null -> returns false
        assertFalse(tag.equals(null));

        // different type -> returns false
        assertFalse(tag.equals("strength"));

        // different tag name -> returns false
        assertFalse(tag.equals(new WorkoutPlanTag("cardio")));

        // same name but different tag type -> returns false
        assertFalse(tag.equals(new StudentTag("strength")));
    }

    @Test
    public void hashCode_equalTags_haveSameHashCode() {
        WorkoutPlanTag first = new WorkoutPlanTag("strength");
        WorkoutPlanTag second = new WorkoutPlanTag("strength");

        assertEquals(first.hashCode(), second.hashCode());
    }
}
