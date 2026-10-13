package seedu.address.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import seedu.address.model.BrotherGym;
import seedu.address.model.ReadOnlyBrotherGym;
import seedu.address.model.student.Address;
import seedu.address.model.student.Email;
import seedu.address.model.student.Name;
import seedu.address.model.student.Phone;
import seedu.address.model.student.Student;
import seedu.address.model.tag.StudentTag;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.WorkoutPlanTag;

/**
 * Contains utility methods for populating {@code BrotherGym} with sample data.
 */
public class SampleDataUtil {
    public static Student[] getSampleStudents() {
        return new Student[] {
            new Student(new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                new Address("Blk 30 Geylang Street 29, #06-40"),
                getStudentTagSet("friends")),
            new Student(new Name("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@example.com"),
                new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"),
                getStudentTagSet("colleagues", "friends")),
            new Student(new Name("Charlotte Oliveiro"), new Phone("93210283"), new Email("charlotte@example.com"),
                new Address("Blk 11 Ang Mo Kio Street 74, #11-04"),
                getStudentTagSet("neighbours")),
            new Student(new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com"),
                new Address("Blk 436 Serangoon Gardens Street 26, #16-43"),
                getStudentTagSet("family")),
            new Student(new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com"),
                new Address("Blk 47 Tampines Street 20, #17-35"),
                getStudentTagSet("classmates")),
            new Student(new Name("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@example.com"),
                new Address("Blk 45 Aljunied Street 85, #11-31"),
                getStudentTagSet("colleagues"))
        };
    }

    public static ReadOnlyBrotherGym getSampleBrotherGym() {
        BrotherGym sampleBrotherGym = new BrotherGym();
        for (Student sampleStudent : getSampleStudents()) {
            sampleBrotherGym.addStudent(sampleStudent);
        }
        return sampleBrotherGym;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    private static <T extends Tag> Set<T> getTagSet(Function<String, T> tagConstructor, String... strings) {
        return Arrays.stream(strings)
                .map(tagConstructor)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a {@code Set<StudentTag>} containing the list of strings given
     */
    public static Set<StudentTag> getStudentTagSet(String... strings) {
        return getTagSet(StudentTag::new, strings);
    }

    /**
     * Returns a {@code Set<WorkoutPlanTag>} containing the list of strings given
     */
    public static Set<WorkoutPlanTag> getWorkoutPlanTagSet(String... strings) {
        return getTagSet(WorkoutPlanTag::new, strings);
    }

}
