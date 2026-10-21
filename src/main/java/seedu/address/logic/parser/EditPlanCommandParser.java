package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PLAN;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditPlanCommand;
import seedu.address.logic.parser.exceptions.NoFieldsToEditException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.WorkoutPlanTag;

/**
 * Parses input arguments and creates a new EditPlanCommand object
 */
public class EditPlanCommandParser implements Parser<EditPlanCommand> {

    private static final Set<Prefix> VALID_PREFIXES = Set.of(PREFIX_NAME, PREFIX_PLAN, PREFIX_DATE, PREFIX_TAG);
    private static final Set<Prefix> REQUIRED_PREFIXES = Set.of();
    private static final Set<Prefix> NO_REPEAT_PREFIXES = Set.of(PREFIX_NAME, PREFIX_PLAN, PREFIX_DATE);

    private static final Logger logger = LogsCenter.getLogger(EditPlanCommandParser.class);

    /**
     * Parses the given {@code String} of arguments in the context of the EditPlanCommand
     * and returns an EditPlanCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public EditPlanCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentValidator argValidator = new ArgumentValidator(VALID_PREFIXES, NO_REPEAT_PREFIXES, REQUIRED_PREFIXES);
        Index index;
        EditPlanCommand.EditPlanDescriptor editPlanDescriptor;

        ParsingStage stage = ParsingStage.TOKENIZATION;
        try {
            ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args);

            stage = ParsingStage.ARGUMENT_VALIDATION;
            argValidator.verifyArgs(argMultimap);

            stage = ParsingStage.FIELD_PARSING;
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
            editPlanDescriptor = this.parseValidEditFields(argMultimap);

        } catch (ParseException pe) {
            logger.finer("Parsing of editPlan command failed at " + stage.getMessage());
            throw pe.withMessage(Messages.formatParserError(pe.getMessage(), EditPlanCommand.MESSAGE_USAGE));
        }

        if (!editPlanDescriptor.isAnyFieldEdited()) {
            throw new NoFieldsToEditException(EditPlanCommand.MESSAGE_NOT_EDITED);
        }

        return new EditPlanCommand(index, editPlanDescriptor);
    }

    /**
     * Parses the validated {@code args} string to produce the EditPlanDescriptor object,
     * wrapping any error messages thrown with the message usage.
     * @throws ParseException if any of the fields contain invalid contents
     */
    private EditPlanCommand.EditPlanDescriptor parseValidEditFields(ArgumentMultimap argMultimap)
            throws ParseException {
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

        return editPlanDescriptor;
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
