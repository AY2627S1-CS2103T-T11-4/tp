package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.student.Address;
import seedu.address.model.student.Email;
import seedu.address.model.student.Name;
import seedu.address.model.student.Phone;
import seedu.address.model.student.Student;
import seedu.address.model.tag.Tag;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.WorkoutPlansList;

/**
 * Jackson-friendly version of {@link Student}.
 */
class JsonAdaptedStudent {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Student's %s field is missing!";

    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();
    private final List<JsonAdaptedWorkoutPlan> workoutPlans = new ArrayList<>();

    @JsonCreator
    public JsonAdaptedStudent(@JsonProperty("name") String name,
                              @JsonProperty("phone") String phone,
                              @JsonProperty("email") String email,
                              @JsonProperty("address") String address,
                              @JsonProperty("tags") List<JsonAdaptedTag> tags,
                              @JsonProperty("workoutPlans") List<JsonAdaptedWorkoutPlan> workoutPlans) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        if (tags != null) {
            this.tags.addAll(tags);
        }
        if (workoutPlans != null) {
            this.workoutPlans.addAll(workoutPlans);
        }
    }

    public JsonAdaptedStudent(Student source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
        workoutPlans.addAll(source.getWorkoutPlansList().getWorkoutList().stream()
                .map(JsonAdaptedWorkoutPlan::new)
                .collect(Collectors.toList()));
    }

    public Student toModelType() throws IllegalValueException {
        if (name == null) {
            throw missing(Name.class);
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        if (phone == null) {
            throw missing(Phone.class);
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        if (email == null) {
            throw missing(Email.class);
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        if (address == null) {
            throw missing(Address.class);
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }

        List<Tag> modelTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            modelTags.add(tag.toModelType());
        }

        List<WorkoutPlan> modelPlans = new ArrayList<>();
        for (JsonAdaptedWorkoutPlan plan : workoutPlans) {
            modelPlans.add(plan.toModelType());
        }

        return new Student(new Name(name), new Phone(phone), new Email(email), new Address(address),
                new HashSet<>(modelTags), new WorkoutPlansList(modelPlans));
    }

    private static IllegalValueException missing(Class<?> fieldClass) {
        return new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                fieldClass.getSimpleName()));
    }
}
