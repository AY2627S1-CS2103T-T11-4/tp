package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
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

    private final Consumer<String> commandFormatHandler;
    private final List<CommandCard> commandCards = new ArrayList<>();
    private final List<CommandCategoryView> commandCategoryViews = new ArrayList<>();
    private final Label noResultsLabel = new Label("No commands found.");

    @FXML
    private VBox commandSections;

    @FXML
    private TextField commandSearchField;

    @FXML
    private ScrollPane helpScroll;

    @FXML
    private Button userGuideButton;

    @FXML
    private Button closeHelpButton;

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     * @param commandFormatHandler handler for inserting a command format into the main command box
     */
    public HelpWindow(Stage root, Consumer<String> commandFormatHandler) {
        super(FXML, requireNonNull(root));
        this.commandFormatHandler = requireNonNull(commandFormatHandler);
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPressed);
        commandSearchField.textProperty().addListener((observable, oldValue, newValue) -> filterCommands(newValue));
        getRoot().focusedProperty().addListener((observable, wasFocused, isFocused) -> {
            if (isFocused) {
                Platform.runLater(commandSearchField::requestFocus);
            }
        });
        populateCommandSections();
    }

    /**
     * Creates a new HelpWindow using a new stage.
     *
     * @param commandFormatHandler handler for inserting a command format into the main command box
     */
    public HelpWindow(Consumer<String> commandFormatHandler) {
        this(new Stage(), commandFormatHandler);
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
        Platform.runLater(commandSearchField::requestFocus);
    }

    /** Closes the Help window. */
    @FXML
    private void closeHelp() {
        hide();
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
        commandCards.clear();
        commandCategoryViews.clear();
        noResultsLabel.getStyleClass().add("no-results-label");
        noResultsLabel.setManaged(false);
        noResultsLabel.setVisible(false);
        commandSections.getChildren().add(noResultsLabel);
        COMMAND_CATEGORIES.stream()
                .map(this::createCommandCategory)
                .forEach(commandCategoryView -> {
                    commandCategoryViews.add(commandCategoryView);
                    commandSections.getChildren().add(commandCategoryView.categoryBox());
                });
    }

    private CommandCategoryView createCommandCategory(CommandCategory category) {
        Label categoryTitle = new Label(category.title());
        categoryTitle.getStyleClass().add("category-title");

        VBox commandCardsBox = new VBox(10);
        commandCardsBox.getStyleClass().add("command-cards");
        List<CommandCard> categoryCards = new ArrayList<>();
        category.commands().stream()
                .map(this::createCommandCard)
                .forEach(commandCard -> {
                    this.commandCards.add(commandCard);
                    categoryCards.add(commandCard);
                    commandCardsBox.getChildren().add(commandCard.card());
                });

        VBox categoryBox = new VBox(9, categoryTitle, commandCardsBox);
        categoryBox.getStyleClass().add("command-category");
        return new CommandCategoryView(categoryBox, categoryCards);
    }

    private CommandCard createCommandCard(CommandReference command) {
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

        Button useFormatButton = new Button("Use Command");
        useFormatButton.setMnemonicParsing(false);
        useFormatButton.setOnAction(event -> commandFormatHandler.accept(command.format()));
        useFormatButton.getStyleClass().add("use-format-button");

        HBox actionRow = new HBox(useFormatButton);
        actionRow.setAlignment(Pos.CENTER_RIGHT);
        actionRow.setMaxWidth(Double.MAX_VALUE);

        VBox card = new VBox(7, commandFormat, description, exampleBlock, actionRow);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setFocusTraversable(true);
        card.getStyleClass().add("command-card");
        return new CommandCard(command, card, useFormatButton);
    }

    private void handleKeyPressed(KeyEvent event) {
        List<CommandCard> visibleCards = getVisibleCommandCards();
        int focusedCardIndex = getFocusedCardIndex(visibleCards);
        boolean isForwardNavigation = event.getCode() == KeyCode.DOWN
                || (event.getCode() == KeyCode.TAB && !event.isShiftDown());
        boolean isBackwardNavigation = event.getCode() == KeyCode.UP
                || (event.getCode() == KeyCode.TAB && event.isShiftDown());

        if (isForwardNavigation || isBackwardNavigation) {
            moveFocus(isForwardNavigation);
            event.consume();
        } else if (focusedCardIndex >= 0 && event.getCode() == KeyCode.ENTER) {
            Button useCommandButton = visibleCards.get(focusedCardIndex).useCommandButton();
            if (useCommandButton.isFocused()) {
                useCommandButton.fire();
                event.consume();
            } else if (isCardFocused(focusedCardIndex)) {
                useCommandButton.requestFocus();
                event.consume();
            }
        } else if (closeHelpButton.isFocused() && event.getCode() == KeyCode.ENTER) {
            closeHelpButton.fire();
            event.consume();
        }
    }

    private void moveFocus(boolean forward) {
        List<CommandCard> visibleCards = getVisibleCommandCards();
        int focusedCardIndex = getFocusedCardIndex(visibleCards);

        if (focusedCardIndex >= 0) {
            if (forward && focusedCardIndex == visibleCards.size() - 1) {
                commandSearchField.requestFocus();
            } else if (!forward && focusedCardIndex == 0) {
                closeHelpButton.requestFocus();
            } else {
                focusCard(focusedCardIndex + (forward ? 1 : -1));
            }
        } else if (closeHelpButton.isFocused()) {
            if (forward) {
                focusCard(0);
            } else {
                commandSearchField.requestFocus();
            }
        } else if (commandSearchField.isFocused()) {
            if (forward) {
                closeHelpButton.requestFocus();
            } else {
                focusCard(visibleCards.size() - 1);
            }
        }
    }

    private int getFocusedCardIndex(List<CommandCard> visibleCards) {
        for (int i = 0; i < visibleCards.size(); i++) {
            CommandCard commandCard = visibleCards.get(i);
            if (commandCard.card().isFocused() || commandCard.useCommandButton().isFocused()) {
                return i;
            }
        }
        return -1;
    }

    private boolean isCardFocused(int index) {
        return getVisibleCommandCards().get(index).card().isFocused();
    }

    private void focusCard(int index) {
        List<CommandCard> visibleCards = getVisibleCommandCards();
        if (visibleCards.isEmpty()) {
            commandSearchField.requestFocus();
            return;
        }

        int boundedIndex = Math.max(0, Math.min(index, visibleCards.size() - 1));
        VBox card = visibleCards.get(boundedIndex).card();
        card.requestFocus();
        Platform.runLater(() -> scrollCardIntoView(card));
    }

    private List<CommandCard> getVisibleCommandCards() {
        return commandCards.stream()
                .filter(commandCard -> commandCard.card().isVisible())
                .toList();
    }

    private void filterCommands(String query) {
        String normalizedQuery = query.trim().toLowerCase(Locale.ROOT);
        boolean hasVisibleCommand = false;

        for (CommandCategoryView categoryView : commandCategoryViews) {
            boolean hasVisibleCard = false;
            for (CommandCard commandCard : categoryView.cards()) {
                boolean isVisible = normalizedQuery.isEmpty()
                        || commandCard.command().commandWord().toLowerCase(Locale.ROOT).contains(normalizedQuery);
                commandCard.card().setVisible(isVisible);
                commandCard.card().setManaged(isVisible);
                hasVisibleCard |= isVisible;
            }
            categoryView.categoryBox().setVisible(hasVisibleCard);
            categoryView.categoryBox().setManaged(hasVisibleCard);
            hasVisibleCommand |= hasVisibleCard;
        }

        noResultsLabel.setVisible(!hasVisibleCommand);
        noResultsLabel.setManaged(!hasVisibleCommand);

        if (!commandSearchField.isFocused() && getFocusedCardIndex(getVisibleCommandCards()) < 0) {
            focusCard(0);
        }
    }

    private void scrollCardIntoView(VBox card) {
        double contentHeight = commandSections.getHeight();
        double viewportHeight = helpScroll.getViewportBounds().getHeight();
        double maximumScroll = contentHeight - viewportHeight;
        if (maximumScroll <= 0) {
            return;
        }

        double contentTop = commandSections.localToScene(commandSections.getBoundsInLocal()).getMinY();
        double viewportTop = helpScroll.localToScene(helpScroll.getViewportBounds()).getMinY();
        double cardTop = card.localToScene(card.getBoundsInLocal()).getMinY() - contentTop;
        double cardBottom = card.localToScene(card.getBoundsInLocal()).getMaxY() - contentTop;
        double visibleTop = viewportTop - contentTop;
        double visibleBottom = visibleTop + viewportHeight;
        double targetScroll = helpScroll.getVvalue() * maximumScroll;

        if (cardTop < visibleTop) {
            targetScroll = cardTop;
        } else if (cardBottom > visibleBottom) {
            targetScroll = cardBottom - viewportHeight;
        }

        helpScroll.setVvalue(Math.max(0, Math.min(1, targetScroll / maximumScroll)));
    }

    private static CommandReference command(String commandWord, String parameters,
                                            String description, String example) {
        return new CommandReference(commandWord, parameters, description, example);
    }

    private record CommandCard(CommandReference command, VBox card, Button useCommandButton) {}

    private record CommandCategoryView(VBox categoryBox, List<CommandCard> cards) {}

    private record CommandCategory(String title, List<CommandReference> commands) {}

    private record CommandReference(String commandWord, String parameters,
                                     String description, String example) {
        String format() {
            return parameters.isBlank() ? commandWord : commandWord + " " + parameters;
        }
    }
}
