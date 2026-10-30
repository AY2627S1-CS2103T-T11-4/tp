package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLEAR_TAGS;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.FieldValueInvalidException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.exercise.ExerciseDescription;
import seedu.address.model.exercise.ExerciseName;
import seedu.address.model.student.Address;
import seedu.address.model.student.Email;
import seedu.address.model.student.Name;
import seedu.address.model.student.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.model.workoutplan.WorkoutDate;
import seedu.address.model.workoutplan.WorkoutDescription;
import seedu.address.model.workoutplan.WorkoutTitle;

@SuppressWarnings("checkstyle:Regexp")
public class ParserUtilTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";
    private static final String INVALID_TITLE = "";
    private static final String INVALID_DESC = "";
    private static final String INVALID_DATE_CHARS = "dd102026";
    private static final String INVALID_DATE_LENGTH = "091026";
    private static final String INVALID_DATE_CALENDAR_VALUE = "31132026";

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_PHONE = "1234567";
    private static final String VALID_ADDRESS = "123 Main Street #0505";
    private static final String VALID_EMAIL = "rachel@example.com";
    private static final String VALID_TAG_1 = "friend";
    private static final String VALID_TAG_2 = "neighbour";
    private static final String VALID_TITLE = "Shoulder Day";
    private static final String VALID_DESC = "Shoulder Press 3x 80kg";
    private static final String VALID_DATE = "09102026";
    private static final Date VALID_DATE_OBJECT = Date.from(LocalDate.of(2026, 10, 9)
            .atStartOfDay(ZoneId.systemDefault()).toInstant()); // mirrors VALID_DATE

    private static final String WHITESPACE = " \t\r\n";

    private final class TagStub extends Tag {

        /**
         * Creates a workout plan tag with the given name.
         */
        public TagStub(String tagName) {
            super(tagName);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }
            if (!(other instanceof TagStub otherTag)) {
                return false;
            }
            return tagName.equals(otherTag.tagName);
        }
    }

    // EP: index input is null (invalid).
    @Test
    public void parseIndex_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseIndex((String) null));
    }

    // EP: non-numeric input (invalid).
    @Test
    public void parseIndex_invalidInput_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("10 a"));
    }

    // BV: index > Integer.MAX_VALUE (invalid).
    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_INVALID_INDEX, ()
            -> ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1)));
    }

    // EP: input contains leading zeroes (invalid).
    @Test
    public void parseIndex_leadingZeros_throwsParseException() {
        String paddedIndex = "0".repeat(24) + "1";
        assertThrows(ParseException.class, MESSAGE_INVALID_INDEX, () -> ParserUtil.parseIndex("01"));
        assertThrows(ParseException.class, MESSAGE_INVALID_INDEX, () -> ParserUtil.parseIndex(paddedIndex));
    }

    // BV: 1 <= index <= Integer.MAX_VALUE (valid).
    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST, ParserUtil.parseIndex("  1  "));
        assertEquals(INDEX_SECOND, ParserUtil.parseIndex("2"));

        // Largest supported one-based index
        assertEquals(Index.fromOneBased(Integer.MAX_VALUE), ParserUtil.parseIndex(Integer.toString(Integer.MAX_VALUE)));
    }

    // EP: name input is null (invalid).
    @Test
    public void parseName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseName((String) null));
    }

    // EP: name violates its format constraints (invalid).
    @Test
    public void parseName_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseName(INVALID_NAME));
    }

    // Each valid input at least once: an unpadded valid name is parsed.
    @Test
    public void parseName_validValueWithoutWhitespace_returnsName() throws Exception {
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(VALID_NAME));
    }

    // Each valid input at least once: surrounding name whitespace is trimmed.
    @Test
    public void parseName_validValueWithWhitespace_returnsTrimmedName() throws Exception {
        String nameWithWhitespace = WHITESPACE + VALID_NAME + WHITESPACE;
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(nameWithWhitespace));
    }

    // EP: keyword input is null (invalid).
    @Test
    public void parseNameKeywords_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseNameKeywords(null));
    }

    // BV: length < 1 (invalid).
    @Test
    public void parseNameKeywords_blank_throwsFieldValueInvalidException() {
        assertThrows(FieldValueInvalidException.class, FindCommand.MESSAGE_INVALID_KEYWORDS, ()
            -> ParserUtil.parseNameKeywords(WHITESPACE));
    }

    // EP: every keyword violates name constraints (invalid).
    @Test
    public void parseNameKeywords_allInvalid_throwsFieldValueInvalidException() {
        assertThrows(FieldValueInvalidException.class, FindCommand.MESSAGE_INVALID_KEYWORDS, ()
            -> ParserUtil.parseNameKeywords("Alice! Bob?"));
    }

    // EP: mixed-validity keywords are retained for matching (valid).
    @Test
    public void parseNameKeywords_mixedValidity_keepsAllKeywords() throws Exception {
        assertEquals(List.of("!!!", "Alice", "Bob?"), ParserUtil.parseNameKeywords("!!! Alice Bob?"));
    }

    // Each valid input at least once: alphanumeric keywords are parsed.
    @Test
    public void parseNameKeywords_alphanumeric_returnsKeywords() throws Exception {
        assertEquals(List.of("Alice123", "123"), ParserUtil.parseNameKeywords("Alice123 123"));
    }

    // Each valid input at least once: keyword whitespace is trimmed.
    @Test
    public void parseNameKeywords_whitespace_returnsTrimmedKeywords() throws Exception {
        assertEquals(List.of("Alice", "Bob"),
                ParserUtil.parseNameKeywords(WHITESPACE + "Alice" + WHITESPACE + "Bob" + WHITESPACE));
    }

    // EP: phone input is null (invalid).
    @Test
    public void parsePhone_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parsePhone((String) null));
    }

    // EP: phone violates its format constraints (invalid).
    @Test
    public void parsePhone_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone(INVALID_PHONE));
    }

    // Each valid input at least once: an unpadded valid phone is parsed.
    @Test
    public void parsePhone_validValueWithoutWhitespace_returnsPhone() throws Exception {
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(VALID_PHONE));
    }

    // Each valid input at least once: surrounding phone whitespace is trimmed.
    @Test
    public void parsePhone_validValueWithWhitespace_returnsTrimmedPhone() throws Exception {
        String phoneWithWhitespace = WHITESPACE + VALID_PHONE + WHITESPACE;
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(phoneWithWhitespace));
    }

    // EP: address input is null (invalid).
    @Test
    public void parseAddress_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseAddress((String) null));
    }

    // EP: address violates its format constraints (invalid).
    @Test
    public void parseAddress_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseAddress(INVALID_ADDRESS));
    }

    // Each valid input at least once: an unpadded valid address is parsed.
    @Test
    public void parseAddress_validValueWithoutWhitespace_returnsAddress() throws Exception {
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(VALID_ADDRESS));
    }

    // Each valid input at least once: surrounding address whitespace is trimmed.
    @Test
    public void parseAddress_validValueWithWhitespace_returnsTrimmedAddress() throws Exception {
        String addressWithWhitespace = WHITESPACE + VALID_ADDRESS + WHITESPACE;
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(addressWithWhitespace));
    }

    // EP: email input is null (invalid).
    @Test
    public void parseEmail_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseEmail((String) null));
    }

    // EP: email violates its format constraints (invalid).
    @Test
    public void parseEmail_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseEmail(INVALID_EMAIL));
    }

    // Each valid input at least once: an unpadded valid email is parsed.
    @Test
    public void parseEmail_validValueWithoutWhitespace_returnsEmail() throws Exception {
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(VALID_EMAIL));
    }

    // Each valid input at least once: surrounding email whitespace is trimmed.
    @Test
    public void parseEmail_validValueWithWhitespace_returnsTrimmedEmail() throws Exception {
        String emailWithWhitespace = WHITESPACE + VALID_EMAIL + WHITESPACE;
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(emailWithWhitespace));
    }

    // Each valid input at least once: valid exercise fields are trimmed and parsed.
    @Test
    public void parseExerciseFields_validValues_returnTrimmedValues() throws Exception {
        assertEquals(new ExerciseName("Front Squat"), ParserUtil.parseExerciseName("  Front Squat  "));
        assertEquals(new ExerciseDescription("Three sets"),
                ParserUtil.parseExerciseDescription("  Three sets  "));
    }

    @Test
    public void parseExerciseFields_invalidValues_throwParseException() {
        // One invalid input at a time: only the exercise name is blank.
        assertThrows(ParseException.class, () -> ParserUtil.parseExerciseName("   "));
        // One invalid input at a time: only the exercise description is blank.
        assertThrows(ParseException.class, () -> ParserUtil.parseExerciseDescription("   "));
    }

    // EP: tag input is null (invalid).
    @Test
    public void parseTag_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTag(null, TagStub::new));
    }

    // EP: tag violates its format constraints (invalid).
    @Test
    public void parseTag_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTag(INVALID_TAG, TagStub::new));
    }

    // BV: length < 1 (invalid).
    @Test
    public void parseTag_emptyValue_throwsParseException() {
        assertThrows(ParseException.class, Tag.MESSAGE_CONSTRAINTS, ()
                -> ParserUtil.parseTag("", TagStub::new));
    }

    // Each valid input at least once: an unpadded valid tag is parsed.
    @Test
    public void parseTag_validValueWithoutWhitespace_returnsTag() throws Exception {
        TagStub expectedTag = new TagStub(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(VALID_TAG_1, TagStub::new));
    }

    // Each valid input at least once: surrounding tag whitespace is trimmed.
    @Test
    public void parseTag_validValueWithWhitespace_returnsTrimmedTag() throws Exception {
        String tagWithWhitespace = WHITESPACE + VALID_TAG_1 + WHITESPACE;
        TagStub expectedTag = new TagStub(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(tagWithWhitespace, TagStub::new));
    }

    // EP: tag collection is null (invalid).
    @Test
    public void parseTags_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTags(null, TagStub::new));
    }

    // One invalid input at a time: the collection contains an invalid tag.
    @Test
    public void parseTags_collectionWithInvalidTags_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTags(List.of(VALID_TAG_1, INVALID_TAG), TagStub::new));
    }

    // BV: 0 <= count <= MAX_TAG_COUNT (valid).
    @Test
    public void parseTags_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseTags(List.of(), TagStub::new).isEmpty());
    }

    // Each valid input at least once: all valid tags are parsed into the set.
    @Test
    public void parseTags_collectionWithValidTags_returnsTagSet() throws Exception {
        Set<TagStub> actualTagSet = ParserUtil.parseTags(List.of(VALID_TAG_1, VALID_TAG_2), TagStub::new);
        Set<TagStub> expectedTagSet = Set.of(new TagStub(VALID_TAG_1), new TagStub(VALID_TAG_2));

        assertEquals(expectedTagSet, actualTagSet);
    }

    // BV: length = 0 (valid).
    @Test
    public void validateEmptyField_emptyValue_success() throws Exception {
        ParserUtil.validateEmptyField("", Messages.getErrorMessageForExtraValues(PREFIX_CLEAR_TAGS));
    }

    // BV: length > 0 (invalid).
    @Test
    public void validateEmptyField_nonEmptyValue_throwsFieldValueInvalidException() {
        String errorMessage = Messages.getErrorMessageForExtraValues(PREFIX_CLEAR_TAGS);
        assertThrows(FieldValueInvalidException.class, errorMessage, () ->
                ParserUtil.validateEmptyField("value", errorMessage));
    }

    // BV: 0 <= count <= MAX_TAG_COUNT (valid).
    @Test
    public void parseTags_tagsAtLimit_returnsTagSet() throws Exception {
        List<String> tags = createTagNames(Tag.MAX_TAG_COUNT);

        assertEquals(Tag.MAX_TAG_COUNT, ParserUtil.parseTags(tags, TagStub::new).size());
    }

    // BV: count > MAX_TAG_COUNT (invalid).
    @Test
    public void parseTags_tooManyTags_throwsParseException() {
        List<String> tags = createTagNames(Tag.MAX_TAG_COUNT + 1);

        assertThrows(ParseException.class, Tag.MESSAGE_TAG_LIMIT, ()
            -> ParserUtil.parseTags(tags, TagStub::new));
    }

    // EP: workout title violates its constraints (invalid).
    @Test
    public void parseTitle_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTitle(INVALID_TITLE));
    }

    // Each valid input at least once: a valid workout title is parsed.
    @Test
    public void parseTitle_validValue_returnsWorkoutTitle() throws Exception {
        WorkoutTitle expectedTitle = new WorkoutTitle(VALID_TITLE);
        assertEquals(expectedTitle, ParserUtil.parseTitle(VALID_TITLE));
    }

    // EP: workout description violates its constraints (invalid).
    @Test
    public void parseDescription_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseDescription(INVALID_DESC));
        assertThrows(ParseException.class, () -> ParserUtil.parseDescription(
                "a".repeat(WorkoutDescription.MAX_LENGTH + 1)));
    }

    // Each valid input at least once: a valid workout description is parsed.
    @Test
    public void parseDescription_validValue_returnsWorkoutDescription() throws Exception {
        WorkoutDescription expectedDesc = new WorkoutDescription(VALID_DESC);
        assertEquals(expectedDesc, ParserUtil.parseDescription(VALID_DESC));
    }

    // EP: date contains non-numeric characters (invalid).
    @Test
    public void parseDate_invalidValueChar_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseDate(INVALID_DATE_CHARS));
    }

    // EP: date has the wrong length (invalid).
    @Test
    public void parseDate_invalidValueLength_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseDate(INVALID_DATE_LENGTH));
    }

    // EP: numeric date is not a calendar date (invalid).
    @Test
    public void parseDate_invalidCalendarValue_throwsFieldValueInvalidException() {
        assertThrows(FieldValueInvalidException.class,
                WorkoutDate.MESSAGE_CONSTRAINTS, ()
                    -> ParserUtil.parseDate(INVALID_DATE_CALENDAR_VALUE));
    }

    // BV: month < 1 or month > 12 (invalid).
    @Test
    public void parseDate_invalidMonth_reportsGenericDateError() {
        for (String date : List.of("01132026", "01002026")) {
            assertThrows(FieldValueInvalidException.class, WorkoutDate.MESSAGE_CONSTRAINTS, ()
                -> ParserUtil.parseDate(date));
        }
    }

    // BV: day < 1 or day > month max (invalid).
    @Test
    public void parseDate_invalidDay_reportsGenericDateError() {
        for (String date : List.of("32122026", "00012026", "31022026", "29022026", "31042026", "29022100")) {
            assertThrows(FieldValueInvalidException.class,
                    WorkoutDate.MESSAGE_CONSTRAINTS, ()
                    -> ParserUtil.parseDate(date));
        }
    }

    // BV: year = 0000 or 9999, month = 1 or 12, and leap days (valid).
    @Test
    public void parseDate_validCalendarBoundaries_preservesInputAndDisplay() throws Exception {
        for (String date : List.of("01010000", "29020000", "01010001", "31129999",
                "28022026", "29022024", "29022000", "30042026", "31122026")) {
            WorkoutDate parsed = ParserUtil.parseDate(date);
            assertEquals(date, parsed.toInputString());
            assertEquals(date.substring(0, 2) + "-" + date.substring(2, 4) + "-" + date.substring(4),
                    parsed.toDisplayString());
        }
    }

    // Each valid input at least once: a typical valid date is parsed.
    @Test
    public void parseDate_validValue_returnsWorkoutDate() throws Exception {
        WorkoutDate expectedDate = new WorkoutDate(VALID_DATE_OBJECT);
        assertEquals(expectedDate, ParserUtil.parseDate(VALID_DATE));
    }

    private static List<String> createTagNames(int count) {
        return IntStream.range(0, count)
                .mapToObj(index -> "tag" + index)
                .toList();
    }
}
