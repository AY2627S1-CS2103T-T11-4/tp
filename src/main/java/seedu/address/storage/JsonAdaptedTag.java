package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.StudentTag;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.WorkoutPlanTag;

/**
 * Jackson-friendly version of {@link Tag}.
 */
class JsonAdaptedTag {

    private final String tagName;

    /**
     * Constructs a JSON-adapted tag from its serialized name.
     *
     * @param tagName The serialized tag name.
     */
    @JsonCreator
    public JsonAdaptedTag(String tagName) {
        this.tagName = tagName;
    }

    /**
     * Converts a model tag into its JSON-adapted representation.
     *
     * @param source The model tag to adapt.
     */
    public JsonAdaptedTag(Tag source) {
        tagName = source.tagName;
    }

    /**
     * Returns the serialized tag name.
     *
     * @return The tag name.
     */
    @JsonValue
    public String getTagName() {
        return tagName;
    }

    /**
     * Converts this JSON-adapted tag into a student tag.
     *
     * @return The student tag.
     * @throws IllegalValueException If the tag name is missing or invalid.
     */
    public StudentTag toStudentTag() throws IllegalValueException {
        validateTagName();
        return new StudentTag(tagName);
    }

    /**
     * Converts this JSON-adapted tag into a workout plan tag.
     *
     * @return The workout plan tag.
     * @throws IllegalValueException If the tag name is missing or invalid.
     */
    public WorkoutPlanTag toWorkoutPlanTag() throws IllegalValueException {
        validateTagName();
        return new WorkoutPlanTag(tagName);
    }

    private void validateTagName() throws IllegalValueException {
        if (tagName == null || !Tag.isValidTagName(tagName)) {
            throw new IllegalValueException(Tag.MESSAGE_CONSTRAINTS);
        }
    }
}
