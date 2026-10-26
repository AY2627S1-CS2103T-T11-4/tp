package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;
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

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String USERGUIDE_URL =
            "https://ay2627s1-cs2103t-t11-4.github.io/tp/UserGuide.html#command-summary";

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

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

    @FXML
    private VBox commandSections;

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public HelpWindow(Stage root) {
        super(FXML, requireNonNull(root));
        populateCommandSections();
    }

    /**
     * Creates a new HelpWindow.
     */
    public HelpWindow() {
        this(new Stage());
    }

    /**
     * Shows the help window.
     * @throws IllegalStateException
     *     <ul>
     *         <li>
     *             if this method is called on a thread other than the JavaFX Application Thread.
     *         </li>
     *         <li>
     *             if this method is called during animation or layout processing.
     *         </li>
     *         <li>
     *             if this method is called on the primary stage.
     *         </li>
     *         <li>
     *             if {@code dialogStage} is already showing.
     *         </li>
     *     </ul>
     */
    public void show() {
        logger.fine("Showing help page about the application.");
        getRoot().show();
        getRoot().centerOnScreen();
        focus();
    }

    /**
     * Returns true if the help window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the help window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the help window.
     */
    public void focus() {
        getRoot().setIconified(false);
        getRoot().toFront();
        getRoot().requestFocus();
    }

    /** Opens the user guide in the user's default browser. */
    @FXML
    private void openUserGuide() {
        try {
            if (!Desktop.isDesktopSupported()) {
                logger.warning("Opening the user guide is not supported on this platform.");
                return;
            }

            Desktop desktop = Desktop.getDesktop();
            if (!desktop.isSupported(Desktop.Action.BROWSE)) {
                logger.warning("Opening the user guide is not supported on this platform.");
                return;
            }

            desktop.browse(URI.create(USERGUIDE_URL));
        } catch (IOException | UnsupportedOperationException | SecurityException e) {
            logger.warning("Unable to open the user guide: " + e.getMessage());
        }
    }

    private void populateCommandSections() {
        commandSections.getChildren().clear();
        COMMAND_CATEGORIES.stream()
                .map(HelpWindow::createCommandCategory)
                .forEach(commandSections.getChildren()::add);
    }

    private static VBox createCommandCategory(CommandCategory category) {
        Label categoryTitle = new Label(category.title());
        categoryTitle.getStyleClass().add("category-title");

        VBox commandCards = new VBox(10);
        commandCards.getStyleClass().add("command-cards");
        category.commands().stream()
                .map(HelpWindow::createCommandCard)
                .forEach(commandCards.getChildren()::add);

        VBox categoryBox = new VBox(9, categoryTitle, commandCards);
        categoryBox.getStyleClass().add("command-category");
        return categoryBox;
    }

    private static VBox createCommandCard(CommandReference command) {
        Label commandFormat = new Label(command.format());
        commandFormat.setWrapText(true);
        commandFormat.setMaxWidth(Double.MAX_VALUE);
        commandFormat.getStyleClass().add("command-format");

        Label description = new Label("Description: " + command.description());
        description.setWrapText(true);
        description.setMaxWidth(Double.MAX_VALUE);
        description.getStyleClass().add("command-description");

        Label exampleLabel = new Label("Example usage:");
        exampleLabel.getStyleClass().add("example-label");

        Label example = new Label(command.example());
        example.setWrapText(true);
        example.setMaxWidth(Double.MAX_VALUE);
        example.getStyleClass().add("command-example");

        VBox exampleBlock = new VBox(4, exampleLabel, example);
        exampleBlock.setMaxWidth(Double.MAX_VALUE);
        exampleBlock.getStyleClass().add("example-block");

        VBox card = new VBox(7, commandFormat, description, exampleBlock);
        card.setMaxWidth(Double.MAX_VALUE);
        card.getStyleClass().add("command-card");
        return card;
    }

    private static CommandReference command(String commandWord, String parameters,
                                            String description, String example) {
        return new CommandReference(commandWord, parameters, description, example);
    }

    private record CommandCategory(String title, List<CommandReference> commands) {}

    private record CommandReference(String commandWord, String parameters,
                                     String description, String example) {
        String format() {
            return parameters.isBlank() ? commandWord : commandWord + " " + parameters;
        }
    }
}
