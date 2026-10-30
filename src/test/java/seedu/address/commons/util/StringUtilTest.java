package seedu.address.commons.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

public class StringUtilTest {

    //---------------- Tests for isNonZeroUnsignedInteger --------------------------------------

    @Test
    public void isNonZeroUnsignedInteger() {

        // EP: length = 0 (invalid).
        // BV: length < 1 (invalid).
        assertFalse(StringUtil.isNonZeroUnsignedInteger(""));
        // EP: only whitespace characters (invalid).
        assertFalse(StringUtil.isNonZeroUnsignedInteger("  "));

        // EP: one non-digit character (invalid).
        assertFalse(StringUtil.isNonZeroUnsignedInteger("a"));
        // EP: multiple non-digit characters (invalid).
        assertFalse(StringUtil.isNonZeroUnsignedInteger("aaa"));

        // BV: value < 1 (invalid).
        assertFalse(StringUtil.isNonZeroUnsignedInteger("0"));

        // EP: digits with a leading zero; value >= 1 (valid).
        assertTrue(StringUtil.isNonZeroUnsignedInteger("01"));

        // EP: leading minus sign (invalid).
        assertFalse(StringUtil.isNonZeroUnsignedInteger("-1"));
        // EP: leading plus sign (invalid).
        assertFalse(StringUtil.isNonZeroUnsignedInteger("+1"));

        // EP: digits with surrounding whitespace (invalid).
        assertFalse(StringUtil.isNonZeroUnsignedInteger(" 10 "));
        // EP: digits separated by whitespace (invalid).
        assertFalse(StringUtil.isNonZeroUnsignedInteger("1 0"));

        // BV: value > Integer.MAX_VALUE (invalid).
        assertFalse(StringUtil.isNonZeroUnsignedInteger(Long.toString(Integer.MAX_VALUE + 1)));

        // BV: value >= 1 (valid).
        assertTrue(StringUtil.isNonZeroUnsignedInteger("1"));
        // EP: 10 <= value <= Integer.MAX_VALUE (valid).
        assertTrue(StringUtil.isNonZeroUnsignedInteger("10"));
    }


    //---------------- Tests for containsWordIgnoreCase --------------------------------------

    /*
     * Invalid equivalence partitions for word: null, empty, multiple words
     * Invalid equivalence partitions for sentence: null
     * The four test cases below test one invalid input at a time.
     */

    // One invalid input at a time: only the word is null.
    @Test
    public void containsWordIgnoreCase_nullWord_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtil.containsWordIgnoreCase("typical sentence", null));
    }

    // One invalid input at a time: only the word is empty.
    @Test
    public void containsWordIgnoreCase_emptyWord_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, "Word parameter cannot be empty", ()
            -> StringUtil.containsWordIgnoreCase("typical sentence", "  "));
    }

    // One invalid input at a time: only the word contains multiple words.
    @Test
    public void containsWordIgnoreCase_multipleWords_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, "Word parameter should be a single word", ()
            -> StringUtil.containsWordIgnoreCase("typical sentence", "aaa BBB"));
    }

    // One invalid input at a time: only the sentence is null.
    @Test
    public void containsWordIgnoreCase_nullSentence_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtil.containsWordIgnoreCase(null, "abc"));
    }

    /*
     * Valid equivalence partitions for word:
     *   - any word
     *   - word containing symbols/numbers
     *   - word with leading/trailing spaces
     *
     * Valid equivalence partitions for sentence:
     *   - empty string
     *   - one word
     *   - multiple words
     *   - sentence with extra spaces
     *
     * Possible scenarios returning true:
     *   - matches first word in sentence
     *   - last word in sentence
     *   - middle word in sentence
     *   - matches multiple words
     *
     * Possible scenarios returning false:
     *   - query word matches part of a sentence word
     *   - sentence word matches part of the query word
     *
     * The test method below tries to verify all above with a reasonably low number of test cases.
     */

    @Test
    public void containsWordIgnoreCase_validInputs_correctResult() {

        // BV: length >= 0 (valid).
        assertFalse(StringUtil.containsWordIgnoreCase("", "abc"));
        // EP: only whitespace characters (valid).
        assertFalse(StringUtil.containsWordIgnoreCase("    ", "123"));

        // EP: query is only part of a sentence word (no match).
        assertFalse(StringUtil.containsWordIgnoreCase("aaa bbb ccc", "bb"));
        // EP: sentence word is only part of the query (no match).
        assertFalse(StringUtil.containsWordIgnoreCase("aaa bbb ccc", "bbbb"));

        // EP: middle word differs only by letter case (match).
        assertTrue(StringUtil.containsWordIgnoreCase("aaa bBb ccc", "Bbb"));
        // BV: position <= last (match).
        assertTrue(StringUtil.containsWordIgnoreCase("aaa bBb ccc@1", "CCc@1"));
        // EP: sentence contains extra whitespace (match).
        assertTrue(StringUtil.containsWordIgnoreCase("  AAA   bBb   ccc  ", "aaa"));
        // BV: count >= 1 (match).
        assertTrue(StringUtil.containsWordIgnoreCase("Aaa", "aaa"));
        // EP: query contains surrounding whitespace (match).
        assertTrue(StringUtil.containsWordIgnoreCase("aaa bbb ccc", "  ccc  "));

        // EP: query occurs multiple times (match).
        assertTrue(StringUtil.containsWordIgnoreCase("AAA bBb ccc  bbb", "bbB"));
    }

    //---------------- Tests for getDetails --------------------------------------

    /*
     * Equivalence Partitions: null, valid throwable object
     */

    // EP: throwable is non-null (valid).
    @Test
    public void getDetails_exceptionGiven() {
        assertTrue(StringUtil.getDetails(new FileNotFoundException("file not found"))
            .contains("java.io.FileNotFoundException: file not found"));
    }

    // EP: throwable is null (invalid).
    @Test
    public void getDetails_nullGiven_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtil.getDetails(null));
    }

}
