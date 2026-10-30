package seedu.address.model.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalStudents.ALICE;
import static seedu.address.testutil.TypicalStudents.BOB;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import seedu.address.model.student.exceptions.DuplicateStudentException;
import seedu.address.model.student.exceptions.StudentListFullException;
import seedu.address.model.student.exceptions.StudentNotFoundException;
import seedu.address.testutil.StudentBuilder;

public class UniqueStudentListTest {

    private final UniqueStudentList uniqueStudentList = new UniqueStudentList();

    // EP: student lookup is null (invalid).
    @Test
    public void contains_nullStudent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueStudentList.contains(null));
    }

    // EP: student is not in the list (absent).
    @Test
    public void contains_studentNotInList_returnsFalse() {
        assertFalse(uniqueStudentList.contains(ALICE));
    }

    // EP: exact student is in the list (present).
    @Test
    public void contains_studentInList_returnsTrue() {
        uniqueStudentList.add(ALICE);
        assertTrue(uniqueStudentList.contains(ALICE));
    }

    // EP: stored student has the same identity fields (present).
    @Test
    public void contains_studentWithSameIdentityFieldsInList_returnsTrue() {
        uniqueStudentList.add(ALICE);
        Student editedAlice = new StudentBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(uniqueStudentList.contains(editedAlice));
    }

    // EP: student-addition input is null (invalid).
    @Test
    public void add_nullStudent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueStudentList.add(null));
    }

    @Test
    public void add_duplicateStudent_throwsDuplicateStudentException() {
        uniqueStudentList.add(ALICE);
        // EP: the same student object is duplicated (invalid).
        assertThrows(DuplicateStudentException.class, () -> uniqueStudentList.add(ALICE));
        Student sameNameDifferentCase = new StudentBuilder(ALICE).withName(ALICE.getName().fullName.toLowerCase())
                .build();
        // EP: duplicate identity differs only by name case (invalid).
        assertThrows(DuplicateStudentException.class, () -> uniqueStudentList.add(sameNameDifferentCase));
    }

    // BV: result > MAX_STUDENTS (invalid).
    @Test
    public void add_studentToFullList_throwsStudentListFullException() {
        uniqueStudentList.setStudents(createStudents(UniqueStudentList.MAX_STUDENTS));

        assertThrows(StudentListFullException.class, ()
            -> uniqueStudentList.add(new StudentBuilder().withName("ExtraStudent").build()));
    }

    // One invalid input at a time: only the target student is null.
    @Test
    public void setStudent_nullTargetStudent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueStudentList.setStudent(null, ALICE));
    }

    // One invalid input at a time: only the edited student is null.
    @Test
    public void setStudent_nullEditedStudent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueStudentList.setStudent(ALICE, null));
    }

    // EP: target student is absent from the list (invalid).
    @Test
    public void setStudent_targetStudentNotInList_throwsStudentNotFoundException() {
        assertThrows(StudentNotFoundException.class, () -> uniqueStudentList.setStudent(ALICE, ALICE));
    }

    // EP: target and edited student are the same object (valid).
    @Test
    public void setStudent_editedStudentIsSameStudent_success() {
        uniqueStudentList.add(ALICE);
        uniqueStudentList.setStudent(ALICE, ALICE);
        UniqueStudentList expectedUniqueStudentList = new UniqueStudentList();
        expectedUniqueStudentList.add(ALICE);
        assertEquals(expectedUniqueStudentList, uniqueStudentList);
    }

    // EP: edited student retains the same identity (valid).
    @Test
    public void setStudent_editedStudentHasSameIdentity_success() {
        uniqueStudentList.add(ALICE);
        Student editedAlice = new StudentBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        uniqueStudentList.setStudent(ALICE, editedAlice);
        UniqueStudentList expectedUniqueStudentList = new UniqueStudentList();
        expectedUniqueStudentList.add(editedAlice);
        assertEquals(expectedUniqueStudentList, uniqueStudentList);
    }

    // EP: edited identity differs only by name case (valid).
    @Test
    public void setStudent_editedStudentNameDiffersOnlyByCase_success() {
        uniqueStudentList.add(ALICE);
        Student editedAlice = new StudentBuilder(ALICE).withName(ALICE.getName().fullName.toLowerCase()).build();
        uniqueStudentList.setStudent(ALICE, editedAlice);
        assertEquals(List.of(editedAlice), uniqueStudentList.asUnmodifiableObservableList());
    }

    // EP: edited student has a new unique identity (valid).
    @Test
    public void setStudent_editedStudentHasDifferentIdentity_success() {
        uniqueStudentList.add(ALICE);
        uniqueStudentList.setStudent(ALICE, BOB);
        UniqueStudentList expectedUniqueStudentList = new UniqueStudentList();
        expectedUniqueStudentList.add(BOB);
        assertEquals(expectedUniqueStudentList, uniqueStudentList);
    }

    @Test
    public void setStudent_duplicateStudent_throwsDuplicateStudentException() {
        uniqueStudentList.add(ALICE);
        uniqueStudentList.add(BOB);
        // EP: replacement duplicates another stored student (invalid).
        assertThrows(DuplicateStudentException.class, () -> uniqueStudentList.setStudent(ALICE, BOB));
        Student editedBob = new StudentBuilder(BOB).withName(ALICE.getName().fullName.toLowerCase()).build();
        // EP: replacement identity duplicates another name case-insensitively (invalid).
        assertThrows(DuplicateStudentException.class, () -> uniqueStudentList.setStudent(BOB, editedBob));
    }

    // EP: student removal target is null (invalid).
    @Test
    public void remove_nullStudent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueStudentList.remove(null));
    }

    // EP: removal target is absent (invalid).
    @Test
    public void remove_studentDoesNotExist_throwsStudentNotFoundException() {
        assertThrows(StudentNotFoundException.class, () -> uniqueStudentList.remove(ALICE));
    }

    // BV: count >= 0 (valid).
    @Test
    public void remove_existingStudent_removesStudent() {
        uniqueStudentList.add(ALICE);
        uniqueStudentList.remove(ALICE);
        UniqueStudentList expectedUniqueStudentList = new UniqueStudentList();
        assertEquals(expectedUniqueStudentList, uniqueStudentList);
    }

    // EP: UniqueStudentList input is null (invalid).
    @Test
    public void setStudents_nullUniqueStudentList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueStudentList.setStudents((UniqueStudentList) null));
    }

    // EP: UniqueStudentList replacement is non-null (valid).
    @Test
    public void setStudents_uniqueStudentList_replacesOwnListWithProvidedUniqueStudentList() {
        uniqueStudentList.add(ALICE);
        UniqueStudentList expectedUniqueStudentList = new UniqueStudentList();
        expectedUniqueStudentList.add(BOB);
        uniqueStudentList.setStudents(expectedUniqueStudentList);
        assertEquals(expectedUniqueStudentList, uniqueStudentList);
    }

    // EP: List input is null (invalid).
    @Test
    public void setStudents_nullList_throwsNullPointerException() {
        uniqueStudentList.add(ALICE);
        assertThrows(NullPointerException.class, () -> uniqueStudentList.setStudents((List<Student>) null));
        assertEquals(List.of(ALICE), uniqueStudentList.asUnmodifiableObservableList());
    }

    // EP: List replacement is non-null (valid).
    @Test
    public void setStudents_list_replacesOwnListWithProvidedList() {
        uniqueStudentList.add(ALICE);
        List<Student> studentList = List.of(BOB);
        uniqueStudentList.setStudents(studentList);
        UniqueStudentList expectedUniqueStudentList = new UniqueStudentList();
        expectedUniqueStudentList.add(BOB);
        assertEquals(expectedUniqueStudentList, uniqueStudentList);
    }

    // EP: replacement list contains duplicate identities (invalid).
    @Test
    public void setStudents_listWithDuplicateStudents_throwsDuplicateStudentException() {
        uniqueStudentList.add(BOB);
        Student sameNameDifferentCase = new StudentBuilder(ALICE).withName(ALICE.getName().fullName.toLowerCase())
                .build();
        List<Student> listWithDuplicateStudents = List.of(ALICE, sameNameDifferentCase);
        assertThrows(DuplicateStudentException.class, () -> uniqueStudentList.setStudents(listWithDuplicateStudents));
        assertEquals(List.of(BOB), uniqueStudentList.asUnmodifiableObservableList());
    }

    // BV: count > MAX_STUDENTS (invalid).
    @Test
    public void setStudents_listExceedingLimit_throwsStudentListFullException() {
        List<Student> students = createStudents(UniqueStudentList.MAX_STUDENTS + 1);

        assertThrows(StudentListFullException.class, () -> uniqueStudentList.setStudents(students));
    }

    // One invalid input at a time: a single-item list contains null.
    @Test
    public void setStudents_singleNullStudent_throwsNullPointerException() {
        uniqueStudentList.add(ALICE);
        List<Student> students = new ArrayList<>();
        students.add(null);

        assertThrows(NullPointerException.class, () -> uniqueStudentList.setStudents(students));
        assertEquals(List.of(ALICE), uniqueStudentList.asUnmodifiableObservableList());
    }

    // One invalid input at a time: only the middle student is null.
    @Test
    public void setStudents_multipleStudentsWithNull_throwsNullPointerException() {
        uniqueStudentList.add(ALICE);
        List<Student> students = new ArrayList<>(List.of(BOB, ALICE));
        students.add(1, null);

        assertThrows(NullPointerException.class, () -> uniqueStudentList.setStudents(students));
        assertEquals(List.of(ALICE), uniqueStudentList.asUnmodifiableObservableList());
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
            -> uniqueStudentList.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void toStringMethod() {
        assertEquals(uniqueStudentList.asUnmodifiableObservableList().toString(), uniqueStudentList.toString());
    }

    private static List<Student> createStudents(int count) {
        return IntStream.range(0, count)
                .mapToObj(index -> new StudentBuilder().withName("Student" + index).build())
                .toList();
    }
}
