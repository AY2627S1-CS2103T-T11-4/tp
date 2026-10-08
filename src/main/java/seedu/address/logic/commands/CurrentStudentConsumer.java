package seedu.address.logic.commands;

import seedu.address.model.student.Student;

/** A command that receives the student currently selected by the Logic layer. */
public interface CurrentStudentConsumer {
    void setCurrentStudent(Student student);
}
