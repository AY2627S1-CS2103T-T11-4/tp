package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.ReadOnlyBrotherGym;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;

/**
 * Manages storage of BrotherGym data in local storage.
 */
public class StorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private final JsonBrotherGymStorage brotherGymStorage;
    private final JsonUserPrefsStorage userPrefsStorage;

    /**
     * Creates a {@code StorageManager} with non-null BrotherGym and user prefs storage.
     */
    public StorageManager(JsonBrotherGymStorage brotherGymStorage, JsonUserPrefsStorage userPrefsStorage) {
        this.brotherGymStorage = requireNonNull(brotherGymStorage);
        this.userPrefsStorage = requireNonNull(userPrefsStorage);
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataLoadingException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ BrotherGym methods ==============================

    @Override
    public Path getBrotherGymFilePath() {
        return brotherGymStorage.getBrotherGymFilePath();
    }

    @Override
    public Optional<ReadOnlyBrotherGym> readBrotherGym() throws DataLoadingException {
        logger.fine("Attempting to read data from file: " + brotherGymStorage.getBrotherGymFilePath());
        return brotherGymStorage.readBrotherGym();
    }

    @Override
    public void saveBrotherGym(ReadOnlyBrotherGym brotherGym) throws IOException {
        logger.fine("Attempting to write to data file: " + brotherGymStorage.getBrotherGymFilePath());
        brotherGymStorage.saveBrotherGym(brotherGym);
    }

}
