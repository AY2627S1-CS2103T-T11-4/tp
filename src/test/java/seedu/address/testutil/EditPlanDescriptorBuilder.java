package seedu.address.testutil;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.commands.EditPlanCommand.EditPlanDescriptor;
import seedu.address.model.tag.WorkoutPlanTag;
import seedu.address.model.workoutplan.WorkoutDate;
import seedu.address.model.workoutplan.WorkoutDescription;
import seedu.address.model.workoutplan.WorkoutPlan;
import seedu.address.model.workoutplan.WorkoutTitle;

/**
 * A utility class to help with building EditPlanDescriptor objects.
 */
public class EditPlanDescriptorBuilder {

    private EditPlanDescriptor descriptor;

    public EditPlanDescriptorBuilder() {
        descriptor = new EditPlanDescriptor();
    }

    public EditPlanDescriptorBuilder(EditPlanDescriptor descriptor) {
        this.descriptor = new EditPlanDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditPlanDescriptor} with fields containing {@code student}'s details
     */
    public EditPlanDescriptorBuilder(WorkoutPlan plan) {
        descriptor = new EditPlanDescriptor();
        descriptor.setTitle(plan.getTitle());
        descriptor.setDescription(plan.getWorkouts());
        descriptor.setDate(plan.getDate());
        descriptor.setWorkoutPlanTags(plan.getTags());
    }

    /**
     * Sets the {@code Title} of the {@code EditPlanDescriptor} that we are building.
     */
    public EditPlanDescriptorBuilder withTitle(String title) {
        descriptor.setTitle(new WorkoutTitle(title));
        return this;
    }

    /**
     * Sets the {@code description} of the {@code EditPlanDescriptor} that we are building.
     */
    public EditPlanDescriptorBuilder withDescription(String desc) {
        descriptor.setDescription(new WorkoutDescription(desc));
        return this;
    }

    /**
     * Sets the {@code date} of the {@code EditPlanDescriptor} that we are building.
     */
    public EditPlanDescriptorBuilder withDate(LocalDate date) {
        descriptor.setDate(WorkoutDate.fromLocalDate(date));
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<WorkoutTag>} and sets it to the {@code EditPlanDescriptor}
     * that we are building.
     */
    public EditPlanDescriptorBuilder withTags(String... tags) {
        Set<WorkoutPlanTag> tagSet = Stream.of(tags).map(WorkoutPlanTag::new).collect(Collectors.toSet());
        descriptor.setWorkoutPlanTags(tagSet);
        return this;
    }

    public EditPlanDescriptor build() {
        return descriptor;
    }
}
