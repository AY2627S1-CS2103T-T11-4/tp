package seedu.address.logic.commands;

import seedu.address.model.student.Student;

/** A command that provides the student that should become the current student. */
public interface CurrentStudentProvider {
    Student getCurrentStudent();
}
