package seedu.address.testutil;

import seedu.address.model.exercise.Exercise;
import seedu.address.model.exercise.ExerciseDescription;
import seedu.address.model.exercise.ExerciseName;

/**
 * A utility class containing typical {@code Exercise} objects to be used in tests.
 */
public class TypicalExercises {

    public static final Exercise SQUATS = new Exercise(new ExerciseName("Squats"),
            new ExerciseDescription("Barbell back squat"));
    public static final Exercise PUSH_UPS = new Exercise(new ExerciseName("Push Ups"),
            new ExerciseDescription("Bodyweight push-up"));
    public static final Exercise LUNGES = new Exercise(new ExerciseName("Lunges"),
            new ExerciseDescription("Forward lunges"));
    public static final Exercise DEADLIFTS = new Exercise(new ExerciseName("Deadlifts"),
            new ExerciseDescription("Conventional deadlift"));

    private TypicalExercises() {} // prevents instantiation
}
