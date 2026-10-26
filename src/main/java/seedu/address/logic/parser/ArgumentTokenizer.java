package seedu.address.logic.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Tokenizes arguments string of the form: {@code preamble <prefix>value <prefix>value ...}<br>
 *     e.g. {@code some preamble text --t 11.00 --t 12.00 --k --m July}  where prefixes are {@code --t --k --m}.<br>
 * 1. An argument's value can be an empty string e.g. the value of {@code --k} in the above example.<br>
 * 2. Leading and trailing whitespaces of an argument value will be discarded.<br>
 * 3. An argument may be repeated and all its values will be accumulated e.g. the value of {@code --t}
 *    in the above example.<br>
 */
public class ArgumentTokenizer {
    private static final String PREFIX_REGEX = "--[A-Za-z0-9]+";
    private static final Pattern PREFIX_PATTERN = Pattern.compile(" " + PREFIX_REGEX);
    private static final Pattern FULL_PREFIX_PATTERN = Pattern.compile(PREFIX_REGEX);

    /**
     * Tokenizes an arguments string and returns an {@code ArgumentMultimap} object that maps prefixes to their
     * respective argument values.
     *
     * @param argsString Arguments string of the form: {@code preamble <prefix> value <prefix> value ...}
     * @return           ArgumentMultimap object that maps prefixes to their arguments
     */
    public static ArgumentMultimap tokenize(String argsString) throws ParseException {
        List<PrefixPosition> positions = findAllPrefixPositions(argsString);
        return extractArguments(argsString, positions);
    }

    /**
     * Find all zero-based prefix positions based on "--" in the given {@code argsString}
     */
    private static List<PrefixPosition> findAllPrefixPositions(String argsString) throws ParseException {
        List<PrefixPosition> positions = new ArrayList<>();

        int prefixPosition = findNextPrefixPosition(argsString, 0);
        while (prefixPosition != -1) {
            String prefixString = getFullPrefix(argsString, prefixPosition);
            int nextPrefixPosition = findNextPrefixPosition(argsString, prefixPosition);
            assert nextPrefixPosition == -1 || nextPrefixPosition > prefixPosition
                    : "Prefix scanning should always progress";

            if (!isValidPrefix(prefixString)) {
                prefixPosition = nextPrefixPosition;
                continue;
            }

            positions.add(new PrefixPosition(new Prefix(prefixString), prefixPosition));
            prefixPosition = nextPrefixPosition;
        }

        return positions;
    }

    /**
     * Returns the index of the first occurrence of any --prefix in {@code argsString}
     * starting from index {@code fromIndex}.
     * An --prefix is detected if there is a literal space before the --prefix, followed by at least one alphanumeric
     * character.
     * Returns -1 if no such occurrence can be found.
     */
    private static int findNextPrefixPosition(String argsString, int fromIndex) {
        Matcher matcher = PREFIX_PATTERN.matcher(argsString);
        return matcher.find(fromIndex) ? matcher.start() + 1 : -1; // +1 as offset of whitespace
    }

    /**
     * From the starting index of the --prefix given in {@code prefixStartIndex},
     * get the entire --prefix string, delimited by a space at the end of the prefix.
     */
    private static String getFullPrefix(String argsString, int prefixStartIndex) {
        int prefixEndIndex = argsString.indexOf(" ", prefixStartIndex);
        // if end index is -1, the argsString ends with the index
        prefixEndIndex = prefixEndIndex == -1 ? argsString.length() : prefixEndIndex;
        return argsString.substring(prefixStartIndex, prefixEndIndex);
    }

    /**
     * Returns whether {@code prefixString} has the form {@code --prefix}, where every character after the initial
     * {@code --} is alphanumeric.
     */
    private static boolean isValidPrefix(String prefixString) {
        return FULL_PREFIX_PATTERN.matcher(prefixString).matches();
    }

    /**
     * Extracts prefixes and their argument values, and returns an {@code ArgumentMultimap} object that maps the
     * extracted prefixes to their respective arguments. Prefixes are extracted based on their zero-based positions in
     * {@code argsString}.
     *
     * @param argsString      Arguments string of the form: {@code preamble <prefix>value <prefix>value ...}
     * @param prefixPositions Zero-based positions of all prefixes in {@code argsString}
     * @return                ArgumentMultimap object that maps prefixes to their arguments
     */
    private static ArgumentMultimap extractArguments(String argsString, List<PrefixPosition> prefixPositions) {

        // Sort by start position
        prefixPositions.sort((prefix1, prefix2) -> prefix1.getStartPosition() - prefix2.getStartPosition());

        // Insert a PrefixPosition to represent the preamble
        PrefixPosition preambleMarker = new PrefixPosition(new Prefix(""), 0);
        prefixPositions.addFirst(preambleMarker);

        // Add a dummy PrefixPosition to represent the end of the string
        PrefixPosition endPositionMarker = new PrefixPosition(new Prefix(""), argsString.length());
        prefixPositions.add(endPositionMarker);

        // Map prefixes to their argument values (if any)
        ArgumentMultimap argMultimap = new ArgumentMultimap();
        for (int i = 0; i < prefixPositions.size() - 1; i++) {
            // Extract and store prefixes and their arguments
            Prefix argPrefix = prefixPositions.get(i).getPrefix();
            String argValue = extractArgumentValue(argsString, prefixPositions.get(i), prefixPositions.get(i + 1));
            argMultimap.put(argPrefix, argValue);
        }

        return argMultimap;
    }

    /**
     * Returns the trimmed value of the argument in the arguments string specified by {@code currentPrefixPosition}.
     * The end position of the value is determined by {@code nextPrefixPosition}.
     */
    private static String extractArgumentValue(String argsString,
                                        PrefixPosition currentPrefixPosition,
                                        PrefixPosition nextPrefixPosition) {
        Prefix prefix = currentPrefixPosition.getPrefix();

        int valueStartPos = currentPrefixPosition.getStartPosition() + prefix.getPrefix().length();
        assert valueStartPos >= 0 : "Current prefix position should be non-negative";

        int valueEndPos = nextPrefixPosition.getStartPosition();
        assert valueEndPos <= argsString.length() : "Next prefix position should be before end of string";

        assert valueStartPos <= valueEndPos : "Prefix positions should define a valid substring range";

        String value = argsString.substring(valueStartPos, valueEndPos);

        return value.trim();
    }

    /**
     * Represents a prefix's position in an arguments string.
     */
    private static class PrefixPosition {
        private int startPosition;
        private final Prefix prefix;

        PrefixPosition(Prefix prefix, int startPosition) {
            this.prefix = prefix;
            this.startPosition = startPosition;
        }

        int getStartPosition() {
            return startPosition;
        }

        Prefix getPrefix() {
            return prefix;
        }
    }

}
