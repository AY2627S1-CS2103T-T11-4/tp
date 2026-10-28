package seedu.address.ui;

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
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;

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
        commandCards.forEach(CommandCard::resetInputState);
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

    private CommandCard createCommandCard(HelpSearch.CommandReference command) {
        FlowPane commandFormat = new FlowPane(6, 5);
        commandFormat.setMaxWidth(Double.MAX_VALUE);
        commandFormat.getStyleClass().add("command-format");
        Label commandWord = new Label(command.commandWord());
        commandWord.getStyleClass().add("command-format-token");
        commandFormat.getChildren().add(commandWord);

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

        List<FieldControl> fieldControls = command.fields().stream()
                .map(this::createFieldControl)
                .toList();
        Runnable refreshCommandFormat = () -> {
            commandFormat.getChildren().clear();
            commandFormat.getChildren().add(commandWord);
            fieldControls.forEach(fieldControl -> commandFormat.getChildren().addAll(fieldControl.formatNodes()));
        };
        fieldControls.forEach(fieldControl -> fieldControl.setLayoutRefresh(refreshCommandFormat));
        refreshCommandFormat.run();

        Label validationMessage = new Label();
        validationMessage.setManaged(false);
        validationMessage.setVisible(false);
        validationMessage.getStyleClass().add("input-validation-message");

        Button useFormatButton = new Button("Use Command");
        useFormatButton.setMnemonicParsing(false);
        useFormatButton.setOnAction(event -> handleCommandSelected(
                command, fieldControls, validationMessage));
        useFormatButton.getStyleClass().add("use-format-button");

        HBox actionRow = new HBox(useFormatButton);
        actionRow.setAlignment(Pos.CENTER_RIGHT);
        actionRow.setMaxWidth(Double.MAX_VALUE);

        VBox card = new VBox(7, commandFormat, description, exampleBlock, validationMessage, actionRow);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setFocusTraversable(true);
        card.getStyleClass().add("command-card");
        return new CommandCard(command, card, useFormatButton, fieldControls, validationMessage);
    }

    private FieldControl createFieldControl(HelpSearch.CommandField field) {
        FieldControl fieldControl = new FieldControl(field);
        fieldControl.addField();
        return fieldControl;
    }

    private void handleCommandSelected(HelpSearch.CommandReference command,
                                       List<FieldControl> fieldControls,
                                       Label validationMessage) {
        if (fieldControls.isEmpty()) {
            commandFormatHandler.accept(command.format());
            return;
        }

        List<List<String>> values = fieldControls.stream()
                .map(FieldControl::values)
                .toList();
        HelpInput.Result result = HelpInput.build(command, values);
        validationMessage.setText(result.error());
        validationMessage.setVisible(!result.isSuccess());
        validationMessage.setManaged(!result.isSuccess());
        if (result.isSuccess()) {
            commandFormatHandler.accept(result.command());
        }
    }

    private void handleKeyPressed(KeyEvent event) {
        List<CommandCard> visibleCards = getVisibleCommandCards();
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
            CommandCard focusedCard = visibleCards.get(focusedCardIndex);
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

    private void handleFormKeyPressed(KeyEvent event, CommandCard card) {
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

    private void filterCommands() {
        boolean hasVisibleCommand = false;

        for (CommandCategoryView categoryView : commandCategoryViews) {
            boolean hasVisibleCard = false;
            for (CommandCard commandCard : categoryView.cards()) {
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

    private record CommandCategoryView(VBox categoryBox, List<CommandCard> cards) {}

    private static final class FieldControl {
        private final HelpSearch.CommandField field;
        private final List<TextField> inputs = new ArrayList<>();
        private final List<Button> removeButtons = new ArrayList<>();
        private final List<Node> formatNodes = new ArrayList<>();
        private final Button addButton = new Button("Add tag");
        private Runnable layoutRefresh;

        FieldControl(HelpSearch.CommandField field) {
            this.field = field;
            if (field.repeatable()) {
                addButton.setMnemonicParsing(false);
                addButton.setText("+");
                addButton.setAccessibleText("Add " + field.label());
                addButton.getStyleClass().add("inline-input-button");
                addButton.setOnAction(event -> addField());
            }
        }

        void addField() {
            TextField input = new TextField();
            input.setPromptText(field.label());
            input.setAccessibleText(field.label() + (field.required() ? " required" : " optional"));
            input.setPrefWidth(Math.max(95, field.hint().length() * 7));
            input.setMinWidth(80);
            input.getStyleClass().add("inline-command-input");
            inputs.add(input);
            if (field.repeatable()) {
                Button removeButton = new Button("-");
                removeButton.setMnemonicParsing(false);
                removeButton.setAccessibleText("Remove " + field.label());
                removeButton.getStyleClass().add("inline-input-button");
                removeButton.setOnAction(event -> removeField(input, removeButton));
                removeButtons.add(removeButton);
            }
            rebuildFormatNodes();
            refreshLayout();
            Platform.runLater(input::requestFocus);
        }

        void removeField(TextField input, Button removeButton) {
            if (inputs.size() == 1) {
                input.clear();
                Platform.runLater(input::requestFocus);
                return;
            }
            int removedIndex = inputs.indexOf(input);
            inputs.remove(input);
            removeButtons.remove(removeButton);
            rebuildFormatNodes();
            refreshLayout();
            TextField focusTarget = inputs.get(Math.min(removedIndex, inputs.size() - 1));
            Platform.runLater(focusTarget::requestFocus);
        }

        List<String> values() {
            return inputs.stream().map(TextField::getText).toList();
        }

        void reset() {
            if (field.repeatable()) {
                while (inputs.size() > 1) {
                    inputs.remove(inputs.size() - 1);
                    removeButtons.remove(removeButtons.size() - 1);
                }
            }
            inputs.forEach(TextField::clear);
            rebuildFormatNodes();
            refreshLayout();
        }

        List<Node> focusableNodes() {
            List<Node> nodes = new ArrayList<>();
            for (int i = 0; i < inputs.size(); i++) {
                nodes.add(inputs.get(i));
                if (field.repeatable()) {
                    nodes.add(removeButtons.get(i));
                }
            }
            if (field.repeatable()) {
                nodes.add(addButton);
            }
            return nodes;
        }

        List<Node> formatNodes() {
            return formatNodes;
        }

        void setLayoutRefresh(Runnable layoutRefresh) {
            this.layoutRefresh = layoutRefresh;
        }

        private void rebuildFormatNodes() {
            formatNodes.clear();
            for (int i = 0; i < inputs.size(); i++) {
                if (!field.prefix().isEmpty()) {
                    Label prefix = new Label(field.prefix());
                    prefix.getStyleClass().add("command-format-token");
                    formatNodes.add(prefix);
                }

                formatNodes.add(inputs.get(i));

                if (field.repeatable()) {
                    formatNodes.add(removeButtons.get(i));
                }
            }
            if (field.repeatable()) {
                formatNodes.add(addButton);
            }
        }

        private void refreshLayout() {
            if (layoutRefresh != null) {
                layoutRefresh.run();
            }
        }
    }

    private static final class CommandCard {
        private final HelpSearch.CommandReference command;
        private final VBox card;
        private final Button useCommandButton;
        private final List<FieldControl> fieldControls;
        private final Label validationMessage;

        CommandCard(HelpSearch.CommandReference command, VBox card, Button useCommandButton,
                    List<FieldControl> fieldControls, Label validationMessage) {
            this.command = command;
            this.card = card;
            this.useCommandButton = useCommandButton;
            this.fieldControls = fieldControls;
            this.validationMessage = validationMessage;
        }

        HelpSearch.CommandReference command() {
            return command;
        }

        VBox card() {
            return card;
        }

        Button useCommandButton() {
            return useCommandButton;
        }

        boolean hasInputFields() {
            return !fieldControls.isEmpty();
        }

        void resetInputState() {
            fieldControls.forEach(FieldControl::reset);
            validationMessage.setText("");
            validationMessage.setVisible(false);
            validationMessage.setManaged(false);
        }

        boolean isFormControlFocused() {
            return useCommandButton.isFocused() || formNodes().stream().anyMatch(Node::isFocused);
        }

        void focusFirstInput() {
            formNodes().stream().findFirst().ifPresent(Node::requestFocus);
        }

        void moveFormFocus(boolean forward) {
            List<Node> nodes = formNodes();
            if (nodes.isEmpty()) {
                return;
            }
            int current = -1;
            for (int i = 0; i < nodes.size(); i++) {
                if (nodes.get(i).isFocused()) {
                    current = i;
                    break;
                }
            }
            int next = current < 0 ? 0 : current + (forward ? 1 : -1);
            if (next < 0) {
                next = nodes.size() - 1;
            } else if (next >= nodes.size()) {
                next = 0;
            }
            nodes.get(next).requestFocus();
        }

        boolean fireFocusedButton() {
            return formNodes().stream()
                    .filter(Button.class::isInstance)
                    .map(Button.class::cast)
                    .filter(Button::isFocused)
                    .findFirst()
                    .map(button -> {
                        button.fire();
                        return true;
                    })
                    .orElse(false);
        }

        private List<Node> formNodes() {
            List<Node> nodes = new ArrayList<>();
            fieldControls.forEach(fieldControl -> nodes.addAll(fieldControl.focusableNodes()));
            nodes.add(useCommandButton);
            return nodes;
        }
    }
}
