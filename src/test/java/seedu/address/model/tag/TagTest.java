package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TagTest {

    private static class TestTag extends Tag {
        TestTag(String tagName) {
            super(tagName);
        }
    }

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new TestTag(null));
    }

    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        String invalidTagName = "";
        assertThrows(IllegalArgumentException.class, () -> new TestTag(invalidTagName));
    }

    @Test
    public void isValidTagName() {
        // null tag name
        assertThrows(NullPointerException.class, () -> Tag.isValidTagName(null));
    }

    @Test
    public void equals() {
        Tag tag = new TestTag("strength");

        // same values -> returns true
        assertTrue(tag.equals(new TestTag("strength")));

        // same object -> returns true
        assertTrue(tag.equals(tag));

        // null -> returns false
        assertFalse(tag.equals(null));

        // different type -> returns false
        assertFalse(tag.equals("strength"));

        // different tag name -> returns false
        assertFalse(tag.equals(new TestTag("cardio")));

        // same name but different tag type -> returns false
        assertFalse(tag.equals(new StudentTag("strength")));
        assertFalse(tag.equals(new WorkoutPlanTag("strength")));
    }
}
