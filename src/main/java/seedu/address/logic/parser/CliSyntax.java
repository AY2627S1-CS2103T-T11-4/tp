package seedu.address.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Common Prefixes */
    public static final Prefix PREFIX_NAME = new Prefix("--name");
    public static final Prefix PREFIX_TAG = new Prefix("--tag");

    /* Student Prefixes */
    public static final Prefix PREFIX_PHONE = new Prefix("--hp");
    public static final Prefix PREFIX_EMAIL = new Prefix("--email");
    public static final Prefix PREFIX_ADDRESS = new Prefix("--addr");

    /* Workout Plan Prefixes */
    public static final Prefix PREFIX_PLAN = new Prefix("--plan");
    public static final Prefix PREFIX_DATE = new Prefix("--date");

    /* Invalid Prefixes */
    public static final Prefix PREFIX_INVALID = new Prefix("--invalid");
}
