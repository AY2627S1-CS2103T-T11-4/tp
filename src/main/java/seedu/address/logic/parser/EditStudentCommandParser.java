package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.getErrorMessageForExtraValues;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADD_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLEAR_TAGS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DELETE_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.Set;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditStudentCommand;
import seedu.address.logic.parser.exceptions.NoFieldsToEditException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.StudentTag;

/**
 * Parses input arguments and creates a new EditStudentCommand object
 */
public class EditStudentCommandParser implements Parser<EditStudentCommand> {

    private static final Set<Prefix> VALID_PREFIXES =
            Set.of(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS,
                    PREFIX_ADD_TAG, PREFIX_DELETE_TAG, PREFIX_CLEAR_TAGS);
    private static final Set<Prefix> REQUIRED_PREFIXES = Set.of();
    private static final Set<Prefix> NO_REPEAT_PREFIXES =
            Set.of(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_CLEAR_TAGS);
    private static final Logger logger = LogsCenter.getLogger(EditStudentCommandParser.class);

    /**
     * Parses the given {@code String} of arguments in the context of the EditStudentCommand
     * and returns an EditStudentCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public EditStudentCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentValidator argValidator = new ArgumentValidator(true, VALID_PREFIXES, NO_REPEAT_PREFIXES,
                REQUIRED_PREFIXES);
        Index index;
        EditStudentCommand.EditStudentDescriptor editStudentDescriptor;

        ParsingStage stage = ParsingStage.TOKENIZATION;
        try {
            ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args);

            stage = ParsingStage.ARGUMENT_VALIDATION;
            argValidator.verifyArgs(argMultimap);

            stage = ParsingStage.FIELD_PARSING;
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
            editStudentDescriptor = this.parseValidEditFields(argMultimap);

        } catch (ParseException pe) {
            logger.finer("Parsing of editStudent failed at " + stage.getMessage());
            throw pe.withMessage(Messages.formatParserError(pe.getMessage(), EditStudentCommand.MESSAGE_USAGE));
        }

        if (!editStudentDescriptor.isAnyFieldEdited()) {
            throw new NoFieldsToEditException(EditStudentCommand.MESSAGE_NOT_EDITED);
        }

        return new EditStudentCommand(index, editStudentDescriptor);
    }


    /**
     * Parses the validated {@code args} string to produce the EditStudentDescriptor object,
     * wrapping any error messages thrown with the message usage.
     * @throws ParseException if any of the fields contain invalid contents
     */
    private EditStudentCommand.EditStudentDescriptor parseValidEditFields(ArgumentMultimap argMultimap)
            throws ParseException {
        EditStudentCommand.EditStudentDescriptor editStudentDescriptor = new EditStudentCommand.EditStudentDescriptor();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editStudentDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            editStudentDescriptor.setPhone(ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get()));
        }
        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            editStudentDescriptor.setEmail(ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get()));
        }
        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            editStudentDescriptor.setAddress(ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get()));
        }

        Set<StudentTag> tagsToAdd = ParserUtil.parseStudentTags(argMultimap.getAllValues(PREFIX_ADD_TAG));
        Set<StudentTag> tagsToDelete = ParserUtil.parseStudentTags(argMultimap.getAllValues(PREFIX_DELETE_TAG));
        ParserUtil.validateNoTagConflicts(tagsToAdd, tagsToDelete);
        if (!tagsToAdd.isEmpty()) {
            editStudentDescriptor.setTagsToAdd(tagsToAdd);
        }
        if (!tagsToDelete.isEmpty()) {
            editStudentDescriptor.setTagsToDelete(tagsToDelete);
        }
        if (argMultimap.getValue(PREFIX_CLEAR_TAGS).isPresent()) {
            verifyClearTag(argMultimap);
            editStudentDescriptor.setClearTags(true);
        }

        return editStudentDescriptor;
    }

    /**
     * Ensures that {@code emptyString} is empty, then produces an empty set of {@code StudentTag}s.
     */
    public static void verifyClearTag(ArgumentMultimap argMultiMap) throws ParseException {
        ParserUtil.validateEmptyField(
                argMultiMap.getValue(PREFIX_CLEAR_TAGS).get(), getErrorMessageForExtraValues(PREFIX_CLEAR_TAGS));
    }

}
