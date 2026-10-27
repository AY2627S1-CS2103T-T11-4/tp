package seedu.address.testutil;

import seedu.address.model.exercise.Exercise;
import seedu.address.model.exercise.ExerciseName;
import seedu.address.model.exercise.ExerciseReps;
import seedu.address.model.exercise.ExerciseWeight;

/**
 * A utility class containing typical {@code Exercise} objects to be used in tests.
 */
public class TypicalExercises {

    public static final Exercise SQUATS = new Exercise(new ExerciseName("Squats"),
            new ExerciseWeight(20.5), new ExerciseReps(10));
    public static final Exercise PUSH_UPS = new Exercise(new ExerciseName("Push-ups"),
            new ExerciseWeight(0.0), new ExerciseReps(15));
    public static final Exercise LUNGES = new Exercise(new ExerciseName("Lunges"),
            new ExerciseWeight(10.0), new ExerciseReps(12));
    public static final Exercise DEADLIFTS = new Exercise(new ExerciseName("Deadlifts"),
            new ExerciseWeight(40.0), new ExerciseReps(8));

    private TypicalExercises() {} // prevents instantiation
}
