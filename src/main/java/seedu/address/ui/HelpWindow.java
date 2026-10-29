package seedu.address.ui;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.metadata.CommandCategory;
import seedu.address.logic.metadata.CommandMetadata;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String USERGUIDE_URL =
            "https://ay2627s1-cs2103t-t11-4.github.io/tp/UserGuide.html#command-summary";

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    private final List<CommandCategory> commandCategories;
    private final Consumer<String> commandFormatHandler;
    private final List<CommandCard> commandCards = new ArrayList<>();

    @FXML
    private VBox commandSections;

    @FXML
    private ScrollPane helpScroll;

    @FXML
    private Button userGuideButton;

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     * @param commandCategories categories and commands to display
     * @param commandFormatHandler handler for inserting a command format into the main command box
     */
    public HelpWindow(Stage root, List<CommandCategory> commandCategories,
                      Consumer<String> commandFormatHandler) {
        super(FXML, requireNonNull(root));
        requireAllNonNull(commandCategories, commandFormatHandler);
        this.commandCategories = List.copyOf(commandCategories);
        this.commandFormatHandler = commandFormatHandler;
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPressed);
        populateCommandSections();
    }

    /**
     * Creates a new HelpWindow using a new stage.
     *
     * @param commandCategories categories and commands to display
     * @param commandFormatHandler handler for inserting a command format into the main command box
     */
    public HelpWindow(List<CommandCategory> commandCategories,
                      Consumer<String> commandFormatHandler) {
        this(new Stage(), commandCategories, commandFormatHandler);
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
        focusCard(0);
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
        commandCategories.stream()
                .map(this::createCommandCategory)
                .forEach(commandSections.getChildren()::add);
    }

    private VBox createCommandCategory(CommandCategory category) {
        Label categoryTitle = new Label(category.title());
        categoryTitle.getStyleClass().add("category-title");

        VBox commandCards = new VBox(10);
        commandCards.getStyleClass().add("command-cards");
        category.commands().stream()
                .map(this::createCommandCard)
                .forEach(commandCard -> {
                    this.commandCards.add(commandCard);
                    commandCards.getChildren().add(commandCard.card());
                });

        VBox categoryBox = new VBox(9, categoryTitle, commandCards);
        categoryBox.getStyleClass().add("command-category");
        return categoryBox;
    }

    private CommandCard createCommandCard(CommandMetadata command) {
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
        return new CommandCard(card, useFormatButton);
    }

    private void handleKeyPressed(KeyEvent event) {
        int focusedCardIndex = getFocusedCardIndex();
        if (focusedCardIndex >= 0) {
            if (event.getCode() == KeyCode.DOWN) {
                focusCard(focusedCardIndex + 1);
                event.consume();
            } else if (event.getCode() == KeyCode.UP) {
                focusCard(focusedCardIndex - 1);
                event.consume();
            } else if (event.getCode() == KeyCode.TAB) {
                if (event.isShiftDown() && focusedCardIndex == 0) {
                    userGuideButton.requestFocus();
                } else if (!event.isShiftDown() && focusedCardIndex == commandCards.size() - 1) {
                    userGuideButton.requestFocus();
                } else {
                    focusCard(focusedCardIndex + (event.isShiftDown() ? -1 : 1));
                }
                event.consume();
            } else if (event.getCode() == KeyCode.ENTER) {
                Button useCommandButton = commandCards.get(focusedCardIndex).useCommandButton();
                if (useCommandButton.isFocused()) {
                    useCommandButton.fire();
                    event.consume();
                } else if (isCardFocused(focusedCardIndex)) {
                    useCommandButton.requestFocus();
                    event.consume();
                }
            }
        } else if (userGuideButton.isFocused() && event.getCode() == KeyCode.TAB) {
            focusCard(event.isShiftDown() ? commandCards.size() - 1 : 0);
            event.consume();
        }
    }

    private int getFocusedCardIndex() {
        for (int i = 0; i < commandCards.size(); i++) {
            CommandCard commandCard = commandCards.get(i);
            if (commandCard.card().isFocused() || commandCard.useCommandButton().isFocused()) {
                return i;
            }
        }
        return -1;
    }

    private boolean isCardFocused(int index) {
        return commandCards.get(index).card().isFocused();
    }

    private void focusCard(int index) {
        if (commandCards.isEmpty()) {
            userGuideButton.requestFocus();
            return;
        }

        int boundedIndex = Math.max(0, Math.min(index, commandCards.size() - 1));
        VBox card = commandCards.get(boundedIndex).card();
        card.requestFocus();
        Platform.runLater(() -> scrollCardIntoView(card));
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

    private record CommandCard(VBox card, Button useCommandButton) {}
}
