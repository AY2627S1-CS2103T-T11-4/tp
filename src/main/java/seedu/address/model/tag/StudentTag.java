package seedu.address.model.tag;

import java.util.Objects;

/**
 * Represents a tag associated with a student.
 */
public final class StudentTag extends Tag {

    /**
     * Creates a student tag with the given name.
     */
    public StudentTag(String tagName) {
        super(tagName);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof StudentTag otherTag)) {
            return false;
        }
        return tagName.equals(otherTag.tagName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(StudentTag.class, tagName);
    }
}
