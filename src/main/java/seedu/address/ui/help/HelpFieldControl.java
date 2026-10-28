package seedu.address.ui.help;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/** Owns the input controls for one command field in a Help command card. */
final class HelpFieldControl {
    private final HelpSearch.CommandField field;
    private final List<TextField> inputs = new ArrayList<>();
    private final List<Button> removeButtons = new ArrayList<>();
    private final List<Node> formatNodes = new ArrayList<>();
    private final Button addButton = new Button();
    private final Label addButtonLabel = new Label();
    private boolean active;
    private Runnable layoutRefresh;

    HelpFieldControl(HelpSearch.CommandField field) {
        this.field = field;
        configureAddButton();
    }

    void initialize() {
        active = field.required();
        if (active) {
            addField(false);
        } else {
            rebuildFormatNodes();
        }
    }

    private void addField(boolean requestFocus) {
        TextField input = new TextField();
        input.setPromptText(field.label());
        input.setAccessibleText(field.label() + (field.required() ? " required" : " optional"));
        input.setPrefWidth(Math.max(95, field.hint().length() * 7));
        input.setMinWidth(80);
        input.getStyleClass().add("inline-command-input");
        inputs.add(input);
        if (!field.required() || field.repeatable()) {
            Button removeButton = new Button("-");
            removeButton.setMnemonicParsing(false);
            removeButton.setAccessibleText("Remove " + field.label());
            removeButton.getStyleClass().add("inline-input-button");
            removeButton.setOnAction(event -> removeField(input, removeButton));
            removeButtons.add(removeButton);
        }
        rebuildFormatNodes();
        refreshLayout();
        if (requestFocus) {
            Platform.runLater(input::requestFocus);
        }
    }

    void removeField(TextField input, Button removeButton) {
        if (inputs.size() == 1) {
            if (field.required()) {
                input.clear();
                Platform.runLater(input::requestFocus);
            } else {
                active = false;
                inputs.clear();
                removeButtons.clear();
                rebuildFormatNodes();
                refreshLayout();
                Platform.runLater(addButton::requestFocus);
            }
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
        if (!active) {
            return List.of();
        }
        return inputs.stream().map(TextField::getText).toList();
    }

    void reset() {
        active = field.required();
        inputs.clear();
        removeButtons.clear();
        if (active) {
            addField(false);
        } else {
            rebuildFormatNodes();
            refreshLayout();
        }
    }

    List<Node> focusableNodes() {
        List<Node> nodes = new ArrayList<>();
        if (!active) {
            nodes.add(addButton);
            return nodes;
        }
        for (int i = 0; i < inputs.size(); i++) {
            nodes.add(inputs.get(i));
            if (!field.required() || field.repeatable()) {
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
        if (!active) {
            formatNodes.add(addButton);
            return;
        }
        for (int i = 0; i < inputs.size(); i++) {
            if (!field.prefix().isEmpty()) {
                Label prefix = new Label(field.prefix());
                prefix.getStyleClass().add("command-format-token");
                formatNodes.add(prefix);
            }

            formatNodes.add(inputs.get(i));

            if (!field.required() || field.repeatable()) {
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

    private void configureAddButton() {
        addButton.setMnemonicParsing(false);
        String buttonText = field.controlName() + "+";
        addButton.setText(buttonText);
        addButton.setAccessibleText("Add " + field.label());
        addButtonLabel.setText(buttonText);
        addButtonLabel.setMouseTransparent(true);
        addButtonLabel.getStyleClass().add("optional-field-button-label");
        addButton.setGraphic(addButtonLabel);
        addButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        addButton.setMinWidth(Math.max(38, field.controlName().length() * 8 + 16));
        addButton.setPrefWidth(Math.max(38, field.controlName().length() * 8 + 16));
        addButton.getStyleClass().add("inline-input-button");
        addButton.getStyleClass().add("optional-field-button");
        addButton.setOnAction(event -> {
            active = true;
            addField(true);
        });
    }
}
