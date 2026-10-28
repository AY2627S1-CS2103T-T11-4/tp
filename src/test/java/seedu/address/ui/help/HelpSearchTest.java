package seedu.address.ui.help;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddStudentCommand;
import seedu.address.logic.commands.EditPlanCommand;
import seedu.address.logic.commands.EditStudentCommand;
import seedu.address.logic.commands.HomeCommand;

class HelpSearchTest {
    @Test
    void blankQuery_returnsAllCommandsInCatalogOrder() {
        HelpSearch search = new HelpSearch();

        assertEquals(List.of("home", "help", "clear", "exit", "addStudent", "viewStudent",
                "editStudent", "deleteStudent", "find", "addPlan", "viewPlan", "editPlan",
                "deletePlan", "markPlan", "unmarkPlan"), commandWords(search.getMatchingCommands()));
    }

    @Test
    void categories_areReturnedInExpectedOrder() {
        HelpSearch search = new HelpSearch();

        assertEquals(List.of("Common commands", "Student commands", "Workout plan commands"),
                search.getCategories().stream().map(HelpSearch.CommandCategory::title).toList());
        assertEquals(List.of(
                List.of("home", "help", "clear", "exit"),
                List.of("addStudent", "viewStudent", "editStudent", "deleteStudent", "find"),
                List.of("addPlan", "viewPlan", "editPlan", "deletePlan", "markPlan", "unmarkPlan")),
                search.getCategories().stream()
                        .map(category -> commandWords(category.commands()))
                        .toList());
    }

    @Test
    void query_isCaseInsensitiveAndSupportsPartialCommandWords() {
        HelpSearch search = new HelpSearch();
        search.setQuery("ADD");

        assertEquals(List.of("addStudent", "addPlan"), commandWords(search.getMatchingCommands()));

        search.setQuery("plan");
        assertEquals(List.of("addPlan", "viewPlan", "editPlan", "deletePlan", "markPlan", "unmarkPlan"),
                commandWords(search.getMatchingCommands()));
    }

    @Test
    void blankAndWhitespaceQueries_returnAllCommands() {
        HelpSearch search = new HelpSearch();
        List<String> allCommands = commandWords(search.getMatchingCommands());

        search.setQuery("   ");

        assertEquals(allCommands, commandWords(search.getMatchingCommands()));
    }

    @Test
    void query_matchesOnlyCommandWords() {
        HelpSearch search = new HelpSearch();
        search.setQuery("student");

        assertEquals(List.of("addStudent", "viewStudent", "editStudent", "deleteStudent"),
                commandWords(search.getMatchingCommands()));

        search.setQuery("description");
        assertTrue(search.getMatchingCommands().isEmpty());
    }

    @Test
    void queryWithNoMatches_returnsEmptyList() {
        HelpSearch search = new HelpSearch();
        search.setQuery("does-not-exist");

        assertTrue(search.getMatchingCommands().isEmpty());
    }

    @Test
    void commandReferences_exposeStructuredFieldsInParserOrder() {
        HelpSearch search = new HelpSearch();

        assertEquals(List.of("Name", "Phone", "Email", "Address", "Tags"),
                fieldLabels(findCommand(search, "addStudent")));
        assertEquals(List.of("Student ID", "Name", "Phone", "Email", "Address", "Tags"),
                fieldLabels(findCommand(search, "editStudent")));
        assertEquals(List.of("Plan title", "Description", "Date", "Tags"),
                fieldLabels(findCommand(search, "addPlan")));
        assertEquals(List.of("Plan ID", "Plan name", "Description", "Date", "Tags"),
                fieldLabels(findCommand(search, "editPlan")));
        assertTrue(findCommand(search, "home").fields().isEmpty());
    }

    @Test
    void commandReference_format_combinesWordAndParameters() {
        HelpSearch search = new HelpSearch();

        assertEquals(AddStudentCommand.COMMAND_WORD + " " + AddStudentCommand.MESSAGE_PARAMETERS,
                findCommand(search, AddStudentCommand.COMMAND_WORD).format());
        assertEquals(EditStudentCommand.COMMAND_WORD + " " + EditStudentCommand.MESSAGE_PARAMETERS,
                findCommand(search, EditStudentCommand.COMMAND_WORD).format());
        assertEquals(EditPlanCommand.COMMAND_WORD + " " + EditPlanCommand.MESSAGE_PARAMETERS,
                findCommand(search, EditPlanCommand.COMMAND_WORD).format());
        assertEquals(HomeCommand.COMMAND_WORD, findCommand(search, HomeCommand.COMMAND_WORD).format());
    }

    private static List<String> commandWords(List<HelpSearch.CommandReference> commands) {
        return commands.stream().map(HelpSearch.CommandReference::commandWord).toList();
    }

    private static HelpSearch.CommandReference findCommand(HelpSearch search, String commandWord) {
        return search.getMatchingCommands().stream()
                .filter(command -> command.commandWord().equals(commandWord))
                .findFirst()
                .orElseThrow();
    }

    private static List<String> fieldLabels(HelpSearch.CommandReference command) {
        return command.fields().stream().map(HelpSearch.CommandField::label).toList();
    }
}
