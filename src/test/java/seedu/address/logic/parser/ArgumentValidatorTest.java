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
import seedu.address.testutil.Assert;

public class ArgumentValidatorTest {
    private static final Prefix REQUIRED_PREFIX = new Prefix("--required");
    private static final Prefix SECOND_REQUIRED_PREFIX = new Prefix("--alsorequired");
    private static final Prefix SINGLE_VALUE_PREFIX = new Prefix("--single");
    private static final Prefix REPEATABLE_PREFIX = new Prefix("--repeatable");
    private static final Prefix INVALID_PREFIX = new Prefix("--invalid");

    private static final ArgumentValidator VALIDATOR = new ArgumentValidator(
            true,
            Set.of(REQUIRED_PREFIX, SECOND_REQUIRED_PREFIX, SINGLE_VALUE_PREFIX, REPEATABLE_PREFIX),
            Set.of(REQUIRED_PREFIX, SECOND_REQUIRED_PREFIX, SINGLE_VALUE_PREFIX),
            Set.of(REQUIRED_PREFIX, SECOND_REQUIRED_PREFIX));

    private static final ArgumentValidator DEFAULT_VALIDATOR = new ArgumentValidator();

    // EP: tokenized arguments are null (invalid).
    @Test
    public void verifyArgs_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> VALIDATOR.verifyArgs(null));
    }

    // Each valid input at least once: valid preamble and prefixes pass validation.
    @Test
    public void verifyArgs_validInput_success() {
        verifySuccessfully(
                VALIDATOR,
                "John --required value --alsorequired value "
                        + "--single one --repeatable first --repeatable second");
    }

    // One invalid input at a time: only the required preamble is missing.
    @Test
    public void verifyArgs_missingPreamble_failure() {
        String message = getValidationError(
                VALIDATOR,
                " --required value --alsorequired value");

        assertTrue(message.startsWith(Messages.MESSAGE_MISSING_PREAMBLE));
    }

    // One invalid input at a time: only one required prefix is missing.
    @Test
    public void verifyArgs_oneRequiredPrefixMissing_failure() {
        String message = getValidationError(
                VALIDATOR,
                "John --required value");

        assertTrue(message.contains(SECOND_REQUIRED_PREFIX.toString()));
    }

    // Invalid inputs combined after separate tests: multiple required prefixes are missing.
    @Test
    public void verifyArgs_multipleRequiredPrefixesMissing_failure() {
        String message = getValidationError(VALIDATOR, "John");

        assertTrue(message.contains(REQUIRED_PREFIX.toString()));
        assertTrue(message.contains(SECOND_REQUIRED_PREFIX.toString()));
    }

    // EP: a single-value prefix is repeated (invalid).
    @Test
    public void verifyArgs_duplicateSingleValuePrefix_failure() {
        String message = getValidationError(
                VALIDATOR,
                "John --required first --required second "
                        + "--alsorequired value");

        assertEquals(
                Messages.getErrorMessageForDuplicatePrefixes(REQUIRED_PREFIX),
                message);
    }

    // Invalid inputs combined after separate tests: multiple single-value prefixes repeat.
    @Test
    public void verifyArgs_multipleDuplicatePrefixes_failure() {
        String message = getValidationError(
                VALIDATOR,
                "John --required first --required second "
                        + "--alsorequired first --alsorequired second");

        assertTrue(message.contains(REQUIRED_PREFIX.toString()));
        assertTrue(message.contains(SECOND_REQUIRED_PREFIX.toString()));
    }

    // One invalid input at a time: only one prefix is unknown.
    @Test
    public void verifyArgs_invalidPrefix_failure() {
        String message = getValidationError(
                VALIDATOR,
                "John --required value --alsorequired value --invalid value");

        assertEquals(
                Messages.getErrorMessageForInvalidPrefixes(INVALID_PREFIX),
                message);
    }

    // Invalid inputs combined after separate tests: multiple prefixes are unknown.
    @Test
    public void verifyArgs_multipleInvalidPrefixes_failure() {
        Prefix anotherInvalidPrefix = new Prefix("--anotherinvalid");

        String message = getValidationError(
                VALIDATOR,
                "John --required value --alsorequired value "
                        + "--invalid first --anotherinvalid second");

        assertTrue(message.contains(INVALID_PREFIX.toString()));
        assertTrue(message.contains(anotherInvalidPrefix.toString()));
    }

    // EP: the same unknown prefix is repeated (invalid).
    @Test
    public void verifyArgs_repeatedInvalidPrefix_failure() {
        String message = getValidationError(
                VALIDATOR,
                "John --required value --alsorequired value "
                        + "--invalid first --invalid second");

        assertEquals(
                Messages.getErrorMessageForInvalidPrefixes(INVALID_PREFIX),
                message);
    }

    // Invalid inputs combined after separate tests: missing, duplicate, and unknown fields coexist.
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

    // EP: validator permits a missing optional preamble (valid).
    @Test
    public void verifyArgs_preambleNotRequired_success() {
        ArgumentValidator validator = new ArgumentValidator(
                false,
                Set.of(REPEATABLE_PREFIX),
                Set.of(),
                Set.of());

        verifySuccessfully(validator, " --repeatable value");
    }

    // BV: length = 0 (valid).
    @Test
    public void verifyNoArgs_emptyInput_success() throws ParseException {
        ArgumentValidator.verifyNoArgs(tokenize(""));
    }

    // EP: whitespace-only input (valid).
    @Test
    public void verifyNoArgs_whitespaceInput_success() throws ParseException {
        ArgumentValidator.verifyNoArgs(tokenize(" \t\n"));
    }

    // EP: non-prefix preamble text (invalid).
    @Test
    public void verifyNoArgs_preamble_failure() {
        ParseException exception = assertThrows(
                ParseException.class, () -> ArgumentValidator.verifyNoArgs(tokenize("unexpected")));

        assertEquals(Messages.MESSAGE_UNEXPECTED_PARAMETERS, exception.getMessage());
    }

    // EP: unexpected prefix argument (invalid).
    @Test
    public void verifyNoArgs_prefix_failure() {
        ParseException exception = assertThrows(
                ParseException.class, () -> ArgumentValidator.verifyNoArgs(tokenize(" --unexpected value")));

        assertEquals(Messages.MESSAGE_UNEXPECTED_PARAMETERS, exception.getMessage());
    }

    // EP: tokenized arguments are null (invalid).
    @Test
    public void verifyNoArgs_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ArgumentValidator.verifyNoArgs(null));
    }

    @Test
    public void verifyArgs_emptyFieldValues_success() {
        verifySuccessfully(
                VALIDATOR,
                "John --required --alsorequired --single --repeatable");
    }

    // EP: default validator receives a prefix (invalid).
    @Test
    public void defaultValidator_rejectsPrefixes() {
        String message = getValidationError(
                DEFAULT_VALIDATOR,
                "1 --invalid value");

        assertEquals(
                Messages.getErrorMessageForInvalidPrefixes(INVALID_PREFIX),
                message);
    }

    // EP: default validator receives preamble only (valid).
    @Test
    public void defaultValidator_acceptsPreambleWithoutPrefixes() {
        verifySuccessfully(DEFAULT_VALIDATOR, "1");
    }

    // One invalid input at a time: only valid-prefix configuration is null.
    @Test
    public void constructor_nullValidPrefixes_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new ArgumentValidator(true, null, Set.of(), Set.of()));
    }

    // One invalid input at a time: only no-repeat configuration is null.
    @Test
    public void constructor_nullNoRepeatPrefixes_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new ArgumentValidator(true, Set.of(), null, Set.of()));
    }

    // One invalid input at a time: only required-prefix configuration is null.
    @Test
    public void constructor_nullRequiredPrefixes_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new ArgumentValidator(true, Set.of(), Set.of(), null));
    }

    // EP: no-repeat prefix is absent from valid prefixes (invalid).
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

    // EP: required prefix is absent from valid prefixes (invalid).
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

    // EP: non-empty valid-prefix configuration (valid).
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

    // EP: non-empty required-prefix configuration (valid).
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

    // EP: non-empty no-repeat-prefix configuration (valid).
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
