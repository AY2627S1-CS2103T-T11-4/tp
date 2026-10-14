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
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.BrotherGym;
import seedu.address.model.ReadOnlyBrotherGym;
import seedu.address.model.student.Student;
import seedu.address.model.tag.StudentTag;
import seedu.address.model.tag.WorkoutPlanTag;
import seedu.address.model.workoutplan.WorkoutDescription;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.WorkoutPlansList;
import seedu.address.model.workoutplan.WorkoutTitle;

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
    public void readAndSaveBrotherGym_typedTags_preservesStudentAndWorkoutPlanTags() throws Exception {
        Path filePath = testFolder.resolve("TaggedBrotherGym.json");
        WorkoutPlan plan = new WorkoutPlan(
                new WorkoutTitle("Strength"),
                new WorkoutDescription("Squats"),
                JsonAdaptedWorkoutPlan.formatDateForModel("2026-10-14"),
                Set.of(new WorkoutPlanTag("strength"), new WorkoutPlanTag("legs")));
        Student student = new Student(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(), ALICE.getAddress(),
                Set.<StudentTag>of(new StudentTag("strength"), new StudentTag("beginner")),
                new WorkoutPlansList(List.of(plan)));
        BrotherGym original = new BrotherGym();
        original.addStudent(student);
        JsonBrotherGymStorage storage = new JsonBrotherGymStorage(filePath);

        storage.saveBrotherGym(original);
        ReadOnlyBrotherGym readBack = storage.readBrotherGym().orElseThrow();

        assertEquals(original, new BrotherGym(readBack));
        Student restored = readBack.getStudentList().get(0);
        assertEquals(student.getTags(), restored.getTags());
        assertEquals(plan.getTags(), restored.getWorkoutPlansList().getWorkoutList().get(0).getTags());
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
