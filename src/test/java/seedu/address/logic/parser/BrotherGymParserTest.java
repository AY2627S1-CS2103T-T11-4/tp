package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNEXPECTED_PARAMETERS;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddExerciseCommand;
import seedu.address.logic.commands.AddPlanCommand;
import seedu.address.logic.commands.AddStudentCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteExerciseCommand;
import seedu.address.logic.commands.DeletePlanCommand;
import seedu.address.logic.commands.DeleteStudentCommand;
import seedu.address.logic.commands.EditExerciseCommand;
import seedu.address.logic.commands.EditExerciseCommand.EditExerciseDescriptor;
import seedu.address.logic.commands.EditPlanCommand;
import seedu.address.logic.commands.EditStudentCommand;
import seedu.address.logic.commands.EditStudentCommand.EditStudentDescriptor;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HomeCommand;
import seedu.address.logic.commands.MarkPlanCommand;
import seedu.address.logic.commands.UnmarkPlanCommand;
import seedu.address.logic.commands.ViewPlanCommand;
import seedu.address.logic.commands.ViewStudentCommand;
import seedu.address.logic.parser.exceptions.FieldValueInvalidException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.exercise.Exercise;
import seedu.address.model.exercise.ExerciseDescription;
import seedu.address.model.exercise.ExerciseName;
import seedu.address.model.student.NameContainsKeywordsPredicate;
import seedu.address.model.student.Student;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.testutil.EditPlanDescriptorBuilder;
import seedu.address.testutil.EditStudentDescriptorBuilder;
import seedu.address.testutil.StudentBuilder;
import seedu.address.testutil.StudentUtil;
import seedu.address.testutil.WorkoutBuilder;
import seedu.address.testutil.WorkoutUtil;

public class BrotherGymParserTest {

    private final BrotherGymParser parser = new BrotherGymParser();

