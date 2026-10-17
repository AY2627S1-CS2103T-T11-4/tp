package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import seedu.address.logic.Messages;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.testutil.Assert;

public class ArgumentTokenizerTest {

    private final Prefix ddashP = new Prefix("--p");
    private final Prefix ddashT = new Prefix("--t");
    private final Prefix ddashQ = new Prefix("--Q");

    @Test
    public void tokenize_emptyArgsString_noValues() {
        String argsString = "  ";
        ArgumentMultimap argMultimap = Assert.assertDoesNotThrow(() -> ArgumentTokenizer.tokenize(argsString, ddashP));

        assertPreambleEmpty(argMultimap);
        assertArgumentAbsent(argMultimap, ddashP);
    }

    private void assertPreamblePresent(ArgumentMultimap argMultimap, String expectedPreamble) {
        assertEquals(expectedPreamble, argMultimap.getPreamble());
    }

    private void assertPreambleEmpty(ArgumentMultimap argMultimap) {
        assertTrue(argMultimap.getPreamble().isEmpty());
    }

    /**
     * Asserts all the arguments in {@code argMultimap} with {@code prefix} match the {@code expectedValues}
     * and only the last value is returned upon calling {@code ArgumentMultimap#getValue(Prefix)}.
     */
    private void assertArgumentPresent(ArgumentMultimap argMultimap, Prefix prefix, String... expectedValues) {

        // Verify the last value is returned
        assertEquals(expectedValues[expectedValues.length - 1], argMultimap.getValue(prefix).get());

        // Verify the number of values returned is as expected
        assertEquals(expectedValues.length, argMultimap.getAllValues(prefix).size());

        // Verify all values returned are as expected and in order
        for (int i = 0; i < expectedValues.length; i++) {
            assertEquals(expectedValues[i], argMultimap.getAllValues(prefix).get(i));
        }
    }

    private void assertArgumentAbsent(ArgumentMultimap argMultimap, Prefix prefix) {
        assertFalse(argMultimap.getValue(prefix).isPresent());
    }

    private void assertInvalidPrefix(Executable tryTokenise, Prefix invalidPrefix) {
        Assert.assertThrows(ParseException.class,
                String.format(Messages.MESSAGE_INVALID_PREFIX, invalidPrefix), tryTokenise);
    }

    @Test
    public void tokenize_noPrefixes_allTakenAsPreamble() {
        String argsString = "  some random string tag with leading and trailing spaces ";
        ArgumentMultimap argMultimap = Assert.assertDoesNotThrow(() -> ArgumentTokenizer.tokenize(argsString));

        // Same string expected as preamble, but leading/trailing spaces should be trimmed
        assertPreamblePresent(argMultimap, argsString.trim());

    }

    @Test
    public void tokenize_oneArgument() {
        // Preamble present
        String argsStringPreamble = "  Some preamble string --p Argument value ";
        ArgumentMultimap argMultimap = Assert.assertDoesNotThrow(() ->
                ArgumentTokenizer.tokenize(argsStringPreamble, ddashP));
        assertPreamblePresent(argMultimap, "Some preamble string");
        assertArgumentPresent(argMultimap, ddashP, "Argument value");

        // No preamble
        String argsStringNoPreamble = " --p   Argument value ";
        argMultimap = Assert.assertDoesNotThrow(() -> ArgumentTokenizer.tokenize(argsStringNoPreamble, ddashP));
        assertPreambleEmpty(argMultimap);
        assertArgumentPresent(argMultimap, ddashP, "Argument value");

    }

