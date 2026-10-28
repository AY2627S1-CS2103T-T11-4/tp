package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class HelpSearchTest {
    @Test
    void blankQuery_returnsAllCommandsInCatalogOrder() {
        HelpSearch search = new HelpSearch();

        assertEquals(List.of("home", "help", "clear", "exit", "addStudent", "viewStudent",
                "editStudent", "deleteStudent", "find", "addPlan", "viewPlan", "editPlan",
                "deletePlan", "markPlan", "unmarkPlan"), commandWords(search.getMatchingCommands()));
    }

    @Test
    void query_isCaseInsensitiveAndSupportsPartialCommandWords() {
        HelpSearch search = new HelpSearch();
        search.setQuery("ADD");

        assertEquals(List.of("addStudent", "addPlan"), commandWords(search.getMatchingCommands()));
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

    private static List<String> commandWords(List<HelpSearch.CommandReference> commands) {
        return commands.stream().map(HelpSearch.CommandReference::commandWord).toList();
    }
}
