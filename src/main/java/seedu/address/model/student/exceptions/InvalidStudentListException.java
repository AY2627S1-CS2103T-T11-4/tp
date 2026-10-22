package seedu.address.model.student.exceptions;

/** Signals that a replacement student list contains an invalid entry. */
public class InvalidStudentListException extends RuntimeException {
    /** Creates an exception for a student list containing a null entry. */
    public InvalidStudentListException() {
        super("Student list cannot contain null students");
    }
}
