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

    // EP: tag name is null (invalid).
    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new TestTag(null));
    }

    // BV: length < 1 (invalid).
    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        String invalidTagName = "";
        assertThrows(IllegalArgumentException.class, () -> new TestTag(invalidTagName));
    }

    @Test
    public void isValidTagName() {
        // EP: tag name is null (invalid).
        assertThrows(NullPointerException.class, () -> Tag.isValidTagName(null));
    }

    @Test
    public void isValidTagCount() {
        // BV: count < 0 (invalid).
        assertFalse(Tag.isValidTagCount(-1));
        // BV: 0 <= count <= MAX_TAG_COUNT (valid).
        assertTrue(Tag.isValidTagCount(0));
        // BV: 0 <= count <= MAX_TAG_COUNT (valid).
        assertTrue(Tag.isValidTagCount(Tag.MAX_TAG_COUNT));
        // BV: count > MAX_TAG_COUNT (invalid).
        assertFalse(Tag.isValidTagCount(Tag.MAX_TAG_COUNT + 1));
    }

    @Test
    public void equals() {
        Tag tag = new TestTag("strength");

        // EP: Same tag type and name (equivalent).
        assertTrue(tag.equals(new TestTag("strength")));

        // EP: The same tag object (equivalent).
        assertTrue(tag.equals(tag));

        // EP: Null (non-equivalent).
        assertFalse(tag.equals(null));

        // EP: A non-tag object (non-equivalent).
        assertFalse(tag.equals("strength"));

        // EP: A different tag name (non-equivalent).
        assertFalse(tag.equals(new TestTag("cardio")));

        // EP: Same name on a student tag (non-equivalent).
        assertFalse(tag.equals(new StudentTag("strength")));
        // EP: Same name on a workout-plan tag (non-equivalent).
        assertFalse(tag.equals(new WorkoutPlanTag("strength")));
    }
}
