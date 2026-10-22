package seedu.address.logic.parser.exceptions;

/**
 * Represents an error caused by an invalid field value.
 */
public class FieldValueInvalidException extends ParseException {

    public FieldValueInvalidException(String message) {
        super(message);
    }

    public FieldValueInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public FieldValueInvalidException withMessage(String message) {
        return new FieldValueInvalidException(message, this);
    }
}
