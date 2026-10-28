package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

/** Validates structured Help-window values and builds command strings. */
final class HelpInput {
    private HelpInput() {}

    static Result build(HelpSearch.CommandReference command, List<List<String>> values) {
        requireNonNull(command);
        requireNonNull(values);

        if (values.size() != command.fields().size()) {
            return Result.failure("Please complete the command form.");
        }

        List<String> parts = new ArrayList<>();
        boolean hasUpdate = false;
        for (int i = 0; i < command.fields().size(); i++) {
            HelpSearch.CommandField field = command.fields().get(i);
            List<String> fieldValues = values.get(i);
            boolean hasValue = false;
            for (String rawValue : fieldValues) {
                String value = rawValue == null ? "" : rawValue.trim();
                if (value.isEmpty()) {
                    continue;
                }
                hasValue = true;
                parts.add(field.prefix().isEmpty() ? value : field.prefix() + " " + value);
            }

            if (field.required() && !hasValue) {
                return Result.failure(field.label() + " is required.");
            }
            if (i > 0 && hasValue) {
                hasUpdate = true;
            }
        }

        if (command.requiresUpdate() && !hasUpdate) {
            return Result.failure("At least one field to update is required.");
        }

        String suffix = String.join(" ", parts);
        return Result.success(suffix.isEmpty() ? command.commandWord() : command.commandWord() + " " + suffix);
    }

    /** Result of building a command form. */
    record Result(String command, String error) {
        static Result success(String command) {
            return new Result(command, "");
        }

        static Result failure(String error) {
            return new Result("", error);
        }

        boolean isSuccess() {
            return error.isEmpty();
        }
    }
}
