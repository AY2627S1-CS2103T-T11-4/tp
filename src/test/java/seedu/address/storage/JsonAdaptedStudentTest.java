package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.testutil.TypicalStudents.BENSON;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
public class JsonAdaptedStudentTest {

    @Test
    public void toModelType_validStudent_returnsStudent() throws Exception {
        assertEquals(BENSON, new JsonAdaptedStudent(BENSON).toModelType());
    }

    @Test
    public void toModelType_missingName_throwsIllegalValueException() {
        JsonAdaptedStudent student = new JsonAdaptedStudent(null, "98765432", "johnd@example.com",
                "311, Clementi Ave 2, #02-25", List.of(new JsonAdaptedTag("friends")), List.of());
        assertThrows(IllegalValueException.class, student::toModelType);
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedStudent student = new JsonAdaptedStudent("R@chel", "98765432", "johnd@example.com",
                "311, Clementi Ave 2, #02-25", List.of(), List.of());
        assertThrows(IllegalValueException.class, student::toModelType);
    }

    @Test
    public void toModelType_invalidTag_throwsIllegalValueException() {
        JsonAdaptedStudent student = new JsonAdaptedStudent("Benson Meier", "98765432", "johnd@example.com",
                "311, Clementi Ave 2, #02-25", List.of(new JsonAdaptedTag("#friend")), List.of());
        assertThrows(IllegalValueException.class, student::toModelType);
    }
}
