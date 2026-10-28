package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class HelpInputTest {
    private final HelpSearch helpSearch = new HelpSearch();

    @Test
    void build_addStudent_preservesFieldOrderAndRepeatedTags() {
        HelpSearch.CommandReference command = findCommand("addStudent");

        HelpInput.Result result = HelpInput.build(command, List.of(
                List.of("John Doe"), List.of("98765432"), List.of("john@example.com"),
                List.of("311, Clementi Ave 2, #02-25"), List.of("friends", "owesMoney")));

        assertTrue(result.isSuccess());
        assertEquals("addStudent John Doe --hp 98765432 --email john@example.com "
                + "--addr 311, Clementi Ave 2, #02-25 --tag friends --tag owesMoney", result.command());
    }

    @Test
    void build_addPlan_preservesFieldOrderAndSpaces() {
        HelpSearch.CommandReference command = findCommand("addPlan");

        HelpInput.Result result = HelpInput.build(command, List.of(
                List.of("Upper Body Strength"), List.of("Chest and shoulders"), List.of("08102025"),
                List.of("strength", "upper body")));

        assertTrue(result.isSuccess());
        assertEquals("addPlan Upper Body Strength --plan Chest and shoulders --date 08102025 "
                + "--tag strength --tag upper body", result.command());
    }

    @Test
    void build_editStudent_omitsBlankOptionalFields() {
        HelpSearch.CommandReference command = findCommand("editStudent");

        HelpInput.Result result = HelpInput.build(command, List.of(
                List.of("1"), List.of(""), List.of("91234567"), List.of(""), List.of(""), List.of("")));

        assertTrue(result.isSuccess());
        assertEquals("editStudent 1 --hp 91234567", result.command());
    }

    @Test
    void build_editPlan_requiresAtLeastOneUpdateField() {
        HelpSearch.CommandReference command = findCommand("editPlan");

        HelpInput.Result result = HelpInput.build(command, List.of(
                List.of("1"), List.of(""), List.of(""), List.of(""), List.of("")));

        assertFalse(result.isSuccess());
        assertEquals("At least one field to update is required.", result.error());
    }

    @Test
    void build_rejectsBlankRequiredFields() {
        HelpSearch.CommandReference command = findCommand("viewStudent");

        HelpInput.Result result = HelpInput.build(command, List.of(List.of("   ")));

        assertFalse(result.isSuccess());
        assertEquals("Student ID is required.", result.error());
    }

    @Test
    void build_noParameterCommand_returnsCommandWord() {
        HelpSearch.CommandReference command = findCommand("help");

        HelpInput.Result result = HelpInput.build(command, List.of());

        assertTrue(result.isSuccess());
        assertEquals("help", result.command());
    }

    private HelpSearch.CommandReference findCommand(String commandWord) {
        return helpSearch.getMatchingCommands().stream()
                .filter(command -> command.commandWord().equals(commandWord))
                .findFirst()
                .orElseThrow();
    }
}
