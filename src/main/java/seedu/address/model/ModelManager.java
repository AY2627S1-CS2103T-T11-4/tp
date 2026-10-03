package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.student.Student;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final BrotherGym brotherGym;
    private final UserPrefs userPrefs;
    private final FilteredList<Student> filteredStudents;

    /**
     * Initializes a ModelManager with the given brotherGym and userPrefs.
     */
    public ModelManager(ReadOnlyBrotherGym brotherGym, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(brotherGym, userPrefs);

        logger.fine("Initializing with address book: " + brotherGym + " and user prefs " + userPrefs);

        this.brotherGym = new BrotherGym(brotherGym);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredStudents = new FilteredList<>(this.brotherGym.getStudentList());
    }

    public ModelManager() {
        this(new BrotherGym(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    //=========== BrotherGym ================================================================================

    @Override
    public void setBrotherGym(ReadOnlyBrotherGym brotherGym) {
        this.brotherGym.resetData(brotherGym);
    }

    @Override
    public ReadOnlyBrotherGym getBrotherGym() {
        return brotherGym;
    }

    @Override
    public boolean hasStudent(Student student) {
        requireNonNull(student);
        return brotherGym.hasStudent(student);
    }

    @Override
    public void deleteStudent(Student target) {
        brotherGym.removeStudent(target);
    }

    @Override
    public void addStudent(Student student) {
        brotherGym.addStudent(student);
        updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
    }

    @Override
    public void setStudent(Student target, Student editedStudent) {
        requireAllNonNull(target, editedStudent);

        brotherGym.setStudent(target, editedStudent);
    }

    //=========== Filtered Student List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Student} backed by the internal list of
     * {@code brotherGym}
     */
    @Override
    public ObservableList<Student> getFilteredStudentList() {
        return filteredStudents;
    }

    @Override
    public void updateFilteredStudentList(Predicate<Student> predicate) {
        requireNonNull(predicate);
        filteredStudents.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager otherModelManager)) {
            return false;
        }

        return brotherGym.equals(otherModelManager.brotherGym)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredStudents.equals(otherModelManager.filteredStudents);
    }

}
