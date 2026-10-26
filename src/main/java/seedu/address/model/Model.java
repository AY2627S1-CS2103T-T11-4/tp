package seedu.address.model;

import java.util.Optional;
import java.util.function.Predicate;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.student.Student;
import seedu.address.model.student.exceptions.StudentNotFoundException;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.exceptions.WorkoutPlanNotFoundException;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluates to true */
    Predicate<Student> PREDICATE_SHOW_ALL_STUDENTS = unused -> true;

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Replaces BrotherGym data with the data in {@code brotherGym}.
     */
    void setBrotherGym(ReadOnlyBrotherGym brotherGym);

    /** Returns the BrotherGym */
    ReadOnlyBrotherGym getBrotherGym();

    /**
     * Returns true if a student with the same identity as {@code student} exists in the student list.
     */
    boolean hasStudent(Student student);

    /**
     * Deletes the given student.
     * The student must exist in the student list.
     */
    void deleteStudent(Student target);

    /**
     * Adds the given student.
     * {@code student} must not already exist in the student list.
     */
    void addStudent(Student student);

    /**
     * Replaces the given student {@code target} with {@code editedStudent}.
     * {@code target} must exist in the student list.
     * The student identity of {@code editedStudent} must not be the same as
     * another existing student in the student list.
     */
    void setStudent(Student target, Student editedStudent);

    /**
     * Appends a workout plan to the student and stores an updated student value.
     * Refreshes the added plan's date-based status before checking for duplicates and storing it.
     *
     * @throws StudentNotFoundException If the student is not in the model.
     */
    void addWorkoutPlanForStudent(Student student, WorkoutPlan plan);

    /**
     * Removes the first plan equal to {@code plan} and stores an updated student value.
     *
     * @throws StudentNotFoundException If the student is not in the model.
     * @throws WorkoutPlanNotFoundException If the student has no matching plan.
     */
    void removeWorkoutPlanForStudent(Student student, WorkoutPlan plan);

    /**
     * Replaces {@code target} with {@code editedPlan} at the same position and stores an updated student value.
     * Refreshes the edited plan's date-based status before checking for duplicates and storing it.
     *
     * @throws StudentNotFoundException If the student is not in the model.
     * @throws WorkoutPlanNotFoundException If the student has no matching plan.
     */
    void setWorkoutPlanForStudent(Student student, WorkoutPlan target, WorkoutPlan editedPlan);

    /**
     * Refreshes and stores the given student's workout statuses, then returns the stored student object.
     * The student must exist in the model.
     */
    Student updateStudentWorkoutsState(Student student);

    /** Returns the currently selected student as an observable, read-only property. */
    ReadOnlyObjectProperty<Student> currentStudentProperty();

    /** Returns the currently selected student, if any. */
    Optional<Student> getCurrentStudent();

    /** Selects a student. */
    void setCurrentStudent(Student student);

    /** Clears the current student selection. */
    void clearCurrentStudent();

    /** Returns an unmodifiable view of the filtered student list */
    ObservableList<Student> getFilteredStudentList();

    /**
     * Updates the filter of the filtered student list to filter by the given {@code predicate}.
     * @throws NullPointerException If {@code predicate} is null.
     */
    void updateFilteredStudentList(Predicate<Student> predicate);

}
