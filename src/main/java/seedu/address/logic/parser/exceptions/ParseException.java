package seedu.address.logic.parser.exceptions;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents a parse error encountered by a parser.
 */
public abstract class ParseException extends IllegalValueException {

    protected ParseException(String message) {
        super(message);
    }

    protected ParseException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Returns an exception of the same specific type with a different message.
     *
     * @param message the new error message
     * @return an exception of the same type with this exception as its cause
     */
    public abstract ParseException withMessage(String message);
}
