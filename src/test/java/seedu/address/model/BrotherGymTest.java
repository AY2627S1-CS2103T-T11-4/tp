package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalStudents.ALICE;
import static seedu.address.testutil.TypicalStudents.BENSON;
import static seedu.address.testutil.TypicalStudents.getTypicalBrotherGym;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.student.Student;
import seedu.address.model.student.exceptions.DuplicateStudentException;
import seedu.address.model.student.exceptions.StudentNotFoundException;
import seedu.address.testutil.StudentBuilder;

public class BrotherGymTest {

    private final BrotherGym brotherGym = new BrotherGym();

    @Test
    public void constructor() {
        assertEquals(List.of(), brotherGym.getStudentList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        brotherGym.addStudent(ALICE);
        assertThrows(NullPointerException.class, () -> brotherGym.resetData(null));
        assertEquals(List.of(ALICE), brotherGym.getStudentList());
    }

    @Test
    public void resetData_withValidReadOnlyBrotherGym_replacesData() {
        BrotherGym newData = getTypicalBrotherGym();
        brotherGym.resetData(newData);
        assertEquals(newData, brotherGym);
    }

    @Test
    public void resetData_withDuplicateStudents_throwsDuplicateStudentException() {
        brotherGym.addStudent(BENSON);
        // Two students with the same identity fields.
        Student editedAlice = new StudentBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        List<Student> newStudents = List.of(ALICE, editedAlice);
        BrotherGymStub newData = new BrotherGymStub(newStudents);

        assertThrows(DuplicateStudentException.class, () -> brotherGym.resetData(newData));
        assertEquals(List.of(BENSON), brotherGym.getStudentList());
    }

    @Test
    public void setStudents_nullList_throwsNullPointerExceptionWithoutChangingData() {
        brotherGym.addStudent(ALICE);

        assertThrows(NullPointerException.class, () -> brotherGym.setStudents(null));
        assertEquals(List.of(ALICE), brotherGym.getStudentList());
    }

    @Test
    public void setStudents_withNullStudent_throwsNullPointerExceptionWithoutChangingData() {
        brotherGym.addStudent(ALICE);
        List<Student> students = new ArrayList<>(List.of(BENSON));
        students.add(null);

        assertThrows(NullPointerException.class, () -> brotherGym.setStudents(students));
        assertEquals(List.of(ALICE), brotherGym.getStudentList());
    }

    @Test
    public void resetData_withNullStudent_throwsNullPointerExceptionWithoutChangingData() {
        brotherGym.addStudent(ALICE);
        List<Student> students = new ArrayList<>(List.of(BENSON));
        students.add(0, null);
        BrotherGymStub newData = new BrotherGymStub(students);

        assertThrows(NullPointerException.class, () -> brotherGym.resetData(newData));
        assertEquals(List.of(ALICE), brotherGym.getStudentList());
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
    public void validateStudentInBrotherGym_storedStudent_returnsStoredInstance() {
        brotherGym.addStudent(ALICE);
        Student equalAlice = new StudentBuilder(ALICE).build();

        assertSame(ALICE, brotherGym.validateStudentInBrotherGym(ALICE));
        assertSame(ALICE, brotherGym.validateStudentInBrotherGym(equalAlice));
    }

    @Test
    public void validateStudentInBrotherGym_studentNotInList_throwsStudentNotFoundException() {
        brotherGym.addStudent(ALICE);
        Student editedAlice = new StudentBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();

        assertThrows(StudentNotFoundException.class, () -> brotherGym.validateStudentInBrotherGym(BENSON));
        assertThrows(StudentNotFoundException.class, () -> brotherGym.validateStudentInBrotherGym(editedAlice));
    }

    @Test
    public void validateStudentInBrotherGym_nullStudent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> brotherGym.validateStudentInBrotherGym(null));
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
