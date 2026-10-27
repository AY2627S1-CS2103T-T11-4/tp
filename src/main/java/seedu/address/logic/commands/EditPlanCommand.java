package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PLAN;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.student.Student;
import seedu.address.model.tag.WorkoutPlanTag;
import seedu.address.model.workoutplan.WorkoutDate;
import seedu.address.model.workoutplan.WorkoutDescription;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.WorkoutTitle;
import seedu.address.model.workoutplan.exceptions.DuplicateWorkoutPlanException;
import seedu.address.model.workoutplan.exceptions.WorkoutPlanIndexOutOfBoundsException;

/** Edits a workout plan belonging to the currently selected student. */
public class EditPlanCommand extends Command {

    public static final String COMMAND_WORD = "editPlan";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits a workout plan from the Student currently being viewed.\n"
            + "Parameters: PLAN_ID [" + PREFIX_NAME + " PLAN_NAME] [" + PREFIX_PLAN + " DESCRIPTION] ["
            + PREFIX_DATE + " DATE(DDMMYYYY)] [" + PREFIX_TAG + " TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_NAME + " NewName " + PREFIX_PLAN + " New description "
            + PREFIX_DATE + " 15102026";

    public static final String MESSAGE_SUCCESS = "Edited workout: %1$s";
    public static final String MESSAGE_NO_STUDENT_SELECTED =
            "No student is currently selected. Use viewStudent first.";
    public static final String MESSAGE_INVALID_WORKOUT_ID =
            "Workout Plan ID exceeded total number of workout plans.";
    public static final String MESSAGE_NOT_EDITED = "At least one workout-plan field must be provided.";
    public static final String MESSAGE_DUPLICATE_PLAN = "This workout plan already exists.";

    private final Index targetIndex;
    private final EditPlanDescriptor editPlanDescriptor;

    /**
     * Creates a command to edit the plan at {@code targetIndex}.
     *
     * @param targetIndex one-based plan index supplied by the user
     * @param editPlanDescriptor fields to replace
     */
    public EditPlanCommand(Index targetIndex, EditPlanDescriptor editPlanDescriptor) {
        requireNonNull(targetIndex);
        requireNonNull(editPlanDescriptor);
        this.targetIndex = targetIndex;
        this.editPlanDescriptor = new EditPlanDescriptor(editPlanDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Student currentStudent = model.getCurrentStudent()
                .orElseThrow(() -> new CommandException(MESSAGE_NO_STUDENT_SELECTED));

        if (!editPlanDescriptor.isAnyFieldEdited()) {
            throw new CommandException(MESSAGE_NOT_EDITED);
        }

        final WorkoutPlan planToEdit;
        try {
            planToEdit = currentStudent.getWorkoutPlansList().getWorkoutPlan(targetIndex.getZeroBased());
        } catch (WorkoutPlanIndexOutOfBoundsException e) {
            throw new CommandException(MESSAGE_INVALID_WORKOUT_ID, e);
        }

        WorkoutPlan editedPlan = createEditedPlan(planToEdit, editPlanDescriptor);
        try {
            model.setWorkoutPlanForStudent(currentStudent, planToEdit, editedPlan);
        } catch (DuplicateWorkoutPlanException e) {
            throw new CommandException(MESSAGE_DUPLICATE_PLAN, e);
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(editedPlan)));
    }

    private static WorkoutPlan createEditedPlan(WorkoutPlan planToEdit, EditPlanDescriptor descriptor) {
        WorkoutTitle updatedTitle = descriptor.getTitle().orElse(planToEdit.getTitle());
        WorkoutDescription updatedDescription = descriptor.getDescription().orElse(planToEdit.getWorkouts());
        WorkoutDate updatedDate = descriptor.getDate().orElse(planToEdit.getDate());
        Set<WorkoutPlanTag> updatedTags = descriptor.getWorkoutPlanTags().orElse(planToEdit.getTags());
        return new WorkoutPlan(updatedTitle, updatedDescription, updatedDate, updatedTags,
                planToEdit.getStatus(), planToEdit.getExercises());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof EditPlanCommand otherEditPlanCommand)) {
            return false;
        }
        return targetIndex.equals(otherEditPlanCommand.targetIndex)
                && editPlanDescriptor.equals(otherEditPlanCommand.editPlanDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("editPlanDescriptor", editPlanDescriptor)
                .toString();
    }

    /** Stores the optional fields to replace on a workout plan. */
    public static class EditPlanDescriptor {
        private WorkoutTitle title;
        private WorkoutDescription description;
        private WorkoutDate date;
        private Set<WorkoutPlanTag> tags;

        public EditPlanDescriptor() {}

        /** Copy constructor. */
        public EditPlanDescriptor(EditPlanDescriptor toCopy) {
            requireNonNull(toCopy);
            setTitle(toCopy.title);
            setDescription(toCopy.description);
            setDate(toCopy.date);
            setWorkoutPlanTags(toCopy.tags);
        }

        public boolean isAnyFieldEdited() {
            return title != null || description != null || date != null || tags != null;
        }

        public void setTitle(WorkoutTitle title) {
            this.title = title;
        }

        public Optional<WorkoutTitle> getTitle() {
            return Optional.ofNullable(title);
        }

        public void setDescription(WorkoutDescription description) {
            this.description = description;
        }

        public Optional<WorkoutDescription> getDescription() {
            return Optional.ofNullable(description);
        }

        public void setDate(WorkoutDate date) {
            this.date = date;
        }

        public Optional<WorkoutDate> getDate() {
            return Optional.ofNullable(date);
        }

        /** Sets tags; an empty set explicitly clears all tags. */
        public void setWorkoutPlanTags(Set<WorkoutPlanTag> tags) {
            this.tags = tags == null ? null : new HashSet<>(tags);
        }

        public Optional<Set<WorkoutPlanTag>> getWorkoutPlanTags() {
            return tags == null
                    ? Optional.empty()
                    : Optional.of(Collections.unmodifiableSet(tags));
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }
            if (!(other instanceof EditPlanDescriptor otherDescriptor)) {
                return false;
            }
            return Objects.equals(title, otherDescriptor.title)
                    && Objects.equals(description, otherDescriptor.description)
                    && Objects.equals(date, otherDescriptor.date)
                    && Objects.equals(tags, otherDescriptor.tags);
        }

        @Override
        public int hashCode() {
            return Objects.hash(title, description, date, tags);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("title", title)
                    .add("description", description)
                    .add("date", date)
                    .add("tags", tags)
                    .toString();
        }
    }
}
