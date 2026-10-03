package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static seedu.address.testutil.TypicalStudents.getTypicalBrotherGym;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.BrotherGym;
import seedu.address.model.ReadOnlyBrotherGym;
import seedu.address.model.UserPrefs;

public class StorageManagerTest {

    @TempDir
    public Path testFolder;

    private StorageManager storageManager;

    @BeforeEach
    public void setUp() {
        JsonBrotherGymStorage brotherGymStorage = new JsonBrotherGymStorage(getTempFilePath("ab"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        storageManager = new StorageManager(brotherGymStorage, userPrefsStorage);
    }

    private Path getTempFilePath(String fileName) {
        return testFolder.resolve(fileName);
    }

    @Test
    public void prefsReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonUserPrefsStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
         */
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(new GuiSettings(300, 600, 4, 6));
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void brotherGymReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonBrotherGymStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonBrotherGymStorageTest} class.
         */
        BrotherGym original = getTypicalBrotherGym();
        storageManager.saveBrotherGym(original);
        ReadOnlyBrotherGym retrieved = storageManager.readBrotherGym().get();
        assertEquals(original, new BrotherGym(retrieved));
    }

    @Test
    public void getBrotherGymFilePath() {
        assertNotNull(storageManager.getBrotherGymFilePath());
    }

}
