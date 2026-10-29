package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.exercise.Exercise;
import seedu.address.model.exercise.ExerciseDescription;
import seedu.address.model.exercise.ExerciseName;
import seedu.address.model.exercise.exceptions.DuplicateExerciseException;
import seedu.address.model.exercise.exceptions.ExerciseIndexOutOfBoundsException;
import seedu.address.model.exercise.exceptions.ExerciseNotFoundException;
import seedu.address.model.student.Student;
import seedu.address.model.student.exceptions.StudentNotFoundException;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.exceptions.WorkoutPlanNotFoundException;

/** Edits an exercise in the selected workout plan. */
public class EditExerciseCommand extends Command {

    public static final String COMMAND_WORD = "editExercise";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits an exercise in the selected workout plan.\n"
            + "Parameters: EXERCISE_ID [" + PREFIX_NAME + " NAME] ["
            + PREFIX_DESCRIPTION + " DESCRIPTION]\n"
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_NAME + " Goblet Squat "
            + PREFIX_DESCRIPTION + " 3 sets of 10 reps";
    public static final String MESSAGE_SUCCESS = "Edited exercise: %1$s";
    public static final String MESSAGE_NO_STUDENT_SELECTED =
            "No student is currently selected. Use viewStudent first.";
    public static final String MESSAGE_NO_PLAN_SELECTED =
            "No workout plan is currently selected. Use viewPlan first.";
    public static final String MESSAGE_INVALID_EXERCISE_ID =
            "The exercise index provided is invalid for the selected workout plan.";
    public static final String MESSAGE_NOT_EDITED = "At least one exercise field must be provided.";
    public static final String MESSAGE_DUPLICATE_EXERCISE =
            "This exercise already exists in the selected workout plan.";
    public static final String MESSAGE_PLAN_NO_LONGER_AVAILABLE =
            "The selected workout plan is no longer available. Use viewPlan to select a plan.";

    private final Index targetIndex;
    private final EditExerciseDescriptor editExerciseDescriptor;

    /** Creates a command to edit the exercise at {@code targetIndex}. */
    public EditExerciseCommand(Index targetIndex, EditExerciseDescriptor editExerciseDescriptor) {
        requireNonNull(targetIndex);
        requireNonNull(editExerciseDescriptor);
        this.targetIndex = targetIndex;
        this.editExerciseDescriptor = new EditExerciseDescriptor(editExerciseDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Student student = model.getCurrentStudent()
                .orElseThrow(() -> new CommandException(MESSAGE_NO_STUDENT_SELECTED));
        WorkoutPlan plan = model.getCurrentWorkoutPlan()
                .orElseThrow(() -> new CommandException(MESSAGE_NO_PLAN_SELECTED));
        if (!editExerciseDescriptor.isAnyFieldEdited()) {
            throw new CommandException(MESSAGE_NOT_EDITED);
        }

        Exercise exerciseToEdit = getExercise(plan);
        Exercise editedExercise = createEditedExercise(exerciseToEdit, editExerciseDescriptor);
        try {
            model.setExerciseForWorkoutPlan(student, plan, exerciseToEdit, editedExercise);
        } catch (DuplicateExerciseException e) {
            throw new CommandException(MESSAGE_DUPLICATE_EXERCISE, e);
        } catch (StudentNotFoundException | WorkoutPlanNotFoundException e) {
            throw new CommandException(MESSAGE_PLAN_NO_LONGER_AVAILABLE, e);
        } catch (ExerciseNotFoundException e) {
            throw new CommandException(MESSAGE_INVALID_EXERCISE_ID, e);
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, editedExercise));
    }

    private Exercise getExercise(WorkoutPlan plan) throws CommandException {
        try {
            return plan.getExercisesList().getExercise(targetIndex.getZeroBased());
        } catch (ExerciseIndexOutOfBoundsException e) {
            throw new CommandException(MESSAGE_INVALID_EXERCISE_ID, e);
        }
    }

    private static Exercise createEditedExercise(Exercise exercise,
            EditExerciseDescriptor descriptor) {
        ExerciseName name = descriptor.getName().orElse(exercise.getName());
        ExerciseDescription description = descriptor.getDescription().orElse(exercise.getDescription());
        return new Exercise(name, description);
    }

    @Override
    public boolean equals(Object other) {
        return other == this || other instanceof EditExerciseCommand command
                && targetIndex.equals(command.targetIndex)
                && editExerciseDescriptor.equals(command.editExerciseDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("editExerciseDescriptor", editExerciseDescriptor)
                .toString();
    }

    /** Stores the optional fields to replace on an exercise. */
    public static class EditExerciseDescriptor {
        private ExerciseName name;
        private ExerciseDescription description;

        public EditExerciseDescriptor() {}

        /** Copy constructor. */
        public EditExerciseDescriptor(EditExerciseDescriptor toCopy) {
            requireNonNull(toCopy);
            name = toCopy.name;
            description = toCopy.description;
        }

        public boolean isAnyFieldEdited() {
            return name != null || description != null;
        }

        public void setName(ExerciseName name) {
            this.name = name;
        }

        public Optional<ExerciseName> getName() {
            return Optional.ofNullable(name);
        }

        public void setDescription(ExerciseDescription description) {
            this.description = description;
        }

        public Optional<ExerciseDescription> getDescription() {
            return Optional.ofNullable(description);
        }

        @Override
        public boolean equals(Object other) {
            return other == this || other instanceof EditExerciseDescriptor descriptor
                    && Objects.equals(name, descriptor.name)
                    && Objects.equals(description, descriptor.description);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, description);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("description", description)
                    .toString();
        }
    }
}
