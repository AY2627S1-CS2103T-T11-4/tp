package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalStudents.ALICE;
import static seedu.address.testutil.TypicalStudents.HOON;
import static seedu.address.testutil.TypicalStudents.IDA;
import static seedu.address.testutil.TypicalStudents.getTypicalBrotherGym;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.BrotherGym;
import seedu.address.model.ReadOnlyBrotherGym;
import seedu.address.model.exercise.Exercise;
import seedu.address.model.exercise.ExerciseDescription;
import seedu.address.model.exercise.ExerciseName;
import seedu.address.model.exercise.ExercisesList;
import seedu.address.model.student.Student;
import seedu.address.model.tag.StudentTag;
import seedu.address.model.tag.WorkoutPlanTag;
import seedu.address.model.workoutplan.WorkoutDescription;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.WorkoutPlanStatus;
import seedu.address.model.workoutplan.WorkoutPlansList;
import seedu.address.model.workoutplan.WorkoutTitle;

public class JsonBrotherGymStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonBrotherGymStorageTest");

    @TempDir
    public Path testFolder;

    // EP: ordered exercises survive storage round trip (valid).
    @Test
    public void readAndSaveBrotherGym_exercises_preservesValuesAndOrder() throws Exception {
        Path filePath = testFolder.resolve("Exercises.json");
        Exercise squat = new Exercise(new ExerciseName("Squat"), new ExerciseDescription("Barbell"));
        Exercise press = new Exercise(new ExerciseName("Press"), new ExerciseDescription("Dumbbell"));
        WorkoutPlan plan = new WorkoutPlan(new WorkoutTitle("Strength"), new WorkoutDescription("Leg day"),
                JsonAdaptedWorkoutPlan.formatDateForModel("2026-10-14"), Set.of(), WorkoutPlanStatus.PENDING,
                new ExercisesList(List.of(squat, press)));
        Student student = new Student(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(), ALICE.getAddress(),
                ALICE.getTags(), new WorkoutPlansList(List.of(plan)));
        BrotherGym original = new BrotherGym();
        original.addStudent(student);
        JsonBrotherGymStorage storage = new JsonBrotherGymStorage(filePath);

        storage.saveBrotherGym(original);
        ReadOnlyBrotherGym restored = storage.readBrotherGym().orElseThrow();

        assertEquals(List.of(squat, press), restored.getStudentList().get(0).getWorkoutPlansList()
                .getWorkoutPlan(0).getExercisesList().getExerciseList());
    }

    // One invalid input at a time: only the stored exercise is corrupt.
    @Test
    public void readBrotherGym_corruptExercise_throwsDataLoadingException() throws Exception {
        Path filePath = testFolder.resolve("InvalidExercise.json");
        JsonBrotherGymStorage storage = new JsonBrotherGymStorage(filePath);
        Files.writeString(filePath, """
                {"students":[{"name":"Benson Meier","phone":"98765432","email":"johnd@example.com",
                  "address":"Clementi","tags":[],"workoutPlans":[{
                    "title":"Strength","workoutDescription":"Squats","date":"2023-11-15",
                    "exercises":[{"name":"Squat","description":"Barbell"},
                                 {"name":"Squat","description":"Barbell"}]
                  }]}]}
                """);

        assertThrows(DataLoadingException.class, storage::readBrotherGym);
    }

    // EP: storage path is null (invalid).
    @Test
    public void constructor_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new JsonBrotherGymStorage(null));
    }

    // One invalid input at a time: only the stored workout plan is invalid.
    @Test
    public void readBrotherGym_invalidWorkoutPlans_throwsDataLoadingException() throws IOException {
        Path filePath = testFolder.resolve("InvalidWorkoutPlans.json");
        JsonBrotherGymStorage storage = new JsonBrotherGymStorage(filePath);
        for (String plans : List.of("[null]",
                "[{\"title\":\" \",\"workoutDescription\":\"Squats\",\"date\":\"2023-11-15\"}]",
                "[{\"title\":\"Strength\",\"workoutDescription\":\"\",\"date\":\"2023-11-15\"}]")) {
            Files.writeString(filePath, """
                    {"students":[{
                      "name":"Benson Meier","phone":"98765432","email":"johnd@example.com",
                      "address":"311, Clementi Ave 2, #02-25","tags":[],"workoutPlans":%s
                    }]}
                    """.formatted(plans));
            assertThrows(DataLoadingException.class, storage::readBrotherGym);
        }
    }

    // EP: read path is null (invalid).
    @Test
    public void readBrotherGym_nullFilePath_throwsNullPointerException() {
        JsonBrotherGymStorage storage = new JsonBrotherGymStorage(testFolder.resolve("brothergym.json"));
        assertThrows(NullPointerException.class, () -> storage.readBrotherGym(null));
    }

    private java.util.Optional<ReadOnlyBrotherGym> readBrotherGym(String filePath) throws Exception {
        return new JsonBrotherGymStorage(Paths.get(filePath)).readBrotherGym(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    // EP: no stored data exists (missing file).
    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readBrotherGym("NonExistentFile.json").isPresent());
    }

    // EP: stored data is not JSON (malformed file).
    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataLoadingException.class, () -> readBrotherGym("notJsonFormatBrotherGym.json"));
    }

    // EP: stored student is invalid (invalid).
    @Test
    public void readBrotherGym_invalidStudentBrotherGym_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readBrotherGym("invalidStudentBrotherGym.json"));
    }

    // EP: one invalid student among valid students (invalid).
    @Test
    public void readBrotherGym_invalidAndValidStudentBrotherGym_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readBrotherGym("invalidAndValidStudentBrotherGym.json"));
    }

    // EP: complete data survives storage round trip (valid).
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

    // Each valid input at least once: both tag types survive storage round trip.
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

    // Each valid input at least once: every status survives storage round trip.
    @Test
    public void readAndSaveBrotherGym_workoutStatuses_preservesAllStatusesWithoutRefreshing() throws Exception {
        Path filePath = testFolder.resolve("WorkoutStatuses.json");
        List<WorkoutPlan> plans = List.of(WorkoutPlanStatus.values()).stream()
                .map(status -> new WorkoutPlan(new WorkoutTitle(status.name()), new WorkoutDescription("Squats"),
                        JsonAdaptedWorkoutPlan.formatDateForModel("2023-11-15"),
                        Set.of(new WorkoutPlanTag("legs")), status))
                .toList();
        Student student = new Student(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(), ALICE.getAddress(),
                ALICE.getTags(), new WorkoutPlansList(plans));
        BrotherGym original = new BrotherGym();
        original.addStudent(student);
        JsonBrotherGymStorage storage = new JsonBrotherGymStorage(filePath);

        storage.saveBrotherGym(original);
        ReadOnlyBrotherGym restored = storage.readBrotherGym().orElseThrow();

        assertEquals(original, new BrotherGym(restored));
        assertEquals(plans, restored.getStudentList().get(0).getWorkoutPlansList().getWorkoutList());

        String invalidJson = JsonUtil.toJsonString(new JsonSerializableBrotherGym(original))
                .replace("\"COMPLETED\"", "\"INVALID_STATUS\"");
        Files.writeString(filePath, invalidJson);
        assertThrows(DataLoadingException.class, storage::readBrotherGym);
    }

    // One invalid input at a time: only the data object is null.
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

    // One invalid input at a time: only the save path is null.
    @Test
    public void saveBrotherGym_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveBrotherGym(new BrotherGym(), null));
    }
}
