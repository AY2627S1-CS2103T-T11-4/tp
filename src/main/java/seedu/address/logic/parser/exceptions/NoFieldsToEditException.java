package seedu.address.logic.parser.exceptions;

/**
 * Represents an edit command that does not specify any fields to edit.
 */
public class NoFieldsToEditException extends ParseException {

    public NoFieldsToEditException(String message) {
        super(message);
    }

    public NoFieldsToEditException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public NoFieldsToEditException withMessage(String message) {
        return new NoFieldsToEditException(message, this);
    }
}
