package seedu.address.logic;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.BrotherGymParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.student.Student;
import seedu.address.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_FORMAT = "Could not save data due to the following error: %s";

    public static final String FILE_OPS_PERMISSION_ERROR_FORMAT =
            "Could not save data to file %s due to insufficient permissions to write to the file or the folder.";

    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final BrotherGymParser brotherGymParser;
    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and {@code Storage}.
     */
    public LogicManager(Model model, Storage storage) {
        this.model = requireNonNull(model);
        this.storage = requireNonNull(storage);
        brotherGymParser = new BrotherGymParser();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        requireNonNull(commandText);
        logger.info("----------------[USER COMMAND]");

        CommandResult commandResult;
        Command command = requireNonNull(brotherGymParser.parseCommand(commandText));

        commandResult = requireNonNull(command.execute(model));

        try {
            storage.saveBrotherGym(model.getBrotherGym());
        } catch (AccessDeniedException e) {
            logger.log(Level.WARNING, "Insufficient permissions while saving application data", e);
            throw new CommandException(String.format(FILE_OPS_PERMISSION_ERROR_FORMAT, e.getMessage()), e);
        } catch (IOException ioe) {
            logger.log(Level.WARNING, "Failed to save application data", ioe);
            throw new CommandException(String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage()), ioe);
        }

        return commandResult;
    }

    @Override
    public ObservableList<Student> getFilteredStudentList() {
        return model.getFilteredStudentList();
    }

    @Override
    public Optional<Student> getCurrentStudent() {
        return model.getCurrentStudent();
    }

    @Override
    public ReadOnlyObjectProperty<Student> currentStudentProperty() {
        return model.currentStudentProperty();
    }

    @Override
    public void setCurrentStudent(Student student) {
        model.setCurrentStudent(student);
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }
}
