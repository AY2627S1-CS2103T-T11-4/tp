package seedu.address.commons.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

public class CollectionUtilTest {
    @Test
    public void requireAllNonNullVarargs() {
        // BV: count >= 0 (valid).
        assertNullPointerExceptionNotThrown();

        // EP: count >= 2 (valid).
        assertNullPointerExceptionNotThrown(new Object(), new Object());
        // EP: count = 1 (valid).
        assertNullPointerExceptionNotThrown("test");
        // EP: empty string is non-null (valid).
        assertNullPointerExceptionNotThrown("");

        // One invalid input at a time: the sole argument is null.
        assertNullPointerExceptionThrown((Object) null);
        // EP: null is the first of multiple arguments (invalid).
        assertNullPointerExceptionThrown(null, "", new Object());
        // EP: null precedes non-null objects (invalid).
        assertNullPointerExceptionThrown(null, new Object(), new Object());

        // EP: null arguments occur in the middle (invalid).
        assertNullPointerExceptionThrown(new Object(), null, null, "test");
        // One invalid input at a time: only the middle argument is null.
        assertNullPointerExceptionThrown("", null, new Object());

        // One invalid input at a time: only the last argument is null.
        assertNullPointerExceptionThrown("", new Object(), null);
        // EP: null follows multiple non-null objects (invalid).
        assertNullPointerExceptionThrown(new Object(), new Object(), null);

        // EP: the varargs array reference is null (invalid).
        assertNullPointerExceptionThrown((Object[]) null);

        // EP: a nested list may contain null (valid).
        List<Object> containingNull = Arrays.asList((Object) null);
        assertNullPointerExceptionNotThrown(containingNull, new Object());
    }

    @Test
    public void requireAllNonNullCollection() {
        // One invalid input at a time: the collection contains one null element.
        assertNullPointerExceptionThrown(Arrays.asList((Object) null));
        // EP: null is the first collection element (invalid).
        assertNullPointerExceptionThrown(Arrays.asList(null, new Object(), ""));

        // One invalid input at a time: only the middle collection element is null.
        assertNullPointerExceptionThrown(Arrays.asList("spam", null, new Object()));
        // Invalid inputs combined after separate tests: two collection elements are null.
        assertNullPointerExceptionThrown(Arrays.asList("spam", null, "eggs", null, new Object()));

        // One invalid input at a time: only the last collection element is null.
        assertNullPointerExceptionThrown(Arrays.asList("spam", new Object(), null));
        // EP: null follows one non-null element (invalid).
        assertNullPointerExceptionThrown(Arrays.asList(new Object(), null));

        // EP: the collection reference is null (invalid).
        assertNullPointerExceptionThrown((Collection<Object>) null);

        // BV: size >= 0 (valid).
        assertNullPointerExceptionNotThrown(Collections.emptyList());

        // EP: count >= 2 (valid).
        assertNullPointerExceptionNotThrown(Arrays.asList(new Object(), "ham", Integer.valueOf(1)));
        // BV: size >= 0 (valid).
        assertNullPointerExceptionNotThrown(Arrays.asList(new Object()));

        // EP: a nested collection may contain null (valid).
        List<Object> containingNull = Arrays.asList((Object) null);
        assertNullPointerExceptionNotThrown(Arrays.asList(containingNull, new Object()));
    }

    @Test
    public void isAnyNonNull() {
        // BV: count < 1 (invalid).
        assertFalse(CollectionUtil.isAnyNonNull());
        // One invalid input at a time: the sole argument is null.
        assertFalse(CollectionUtil.isAnyNonNull((Object) null));
        // EP: the varargs array reference is null (invalid).
        assertFalse(CollectionUtil.isAnyNonNull((Object[]) null));
        // BV: count >= 1 (valid).
        assertTrue(CollectionUtil.isAnyNonNull(new Object()));
        // EP: count >= 1 (valid).
        assertTrue(CollectionUtil.isAnyNonNull(new Object(), null));
    }

    /**
     * Asserts that {@code CollectionUtil#requireAllNonNull(Object...)} throws {@code NullPointerException}
     * if {@code objects} or any element of {@code objects} is null.
     */
    private void assertNullPointerExceptionThrown(Object... objects) {
        assertThrows(NullPointerException.class, () -> requireAllNonNull(objects));
    }

    /**
     * Asserts that {@code CollectionUtil#requireAllNonNull(Collection<?>)} throws {@code NullPointerException}
     * if {@code collection} or any element of {@code collection} is null.
     */
    private void assertNullPointerExceptionThrown(Collection<?> collection) {
        assertThrows(NullPointerException.class, () -> requireAllNonNull(collection));
    }

    private void assertNullPointerExceptionNotThrown(Object... objects) {
        requireAllNonNull(objects);
    }

    private void assertNullPointerExceptionNotThrown(Collection<?> collection) {
        requireAllNonNull(collection);
    }
}
