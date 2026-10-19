package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertDoesNotThrow;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.parser.exceptions.ParseException;

public class ArgumentValidatorTest {
    private static final Prefix REQUIRED_PREFIX = new Prefix("--required");
    private static final Prefix SECOND_REQUIRED_PREFIX = new Prefix("--also-required");
    private static final Prefix SINGLE_VALUE_PREFIX = new Prefix("--single");
    private static final Prefix REPEATABLE_PREFIX = new Prefix("--repeatable");
    private static final Prefix INVALID_PREFIX = new Prefix("--invalid");

    private static final ArgumentValidator VALIDATOR = new ArgumentValidator(
            true,
            Set.of(REQUIRED_PREFIX, SECOND_REQUIRED_PREFIX, SINGLE_VALUE_PREFIX, REPEATABLE_PREFIX),
            Set.of(REQUIRED_PREFIX, SECOND_REQUIRED_PREFIX, SINGLE_VALUE_PREFIX),
            Set.of(REQUIRED_PREFIX, SECOND_REQUIRED_PREFIX));

    private static final ArgumentValidator DEFAULT_VALIDATOR = new ArgumentValidator();

    @Test
    public void verifyArgs_validInput_success() {
        verifySuccessfully(
                VALIDATOR,
                "John --required value --also-required value "
                        + "--single one --repeatable first --repeatable second");
    }

    @Test
    public void verifyArgs_missingPreamble_failure() {
        String message = getValidationError(
                VALIDATOR,
                " --required value --also-required value");

        assertTrue(message.startsWith(Messages.MESSAGE_MISSING_PREAMBLE));
    }

    @Test
    public void verifyArgs_oneRequiredPrefixMissing_failure() {
        String message = getValidationError(
                VALIDATOR,
                "John --required value");

        assertTrue(message.contains(SECOND_REQUIRED_PREFIX.toString()));
    }

    @Test
    public void verifyArgs_multipleRequiredPrefixesMissing_failure() {
        String message = getValidationError(VALIDATOR, "John");

        assertTrue(message.contains(REQUIRED_PREFIX.toString()));
        assertTrue(message.contains(SECOND_REQUIRED_PREFIX.toString()));
    }

    @Test
    public void verifyArgs_duplicateSingleValuePrefix_failure() {
        String message = getValidationError(
                VALIDATOR,
                "John --required first --required second "
                        + "--also-required value");

        assertEquals(
                Messages.getErrorMessageForDuplicatePrefixes(REQUIRED_PREFIX),
                message);
    }

    @Test
    public void verifyArgs_multipleDuplicatePrefixes_failure() {
        String message = getValidationError(
                VALIDATOR,
                "John --required first --required second "
                        + "--also-required first --also-required second");

        assertTrue(message.contains(REQUIRED_PREFIX.toString()));
        assertTrue(message.contains(SECOND_REQUIRED_PREFIX.toString()));
    }

    @Test
    public void verifyArgs_invalidPrefix_failure() {
        String message = getValidationError(
                VALIDATOR,
                "John --required value --also-required value --invalid value");

        assertEquals(
                Messages.getErrorMessageForInvalidPrefixes(INVALID_PREFIX),
                message);
    }

    @Test
    public void verifyArgs_multipleInvalidPrefixes_failure() {
        Prefix anotherInvalidPrefix = new Prefix("--another-invalid");

        String message = getValidationError(
                VALIDATOR,
                "John --required value --also-required value "
                        + "--invalid first --another-invalid second");

        assertTrue(message.contains(INVALID_PREFIX.toString()));
        assertTrue(message.contains(anotherInvalidPrefix.toString()));
    }

    @Test
    public void verifyArgs_repeatedInvalidPrefix_failure() {
        String message = getValidationError(
                VALIDATOR,
                "John --required value --also-required value "
                        + "--invalid first --invalid second");

        assertEquals(
                Messages.getErrorMessageForInvalidPrefixes(INVALID_PREFIX),
                message);
    }

    @Test
    public void verifyArgs_multipleProblems_reportsAllErrors() {
        String message = getValidationError(
                VALIDATOR,
                " --single first --single second --invalid value");

        String[] lines = message.split("\\R");

        assertEquals(Messages.MESSAGE_MISSING_PREAMBLE, lines[0]);
        assertTrue(lines[1].contains(REQUIRED_PREFIX.toString()));
        assertTrue(lines[1].contains(SECOND_REQUIRED_PREFIX.toString()));
        assertTrue(lines[2].contains(SINGLE_VALUE_PREFIX.toString()));
        assertTrue(lines[3].contains(INVALID_PREFIX.toString()));
    }

