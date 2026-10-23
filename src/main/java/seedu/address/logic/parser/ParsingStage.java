package seedu.address.logic.parser;

enum ParsingStage {
    TOKENIZATION("Tokenization stage"),
    ARGUMENT_VALIDATION("Argument validation stage"),
    FIELD_PARSING("Field parsing stage");

    private String message;

    ParsingStage(String message) {
        this.message = message;
    }

    String getMessage() {
        return this.message;
    }
}
