package seedu.address.commons.core.index;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class IndexTest {

    @Test
    public void createOneBasedIndex() {
        // BV: index < 1 (invalid).
        assertThrows(IndexOutOfBoundsException.class, () -> Index.fromOneBased(0));

        // BV: index >= 1 (valid).
        assertEquals(1, Index.fromOneBased(1).getOneBased());
        // EP: index > 1 (valid).
        assertEquals(5, Index.fromOneBased(5).getOneBased());

        // BV: index >= 1 (valid).
        assertEquals(0, Index.fromOneBased(1).getZeroBased());
        // EP: index > 1 (valid).
        assertEquals(4, Index.fromOneBased(5).getZeroBased());
    }

    @Test
    public void createZeroBasedIndex() {
        // BV: index < 0 (invalid).
        assertThrows(IndexOutOfBoundsException.class, () -> Index.fromZeroBased(-1));

        // BV: index >= 0 (valid).
        assertEquals(0, Index.fromZeroBased(0).getZeroBased());
        // EP: index > 0 (valid).
        assertEquals(5, Index.fromZeroBased(5).getZeroBased());

        // BV: index >= 0 (valid).
        assertEquals(1, Index.fromZeroBased(0).getOneBased());
        // EP: index > 0 (valid).
        assertEquals(6, Index.fromZeroBased(5).getOneBased());
    }

    @Test
    public void equals() {
        final Index fifthStudentIndex = Index.fromOneBased(5);

        // EP: Indexes with the same one-based value (equivalent).
        assertTrue(fifthStudentIndex.equals(Index.fromOneBased(5)));
        // EP: Equal indexes created using different bases (equivalent).
        assertTrue(fifthStudentIndex.equals(Index.fromZeroBased(4)));

        // EP: The same index object (equivalent).
        assertTrue(fifthStudentIndex.equals(fifthStudentIndex));

        // EP: Null (non-equivalent).
        assertFalse(fifthStudentIndex.equals(null));

        // EP: An object of another type (non-equivalent).
        assertFalse(fifthStudentIndex.equals(5.0f));

        // EP: A different index value (non-equivalent).
        assertFalse(fifthStudentIndex.equals(Index.fromOneBased(1)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromZeroBased(0);
        String expected = Index.class.getCanonicalName() + "{zeroBasedIndex=" + index.getZeroBased() + "}";
        assertEquals(expected, index.toString());
    }
}
