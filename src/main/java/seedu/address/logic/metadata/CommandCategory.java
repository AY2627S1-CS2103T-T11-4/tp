package seedu.address.logic.metadata;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;

/** A category of commands displayed on the Help page. */
public record CommandCategory(
        String title,
        List<CommandMetadata> commands) {

    /** Creates a command category. */
    public CommandCategory {
        requireAllNonNull(title, commands);
        commands = List.copyOf(commands);
    }
}
