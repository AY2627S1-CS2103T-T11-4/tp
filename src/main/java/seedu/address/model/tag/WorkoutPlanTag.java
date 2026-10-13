package seedu.address.model.tag;

import java.util.Objects;

/**
 * Represents a tag associated with a workout plan.
 */
public final class WorkoutPlanTag extends Tag {

    /**
     * Creates a workout plan tag with the given name.
     */
    public WorkoutPlanTag(String tagName) {
        super(tagName);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof WorkoutPlanTag otherTag)) {
            return false;
        }
        return tagName.equals(otherTag.tagName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(WorkoutPlanTag.class, tagName);
    }
}