    @Test
    public void tokenize_multipleArguments() {
        // Only two arguments are present
        String argsStringTwoArgs = "SomePreambleString --t ddashT-Value --p ddashP value";
        ArgumentMultimap argMultimap = Assert.assertDoesNotThrow(() ->
                ArgumentTokenizer.tokenize(argsStringTwoArgs, ddashP, ddashT, ddashQ));
        assertPreamblePresent(argMultimap, "SomePreambleString");
        assertArgumentPresent(argMultimap, ddashP, "ddashP value");
        assertArgumentPresent(argMultimap, ddashT, "ddashT-Value");
        assertArgumentAbsent(argMultimap, ddashQ);

        // All three arguments are present
        String argsStringThreeArgs = "Different Preamble String --Q 111 --t ddashT-Value --p ddashP value";
        argMultimap = Assert.assertDoesNotThrow(() ->
                ArgumentTokenizer.tokenize(argsStringThreeArgs, ddashP, ddashT, ddashQ));
        assertPreamblePresent(argMultimap, "Different Preamble String");
        assertArgumentPresent(argMultimap, ddashP, "ddashP value");
        assertArgumentPresent(argMultimap, ddashT, "ddashT-Value");
        assertArgumentPresent(argMultimap, ddashQ, "111");

        /* Also covers: Reusing of the tokenizer multiple times */

        // Reuse tokenizer on an empty string to ensure ArgumentMultimap is correctly reset
        // (i.e. no stale values from the previous tokenizing remain)
        String argsStringNoArgs = "";
        argMultimap = Assert.assertDoesNotThrow(() -> ArgumentTokenizer.tokenize(
                argsStringNoArgs, ddashP, ddashT, ddashQ));
        assertPreambleEmpty(argMultimap);
        assertArgumentAbsent(argMultimap, ddashP);
    }

    @Test
    public void tokenize_invalidPrefixes() {
        // Prefixes not previously given to the tokenizer should throw an error
        // here we treat "--Q" as our invalid value
        String invalidArg = " --Q ddashQ value ";
        assertInvalidPrefix(() -> ArgumentTokenizer.tokenize(invalidArg, ddashP, ddashT), ddashQ);

        // Invalid prefix before valid prefix
        String invalidArgValidArg = " --Q ddashQ value --t ddashT value";
        assertInvalidPrefix(() -> ArgumentTokenizer.tokenize(invalidArgValidArg, ddashP, ddashT), ddashQ);

        // Valid prefix before invalid prefix
        String validArgInvalidArg = " --t ddashT value --Q ddashQ value";
        assertInvalidPrefix(() -> ArgumentTokenizer.tokenize(validArgInvalidArg, ddashP, ddashT), ddashQ);
    }

    @Test
    public void tokenize_multipleArgumentsWithRepeats() {
        // Two arguments repeated, some have empty values
        String argsString = "SomePreambleString --t ddashT-Value --Q --Q --t another ddashT value --p ddashP value --t";
        ArgumentMultimap argMultimap = Assert.assertDoesNotThrow(() ->
                ArgumentTokenizer.tokenize(argsString, ddashP, ddashT, ddashQ));
        assertPreamblePresent(argMultimap, "SomePreambleString");
        assertArgumentPresent(argMultimap, ddashP, "ddashP value");
        assertArgumentPresent(argMultimap, ddashT, "ddashT-Value", "another ddashT value", "");
        assertArgumentPresent(argMultimap, ddashQ, "", "");
    }

    @Test
    public void tokenize_multipleArgumentsJoined() {
        String argsString = "SomePreambleString--p ddashP joined--tjoined --t not joined--Qjoined";
        ArgumentMultimap argMultimap = Assert.assertDoesNotThrow(() ->
                ArgumentTokenizer.tokenize(argsString, ddashP, ddashT, ddashQ));
        assertPreamblePresent(argMultimap, "SomePreambleString--p ddashP joined--tjoined");
        assertArgumentAbsent(argMultimap, ddashP);
        assertArgumentPresent(argMultimap, ddashT, "not joined--Qjoined");
        assertArgumentAbsent(argMultimap, ddashQ);
    }

    @Test
    public void equalsMethod() {
        Prefix aaa = new Prefix("aaa");

        assertEquals(aaa, aaa);
        assertEquals(aaa, new Prefix("aaa"));

        assertNotEquals(aaa, "aaa");
        assertNotEquals(aaa, new Prefix("aab"));
    }

}
