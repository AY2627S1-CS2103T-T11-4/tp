package seedu.address.testutil;

import seedu.address.model.BrotherGym;
import seedu.address.model.student.Student;

/**
 * A utility class to help with building BrotherGym objects.
 * Example usage: <br>
 *     {@code BrotherGym bro = new BrotherGymBuilder().withStudent("John", "Doe").build();}
 */
public class BrotherGymBuilder {

    private BrotherGym brotherGym;

    public BrotherGymBuilder() {
        brotherGym = new BrotherGym();
    }

    public BrotherGymBuilder(BrotherGym brotherGym) {
        this.brotherGym = brotherGym;
    }

    /**
     * Adds a new {@code Student} to the {@code BrotherGym} that we are building.
     */
    public BrotherGymBuilder withStudent(Student student) {
        brotherGym.addStudent(student);
        return this;
    }

    public BrotherGym build() {
        return brotherGym;
    }
}
