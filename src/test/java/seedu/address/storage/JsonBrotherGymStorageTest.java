package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalStudents.ALICE;
import static seedu.address.testutil.TypicalStudents.HOON;
import static seedu.address.testutil.TypicalStudents.IDA;
import static seedu.address.testutil.TypicalStudents.getTypicalBrotherGym;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.BrotherGym;
import seedu.address.model.ReadOnlyBrotherGym;

public class JsonBrotherGymStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonBrotherGymStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readBrotherGym_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readBrotherGym(null));
    }

    private java.util.Optional<ReadOnlyBrotherGym> readBrotherGym(String filePath) throws Exception {
        return new JsonBrotherGymStorage(Paths.get(filePath)).readBrotherGym(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readBrotherGym("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataLoadingException.class, () -> readBrotherGym("notJsonFormatBrotherGym.json"));
    }

    @Test
    public void readBrotherGym_invalidStudentBrotherGym_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readBrotherGym("invalidStudentBrotherGym.json"));
    }

    @Test
    public void readBrotherGym_invalidAndValidStudentBrotherGym_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readBrotherGym("invalidAndValidStudentBrotherGym.json"));
    }

    @Test
    public void readAndSaveBrotherGym_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("TempBrotherGym.json");
        BrotherGym original = getTypicalBrotherGym();
        JsonBrotherGymStorage jsonBrotherGymStorage = new JsonBrotherGymStorage(filePath);

        // Save in new file and read back
        jsonBrotherGymStorage.saveBrotherGym(original, filePath);
        ReadOnlyBrotherGym readBack = jsonBrotherGymStorage.readBrotherGym(filePath).get();
        assertEquals(original, new BrotherGym(readBack));

        // Modify data, overwrite existing file, and read back
        original.addStudent(HOON);
        original.removeStudent(ALICE);
        jsonBrotherGymStorage.saveBrotherGym(original, filePath);
        readBack = jsonBrotherGymStorage.readBrotherGym(filePath).get();
        assertEquals(original, new BrotherGym(readBack));

        // Save and read without specifying file path
        original.addStudent(IDA);
        jsonBrotherGymStorage.saveBrotherGym(original); // file path not specified
        readBack = jsonBrotherGymStorage.readBrotherGym().get(); // file path not specified
        assertEquals(original, new BrotherGym(readBack));

    }

    @Test
    public void saveBrotherGym_nullBrotherGym_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveBrotherGym(null, "SomeFile.json"));
    }

    /**
     * Saves {@code brotherGym} at the specified {@code filePath}.
     */
    private void saveBrotherGym(ReadOnlyBrotherGym brotherGym, String filePath) {
        try {
            new JsonBrotherGymStorage(Paths.get(filePath))
                    .saveBrotherGym(brotherGym, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveBrotherGym_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveBrotherGym(new BrotherGym(), null));
    }
}
