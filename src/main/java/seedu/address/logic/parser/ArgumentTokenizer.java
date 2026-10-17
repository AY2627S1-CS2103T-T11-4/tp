package seedu.address.logic.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.logic.Messages;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Tokenizes arguments string of the form: {@code preamble <prefix>value <prefix>value ...}<br>
 *     e.g. {@code some preamble text t/ 11.00 t/12.00 k/ m/ July}  where prefixes are {@code t/ k/ m/}.<br>
 * 1. An argument's value can be an empty string e.g. the value of {@code k/} in the above example.<br>
 * 2. Leading and trailing whitespaces of an argument value will be discarded.<br>
 * 3. An argument may be repeated and all its values will be accumulated e.g. the value of {@code t/}
 *    in the above example.<br>
 */
public class ArgumentTokenizer {
    /**
     * Tokenizes an arguments string and returns an {@code ArgumentMultimap} object that maps prefixes to their
     * respective argument values. Only the given prefixes will be recognized in the arguments string.
     *
     * @param argsString Arguments string of the form: {@code preamble <prefix> value <prefix> value ...}
     * @param prefixes   Prefixes to tokenize the arguments string with
     * @return           ArgumentMultimap object that maps prefixes to their arguments
     */
    public static ArgumentMultimap tokenize(String argsString, Prefix... prefixes) throws ParseException {
        List<PrefixPosition> positions = findAllPrefixPositions(argsString, prefixes);
        return extractArguments(argsString, positions);
    }

    /**
     * Find all zero-based prefix positions based on "--" in the given {@code argsString}
     *
     * @throws ParseException if any --prefix in {@code argsString} is not found in the list of valid {@code prefixes}
     */
    private static List<PrefixPosition> findAllPrefixPositions(String argsString, Prefix... prefixes)
            throws ParseException {
        List<PrefixPosition> positions = new ArrayList<>();

        int prefixPosition = findNextPrefixPosition(argsString, 0);
        while (prefixPosition != -1) {
            String prefixString = getFullPrefix(argsString, prefixPosition);
            Prefix prefix = matchValidPrefix(prefixString, prefixes);
            PrefixPosition extendedPrefix = new PrefixPosition(prefix, prefixPosition);
            positions.add(extendedPrefix);
            prefixPosition = findNextPrefixPosition(argsString, prefixPosition);
        }

        return positions;
    }

    /**
     * Returns the index of the first occurrence of any --prefix in {@code argsString}
     * starting from index {@code fromIndex}.
     * An --prefix is detected if there is a whitespace before the --prefix
     * Returns -1 if no such occurrence can be found.
     */
    private static int findNextPrefixPosition(String argsString, int fromIndex) {
        int prefixIndex = argsString.indexOf(" --", fromIndex);
        return prefixIndex == -1 ? -1 : prefixIndex + 1; // +1 as offset of whitespace
    }

    /**
     * From the starting index of the --prefix given in {@code prefixStartIndex},
     * get the entire --prefix string, delimited by a space at the end of the prefix
     *
     * @throws ParseException if the prefix name is empty, i.e. occurrences of " -- "
     */
    private static String getFullPrefix(String argsString, int prefixStartIndex) throws ParseException {
        int prefixEndIndex = argsString.indexOf(" ", prefixStartIndex);
        // if end index is -1, the argsString ends with the index
        prefixEndIndex = prefixEndIndex == -1 ? argsString.length() : prefixEndIndex;
        if (prefixEndIndex - prefixStartIndex == 2) { // checking for "--"
            throw new ParseException(Messages.MESSAGE_EMPTY_PREFIX);
        }
        return argsString.substring(prefixStartIndex, prefixEndIndex);
    }

    /**
     * Verifies the detected {@code prefixString} is part of the valid list of {@code prefixes}
     * before extracting the corresponding {@code Prefix} object.
     *
     * @return {@code Prefix} object in {@code prefixes} that corresponds with {@code prefixString}
     * @throws ParseException if none of the {@code prefixes} corresponds with {@code prefixString}
     */
    private static Prefix matchValidPrefix(String prefixString, Prefix... prefixes) throws ParseException {
        return Arrays.stream(prefixes)
                .filter(p -> p.getPrefix().equals(prefixString))
                .findFirst()
                .orElseThrow(() -> new ParseException(String.format(Messages.MESSAGE_INVALID_PREFIX, prefixString)));
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
        String value = argsString.substring(valueStartPos, nextPrefixPosition.getStartPosition());

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
