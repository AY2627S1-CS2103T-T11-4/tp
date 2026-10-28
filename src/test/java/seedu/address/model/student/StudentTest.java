package seedu.address.model.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalStudents.ALICE;
import static seedu.address.testutil.TypicalStudents.BOB;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.tag.StudentTag;
import seedu.address.model.workoutplan.WorkoutDate;
import seedu.address.model.workoutplan.WorkoutDescription;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.WorkoutPlanStatus;
import seedu.address.model.workoutplan.WorkoutPlansList;
import seedu.address.model.workoutplan.WorkoutTitle;
import seedu.address.testutil.StudentBuilder;

public class StudentTest {

    private static final WorkoutPlan WORKOUT_PLAN = new WorkoutPlan(new WorkoutTitle("Strength"),
            new WorkoutDescription("Squats"), new WorkoutDate(new Date(0)));

    @Test
    public void updateWorkoutsState_returnsNewStudentWhenAPlanChanges() {
        Student original = new Student(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), ALICE.getTags(), new WorkoutPlansList(List.of(WORKOUT_PLAN)));
        Student updated = original.updateWorkoutsState();

        assertNotSame(original, updated);
        assertEquals(WorkoutPlanStatus.PENDING, original.getWorkoutPlansList().getWorkoutList().get(0).getStatus());
        assertEquals(WorkoutPlanStatus.OVERDUE, updated.getWorkoutPlansList().getWorkoutList().get(0).getStatus());
        assertEquals(original.getName(), updated.getName());
        assertSame(updated, updated.updateWorkoutsState());
    }

    @Test
    public void constructors_workoutPlansList() {
        assertEquals(List.of(), ALICE.getWorkoutPlansList().getWorkoutList());

        WorkoutPlansList plans = new WorkoutPlansList(List.of(WORKOUT_PLAN));
        Student aliceWithPlan = new Student(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), ALICE.getTags(), plans);
        assertEquals(plans, aliceWithPlan.getWorkoutPlansList());
        assertThrows(NullPointerException.class, () -> new Student(ALICE.getName(), ALICE.getPhone(),
                ALICE.getEmail(), ALICE.getAddress(), ALICE.getTags(), null));
    }

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Student student = new StudentBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> student.getTags().remove(0));
    }

    @Test
    public void constructor_acceptsStudentTags() {
        Set<StudentTag> tags = Set.of(new StudentTag("friend"));
        Student student = new Student(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), tags);

        Set<StudentTag> storedTags = student.getTags();
        assertEquals(Set.of(new StudentTag("friend")), storedTags);
    }

    @Test
    public void constructor_nullTag_throwsNullPointerException() {
        Set<StudentTag> tags = new HashSet<>();
        tags.add(null);

        assertThrows(NullPointerException.class, () -> new Student(ALICE.getName(), ALICE.getPhone(),
                ALICE.getEmail(), ALICE.getAddress(), tags));
    }

    @Test
    public void constructor_copiesTagsAndGetterCannotChangeThem() {
        Set<StudentTag> inputTags = new HashSet<>(Set.of(new StudentTag("friend")));
        Student student = new Student(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), inputTags);

        inputTags.clear();
        assertEquals(Set.of(new StudentTag("friend")), student.getTags());
        assertThrows(UnsupportedOperationException.class, () -> student.getTags().clear());
    }

    @Test
    public void isSameStudent() {
        // same object -> returns true
        assertTrue(ALICE.isSameStudent(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSameStudent(null));

        // same name, all other attributes different -> returns true
        Student editedAlice = new StudentBuilder(ALICE).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND).build();
        assertTrue(ALICE.isSameStudent(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = new StudentBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSameStudent(editedAlice));

        // name differs only in case, all other attributes same -> returns true
        Student editedBob = new StudentBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertTrue(BOB.isSameStudent(editedBob));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new StudentBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertFalse(BOB.isSameStudent(editedBob));

        // different workout plans do not change identity
        Student aliceWithPlan = new Student(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), ALICE.getTags(), new WorkoutPlansList(List.of(WORKOUT_PLAN)));
        assertTrue(ALICE.isSameStudent(aliceWithPlan));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Student aliceCopy = new StudentBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different student -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Student editedAlice = new StudentBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new StudentBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new StudentBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new StudentBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new StudentBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));

        // different workout plans -> returns false
        Student aliceWithPlan = new Student(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), ALICE.getTags(), new WorkoutPlansList(List.of(WORKOUT_PLAN)));
        assertFalse(ALICE.equals(aliceWithPlan));
        assertEquals(aliceWithPlan, new Student(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), ALICE.getTags(), new WorkoutPlansList(List.of(WORKOUT_PLAN))));
    }

    @Test
    public void toStringMethod() {
        String expected = Student.class.getCanonicalName() + "{name=" + ALICE.getName() + ", phone=" + ALICE.getPhone()
                + ", email=" + ALICE.getEmail() + ", address=" + ALICE.getAddress() + ", tags=" + ALICE.getTags()
                + ", workoutPlansList=" + ALICE.getWorkoutPlansList() + "}";
        assertEquals(expected, ALICE.toString());
    }
}
