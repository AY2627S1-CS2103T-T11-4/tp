package seedu.address.testutil;

import java.util.List;
import java.util.Set;

import seedu.address.model.BrotherGym;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.exercise.Exercise;
import seedu.address.model.exercise.ExercisesList;
import seedu.address.model.student.Student;
import seedu.address.model.workoutplan.WorkoutDate;
import seedu.address.model.workoutplan.WorkoutDescription;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.WorkoutPlanStatus;
import seedu.address.model.workoutplan.WorkoutPlansList;
import seedu.address.model.workoutplan.WorkoutTitle;

/** Builds models with workout plans and exercises for command tests. */
public class ExerciseTestUtil {

    private ExerciseTestUtil() {
    }

    /** Creates a pending plan with the given exercises. */
    public static WorkoutPlan plan(String title, String description, WorkoutDate date, List<Exercise> exercises) {
        return new WorkoutPlan(new WorkoutTitle(title), new WorkoutDescription(description), date,
                Set.of(), WorkoutPlanStatus.PENDING, new ExercisesList(exercises));
    }

    /** Copies a student and replaces their workout plans. */
    public static Student withPlans(Student base, WorkoutPlan... plans) {
        return new Student(base.getName(), base.getPhone(), base.getEmail(), base.getAddress(),
                base.getTags(), new WorkoutPlansList(List.of(plans)));
    }

    /** Creates a model containing the given students. */
    public static ModelManager modelWith(Student... students) {
        BrotherGym data = new BrotherGym();
        for (Student student : students) {
            data.addStudent(student);
        }
        return new ModelManager(data, new UserPrefs());
    }
}
