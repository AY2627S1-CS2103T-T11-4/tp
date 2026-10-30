package seedu.address.model.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import seedu.address.model.tag.StudentTag;
import seedu.address.model.tag.Tag;
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

    // EP: non-null replacement workout-plan list (valid).
    @Test
    public void withWorkoutPlans_newPlans_returnsCopyWithOtherFieldsUnchanged() {
        WorkoutPlansList newPlans = new WorkoutPlansList(List.of(WORKOUT_PLAN));

        Student copy = ALICE.withWorkoutPlans(newPlans);

        assertNotSame(ALICE, copy);
        assertEquals(newPlans, copy.getWorkoutPlansList());
        assertEquals(List.of(), ALICE.getWorkoutPlansList().getWorkoutList());
        assertEquals(ALICE.getName(), copy.getName());
        assertEquals(ALICE.getPhone(), copy.getPhone());
        assertEquals(ALICE.getEmail(), copy.getEmail());
        assertEquals(ALICE.getAddress(), copy.getAddress());
        assertEquals(ALICE.getTags(), copy.getTags());
    }

    // One invalid input at a time: only the workout-plan list is null.
    @Test
    public void withWorkoutPlans_nullPlans_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ALICE.withWorkoutPlans(null));
    }

    @Test
    public void updateWorkoutsState_returnsNewStudentWhenAPlanChanges() {
        Student original = new Student(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), ALICE.getTags(), new WorkoutPlansList(List.of(WORKOUT_PLAN)));
        Student updated = original.updateWorkoutsState();

        assertNotSame(original, updated);
        assertEquals(WorkoutPlanStatus.PENDING, original.getWorkoutPlansList().getWorkoutList().get(0).getStatus());
        assertEquals(WorkoutPlanStatus.OVERDUE, updated.getWorkoutPlansList().getWorkoutList().get(0).getStatus());
        assertEquals(original.getName(), updated.getName());
        assertEquals(updated, updated.updateWorkoutsState());
    }

    @Test
    public void constructors_workoutPlansList() {
        // BV: count = 0 (valid).
        assertEquals(List.of(), ALICE.getWorkoutPlansList().getWorkoutList());

        WorkoutPlansList plans = new WorkoutPlansList(List.of(WORKOUT_PLAN));
        Student aliceWithPlan = new Student(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), ALICE.getTags(), plans);
        // BV: count >= 0 (valid).
        assertEquals(plans, aliceWithPlan.getWorkoutPlansList());
        // One invalid input at a time: only the workout-plan list is null.
        assertThrows(NullPointerException.class, () -> new Student(ALICE.getName(), ALICE.getPhone(),
                ALICE.getEmail(), ALICE.getAddress(), ALICE.getTags(), null));
    }

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Student student = new StudentBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> student.getTags().remove(0));
    }

    // EP: a student with one non-null tag (valid).
    @Test
    public void constructor_acceptsStudentTags() {
        Set<StudentTag> tags = Set.of(new StudentTag("friend"));
        Student student = new Student(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), tags);

        Set<StudentTag> storedTags = student.getTags();
        assertEquals(Set.of(new StudentTag("friend")), storedTags);
    }

    // One invalid input at a time: the tag set contains one null tag.
    @Test
    public void constructor_nullTag_throwsNullPointerException() {
        Set<StudentTag> tags = new HashSet<>();
        tags.add(null);

        assertThrows(NullPointerException.class, () -> new Student(ALICE.getName(), ALICE.getPhone(),
                ALICE.getEmail(), ALICE.getAddress(), tags));
    }

    // BV: 0 <= count <= MAX_TAG_COUNT (valid).
    @Test
    public void constructor_tagsAtLimit_success() {
        Set<StudentTag> tags = createStudentTags(Tag.MAX_TAG_COUNT);

        Student student = new Student(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), tags);

        assertEquals(tags, student.getTags());
    }

    // BV: count > MAX_TAG_COUNT (invalid).
    @Test
    public void constructor_tooManyTags_throwsIllegalArgumentException() {
        Set<StudentTag> tags = createStudentTags(Tag.MAX_TAG_COUNT + 1);

        assertThrows(IllegalArgumentException.class, Tag.MESSAGE_TAG_LIMIT, ()
            -> new Student(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(), ALICE.getAddress(), tags));
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
        // EP: the same student object (same identity).
        assertTrue(ALICE.isSameStudent(ALICE));

        // EP: null (different identity).
        assertFalse(ALICE.isSameStudent(null));

        // EP: same name with different non-identity fields (same identity).
        Student editedAlice = new StudentBuilder(ALICE).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND).build();
        assertTrue(ALICE.isSameStudent(editedAlice));

        // EP: only the name differs (different identity).
        editedAlice = new StudentBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSameStudent(editedAlice));

        // EP: name differs only by case (same identity).
        Student editedBob = new StudentBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertTrue(BOB.isSameStudent(editedBob));

        // EP: name has trailing whitespace (different identity).
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new StudentBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertFalse(BOB.isSameStudent(editedBob));

        // EP: workout plans differ but name is unchanged (same identity).
        Student aliceWithPlan = new Student(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), ALICE.getTags(), new WorkoutPlansList(List.of(WORKOUT_PLAN)));
        assertTrue(ALICE.isSameStudent(aliceWithPlan));
    }

    @Test
    public void equals() {
        // EP: Another student with equal fields (equivalent).
        Student aliceCopy = new StudentBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // EP: The same student object (equivalent).
        assertTrue(ALICE.equals(ALICE));

        // EP: Null (non-equivalent).
        assertFalse(ALICE.equals(null));

        // EP: An object of another type (non-equivalent).
        assertFalse(ALICE.equals(5));

        // EP: A student with different fields (non-equivalent).
        assertFalse(ALICE.equals(BOB));

        // EP: Only the name differs (non-equivalent).
        Student editedAlice = new StudentBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // EP: Only the phone differs (non-equivalent).
        editedAlice = new StudentBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // EP: Only the email differs (non-equivalent).
        editedAlice = new StudentBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // EP: Only the address differs (non-equivalent).
        editedAlice = new StudentBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // EP: Only the tags differ (non-equivalent).
        editedAlice = new StudentBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));

        // EP: Only the workout-plan list differs (non-equivalent).
        Student aliceWithPlan = new Student(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), ALICE.getTags(), new WorkoutPlansList(List.of(WORKOUT_PLAN)));
        assertFalse(ALICE.equals(aliceWithPlan));
        // EP: Students with the same single workout plan (equivalent).
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

    private static Set<StudentTag> createStudentTags(int count) {
        return IntStream.range(0, count)
                .mapToObj(index -> new StudentTag("tag" + index))
                .collect(Collectors.toSet());
    }
}
