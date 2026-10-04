package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.ReadOnlyBrotherGym;

/**
 * A class to access BrotherGym data stored as a JSON file on the hard disk.
 */
public class JsonBrotherGymStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonBrotherGymStorage.class);

    private Path filePath;

    public JsonBrotherGymStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getBrotherGymFilePath() {
        return filePath;
    }

    /**
     * Returns BrotherGym data as a {@link ReadOnlyBrotherGym}.
     * Returns {@code Optional.empty()} if storage file is not found.
     *
     * @throws DataLoadingException if loading the data from storage failed.
     */
    public Optional<ReadOnlyBrotherGym> readBrotherGym() throws DataLoadingException {
        return readBrotherGym(filePath);
    }

    /**
     * Similar to {@link #readBrotherGym()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataLoadingException if loading the data from storage failed.
     */
    public Optional<ReadOnlyBrotherGym> readBrotherGym(Path filePath) throws DataLoadingException {
        requireNonNull(filePath);

        Optional<JsonSerializableBrotherGym> jsonBrotherGym = JsonUtil.readJsonFile(
                filePath, JsonSerializableBrotherGym.class);
        if (!jsonBrotherGym.isPresent()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonBrotherGym.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataLoadingException(ive);
        }
    }

    /**
     * Saves the given {@link ReadOnlyBrotherGym} to the storage.
     * @param brotherGym cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    public void saveBrotherGym(ReadOnlyBrotherGym brotherGym) throws IOException {
        saveBrotherGym(brotherGym, filePath);
    }

    /**
     * Similar to {@link #saveBrotherGym(ReadOnlyBrotherGym)}.
     *
     * @param filePath location of the data. Cannot be null.
     */
    public void saveBrotherGym(ReadOnlyBrotherGym brotherGym, Path filePath) throws IOException {
        requireNonNull(brotherGym);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableBrotherGym(brotherGym), filePath);
    }

}
