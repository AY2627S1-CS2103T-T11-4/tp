package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.index.Index;

/**
 * Represents a Command which takes in just a single index to execute its hidden logic
 */
public abstract class SingleIndexCommand extends Command {
    protected final Index targetIndex;

    /**
     * Initialise the command with a single targetIndex index
     *
     * @param targetIndex the targetIndex index for the command
     */
    public SingleIndexCommand(Index targetIndex) {
        requireNonNull(targetIndex);
        this.targetIndex = targetIndex;
    }
}
