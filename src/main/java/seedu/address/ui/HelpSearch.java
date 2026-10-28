package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Locale;

import seedu.address.logic.commands.AddPlanCommand;
import seedu.address.logic.commands.AddStudentCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeletePlanCommand;
import seedu.address.logic.commands.DeleteStudentCommand;
import seedu.address.logic.commands.EditPlanCommand;
import seedu.address.logic.commands.EditStudentCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HomeCommand;
import seedu.address.logic.commands.MarkPlanCommand;
import seedu.address.logic.commands.UnmarkPlanCommand;
import seedu.address.logic.commands.ViewPlanCommand;
import seedu.address.logic.commands.ViewStudentCommand;

/** Provides the Help window's command catalog and command-word search behavior. */
final class HelpSearch {
    private static final List<CommandCategory> COMMAND_CATEGORIES = List.of(
            new CommandCategory("Common commands", List.of(
                    command(HomeCommand.COMMAND_WORD, HomeCommand.MESSAGE_PARAMETERS,
                            HomeCommand.MESSAGE_DESCRIPTION, HomeCommand.MESSAGE_EXAMPLE),
                    command(HelpCommand.COMMAND_WORD, HelpCommand.MESSAGE_PARAMETERS,
                            HelpCommand.MESSAGE_DESCRIPTION, HelpCommand.MESSAGE_EXAMPLE),
                    command(ClearCommand.COMMAND_WORD, ClearCommand.MESSAGE_PARAMETERS,
                            ClearCommand.MESSAGE_DESCRIPTION, ClearCommand.MESSAGE_EXAMPLE),
                    command(ExitCommand.COMMAND_WORD, ExitCommand.MESSAGE_PARAMETERS,
                            ExitCommand.MESSAGE_DESCRIPTION, ExitCommand.MESSAGE_EXAMPLE))),
            new CommandCategory("Student commands", List.of(
                    command(AddStudentCommand.COMMAND_WORD, AddStudentCommand.MESSAGE_PARAMETERS,
                            AddStudentCommand.MESSAGE_DESCRIPTION, AddStudentCommand.MESSAGE_EXAMPLE),
                    command(ViewStudentCommand.COMMAND_WORD, ViewStudentCommand.MESSAGE_PARAMETERS,
                            ViewStudentCommand.MESSAGE_DESCRIPTION, ViewStudentCommand.MESSAGE_EXAMPLE),
                    command(EditStudentCommand.COMMAND_WORD, EditStudentCommand.MESSAGE_PARAMETERS,
                            EditStudentCommand.MESSAGE_DESCRIPTION, EditStudentCommand.MESSAGE_EXAMPLE),
                    command(DeleteStudentCommand.COMMAND_WORD, DeleteStudentCommand.MESSAGE_PARAMETERS,
                            DeleteStudentCommand.MESSAGE_DESCRIPTION, DeleteStudentCommand.MESSAGE_EXAMPLE),
                    command(FindCommand.COMMAND_WORD, FindCommand.MESSAGE_PARAMETERS,
                            FindCommand.MESSAGE_DESCRIPTION, FindCommand.MESSAGE_EXAMPLE))),
            new CommandCategory("Workout plan commands", List.of(
                    command(AddPlanCommand.COMMAND_WORD, AddPlanCommand.MESSAGE_PARAMETERS,
                            AddPlanCommand.MESSAGE_DESCRIPTION, AddPlanCommand.MESSAGE_EXAMPLE),
                    command(ViewPlanCommand.COMMAND_WORD, ViewPlanCommand.MESSAGE_PARAMETERS,
                            ViewPlanCommand.MESSAGE_DESCRIPTION, ViewPlanCommand.MESSAGE_EXAMPLE),
                    command(EditPlanCommand.COMMAND_WORD, EditPlanCommand.MESSAGE_PARAMETERS,
                            EditPlanCommand.MESSAGE_DESCRIPTION, EditPlanCommand.MESSAGE_EXAMPLE),
                    command(DeletePlanCommand.COMMAND_WORD, DeletePlanCommand.MESSAGE_PARAMETERS,
                            DeletePlanCommand.MESSAGE_DESCRIPTION, DeletePlanCommand.MESSAGE_EXAMPLE),
                    command(MarkPlanCommand.COMMAND_WORD, MarkPlanCommand.MESSAGE_PARAMETERS,
                            MarkPlanCommand.MESSAGE_DESCRIPTION, MarkPlanCommand.MESSAGE_EXAMPLE),
                    command(UnmarkPlanCommand.COMMAND_WORD, UnmarkPlanCommand.MESSAGE_PARAMETERS,
                            UnmarkPlanCommand.MESSAGE_DESCRIPTION, UnmarkPlanCommand.MESSAGE_EXAMPLE))));

    private String normalizedQuery = "";

    List<CommandCategory> getCategories() {
        return COMMAND_CATEGORIES;
    }

    void setQuery(String query) {
        normalizedQuery = requireNonNull(query).trim().toLowerCase(Locale.ROOT);
    }

    boolean matches(CommandReference command) {
        return normalizedQuery.isEmpty()
                || command.commandWord().toLowerCase(Locale.ROOT).contains(normalizedQuery);
    }

    List<CommandReference> getMatchingCommands() {
        return COMMAND_CATEGORIES.stream()
                .flatMap(category -> category.commands().stream())
                .filter(this::matches)
                .toList();
    }

    private static CommandReference command(String commandWord, String parameters,
                                            String description, String example) {
        return new CommandReference(commandWord, parameters, description, example);
    }

    /** A category of commands shown in the Help window. */
    record CommandCategory(String title, List<CommandReference> commands) {}

    /** Metadata used to render and search a command reference. */
    record CommandReference(String commandWord, String parameters, String description, String example) {
        String format() {
            return parameters.isBlank() ? commandWord : commandWord + " " + parameters;
        }
    }
}
