package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

public class StudentTagTest {

    @Test
    public void equals_sameTypeAndName_equalWithSameHashCode() {
        StudentTag first = new StudentTag("friend");
        StudentTag second = new StudentTag("friend");

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    public void equals_differentNameOrType_notEqual() {
        StudentTag tag = new StudentTag("friend");

        assertFalse(tag.equals(new StudentTag("family")));
        assertFalse(tag.equals(new WorkoutPlanTag("friend")));
        assertFalse(new WorkoutPlanTag("friend").equals(tag));
        assertFalse(tag.equals(null));
    }
}
