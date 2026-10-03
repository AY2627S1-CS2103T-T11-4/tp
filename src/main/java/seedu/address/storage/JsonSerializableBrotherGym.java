package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.BrotherGym;
import seedu.address.model.ReadOnlyBrotherGym;
import seedu.address.model.student.Student;

/**
 * An Immutable BrotherGym that is serializable to JSON format.
 */
@JsonRootName(value = "brothergym")
class JsonSerializableBrotherGym {

    public static final String MESSAGE_DUPLICATE_STUDENT = "Students list contains duplicate student(s).";

    private final List<JsonAdaptedStudent> students = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableBrotherGym} with the given students.
     */
    @JsonCreator
    public JsonSerializableBrotherGym(@JsonProperty("students") List<JsonAdaptedStudent> students) {
        this.students.addAll(students);
    }

    /**
     * Converts a given {@code ReadOnlyBrotherGym} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableBrotherGym}.
     */
    public JsonSerializableBrotherGym(ReadOnlyBrotherGym source) {
        students.addAll(source.getStudentList().stream().map(JsonAdaptedStudent::new).collect(Collectors.toList()));
    }

    /**
     * Converts this BrotherGym into the model's {@code BrotherGym} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public BrotherGym toModelType() throws IllegalValueException {
        BrotherGym brotherGym = new BrotherGym();
        for (JsonAdaptedStudent jsonAdaptedStudent : students) {
            Student student = jsonAdaptedStudent.toModelType();
            if (brotherGym.hasStudent(student)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_STUDENT);
            }
            brotherGym.addStudent(student);
        }
        return brotherGym;
    }

}
