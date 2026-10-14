package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class StudentTagTest {

    @Test
    public void equals() {
        StudentTag tag = new StudentTag("friend");

        // same values -> returns true
        assertTrue(tag.equals(new StudentTag("friend")));

        // same object -> returns true
        assertTrue(tag.equals(tag));

        // null -> returns false
        assertFalse(tag.equals(null));

        // different type -> returns false
        assertFalse(tag.equals("friend"));

        // different tag name -> returns false
        assertFalse(tag.equals(new StudentTag("family")));

        // same name but different tag type -> returns false
        assertFalse(tag.equals(new WorkoutPlanTag("friend")));
    }

    @Test
    public void hashCode_equalTags_haveSameHashCode() {
        StudentTag first = new StudentTag("friend");
        StudentTag second = new StudentTag("friend");

        assertEquals(first.hashCode(), second.hashCode());
    }
}