    @Test
    public void parseCommand_addStudent() throws Exception {
        Student student = new StudentBuilder().build();
        AddStudentCommand command = (AddStudentCommand) parser.parseCommand(StudentUtil.getAddStudentCommand(student));
        assertEquals(new AddStudentCommand(student), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " \t ") instanceof ClearCommand);
        assertUnexpectedArgumentsRejected(ClearCommand.COMMAND_WORD, ClearCommand.MESSAGE_USAGE);
    }

    @Test
    public void parseCommand_deleteStudent() throws Exception {
        DeleteStudentCommand command = (DeleteStudentCommand) parser.parseCommand(
                DeleteStudentCommand.COMMAND_WORD + " " + INDEX_FIRST.getOneBased());
        assertEquals(new DeleteStudentCommand(INDEX_FIRST), command);
    }

    @Test
    public void parseCommand_editStudent() throws Exception {
        Student student = new StudentBuilder().build();
        EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder(student).build();
        EditStudentCommand command = (EditStudentCommand) parser.parseCommand(EditStudentCommand.COMMAND_WORD + " "
                + INDEX_FIRST.getOneBased() + " " + StudentUtil.getEditStudentDescriptorDetails(descriptor));
        assertEquals(new EditStudentCommand(INDEX_FIRST, descriptor), command);
    }

    @Test
    public void parseCommand_deletePlan() throws Exception {
        DeletePlanCommand command = (DeletePlanCommand) parser.parseCommand(
                DeletePlanCommand.COMMAND_WORD + " " + INDEX_FIRST.getOneBased());
        assertEquals(new DeletePlanCommand(INDEX_FIRST), command);
    }

    @Test
    public void parseCommand_deleteExercise() throws Exception {
        DeleteExerciseCommand command = (DeleteExerciseCommand) parser.parseCommand(
                DeleteExerciseCommand.COMMAND_WORD + " " + INDEX_FIRST.getOneBased());
        assertEquals(new DeleteExerciseCommand(INDEX_FIRST), command);
    }

    @Test
    public void parseCommand_deleteExerciseInvalidIndex_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parseCommand(DeleteExerciseCommand.COMMAND_WORD + " 0"));
        assertThrows(ParseException.class, () -> parser.parseCommand(DeleteExerciseCommand.COMMAND_WORD + " abc"));
    }

    @Test
    public void parseCommand_editExercise() throws Exception {
        EditExerciseDescriptor descriptor = new EditExerciseDescriptor();
        descriptor.setName(new ExerciseName("Front Squat"));

        EditExerciseCommand command = (EditExerciseCommand) parser.parseCommand(
                EditExerciseCommand.COMMAND_WORD + " 1 --name Front Squat");

        assertEquals(new EditExerciseCommand(INDEX_FIRST, descriptor), command);
    }

    @Test
    public void parseCommand_addPlan() throws Exception {
        WorkoutPlan workout = new WorkoutBuilder().build();
        AddPlanCommand command = (AddPlanCommand) parser.parseCommand(WorkoutUtil.getAddPlanCommand(workout));
        assertEquals(new AddPlanCommand(workout), command);
    }

    @Test
    public void parseCommand_addExercise() throws Exception {
        Exercise exercise = new Exercise(new ExerciseName("Squats"),
                new ExerciseDescription("Barbell back squat"));
        AddExerciseCommand command = (AddExerciseCommand) parser.parseCommand(
                "addExercise Squats --description Barbell back squat");
        assertEquals(new AddExerciseCommand(exercise), command);
    }

    @Test
    public void parseCommand_editPlan() throws Exception {
        WorkoutPlan plan = new WorkoutBuilder().build();
        EditPlanCommand.EditPlanDescriptor descriptor = new EditPlanDescriptorBuilder(plan).build();
        EditPlanCommand command = (EditPlanCommand) parser.parseCommand(EditPlanCommand.COMMAND_WORD + " "
                + INDEX_FIRST.getOneBased() + " " + WorkoutUtil.getEditPlanDescriptorDetails(descriptor));
        assertEquals(new EditPlanCommand(INDEX_FIRST, descriptor), command);
    }

    @Test
    public void parseCommand_markPlan() throws Exception {
        MarkPlanCommand command = (MarkPlanCommand) parser.parseCommand(
                MarkPlanCommand.COMMAND_WORD + " " + INDEX_FIRST.getOneBased());
        assertEquals(new MarkPlanCommand(INDEX_FIRST), command);
    }

    @Test
    public void parseCommand_unmarkPlan() throws Exception {
        UnmarkPlanCommand command = (UnmarkPlanCommand) parser.parseCommand(
                UnmarkPlanCommand.COMMAND_WORD + " " + INDEX_FIRST.getOneBased());
        assertEquals(new UnmarkPlanCommand(INDEX_FIRST), command);
    }

    @Test
    public void parseCommand_viewStudent() throws Exception {
        ViewStudentCommand command = (ViewStudentCommand) parser.parseCommand(
                ViewStudentCommand.COMMAND_WORD + " " + INDEX_FIRST.getOneBased());
        assertEquals(new ViewStudentCommand(INDEX_FIRST), command);
    }

    @Test
    public void parseCommand_viewPlan() throws Exception {
        ViewPlanCommand command = (ViewPlanCommand) parser.parseCommand(
                ViewPlanCommand.COMMAND_WORD + " " + INDEX_FIRST.getOneBased());
        assertEquals(new ViewPlanCommand(INDEX_FIRST), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " \t ") instanceof ExitCommand);
        assertUnexpectedArgumentsRejected(ExitCommand.COMMAND_WORD, ExitCommand.MESSAGE_USAGE);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = List.of("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_findInvalidKeywords_throwsParseException() {
        assertThrows(ParseException.class,
                Messages.formatParserError(FindCommand.MESSAGE_INVALID_KEYWORDS, FindCommand.MESSAGE_USAGE), ()
                    -> parser.parseCommand("find !!!"));
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " \t ") instanceof HelpCommand);
        assertUnexpectedArgumentsRejected(HelpCommand.COMMAND_WORD, HelpCommand.MESSAGE_USAGE);
    }

    @Test
    public void parseCommand_home() throws Exception {
        assertTrue(parser.parseCommand(HomeCommand.COMMAND_WORD) instanceof HomeCommand);
        assertTrue(parser.parseCommand(HomeCommand.COMMAND_WORD + " \t ") instanceof HomeCommand);
        assertUnexpectedArgumentsRejected(HomeCommand.COMMAND_WORD, HomeCommand.MESSAGE_USAGE);
    }

    private void assertUnexpectedArgumentsRejected(String commandWord, String usage) {
        for (String arguments : List.of(" 1", " arbitrary text", " n/Alice", " \t 1 \t ")) {
            assertThrows(FieldValueInvalidException.class,
                    Messages.formatParserError(MESSAGE_UNEXPECTED_PARAMETERS, usage), ()
                    -> parser.parseCommand(commandWord + arguments));
        }
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
