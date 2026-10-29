package seedu.address.logic.metadata;

import java.util.List;

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

/** Catalog of command metadata displayed on the Help page. */
public final class CommandMetadataCatalog {
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

    private CommandMetadataCatalog() {}

    /** Returns the command categories displayed on the Help page. */
    public static List<CommandCategory> allCommandCategories() {
        return COMMAND_CATEGORIES;
    }

    private static CommandMetadata command(
            String commandWord,
            String parameters,
            String description,
            String example) {
        return new CommandMetadata(commandWord, parameters, description, example);
    }
}
