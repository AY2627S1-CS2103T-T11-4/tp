package seedu.address.logic.parser.exceptions;

/**
 * Represents an error caused by an invalid command input format.
 */
public class InputFormatInvalidException extends ParseException {

    public InputFormatInvalidException(String message) {
        super(message);
    }

    public InputFormatInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public InputFormatInvalidException withMessage(String message) {
        return new InputFormatInvalidException(message, this);
    }
}
