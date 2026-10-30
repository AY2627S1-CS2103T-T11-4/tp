package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.testutil.TypicalStudents.BENSON;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.StudentTag;
import seedu.address.model.tag.Tag;

public class JsonAdaptedStudentTest {

    // EP: duplicate serialized tags collapse into a set (valid).
    @Test
    public void toModelType_duplicateTags_returnsStudentTagSet() throws Exception {
        JsonAdaptedStudent student = new JsonAdaptedStudent("Benson Meier", "98765432", "johnd@example.com",
                "311, Clementi Ave 2, #02-25",
                List.of(new JsonAdaptedTag("friends"), new JsonAdaptedTag("friends")), List.of());

        assertEquals(Set.of(new StudentTag("friends")), student.toModelType().getTags());
    }

    // One invalid input at a time: only one serialized tag entry is null.
    @Test
    public void toModelType_nullTagEntry_throwsIllegalValueException() {
        JsonAdaptedStudent student = new JsonAdaptedStudent("Benson Meier", "98765432", "johnd@example.com",
                "311, Clementi Ave 2, #02-25", Arrays.asList((JsonAdaptedTag) null), List.of());

        assertThrows(IllegalValueException.class, student::toModelType);
    }

    // Each valid input at least once: all student fields deserialize successfully.
    @Test
    public void toModelType_validStudent_returnsStudent() throws Exception {
        assertEquals(BENSON, new JsonAdaptedStudent(BENSON).toModelType());
    }

    // One invalid input at a time: only the name is missing.
    @Test
    public void toModelType_missingName_throwsIllegalValueException() {
        JsonAdaptedStudent student = new JsonAdaptedStudent(null, "98765432", "johnd@example.com",
                "311, Clementi Ave 2, #02-25", List.of(new JsonAdaptedTag("friends")), List.of());
        assertThrows(IllegalValueException.class, student::toModelType);
    }

    // One invalid input at a time: only the name is invalid.
    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedStudent student = new JsonAdaptedStudent("R@chel", "98765432", "johnd@example.com",
                "311, Clementi Ave 2, #02-25", List.of(), List.of());
        assertThrows(IllegalValueException.class, student::toModelType);
    }

    // One invalid input at a time: only the tag is invalid.
    @Test
    public void toModelType_invalidTag_throwsIllegalValueException() {
        JsonAdaptedStudent student = new JsonAdaptedStudent("Benson Meier", "98765432", "johnd@example.com",
                "311, Clementi Ave 2, #02-25", List.of(new JsonAdaptedTag("#friend")), List.of());
        assertThrows(IllegalValueException.class, student::toModelType);
    }

    // BV: count > MAX_TAG_COUNT (invalid).
    @Test
    public void toModelType_tooManyTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> tags = IntStream.range(0, Tag.MAX_TAG_COUNT + 1)
                .mapToObj(index -> new JsonAdaptedTag("tag" + index))
                .toList();
        JsonAdaptedStudent student = new JsonAdaptedStudent("Benson Meier", "98765432", "johnd@example.com",
                "311, Clementi Ave 2, #02-25", tags, List.of());

        IllegalValueException exception = assertThrows(IllegalValueException.class, student::toModelType);
        assertEquals(Tag.MESSAGE_TAG_LIMIT, exception.getMessage());
    }

    // One invalid input at a time: only the phone is missing.
    @Test
    public void toModelType_missingPhone_throwsIllegalValueException() {
        JsonAdaptedStudent student = new JsonAdaptedStudent("Benson Meier", null, "johnd@example.com",
                "311, Clementi Ave 2, #02-25", List.of(), List.of());
        assertThrows(IllegalValueException.class, student::toModelType);
    }

    // One invalid input at a time: only the email is missing.
    @Test
    public void toModelType_missingEmail_throwsIllegalValueException() {
        JsonAdaptedStudent student = new JsonAdaptedStudent("Benson Meier", "98765432", null,
                "311, Clementi Ave 2, #02-25", List.of(), List.of());
        assertThrows(IllegalValueException.class, student::toModelType);
    }

    // One invalid input at a time: only the address is missing.
    @Test
    public void toModelType_missingAddress_throwsIllegalValueException() {
        JsonAdaptedStudent student = new JsonAdaptedStudent("Benson Meier", "98765432", "johnd@example.com",
                null, List.of(), List.of());
        assertThrows(IllegalValueException.class, student::toModelType);
    }

    // One invalid input at a time: only the phone is invalid.
    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedStudent student = new JsonAdaptedStudent("Benson Meier", "invalid", "johnd@example.com",
                "311, Clementi Ave 2, #02-25", List.of(), List.of());
        assertThrows(IllegalValueException.class, student::toModelType);
    }

    // One invalid input at a time: only the email is invalid.
    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedStudent student = new JsonAdaptedStudent("Benson Meier", "98765432", "invalid",
                "311, Clementi Ave 2, #02-25", List.of(), List.of());
        assertThrows(IllegalValueException.class, student::toModelType);
    }

    // One invalid input at a time: only the address is invalid.
    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        JsonAdaptedStudent student = new JsonAdaptedStudent("Benson Meier", "98765432", "johnd@example.com",
                " ", List.of(), List.of());
        assertThrows(IllegalValueException.class, student::toModelType);
    }

    // One invalid input at a time: only one tag value is missing.
    @Test
    public void toModelType_missingTag_throwsIllegalValueException() {
        JsonAdaptedStudent student = new JsonAdaptedStudent("Benson Meier", "98765432", "johnd@example.com",
                "311, Clementi Ave 2, #02-25", List.of(new JsonAdaptedTag((String) null)), List.of());
        assertThrows(IllegalValueException.class, student::toModelType);
    }

    // One invalid input at a time: only the nested workout plan is invalid.
    @Test
    public void toModelType_invalidWorkoutPlan_throwsIllegalValueException() {
        JsonAdaptedStudent student = new JsonAdaptedStudent("Benson Meier", "98765432", "johnd@example.com",
                "311, Clementi Ave 2, #02-25", List.of(),
                List.of(new JsonAdaptedWorkoutPlan(null, "Squats", "2023-11-15")));
        assertThrows(IllegalValueException.class, student::toModelType);
    }

    // One invalid input at a time: only one nested workout plan is null.
    @Test
    public void toModelType_nullWorkoutPlan_throwsIllegalValueException() {
        JsonAdaptedStudent student = new JsonAdaptedStudent("Benson Meier", "98765432", "johnd@example.com",
                "311, Clementi Ave 2, #02-25", List.of(), Arrays.asList((JsonAdaptedWorkoutPlan) null));

        IllegalValueException exception = assertThrows(IllegalValueException.class, student::toModelType);
        assertEquals("Workout plans list contains a null workout plan.", exception.getMessage());
    }

    // Each valid input at least once: a valid nested workout plan deserializes.
    @Test
    public void toModelType_validWorkoutPlan_returnsStudentWithPlan() throws Exception {
        JsonAdaptedStudent student = new JsonAdaptedStudent("Benson Meier", "98765432", "johnd@example.com",
                "311, Clementi Ave 2, #02-25", List.of(),
                List.of(new JsonAdaptedWorkoutPlan("Strength", "Squats", "2023-11-15")));
        assertEquals(1, student.toModelType().getWorkoutPlansList().getWorkoutList().size());
    }
}
