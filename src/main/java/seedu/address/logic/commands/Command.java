package seedu.address.logic.commands;

import java.util.Optional;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.student.Student;

/**
 * Represents a command with hidden internal logic and the ability to be executed.
 */
public abstract class Command {

    /** Receives the student currently selected by the Logic layer when relevant. */
    public void setCurrentStudent(Student student) {
        // Commands that operate on the current student may override this method.
    }

    /** Returns the student that should become current after successful execution, if any. */
    public Optional<Student> getCurrentStudent() {
        return Optional.empty();
    }

    /**
     * Executes the command and returns the result message.
     *
     * @param model {@code Model} which the command should operate on.
     * @return feedback message of the operation result for display
     * @throws CommandException If an error occurs during command execution.
     */
    public abstract CommandResult execute(Model model) throws CommandException;

}
