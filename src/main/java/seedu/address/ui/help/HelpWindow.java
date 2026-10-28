package seedu.address.ui.help;

import static java.util.Objects.requireNonNull;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;
import seedu.address.ui.UiPart;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String USERGUIDE_URL =
            "https://ay2627s1-cs2103t-t11-4.github.io/tp/UserGuide.html#command-summary";

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    private final HelpSearch helpSearch = new HelpSearch();
    private final Consumer<String> commandFormatHandler;
    private final List<HelpCommandCard> commandCards = new ArrayList<>();
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
        commandSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            helpSearch.setQuery(newValue);
            filterCommands();
        });
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
        resetHelpState();
        getRoot().setIconified(false);
        getRoot().toFront();
        Platform.runLater(commandSearchField::requestFocus);
    }

    /**
     * Resets the Help window to a fresh state before it is shown or refocused.
     */
    private void resetHelpState() {
        helpSearch.setQuery("");
        commandSearchField.setText("");
        commandCards.forEach(HelpCommandCard::resetInputState);
        filterCommands();
        helpScroll.setVvalue(0);
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
        helpSearch.getCategories().stream()
                .map(this::createCommandCategory)
                .forEach(commandCategoryView -> {
                    commandCategoryViews.add(commandCategoryView);
                    commandSections.getChildren().add(commandCategoryView.categoryBox());
                });
    }

    private CommandCategoryView createCommandCategory(HelpSearch.CommandCategory category) {
        Label categoryTitle = new Label(category.title());
        categoryTitle.getStyleClass().add("category-title");

        VBox commandCardsBox = new VBox(10);
        commandCardsBox.getStyleClass().add("command-cards");
        List<HelpCommandCard> categoryCards = new ArrayList<>();
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

    private HelpCommandCard createCommandCard(HelpSearch.CommandReference command) {
        return new HelpCommandCard(command, commandFormatHandler);
    }

    private void handleKeyPressed(KeyEvent event) {
        List<HelpCommandCard> visibleCards = getVisibleCommandCards();
        int focusedCardIndex = getFocusedCardIndex(visibleCards);
        if (focusedCardIndex >= 0 && focusedCardIndex < visibleCards.size()
                && visibleCards.get(focusedCardIndex).isFormControlFocused()) {
            handleFormKeyPressed(event, visibleCards.get(focusedCardIndex));
            return;
        }

        boolean isForwardNavigation = event.getCode() == KeyCode.DOWN
                || (event.getCode() == KeyCode.TAB && !event.isShiftDown());
        boolean isBackwardNavigation = event.getCode() == KeyCode.UP
                || (event.getCode() == KeyCode.TAB && event.isShiftDown());

        if (isForwardNavigation || isBackwardNavigation) {
            moveFocus(isForwardNavigation);
            event.consume();
        } else if (focusedCardIndex >= 0 && event.getCode() == KeyCode.ENTER) {
            HelpCommandCard focusedCard = visibleCards.get(focusedCardIndex);
            if (focusedCard.hasInputFields()) {
                focusedCard.focusFirstInput();
                event.consume();
            } else if (focusedCard.useCommandButton().isFocused()) {
                focusedCard.useCommandButton().fire();
                event.consume();
            } else if (isCardFocused(focusedCardIndex)) {
                focusedCard.useCommandButton().requestFocus();
                event.consume();
            }
        } else if (closeHelpButton.isFocused() && event.getCode() == KeyCode.ENTER) {
            closeHelpButton.fire();
            event.consume();
        }
    }

    private void handleFormKeyPressed(KeyEvent event, HelpCommandCard card) {
        boolean isForward = event.getCode() == KeyCode.RIGHT
                || (event.getCode() == KeyCode.TAB && !event.isShiftDown());
        boolean isBackward = event.getCode() == KeyCode.LEFT
                || (event.getCode() == KeyCode.TAB && event.isShiftDown());
        if (isForward || isBackward) {
            card.moveFormFocus(isForward);
            event.consume();
        } else if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) {
            moveFocus(event.getCode() == KeyCode.DOWN);
            event.consume();
        } else if (event.getCode() == KeyCode.ENTER && card.fireFocusedButton()) {
            event.consume();
        }
    }

    private void moveFocus(boolean forward) {
        List<HelpCommandCard> visibleCards = getVisibleCommandCards();
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

    private int getFocusedCardIndex(List<HelpCommandCard> visibleCards) {
        for (int i = 0; i < visibleCards.size(); i++) {
            HelpCommandCard commandCard = visibleCards.get(i);
            if (commandCard.card().isFocused() || commandCard.isFormControlFocused()) {
                return i;
            }
        }
        return -1;
    }

    private boolean isCardFocused(int index) {
        return getVisibleCommandCards().get(index).card().isFocused();
    }

    private void focusCard(int index) {
        List<HelpCommandCard> visibleCards = getVisibleCommandCards();
        if (visibleCards.isEmpty()) {
            commandSearchField.requestFocus();
            return;
        }

        int boundedIndex = Math.max(0, Math.min(index, visibleCards.size() - 1));
        VBox card = visibleCards.get(boundedIndex).card();
        card.requestFocus();
        Platform.runLater(() -> scrollCardIntoView(card));
    }

    private List<HelpCommandCard> getVisibleCommandCards() {
        return commandCards.stream()
                .filter(commandCard -> commandCard.card().isVisible())
                .toList();
    }

    private void filterCommands() {
        boolean hasVisibleCommand = false;

        for (CommandCategoryView categoryView : commandCategoryViews) {
            boolean hasVisibleCard = false;
            for (HelpCommandCard commandCard : categoryView.cards()) {
                boolean isVisible = helpSearch.matches(commandCard.command());
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

    private record CommandCategoryView(VBox categoryBox, List<HelpCommandCard> cards) {}

}
