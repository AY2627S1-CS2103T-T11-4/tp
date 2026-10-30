package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class StudentTagTest {

    @Test
    public void equals() {
        StudentTag tag = new StudentTag("friend");

        // EP: Same student-tag name (equivalent).
        assertTrue(tag.equals(new StudentTag("friend")));

        // EP: The same student-tag object (equivalent).
        assertTrue(tag.equals(tag));

        // EP: Null (non-equivalent).
        assertFalse(tag.equals(null));

        // EP: A non-tag object (non-equivalent).
        assertFalse(tag.equals("friend"));

        // EP: A different student-tag name (non-equivalent).
        assertFalse(tag.equals(new StudentTag("family")));

        // EP: Same name on a workout-plan tag (non-equivalent).
        assertFalse(tag.equals(new WorkoutPlanTag("friend")));
    }

    @Test
    public void hashCode_equalTags_haveSameHashCode() {
        StudentTag first = new StudentTag("friend");
        StudentTag second = new StudentTag("friend");

        assertEquals(first.hashCode(), second.hashCode());
    }
}
