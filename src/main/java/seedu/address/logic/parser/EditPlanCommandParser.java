package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PLAN;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditPlanCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.WorkoutPlanTag;

/**
 * Parses input arguments and creates a new EditPlanCommand object
 */
public class EditPlanCommandParser implements Parser<EditPlanCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditPlanCommand
     * and returns an EditPlanCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public EditPlanCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PLAN, PREFIX_DATE, PREFIX_TAG);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                            EditPlanCommand.MESSAGE_USAGE),
                    pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PLAN, PREFIX_DATE);

        EditPlanCommand.EditPlanDescriptor editPlanDescriptor = new EditPlanCommand.EditPlanDescriptor();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editPlanDescriptor.setTitle(ParserUtil.parseTitle(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_PLAN).isPresent()) {
            editPlanDescriptor.setDescription(ParserUtil.parseDescription(argMultimap.getValue(PREFIX_PLAN).get()));
        }
        if (argMultimap.getValue(PREFIX_DATE).isPresent()) {
            editPlanDescriptor.setDate(ParserUtil.parseDate(argMultimap.getValue(PREFIX_DATE).get()));
        }
        parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editPlanDescriptor::setWorkoutPlanTags);

        if (!editPlanDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditPlanCommand.MESSAGE_NOT_EDITED);
        }

        return new EditPlanCommand(index, editPlanDescriptor);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<WorkoutPlanTag>} if {@code tags} is non-empty.
     * If {@code tags} contains only one element which is an empty string, it will be parsed into a
     * {@code Set<StudentTag>} containing zero tags.
     */
    private Optional<Set<WorkoutPlanTag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseWorkoutPlanTags(tagSet));
    }

}
