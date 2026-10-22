package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.student.Student;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.WorkoutPlansList;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final BrotherGym brotherGym;
    private final UserPrefs userPrefs;
    private final FilteredList<Student> filteredStudents;
    private final ReadOnlyObjectWrapper<Student> currentStudent = new ReadOnlyObjectWrapper<>();

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
        clearCurrentStudent();
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
        if (currentStudent.get() == target) {
            clearCurrentStudent();
        }
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
        if (currentStudent.get() == target) {
            setCurrentStudent(editedStudent);
        }
    }

    @Override
    public void addWorkoutPlanForStudent(Student student, WorkoutPlan plan) {
        requireAllNonNull(student, plan);
        Student storedStudent = brotherGym.validateStudentInBrotherGym(student);
        WorkoutPlansList updatedPlans = storedStudent.getWorkoutPlansList().addWorkoutPlan(plan);
        Student updatedStudent = new Student(storedStudent.getName(), storedStudent.getPhone(),
                storedStudent.getEmail(), storedStudent.getAddress(), storedStudent.getTags(), updatedPlans);
        setStudent(storedStudent, updatedStudent);
    }

    @Override
    public void removeWorkoutPlanForStudent(Student student, WorkoutPlan plan) {
        requireAllNonNull(student, plan);
        Student storedStudent = brotherGym.validateStudentInBrotherGym(student);
        WorkoutPlansList updatedPlans = storedStudent.getWorkoutPlansList().removeWorkoutPlan(plan);
        Student updatedStudent = new Student(storedStudent.getName(), storedStudent.getPhone(),
                storedStudent.getEmail(), storedStudent.getAddress(), storedStudent.getTags(), updatedPlans);
        setStudent(storedStudent, updatedStudent);
    }

    @Override
    public void setWorkoutPlanForStudent(Student student, WorkoutPlan target, WorkoutPlan editedPlan) {
        requireAllNonNull(student, target, editedPlan);
        Student storedStudent = brotherGym.validateStudentInBrotherGym(student);
        WorkoutPlansList updatedPlans = storedStudent.getWorkoutPlansList().setWorkoutPlan(target, editedPlan);
        Student updatedStudent = new Student(storedStudent.getName(), storedStudent.getPhone(),
                storedStudent.getEmail(), storedStudent.getAddress(), storedStudent.getTags(), updatedPlans);
        setStudent(storedStudent, updatedStudent);
    }

    @Override
    public Student updateStudentWorkoutsState(Student student) {
        Student storedStudent = brotherGym.validateStudentInBrotherGym(student);
        Student updatedStudent = storedStudent.updateWorkoutsState();
        if (updatedStudent != storedStudent) {
            setStudent(storedStudent, updatedStudent);
        }
        return updatedStudent;
    }

    @Override
    public ReadOnlyObjectProperty<Student> currentStudentProperty() {
        return currentStudent.getReadOnlyProperty();
    }

    @Override
    public Optional<Student> getCurrentStudent() {
        return Optional.ofNullable(currentStudent.get());
    }

    @Override
    public void setCurrentStudent(Student student) {
        currentStudent.set(brotherGym.validateStudentInBrotherGym(student));
    }

    @Override
    public void clearCurrentStudent() {
        currentStudent.set(null);
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
