package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalStudents.ALICE;
import static seedu.address.testutil.TypicalStudents.getTypicalBrotherGym;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.student.Student;
import seedu.address.model.student.exceptions.DuplicateStudentException;
import seedu.address.testutil.StudentBuilder;

public class BrotherGymTest {

    private final BrotherGym brotherGym = new BrotherGym();

    @Test
    public void constructor() {
        assertEquals(List.of(), brotherGym.getStudentList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> brotherGym.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyBrotherGym_replacesData() {
        BrotherGym newData = getTypicalBrotherGym();
        brotherGym.resetData(newData);
        assertEquals(newData, brotherGym);
    }

    @Test
    public void resetData_withDuplicateStudents_throwsDuplicateStudentException() {
        // Two student
        // with the same identity fields
        Student editedAlice = new StudentBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        List<Student> newStudents = List.of(ALICE, editedAlice);
        BrotherGymStub newData = new BrotherGymStub(newStudents);

        assertThrows(DuplicateStudentException.class, () -> brotherGym.resetData(newData));
    }

    @Test
    public void hasStudent_nullStudent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> brotherGym.hasStudent(null));
    }

    @Test
    public void hasStudent_studentNotInBrotherGym_returnsFalse() {
        assertFalse(brotherGym.hasStudent(ALICE));
    }

    @Test
    public void hasStudent_studentInBrotherGym_returnsTrue() {
        brotherGym.addStudent(ALICE);
        assertTrue(brotherGym.hasStudent(ALICE));
    }

    @Test
    public void hasStudent_studentWithSameIdentityFieldsInBrotherGym_returnsTrue() {
        brotherGym.addStudent(ALICE);
        Student editedAlice = new StudentBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(brotherGym.hasStudent(editedAlice));
    }

    @Test
    public void getStudentList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> brotherGym.getStudentList().remove(0));
    }

    @Test
    public void toStringMethod() {
        String expected = BrotherGym.class.getCanonicalName() + "{students=" + brotherGym.getStudentList() + "}";
        assertEquals(expected, brotherGym.toString());
    }

    /**
     * A stub ReadOnlyBrotherGym whose student
     * list can violate interface constraints.
     */
    private static class BrotherGymStub implements ReadOnlyBrotherGym {
        private final ObservableList<Student> students = FXCollections.observableArrayList();

        BrotherGymStub(Collection<Student> students) {
            this.students.setAll(students);
        }

        @Override
        public ObservableList<Student> getStudentList() {
            return students;
        }
    }

}