    @Test
    public void verifyArgs_preambleNotRequired_success() {
        ArgumentValidator validator = new ArgumentValidator(
                false,
                Set.of(REPEATABLE_PREFIX),
                Set.of(),
                Set.of());

        verifySuccessfully(validator, " --repeatable value");
    }

    @Test
    public void verifyArgs_emptyFieldValues_success() {
        verifySuccessfully(
                VALIDATOR,
                "John --required --also-required --single --repeatable");
    }

    @Test
    public void defaultValidator_rejectsPrefixes() {
        String message = getValidationError(
                DEFAULT_VALIDATOR,
                "1 --invalid value");

        assertEquals(
                Messages.getErrorMessageForInvalidPrefixes(INVALID_PREFIX),
                message);
    }

    @Test
    public void defaultValidator_acceptsPreambleWithoutPrefixes() {
        verifySuccessfully(DEFAULT_VALIDATOR, "1");
    }

    @Test
    public void constructor_nullValidPrefixes_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new ArgumentValidator(true, null, Set.of(), Set.of()));
    }

    @Test
    public void constructor_nullNoRepeatPrefixes_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new ArgumentValidator(true, Set.of(), null, Set.of()));
    }

    @Test
    public void constructor_nullRequiredPrefixes_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new ArgumentValidator(true, Set.of(), Set.of(), null));
    }

    @Test
    public void constructor_noRepeatPrefixNotValid_throwsIllegalArgumentException() {
        Prefix undeclaredPrefix = new Prefix("--undeclared");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> new ArgumentValidator(
                        true, Set.of(REQUIRED_PREFIX), Set.of(undeclaredPrefix), Set.of()));

        assertEquals(
                "noRepeatPrefixes is not a subset of validPrefixes",
                exception.getMessage());
    }

    @Test
    public void constructor_requiredPrefixNotValid_throwsIllegalArgumentException() {
        Prefix undeclaredPrefix = new Prefix("--undeclared");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, () -> new ArgumentValidator(
                        true, Set.of(REQUIRED_PREFIX), Set.of(), Set.of(undeclaredPrefix)));

        assertEquals(
                "requiredPrefixes is not a subset of validPrefixes",
                exception.getMessage());
    }

    @Test
    public void constructor_copiesValidPrefixes() {
        Set<Prefix> validPrefixes = new HashSet<>(Set.of(REQUIRED_PREFIX));
        ArgumentValidator validator = new ArgumentValidator(
                true, validPrefixes, Set.of(REQUIRED_PREFIX), Set.of(REQUIRED_PREFIX));

        validPrefixes.add(INVALID_PREFIX);

        String message = getValidationError(
                validator, "John --required value --invalid value");

        assertEquals(
                Messages.getErrorMessageForInvalidPrefixes(INVALID_PREFIX),
                message);
    }

    @Test
    public void constructor_copiesRequiredPrefixes() {
        Set<Prefix> requiredPrefixes = new HashSet<>(Set.of(REQUIRED_PREFIX));
        ArgumentValidator validator = new ArgumentValidator(
                true, Set.of(REQUIRED_PREFIX), Set.of(REQUIRED_PREFIX), requiredPrefixes);

        requiredPrefixes.clear();

        String message = getValidationError(validator, "John");

        assertEquals(
                Messages.getErrorMessageForMissingPrefixes(REQUIRED_PREFIX),
                message);
    }

    @Test
    public void constructor_copiesNoRepeatPrefixes() {
        Set<Prefix> noRepeatPrefixes = new HashSet<>(Set.of(REQUIRED_PREFIX));
        ArgumentValidator validator = new ArgumentValidator(
                true, Set.of(REQUIRED_PREFIX), noRepeatPrefixes, Set.of(REQUIRED_PREFIX));

        noRepeatPrefixes.clear();

        String message = getValidationError(
                validator, "John --required first --required second");

        assertEquals(
                Messages.getErrorMessageForDuplicatePrefixes(REQUIRED_PREFIX),
                message);
    }

    private static ArgumentMultimap tokenize(String input) {
        return assertDoesNotThrow(() -> ArgumentTokenizer.tokenize(input));
    }

    private static void verifySuccessfully(ArgumentValidator validator, String input) {
        ArgumentMultimap arguments = tokenize(input);

        assertDoesNotThrow(() -> {
            validator.verifyArgs(arguments);
            return null;
        });
    }

    private static String getValidationError(ArgumentValidator validator, String input) {
        ArgumentMultimap arguments = tokenize(input);

        ParseException exception = assertThrows(
                ParseException.class, () -> validator.verifyArgs(arguments));

        return exception.getMessage();
    }
}
