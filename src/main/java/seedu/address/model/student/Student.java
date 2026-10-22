package seedu.address.model.student;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.StudentTag;
import seedu.address.model.workoutplan.WorkoutPlansList;

/**
 * Represents a Student in BrotherGym.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Student {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final Set<StudentTag> tags;
    private final WorkoutPlansList workoutPlansList;

    /**
     * Creates a student with tags and workout plans. Every field must be present and not null.
     */
    public Student(
            Name name,
            Phone phone,
            Email email,
            Address address,
            Set<StudentTag> tags,
            WorkoutPlansList workoutPlansList) {
        requireAllNonNull(name, phone, email, address, tags, workoutPlansList);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        tags.forEach(Objects::requireNonNull);
        this.tags = Collections.unmodifiableSet(new HashSet<>(tags));
        this.workoutPlansList = workoutPlansList;
    }

    /**
     * Creates a student with tags and an empty workout plan list.
     */
    public Student(Name name, Phone phone, Email email, Address address, Set<StudentTag> tags) {
        this(name, phone, email, address, tags, new WorkoutPlansList());
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    /**
     * Returns the student's unmodifiable, typed tags.
     */
    public Set<StudentTag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public WorkoutPlansList getWorkoutPlansList() {
        return workoutPlansList;
    }

    /** Returns a student whose workout plans have refreshed statuses. */
    public Student updateWorkoutsState() {
        WorkoutPlansList updatedPlans = workoutPlansList.updateState();
        return updatedPlans == workoutPlansList ? this
                : new Student(name, phone, email, address, tags, updatedPlans);
    }

    /**
     * Returns true if both students have the same name.
     * This defines a weaker notion of equality between two students.
     */
    public boolean isSameStudent(Student otherStudent) {
        if (otherStudent == this) {
            return true;
        }

        return otherStudent != null
                && otherStudent.getName().equals(getName());
    }

    /**
     * Returns true if both students have the same identity and data fields.
     * This defines a stronger notion of equality between two students.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Student otherStudent)) {
            return false;
        }

        return name.equals(otherStudent.name)
                && phone.equals(otherStudent.phone)
                && email.equals(otherStudent.email)
                && address.equals(otherStudent.address)
                && tags.equals(otherStudent.tags)
                && workoutPlansList.equals(otherStudent.workoutPlansList);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, tags, workoutPlansList);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("tags", tags)
                .add("workoutPlansList", workoutPlansList)
                .toString();
    }

}
