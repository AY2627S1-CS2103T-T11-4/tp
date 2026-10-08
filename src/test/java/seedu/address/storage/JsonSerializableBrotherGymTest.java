package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.BrotherGym;
import seedu.address.testutil.TypicalStudents;


public class JsonSerializableBrotherGymTest {

    @Test
    public void toModelType_nullStudent_throwsIllegalValueException() {
        JsonSerializableBrotherGym adapted = new JsonSerializableBrotherGym(
                Arrays.asList((JsonAdaptedStudent) null));
        assertThrows(IllegalValueException.class, adapted::toModelType);
    }

    @Test
    public void constructor_nullStudents_createsEmptyModel() throws Exception {
        assertEquals(new BrotherGym(), new JsonSerializableBrotherGym((List<JsonAdaptedStudent>) null).toModelType());
    }

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableBrotherGymTest");
    private static final Path TYPICAL_STUDENTS_FILE = TEST_DATA_FOLDER.resolve("typicalStudentsBrotherGym.json");
    private static final Path INVALID_STUDENT_FILE = TEST_DATA_FOLDER.resolve("invalidStudentBrotherGym.json");
    private static final Path DUPLICATE_STUDENT_FILE = TEST_DATA_FOLDER.resolve("duplicateStudentBrotherGym.json");

    @Test
    public void toModelType_typicalStudentsFile_success() throws Exception {
        JsonSerializableBrotherGym dataFromFile = JsonUtil.readJsonFile(TYPICAL_STUDENTS_FILE,
                JsonSerializableBrotherGym.class).get();
        BrotherGym brotherGymFromFile = dataFromFile.toModelType();
        BrotherGym typicalStudentsBrotherGym = TypicalStudents.getTypicalBrotherGym();
        assertEquals(brotherGymFromFile, typicalStudentsBrotherGym);
    }

    @Test
    public void toModelType_invalidStudentFile_throwsIllegalValueException() throws Exception {
        JsonSerializableBrotherGym dataFromFile = JsonUtil.readJsonFile(INVALID_STUDENT_FILE,
                JsonSerializableBrotherGym.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicateStudents_throwsIllegalValueException() throws Exception {
        JsonSerializableBrotherGym dataFromFile = JsonUtil.readJsonFile(DUPLICATE_STUDENT_FILE,
                JsonSerializableBrotherGym.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableBrotherGym.MESSAGE_DUPLICATE_STUDENT,
                dataFromFile::toModelType);
    }

}
