package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.Messages.MESSAGE_MISSING_PREAMBLE;

import java.util.HashSet;
import java.util.Set;

import seedu.address.logic.Messages;
import seedu.address.logic.parser.exceptions.InputFormatInvalidException;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Validates the prefixes of the {@code ArgumentMultiMap} with the format of the corresponding format.
 */
public class ArgumentValidator {

    private final boolean requirePreamble; // whether this command requires a preamble (almost always yes)
    private final Set<Prefix> validPrefixes; // all valid prefixes
    private final Set<Prefix> noRepeatPrefixes; // only prefixes that cannot be repeated
    private final Set<Prefix> requiredPrefixes; // only prefixes that are not optional

    public ArgumentValidator() throws IllegalArgumentException {
        this(true, new HashSet<>(), new HashSet<>(), new HashSet<>());
    }

    public ArgumentValidator(Set<Prefix> validPrefixes,
                             Set<Prefix> noRepeatPrefixes,
                             Set<Prefix> requiredPrefixes) throws IllegalArgumentException {
        this(true, validPrefixes, noRepeatPrefixes, requiredPrefixes);
    }

    /**
     * Initialise ArgumentValidator with the required validation settings.
     *
     * @param requirePreamble whether this command requires a preamble.
     * @param validPrefixes set of all valid prefixes for this command.
     * @param noRepeatPrefixes set of all prefixes that cannot be repeated.
     * @param requiredPrefixes set of all prefixes that are not optional.
     *
     * @throws IllegalArgumentException if validation logic provided is inconsistent
     */
    public ArgumentValidator(boolean requirePreamble,
                             Set<Prefix> validPrefixes,
                             Set<Prefix> noRepeatPrefixes,
                             Set<Prefix> requiredPrefixes) throws IllegalArgumentException {
        requireAllNonNull(requirePreamble, validPrefixes, noRepeatPrefixes, requiredPrefixes);
        if (!validPrefixes.containsAll(noRepeatPrefixes)) {
            throw new IllegalArgumentException("noRepeatPrefixes is not a subset of validPrefixes");
        }
        if (!validPrefixes.containsAll(requiredPrefixes)) {
            throw new IllegalArgumentException("requiredPrefixes is not a subset of validPrefixes");
        }

        this.requirePreamble = requirePreamble;
        this.validPrefixes = Set.copyOf(validPrefixes);
        this.noRepeatPrefixes = Set.copyOf(noRepeatPrefixes);
        this.requiredPrefixes = Set.copyOf(requiredPrefixes);
    }

    private boolean hasNoPreamble(ArgumentMultimap argMultimap) {
        return argMultimap.getPreamble().isEmpty();
    }

    private Prefix[] getInvalidPrefixes(ArgumentMultimap argMultimap) {
        return argMultimap.getPrefixSet().stream()
                .filter(argPrefix -> !argPrefix.getPrefix().isEmpty() && !this.validPrefixes.contains(argPrefix))
                .toArray(Prefix[]::new);
    }

    private Prefix[] getDuplicatePrefixes(ArgumentMultimap argMultimap) {
        return this.noRepeatPrefixes.stream()
                .filter(prefix -> argMultimap.getAllValues(prefix).size() > 1)
                .toArray(Prefix[]::new);
    }

    private Prefix[] getMissingPrefixes(ArgumentMultimap argMultimap) {
        Set<Prefix> argPrefixes = argMultimap.getPrefixSet();
        return this.requiredPrefixes.stream()
                .filter(prefix -> !argPrefixes.contains(prefix))
                .toArray(Prefix[]::new);
    }

    /**
     * Verifies that the supplied {@code argMultimap} has:
     * - a preamble supplied if {@code this.requirePreamble} is {@code true}
     * - no missing required prefixes
     * - no duplicate single-use prefixes
     * - no invalid prefixes
     * @throws ParseException if any of the above tests fail
     */
    public void verifyArgs(ArgumentMultimap argMultimap) throws ParseException {
        requireNonNull(argMultimap);

        boolean throwError = false;
        StringBuilder errorMessage = new StringBuilder();

        if (this.requirePreamble && this.hasNoPreamble(argMultimap)) {
            throwError = true;
            errorMessage.append(MESSAGE_MISSING_PREAMBLE).append("\n");
        }

        Prefix[] missingPrefixes = getMissingPrefixes(argMultimap);
        if (missingPrefixes.length > 0) {
            throwError = true;
            errorMessage.append(Messages.getErrorMessageForMissingPrefixes(missingPrefixes)).append("\n");
        }

        Prefix[] duplicatePrefixes = getDuplicatePrefixes(argMultimap);
        if (duplicatePrefixes.length > 0) {
            throwError = true;
            errorMessage.append(Messages.getErrorMessageForDuplicatePrefixes(duplicatePrefixes)).append("\n");
        }

        Prefix[] invalidPrefixes = getInvalidPrefixes(argMultimap);
        if (invalidPrefixes.length > 0) {
            throwError = true;
            errorMessage.append(Messages.getErrorMessageForInvalidPrefixes(invalidPrefixes)).append("\n");
        }

        if (throwError) {
            errorMessage.setLength(errorMessage.length() - 1); // remove last newline
            throw new InputFormatInvalidException(errorMessage.toString());
        }
    }
}
