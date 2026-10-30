package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.Set;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditExerciseCommand;
import seedu.address.logic.parser.exceptions.NoFieldsToEditException;
import seedu.address.logic.parser.exceptions.ParseException;

/** Parses arguments for {@link EditExerciseCommand}. */
public class EditExerciseCommandParser implements Parser<EditExerciseCommand> {

    private static final Set<Prefix> VALID_PREFIXES = Set.of(PREFIX_NAME, PREFIX_DESCRIPTION);
    private static final Set<Prefix> NO_REPEAT_PREFIXES = Set.of(PREFIX_NAME, PREFIX_DESCRIPTION);
    private static final Set<Prefix> REQUIRED_PREFIXES = Set.of();
    private static final Logger logger = LogsCenter.getLogger(EditExerciseCommandParser.class);

    @Override
    public EditExerciseCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentValidator argValidator = new ArgumentValidator(
                VALID_PREFIXES, NO_REPEAT_PREFIXES, REQUIRED_PREFIXES);
        Index index;
        EditExerciseCommand.EditExerciseDescriptor descriptor;

        ParsingStage stage = ParsingStage.TOKENIZATION;
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args);

        try {
            stage = ParsingStage.ARGUMENT_VALIDATION;
            argValidator.verifyArgs(argMultimap);

            stage = ParsingStage.FIELD_PARSING;
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
            descriptor = parseValidEditFields(argMultimap);

        } catch (ParseException pe) {
            logger.finer("Parsing of editExercise command failed at " + stage.getMessage());
            throw pe.withMessage(Messages.formatParserError(pe.getMessage(), EditExerciseCommand.MESSAGE_USAGE));
        }

        validateAtLeastOneFieldEdited(descriptor);
        return new EditExerciseCommand(index, descriptor);
    }

    private EditExerciseCommand.EditExerciseDescriptor parseValidEditFields(ArgumentMultimap argMultimap)
            throws ParseException {
        EditExerciseCommand.EditExerciseDescriptor descriptor =
                new EditExerciseCommand.EditExerciseDescriptor();
        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            descriptor.setName(ParserUtil.parseExerciseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_DESCRIPTION).isPresent()) {
            descriptor.setDescription(ParserUtil.parseExerciseDescription(
                    argMultimap.getValue(PREFIX_DESCRIPTION).get()));
        }
        return descriptor;
    }

    private void validateAtLeastOneFieldEdited(
            EditExerciseCommand.EditExerciseDescriptor descriptor) throws NoFieldsToEditException {
        if (!descriptor.isAnyFieldEdited()) {
            throw new NoFieldsToEditException(EditExerciseCommand.MESSAGE_NOT_EDITED);
        }
    }
}
