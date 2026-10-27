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
import seedu.address.model.exercise.Exercise;
import seedu.address.model.exercise.ExercisesList;
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

        logger.fine(() -> "Initializing with address book: " + brotherGym + " and user prefs " + userPrefs);

        this.brotherGym = new BrotherGym(brotherGym);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredStudents = new FilteredList<>(this.brotherGym.getStudentList());

        logger.fine(() -> "Initialization completed");
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
        logger.fine(() -> "Attempting to set GUI settings: " + guiSettings);

        userPrefs.setGuiSettings(guiSettings);

        logger.info(() -> "GUI settings set: " + guiSettings);
    }

    //=========== BrotherGym ================================================================================

    @Override
    public void setBrotherGym(ReadOnlyBrotherGym brotherGym) {
        requireNonNull(brotherGym);
        logger.fine(() -> "Attempting to set BrotherGym model; current number of students: "
                + this.brotherGym.getStudentList().size()
                + "; replacement number of students: " + brotherGym.getStudentList().size());
        this.brotherGym.resetData(brotherGym);
        clearCurrentStudent();
        logger.info(() -> "BrotherGym model set; Total number of students: "
                + this.brotherGym.getStudentList().size());
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
        requireNonNull(target);
        logger.fine(() -> "Attempting to remove student from model: " + target);
        brotherGym.removeStudent(target);
        if (currentStudent.get() == target) {
            clearCurrentStudent();
        }
        logger.info(() -> "Student removed from model: " + target.getName()
                + "; Total number of students: " + brotherGym.getStudentList().size());
    }

    @Override
    public void addStudent(Student student) {
        requireNonNull(student);
        logger.fine(() -> "Attempting to add student to model: " + student);
        brotherGym.addStudent(student);
        updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
        logger.info(() -> "Student added to model: " + student.getName()
                + "; Total number of students: " + brotherGym.getStudentList().size());
    }

    @Override
    public void setStudent(Student target, Student editedStudent) {
        requireAllNonNull(target, editedStudent);
        logger.fine(() -> "Attempting to set student in model: " + target + " -> " + editedStudent);
        brotherGym.setStudent(target, editedStudent);
        if (currentStudent.get() == target) {
            setCurrentStudent(editedStudent);
        }
        logger.info(() -> "Student set in model: " + target.getName() + " -> " + editedStudent.getName());
    }

    @Override
    public void addWorkoutPlanForStudent(Student student, WorkoutPlan plan) {
        requireAllNonNull(student, plan);
        logger.fine(() -> "Attempting to add workout plan for student " + student.getName() + ": " + plan);

        Student storedStudent = brotherGym.validateStudentInBrotherGym(student);
        WorkoutPlansList updatedPlans = storedStudent.getWorkoutPlansList().addWorkoutPlan(plan);
        Student updatedStudent = new Student(storedStudent.getName(), storedStudent.getPhone(),
                storedStudent.getEmail(), storedStudent.getAddress(), storedStudent.getTags(), updatedPlans);
        setStudent(storedStudent, updatedStudent);

        logger.info(() -> "Workout plan added for student " + student.getName() + ": " + plan.getTitle()
                + "; Total number of workout plans: " + updatedPlans.getWorkoutList().size());
    }

    @Override
    public void removeWorkoutPlanForStudent(Student student, WorkoutPlan plan) {
        requireAllNonNull(student, plan);
        logger.fine(() -> "Attempting to remove workout plan for student " + student.getName() + ": " + plan);

        Student storedStudent = brotherGym.validateStudentInBrotherGym(student);
        WorkoutPlansList updatedPlans = storedStudent.getWorkoutPlansList().removeWorkoutPlan(plan);
        Student updatedStudent = new Student(storedStudent.getName(), storedStudent.getPhone(),
                storedStudent.getEmail(), storedStudent.getAddress(), storedStudent.getTags(), updatedPlans);
        setStudent(storedStudent, updatedStudent);

        logger.info(() -> "Workout plan removed for student " + student.getName() + ": " + plan.getTitle()
                + "; Total number of workout plans: " + updatedPlans.getWorkoutList().size());
    }

    @Override
    public void setWorkoutPlanForStudent(Student student, WorkoutPlan target, WorkoutPlan editedPlan) {
        requireAllNonNull(student, target, editedPlan);
        logger.fine(() -> "Attempting to set workout plan for student " + student.getName()
                + ": " + target + " -> " + editedPlan);

        Student storedStudent = brotherGym.validateStudentInBrotherGym(student);
        WorkoutPlansList updatedPlans = storedStudent.getWorkoutPlansList().setWorkoutPlan(target, editedPlan);
        Student updatedStudent = new Student(storedStudent.getName(), storedStudent.getPhone(),
                storedStudent.getEmail(), storedStudent.getAddress(), storedStudent.getTags(), updatedPlans);
        setStudent(storedStudent, updatedStudent);

        logger.info(() -> "Workout plan set for student " + student.getName() + ": "
                + target.getTitle() + " -> " + editedPlan.getTitle());
    }

    @Override
    public void addExerciseForWorkoutPlan(Student student, WorkoutPlan plan, Exercise exercise) {
        requireAllNonNull(student, plan, exercise);
        logger.fine(() -> "Attempting to add exercise to workout plan for student "
                + student.getName() + ": " + exercise);

        Student storedStudent = brotherGym.validateStudentInBrotherGym(student);
        WorkoutPlan storedPlan = storedStudent.getWorkoutPlansList().getWorkoutPlan(plan);
        ExercisesList updatedExercises = storedPlan.getExercises().addExercise(exercise);
        WorkoutPlan updatedPlan = new WorkoutPlan(storedPlan.getTitle(), storedPlan.getWorkouts(),
                storedPlan.getDate(), storedPlan.getTags(), storedPlan.getStatus(), updatedExercises);
        WorkoutPlansList updatedPlans = storedStudent.getWorkoutPlansList()
                .setWorkoutPlan(storedPlan, updatedPlan);
        Student updatedStudent = new Student(storedStudent.getName(), storedStudent.getPhone(),
                storedStudent.getEmail(), storedStudent.getAddress(), storedStudent.getTags(), updatedPlans);
        setStudent(storedStudent, updatedStudent);
    }

    @Override
    public void setExerciseForWorkoutPlan(Student student, WorkoutPlan plan,
            Exercise target, Exercise editedExercise) {
        requireAllNonNull(student, plan, target, editedExercise);
        logger.fine(() -> "Attempting to set exercise in workout plan for student "
                + student.getName() + ": " + target + " -> " + editedExercise);

        Student storedStudent = brotherGym.validateStudentInBrotherGym(student);
        WorkoutPlan storedPlan = storedStudent.getWorkoutPlansList().getWorkoutPlan(plan);
        ExercisesList updatedExercises = storedPlan.getExercises().setExercise(target, editedExercise);
        WorkoutPlan updatedPlan = new WorkoutPlan(storedPlan.getTitle(), storedPlan.getWorkouts(),
                storedPlan.getDate(), storedPlan.getTags(), storedPlan.getStatus(), updatedExercises);
        WorkoutPlansList updatedPlans = storedStudent.getWorkoutPlansList()
                .setWorkoutPlan(storedPlan, updatedPlan);
        Student updatedStudent = new Student(storedStudent.getName(), storedStudent.getPhone(),
                storedStudent.getEmail(), storedStudent.getAddress(), storedStudent.getTags(), updatedPlans);
        setStudent(storedStudent, updatedStudent);
    }

    @Override
    public void removeExerciseForWorkoutPlan(Student student, WorkoutPlan plan, Exercise target) {
        requireAllNonNull(student, plan, target);
        logger.fine(() -> "Attempting to remove exercise from workout plan for student "
                + student.getName() + ": " + target);

        Student storedStudent = brotherGym.validateStudentInBrotherGym(student);
        WorkoutPlan storedPlan = storedStudent.getWorkoutPlansList().getWorkoutPlan(plan);
        ExercisesList updatedExercises = storedPlan.getExercises().removeExercise(target);
        WorkoutPlan updatedPlan = new WorkoutPlan(storedPlan.getTitle(), storedPlan.getWorkouts(),
                storedPlan.getDate(), storedPlan.getTags(), storedPlan.getStatus(), updatedExercises);
        WorkoutPlansList updatedPlans = storedStudent.getWorkoutPlansList()
                .setWorkoutPlan(storedPlan, updatedPlan);
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
        requireNonNull(student);
        logger.fine(() -> "Attempting to set selected student: " + student.getName());
        currentStudent.set(brotherGym.validateStudentInBrotherGym(student));
        logger.info(() -> "Selected student set: " + student.getName());
    }

    @Override
    public void clearCurrentStudent() {
        Student selectedStudent = currentStudent.get();
        if (selectedStudent != null) {
            logger.fine(() -> "Attempting to clear selected student: " + selectedStudent.getName());
        }
        currentStudent.set(null);
        if (selectedStudent != null) {
            logger.info(() -> "Selected student removed: " + selectedStudent.getName());
        }
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
