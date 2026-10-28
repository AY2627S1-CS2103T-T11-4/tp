package seedu.address.ui.help;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/** Owns the input controls for one command field in a Help command card. */
final class HelpFieldControl {
    private final HelpSearch.CommandField field;
    private final List<TextField> inputs = new ArrayList<>();
    private final List<Button> removeButtons = new ArrayList<>();
    private final List<Node> formatNodes = new ArrayList<>();
    private final Button addButton = new Button("Add tag");
    private Runnable layoutRefresh;

    HelpFieldControl(HelpSearch.CommandField field) {
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
