package seedu.address.model.student.exceptions;

/** Signals that an operation would exceed the maximum number of students. */
public class StudentListFullException extends RuntimeException {
    public StudentListFullException() {
        super("Operation would exceed the maximum number of students");
    }
}
