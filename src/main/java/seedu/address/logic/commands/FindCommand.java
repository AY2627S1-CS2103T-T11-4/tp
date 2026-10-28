package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.student.NameContainsKeywordsPredicate;

/**
 * Finds and lists all students in the student list whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_DESCRIPTION = "Finds all students whose names contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.";
    public static final String MESSAGE_PARAMETERS = "KEYWORD [MORE_KEYWORDS]...";
    public static final String MESSAGE_EXAMPLE = COMMAND_WORD + " alice bob charlie";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": " + MESSAGE_DESCRIPTION + "\n"
            + "Parameters: " + MESSAGE_PARAMETERS + "\n"
            + "Example: " + MESSAGE_EXAMPLE;

    private final NameContainsKeywordsPredicate predicate;

    public FindCommand(NameContainsKeywordsPredicate predicate) {
        this.predicate = requireNonNull(predicate);
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredStudentList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_STUDENTS_LISTED_OVERVIEW, model.getFilteredStudentList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindCommand otherFindCommand)) {
            return false;
        }

        return predicate.equals(otherFindCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
