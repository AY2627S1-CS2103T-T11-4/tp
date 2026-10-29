package seedu.address.model.exercise.exceptions;

import java.util.NoSuchElementException;

import seedu.address.model.exercise.Exercise;

/** Signals that the specified exercise could not be found. */
public class ExerciseNotFoundException extends NoSuchElementException {

    /** Creates an exception for a missing exercise. */
    public ExerciseNotFoundException(Exercise exercise) {
        super("Exercise not found: " + exercise);
    }
}
