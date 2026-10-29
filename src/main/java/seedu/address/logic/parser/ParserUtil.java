package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.FieldValueInvalidException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.student.Address;
import seedu.address.model.student.Email;
import seedu.address.model.student.Name;
import seedu.address.model.student.Phone;
import seedu.address.model.tag.StudentTag;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.WorkoutPlanTag;
import seedu.address.model.workoutplan.WorkoutDate;
import seedu.address.model.workoutplan.WorkoutDescription;
import seedu.address.model.workoutplan.WorkoutTitle;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index must be a positive integer between 1 and "
            + Integer.MAX_VALUE + ", without leading zeros.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the index is outside the positive integer range or has leading zeros.
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        requireNonNull(oneBasedIndex);

        String trimmedIndex = oneBasedIndex.trim();
        if (trimmedIndex.startsWith("0") || !StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new FieldValueInvalidException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new FieldValueInvalidException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses whitespace-separated name search keywords, trimming leading and trailing whitespace.
     * Keeps all keywords when at least one can match a valid student name.
     *
     * @throws FieldValueInvalidException if no keyword can match a valid student name.
     */
    public static List<String> parseNameKeywords(String queries) throws FieldValueInvalidException {
        requireNonNull(queries);
        List<String> nameKeywords = List.of(queries.trim().split("\\s+"));
        if (nameKeywords.stream().noneMatch(Name::isValidName)) {
            throw new FieldValueInvalidException(FindCommand.MESSAGE_INVALID_KEYWORDS);
        }
        return nameKeywords;
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new FieldValueInvalidException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new FieldValueInvalidException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new FieldValueInvalidException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag} object, depending on the constructor provided.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    protected static <T extends Tag> T parseTag(String tag, Function<String, T> tagConstructor) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new FieldValueInvalidException(Tag.MESSAGE_CONSTRAINTS);
        }
        return tagConstructor.apply(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into {@code Set<Tag>} based on {@code tagConstructor}.
     */
    protected static <T extends Tag> Set<T> parseTags(
            Collection<String> tags,
            Function<String, T> tagConstructor) throws ParseException {
        requireNonNull(tags);
        final Set<T> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName, tagConstructor));
        }
        if (!Tag.isValidTagCount(tagSet.size())) {
            throw new FieldValueInvalidException(Tag.MESSAGE_TAG_LIMIT);
        }
        return tagSet;
    }

    /**
     * Parses {@code Collection<String> tags} into {@code Set<WorkoutPlanTag>}.
     */
    public static Set<WorkoutPlanTag> parseWorkoutPlanTags(Collection<String> tags) throws ParseException {
        return parseTags(tags, WorkoutPlanTag::new);
    }

    /**
     * Parses {@code Collection<String> tags} into {@code Set<StudentTag>}.
     */
    public static Set<StudentTag> parseStudentTags(Collection<String> tags) throws ParseException {
        return parseTags(tags, StudentTag::new);
    }

    /**
     * Parses a {@code String title} into a {@code WorkoutTitle}
     *
     * @throws ParseException if the given {@code title} is invalid
     */
    public static WorkoutTitle parseTitle(String title) throws ParseException {
        requireNonNull(title);
        if (!WorkoutTitle.isValidWorkoutTitle(title)) {
            throw new FieldValueInvalidException(WorkoutTitle.MESSAGE_CONSTRAINTS);
        }
        return new WorkoutTitle(title);
    }

    /**
     * Parses a {@code String description} into a {@code WorkoutDescription}
     *
     * @throws ParseException if the given {@code description} is invalid
     */
    public static WorkoutDescription parseDescription(String desc) throws ParseException {
        requireNonNull(desc);
        if (!WorkoutDescription.isValidWorkoutDescription(desc)) {
            throw new FieldValueInvalidException(WorkoutDescription.MESSAGE_CONSTRAINTS);
        }
        return new WorkoutDescription(desc);
    }

    /**
     * Parses a {@code String date} into a {@code WorkoutDate}
     *
     * @throws ParseException if the given {@code date} is invalid
     */
    public static WorkoutDate parseDate(String date) throws ParseException {
        requireNonNull(date);
        if (!WorkoutDate.isValidDateString(date)) {
            throw new FieldValueInvalidException(WorkoutDate.MESSAGE_CONSTRAINTS);
        }
        LocalDate dateObject;
        try {
            dateObject = LocalDate.parse(date, WorkoutDate.INPUT_DATE_FORMAT);
        } catch (DateTimeParseException e) {
            throw new FieldValueInvalidException(WorkoutDate.MESSAGE_CONSTRAINTS, e);
        }
        return WorkoutDate.fromLocalDate(dateObject);
    }

    /**
     * Checks that {@code emptyField} is an empty string
     *
     * @throws ParseException with {@code errorMessage} if {@code} emptyField is not an empty string
     */
    public static void validateEmptyField(String emptyField, String errorMessage) throws ParseException {
        if (!emptyField.isEmpty()) {
            throw new FieldValueInvalidException(errorMessage);
        }
    }
}
