package seedu.address.logic.metadata;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

/** Metadata used to display a command on the Help page. */
public record CommandMetadata(
        String commandWord,
        String parameters,
        String description,
        String example) {

    /** Creates metadata for a command. */
    public CommandMetadata {
        requireAllNonNull(commandWord, parameters, description, example);
    }

    /** Returns the command word followed by its parameters. */
    public String format() {
        return parameters.isBlank()
                ? commandWord
                : commandWord + " " + parameters;
    }
}
