package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.SingleIndexCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

public class SingleIndexCommandParserTest {
    private class SingleIndexCommandStub extends SingleIndexCommand {

        public static final String COMMAND_WORD = "deletePlan";

        public static final String MESSAGE_USAGE = COMMAND_WORD
                + ": A dummy SingleIndexCommand\n"
                + "Parameters: ID (must be a positive integer)\n"
                + "Example: " + COMMAND_WORD + " 1";

        /**
         * Creates a command to delete the workout plan at the given index.
         *
         * @param targetIndex index of the workout plan to delete
         */
        public SingleIndexCommandStub(Index targetIndex) {
            super(targetIndex);
        }


        @Override
        public CommandResult execute(Model model) throws CommandException {
            throw new CommandException("Cannot execute stub object");
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof SingleIndexCommandStub otherSingleIndexCommandStub)) {
                return false;
            }

            return targetIndex.equals(otherSingleIndexCommandStub.targetIndex);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("targetIndex", targetIndex)
                    .toString();
        }
    }

    private SingleIndexCommandParser<SingleIndexCommandStub> parser =
            new SingleIndexCommandParser<>(SingleIndexCommandStub::new, SingleIndexCommandStub.MESSAGE_USAGE);

    @Test
    public void parse_validArgs_returnsSingleIndexCommand() {
        assertParseSuccess(parser, "1", new SingleIndexCommandStub(INDEX_FIRST));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SingleIndexCommandStub.MESSAGE_USAGE));
    }
}
