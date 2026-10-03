package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.ReadOnlyBrotherGym;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;

/**
 * API of the Storage component
 */
public interface Storage {

    /**
     * Returns the file path of the UserPrefs data file.
     */
    Path getUserPrefsFilePath();

    /**
     * Returns UserPrefs data from storage.
     * Returns {@code Optional.empty()} if storage file is not found.
     *
     * @throws DataLoadingException if the loading of data from preference file failed.
     */
    Optional<UserPrefs> readUserPrefs() throws DataLoadingException;

    /**
     * Saves the given {@link seedu.address.model.ReadOnlyUserPrefs} to the storage.
     * @param userPrefs cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException;

    /**
     * Returns the file path of the BrotherGym data file.
     */
    Path getBrotherGymFilePath();

    /**
     * Returns BrotherGym data as a {@link ReadOnlyBrotherGym}.
     * Returns {@code Optional.empty()} if storage file is not found.
     *
     * @throws DataLoadingException if loading the data from storage failed.
     */
    Optional<ReadOnlyBrotherGym> readBrotherGym() throws DataLoadingException;

    /**
     * Saves the given {@link ReadOnlyBrotherGym} to the storage.
     * @param brotherGym cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveBrotherGym(ReadOnlyBrotherGym brotherGym) throws IOException;

}
