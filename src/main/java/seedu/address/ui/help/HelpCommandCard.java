package seedu.address.ui.help;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/** Owns the presentation and interaction logic for one Help command card. */
final class HelpCommandCard {
    private final HelpSearch.CommandReference command;
    private final VBox card;
    private final Button useCommandButton;
    private final List<HelpFieldControl> fieldControls;
    private final Label validationMessage;

    HelpCommandCard(HelpSearch.CommandReference command, Consumer<String> commandFormatHandler) {
        this.command = requireNonNull(command);
        requireNonNull(commandFormatHandler);

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

        fieldControls = command.fields().stream()
                .map(HelpFieldControl::new)
                .toList();
        fieldControls.forEach(HelpFieldControl::initialize);
        Runnable refreshCommandFormat = () -> {
            commandFormat.getChildren().clear();
            commandFormat.getChildren().add(commandWord);
            fieldControls.forEach(fieldControl -> commandFormat.getChildren().addAll(fieldControl.formatNodes()));
        };
        fieldControls.forEach(fieldControl -> fieldControl.setLayoutRefresh(refreshCommandFormat));
        refreshCommandFormat.run();

        validationMessage = new Label();
        validationMessage.setManaged(false);
        validationMessage.setVisible(false);
        validationMessage.getStyleClass().add("input-validation-message");

        useCommandButton = new Button("Use Command");
        useCommandButton.setMnemonicParsing(false);
        useCommandButton.setOnAction(event -> handleCommandSelected(commandFormatHandler));
        useCommandButton.getStyleClass().add("use-format-button");

        HBox actionRow = new HBox(useCommandButton);
        actionRow.setAlignment(Pos.CENTER_RIGHT);
        actionRow.setMaxWidth(Double.MAX_VALUE);

        card = new VBox(7, commandFormat, description, exampleBlock, validationMessage, actionRow);
        card.setMaxWidth(Double.MAX_VALUE);
        card.setFocusTraversable(true);
        card.getStyleClass().add("command-card");
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
        fieldControls.forEach(HelpFieldControl::reset);
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

    private void handleCommandSelected(Consumer<String> commandFormatHandler) {
        if (fieldControls.isEmpty()) {
            commandFormatHandler.accept(command.format());
            return;
        }

        List<List<String>> values = fieldControls.stream()
                .map(HelpFieldControl::values)
                .toList();
        HelpInput.Result result = HelpInput.build(command, values);
        validationMessage.setText(result.error());
        validationMessage.setVisible(!result.isSuccess());
        validationMessage.setManaged(!result.isSuccess());
        if (result.isSuccess()) {
            commandFormatHandler.accept(result.command());
        }
    }

    private List<Node> formNodes() {
        List<Node> nodes = new ArrayList<>();
        fieldControls.forEach(fieldControl -> nodes.addAll(fieldControl.focusableNodes()));
        nodes.add(useCommandButton);
        return nodes;
    }
}
