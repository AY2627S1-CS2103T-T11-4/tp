package seedu.address.logic.metadata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddExerciseCommand;
import seedu.address.logic.commands.AddPlanCommand;
import seedu.address.logic.commands.AddStudentCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteExerciseCommand;
import seedu.address.logic.commands.DeletePlanCommand;
import seedu.address.logic.commands.DeleteStudentCommand;
import seedu.address.logic.commands.EditExerciseCommand;
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

public class CommandMetadataTest {

    @Test
    public void format_withoutParameters_returnsCommandWord() {
        CommandMetadata metadata = new CommandMetadata("help", "", "description", "help");

        assertEquals("help", metadata.format());
    }

    @Test
    public void format_withParameters_returnsCommandAndParameters() {
        CommandMetadata metadata = new CommandMetadata("find", "KEYWORD...", "description", "find alice");

        assertEquals("find KEYWORD...", metadata.format());
    }

    @Test
    public void constructor_withNullValue_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new CommandMetadata(null, "", "description", "example"));
        assertThrows(NullPointerException.class, () ->
                new CommandMetadata("help", null, "description", "example"));
        assertThrows(NullPointerException.class, () ->
                new CommandMetadata("help", "", null, "example"));
        assertThrows(NullPointerException.class, () ->
                new CommandMetadata("help", "", "description", null));
    }

    @Test
    public void category_commands_areDefensivelyCopied() {
        List<CommandMetadata> commands = List.of(
                new CommandMetadata("help", "", "description", "help"));
        CommandCategory category = new CommandCategory("Common commands", commands);

        assertEquals(commands, category.commands());
        assertThrows(UnsupportedOperationException.class, () ->
                category.commands().add(commands.get(0)));
    }

    @Test
    public void category_withNullValue_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new CommandCategory(null, List.of()));
        assertThrows(NullPointerException.class, () ->
                new CommandCategory("Common commands", null));
    }

    @Test
    public void catalog_containsExpectedCategoriesAndCommands() {
        List<CommandCategory> categories = CommandMetadataCatalog.allCommandCategories();

        assertEquals(List.of("Common commands", "Student commands", "Workout plan commands", "Exercise commands"),
                categories.stream().map(CommandCategory::title).toList());
        assertEquals(List.of(
                        HomeCommand.COMMAND_WORD,
                        HelpCommand.COMMAND_WORD,
                        ClearCommand.COMMAND_WORD,
                        ExitCommand.COMMAND_WORD),
                commandWords(categories.get(0)));
        assertEquals(List.of(
                        AddStudentCommand.COMMAND_WORD,
                        ViewStudentCommand.COMMAND_WORD,
                        EditStudentCommand.COMMAND_WORD,
                        DeleteStudentCommand.COMMAND_WORD,
                        FindCommand.COMMAND_WORD),
                commandWords(categories.get(1)));
        assertEquals(List.of(
                        AddPlanCommand.COMMAND_WORD,
                        ViewPlanCommand.COMMAND_WORD,
                        EditPlanCommand.COMMAND_WORD,
                        DeletePlanCommand.COMMAND_WORD,
                        MarkPlanCommand.COMMAND_WORD,
                        UnmarkPlanCommand.COMMAND_WORD),
                commandWords(categories.get(2)));
        assertEquals(List.of(
                        AddExerciseCommand.COMMAND_WORD,
                        EditExerciseCommand.COMMAND_WORD,
                        DeleteExerciseCommand.COMMAND_WORD),
                commandWords(categories.get(3)));

        CommandMetadata addPlan = categories.get(2).commands().get(0);
        assertEquals(AddPlanCommand.MESSAGE_PARAMETERS, addPlan.parameters());
        assertEquals(AddPlanCommand.MESSAGE_DESCRIPTION, addPlan.description());
        assertEquals(AddPlanCommand.MESSAGE_EXAMPLE, addPlan.example());
        assertEquals(AddPlanCommand.COMMAND_WORD + " " + AddPlanCommand.MESSAGE_PARAMETERS,
                addPlan.format());
    }

    private static List<String> commandWords(CommandCategory category) {
        return category.commands().stream()
                .map(CommandMetadata::commandWord)
                .toList();
    }
}
