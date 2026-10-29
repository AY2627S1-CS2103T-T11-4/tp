package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_STUDENTS;

import seedu.address.model.Model;

/**
 * Lists all students in the student list to the user.
 */
public class HomeCommand extends Command {

    public static final String COMMAND_WORD = "home";

    public static final String MESSAGE_DESCRIPTION = "Lists all students.";
    public static final String MESSAGE_PARAMETERS = "";
    public static final String MESSAGE_EXAMPLE = COMMAND_WORD;
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": " + MESSAGE_DESCRIPTION + "\n"
            + "Example: " + MESSAGE_EXAMPLE;

    public static final String MESSAGE_SUCCESS = "Listed all students.";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
        model.clearCurrentStudent();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
