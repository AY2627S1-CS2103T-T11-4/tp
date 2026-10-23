---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# BrotherGym Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* _{List the sources of reused or adapted ideas, code, documentation, and third-party libraries here, with links to the originals.}_

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

The following provides a quick overview of the main components and their interactions.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
  * [**`Parser`**](#parser-classes): Parsers for user inputs
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `deleteStudent 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* provides its functionality through a concrete `{Component Name}Manager` class that implements the corresponding API interface.

For example, the `Logic` component defines its API in `Logic.java` and implements it in `LogicManager.java`. Other components interact with a component through its interface rather than its concrete class, preventing them from coupling to that component's implementation, as illustrated in the following partial class diagram.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` and its parts, such as `CommandBox`, `ResultDisplay`, `StudentListPanel`, and `StatusBarFooter`. All of these, including `MainWindow`, inherit from the abstract `UiPart` class, which captures common behavior among classes that represent visible GUI parts.

The `UI` component uses the JavaFX UI framework. The layouts of these UI parts are defined in matching `.fxml` files in `src/main/resources/view`. For example, [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml) specifies the layout of [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java).

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component because it displays `Student` objects from the model.

### Logic component

**API**: [`Logic.java`](https://github.com/AY2627S1-CS2103T-T11-4/tp/blob/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="650"/>

The sequence diagram below illustrates the interactions within the `Logic` component, using `execute("deleteStudent 1")` as an example:

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Logic component interactions for the `deleteStudent 1` command" />

#### How the `Logic` component works

##### Command execution

1. The UI calls `Logic.execute(commandText)`. `LogicManager` passes the command text to `BrotherGymParser`, which returns the corresponding `Command` object.
2. `LogicManager` executes the command with the `Model`. Commands can read or update model data and return a `CommandResult`.
3. After a command executes successfully, `LogicManager` saves the BrotherGym data through `Storage` and returns the `CommandResult` to the UI. `MainWindow` displays the result's feedback; parsing, command, and storage errors are also displayed there.

##### Interaction with the UI

`Logic` exposes the filtered student list, the current student and its read-only observable property, and GUI settings to the UI.

* **Student list and selection:** The UI observes the filtered student list so it reflects changes to the student filter. `MainWindow` observes the current-student property and updates the student selection and workout-plan panel when it changes.
* **GUI settings:** The UI reads these settings when initializing the window and updates them through `Logic` when exiting.
* **Workout plans:** The `home` command shows all students and clears the current selection. Plan commands operate on the currently selected student.

#### Parser Classes
Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

Here are the classes dedicated to parsing the arguments of the command strings:

<puml src="diagrams/ArgumentProcessors.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `BrotherGymParser` class creates either an `XYZCommandParser` or a `SingleIndexCommandParser`
  * `addStudent`, `addPlan`, `editStudent` and `editPlan` commands each have a dedicated `XYZCommandParser`
    (`XYZ` is a placeholder for the specific command name, e.g., `AddStudentCommandParser`),
  * `viewStudent`, `viewPlan`, `deleteStudent` and `deletePlan` commands are `SingleIndexCommand`s that only parse a single index, 
    hence they use the same generic `SingleIndexCommandParser`
  * both types of parsers implement the `Parser` interface so they can be treated similarly where appropriate, for example during testing.
* The parser class uses a few dependencies to parse the user input into a valid command object (e.g. `AddStudentCommand`, `DeleteStudentCommand`):
  1. The user input string is passed into `ArgumentTokenizer` to create a `ArgumentMultimap` object to store the entered arguments.
  2. The `ArgumentValidator` ensures that the `ArgumentMultimap` contains the correct arguments for the command.
  3. The arguments in the `ArgumentMultimap` is then passed into the relevant `ParserUtil` class to generate the objects required for creating the `Command` object
* The `BrotherGymParser` returns that object as a `Command` object.

The sequence diagram shows how the parser classes create the `DeleteStudentCommand` object 
when `BrotherGymParser#parseCommand("deleteStudent 1")` is called from `LogicManager`:

<puml src="diagrams/AddPlanParserSequenceDiagram.puml" />

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the BrotherGym data i.e., all `Student` objects (which are contained in a `UniqueStudentList` object).
* stores the `Student` objects selected by the current filter, such as search results, in a separate _filtered_ list. It exposes this list as an unmodifiable `ObservableList<Student>` that the UI can observe and bind to, so the UI updates when the list changes.
* stores a `UserPrefs` object that represents the user’s preferences (currently, just the GUI settings). This is exposed to the outside as a `ReadOnlyUserPrefs` object.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)


<box type="info" seamless>

**Note:** The alternative, arguably more object-oriented, design below keeps a unique list of tags in `BrotherGym`, and each `Student` references tags from that list. This lets `BrotherGym` maintain one `Tag` object per unique tag instead of each `Student` holding its own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />
</box>


### Storage component

**API**: `seedu.address.storage.Storage` (defined in `src/main/java/seedu/address/storage/Storage.java`).

The `Storage` component persists BrotherGym data and user preferences as local JSON files. It converts stored data into model objects when the application starts and writes model data back to disk when requested by `LogicManager` or `MainApp`.

#### Structure and responsibilities

<puml src="diagrams/StorageClassDiagram.puml" alt="Storage classes and their JSON adapters, including nested workout plans" width="700" />

* `Storage` exposes methods to read and save BrotherGym data and user preferences, and to retrieve their file paths. Callers use this interface without handling JSON conversion themselves.
* `StorageManager` implements `Storage` and delegates operations to `JsonBrotherGymStorage` and `JsonUserPrefsStorage`, with one storage object per file.
* `JsonBrotherGymStorage` handles the BrotherGym data file, using JSON adapters to keep its stored representation separate from the model.
* `JsonUserPrefsStorage` reads and writes `UserPrefs` directly, without a separate JSON adapter.

Both file storage classes use `JsonUtil` from the `Commons` component for JSON file access. Storage depends on the model types it persists; the model does not depend on Storage.

#### Stored data and file locations

`MainApp` supplies the following paths when constructing the storage objects. Both paths are relative to the application's working directory.

| File | Contents | Representation |
|------|----------|----------------|
| `data/brothergym.json` | All students, their contact details, tags, and workout plans | `JsonSerializableBrotherGym` and its nested adapters |
| `preferences.json` | GUI settings, including window size and position | `UserPrefs` |

BrotherGym data is stored as a top-level `students` array. The following example shows one student with one workout plan:

```json
{
  "students": [
    {
      "name": "Alex Tan",
      "phone": "91234567",
      "email": "alex@example.com",
      "address": "123 Gym Road",
      "tags": ["beginner"],
      "workoutPlans": [
        {
          "title": "Leg day",
          "workoutDescription": "Squats: 3 sets of 10 reps",
          "date": "2026-10-15",
          "tags": ["strength"]
        }
      ]
    }
  ]
}
```

Workout dates use ISO local-date format (`yyyy-MM-dd`). `JsonAdaptedWorkoutPlan` converts between this string and the model's `WorkoutDate` using the system's default time zone. Tags are JSON strings; their type is determined by whether they belong to a student or a workout plan.

Missing or `null` tag lists and student workout-plan lists are treated as empty lists. Jackson ignores unknown JSON properties. These choices allow older files without the optional lists to load, but required student fields and workout-plan fields must still be present.

#### Loading and validation

During startup, `MainApp` first loads preferences through `JsonUserPrefsStorage`, then constructs `StorageManager` and reads BrotherGym data through the `Storage` interface. The sequence below shows a successful BrotherGym read; nested adapter conversions are summarized in the note.

<puml src="diagrams/StorageReadSequenceDiagram.puml" alt="Successful startup read from JSON through adapters into the model" width="800" />

1. `StorageManager` delegates `readBrotherGym()` to `JsonBrotherGymStorage`.
1. `JsonUtil.readJsonFile()` reads the file and deserializes it into `JsonSerializableBrotherGym`.
1. `JsonSerializableBrotherGym.toModelType()` creates an empty `BrotherGym` and calls `JsonAdaptedStudent.toModelType()` for each stored student. Each student adapter validates its contact fields, converts its tags using `JsonAdaptedTag.toStudentTag()`, and calls `JsonAdaptedWorkoutPlan.toModelType()` for each stored workout plan. Each workout-plan adapter checks required fields, parses its date, and converts its tags using `JsonAdaptedTag.toWorkoutPlanTag()` before constructing a `WorkoutPlan`. The student adapter then collects these plans into a `WorkoutPlansList` and constructs a `Student` using the converted contact fields, tags, and plans. Finally, `JsonSerializableBrotherGym.toModelType()` adds each reconstructed student to the `BrotherGym`. This chain assembles smaller model objects into larger ones: tags and workout fields form workout plans; contact fields, student tags, and workout plans form students; students form the complete `BrotherGym`. Model constructors also enforce their own constraints.
1. `JsonSerializableBrotherGym` rejects duplicate students using the model's `hasStudent()` check. Only a completed conversion is returned as `Optional<ReadOnlyBrotherGym>`; a failed conversion does not return a partially loaded model.

The caller decides what to use when data cannot be loaded:

| Read outcome | Storage behavior | Startup behavior in `MainApp` |
|--------------|------------------|-------------------------------|
| BrotherGym file is missing | Returns `Optional.empty()` | Initializes the model with sample data |
| BrotherGym file cannot be read or deserialized | Throws `DataLoadingException` | Logs a warning and initializes an empty BrotherGym |
| An adapter reports an `IllegalValueException`, such as an invalid phone number or duplicate student | Wraps it in `DataLoadingException` | Logs a warning and initializes an empty BrotherGym |
| Preferences are missing or loading throws `DataLoadingException` | Returns `Optional.empty()` or propagates the exception | Uses default preferences |

The fallback above applies to `DataLoadingException`. Unchecked exceptions from model constructors or malformed nested values are not all translated into this exception by the current adapters.

After initializing preferences, `MainApp` attempts to save them, including defaults if a fallback was used. Loading BrotherGym data itself does not write a data file; sample or empty data is saved on the next successful command.

#### Writing data: from model objects to JSON

Writing proceeds from the complete `BrotherGym` down to its nested objects. `JsonBrotherGymStorage.saveBrotherGym()` accepts the model through its `ReadOnlyBrotherGym` interface and constructs a separate tree of JSON adapters. This conversion uses constructors that accept model objects:

1. **BrotherGym:** `new JsonSerializableBrotherGym(brotherGym)` reads `brotherGym.getStudentList()` and creates a `JsonAdaptedStudent` for each student. These adapters form its `students` list.
1. **Students:** `new JsonAdaptedStudent(student)` extracts the student's name, phone, email, and address as strings. It converts each student tag into a `JsonAdaptedTag` and each plan in the student's `WorkoutPlansList` into a `JsonAdaptedWorkoutPlan`, collecting them into its `tags` and `workoutPlans` lists.
1. **Workout plans:** `new JsonAdaptedWorkoutPlan(plan)` extracts the title and workout description as strings, formats the date using `formatDateForStorage()`, and creates a `JsonAdaptedTag` for each workout-plan tag.
1. **Tags:** `new JsonAdaptedTag(tag)` copies the tag's name. The same adapter accepts both `StudentTag` and `WorkoutPlanTag`; its position in the adapter tree preserves which object owns the tag.
1. **JSON output:** Once the adapter tree is complete, `JsonUtil.saveJsonFile(data, filePath)` uses Jackson to serialize it into formatted JSON. The adapter lists become JSON arrays, and `JsonAdaptedTag` uses `@JsonValue` to serialize each tag as a string. `FileUtil` then writes the JSON text to the destination file in UTF-8.

The resulting structure matches the example under **Stored data and file locations**: the top-level `students` array contains student objects, each student's `workoutPlans` array contains workout-plan objects, and both students and workout plans have their own `tags` arrays. The adapters copy values from the model without modifying it.

#### Automatic saving and write failures

`LogicManager.execute()` calls `storage.saveBrotherGym(model.getBrotherGym())` after a command executes successfully and before returning its `CommandResult`. This happens for every successful command, including commands that only change the view. Parsing errors and command execution errors prevent this save step from being reached.

<puml src="diagrams/StorageSaveSequenceDiagram.puml" alt="Successful automatic save after a command, from the model through JSON adapters to disk" width="800" />

Before conversion, `JsonBrotherGymStorage` creates the data file and any missing parent directories. Each save replaces the existing file contents with the entire BrotherGym rather than appending an individual change. `MainApp.stop()` separately saves the current user preferences on shutdown.

If saving BrotherGym data throws an `IOException`, `LogicManager` reports a `CommandException`; an `AccessDeniedException` produces a message about insufficient write permissions. The command has already executed at this point, so a failed save does not roll back its changes in memory. Preference save failures are logged by `MainApp`.

<box type="info" seamless>

**Persistence limitations:** Writes currently go directly to the destination file, without an atomic file replacement or automatic backup. A write failure can leave the file incomplete. After startup falls back to an empty BrotherGym, the next successful command attempts to overwrite the existing data file with the current in-memory data.
</box>

Workout plans store their `status` as `PENDING`, `COMPLETED`, or `OVERDUE`. Reading a file preserves the saved status
without recalculating deadlines. Older files with a missing or null status default to `PENDING`; invalid status values
cause a data-loading error. Date-based statuses are refreshed when the user runs `viewStudent`.

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedBrotherGym`. It extends `BrotherGym` with an undo/redo history, stored internally as an `brotherGymStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedBrotherGym#commit()` -- Saves the current BrotherGym state in its history.
* `VersionedBrotherGym#undo()` -- Restores the previous BrotherGym state from its history.
* `VersionedBrotherGym#redo()` -- Restores a previously undone BrotherGym state from its history.

These operations are exposed in the `Model` interface as `Model#commitBrotherGym()`, `Model#undoBrotherGym()` and `Model#redoBrotherGym()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedBrotherGym` will be initialized with the initial BrotherGym state, and the `currentStatePointer` pointing to that single BrotherGym state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th student in the BrotherGym. The `delete` command calls `Model#commitBrotherGym()`, causing the modified state of the BrotherGym after the `delete 5` command executes to be saved in the `brotherGymStateList`, and the `currentStatePointer` is shifted to the newly inserted BrotherGym state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new student. The `add` command also calls `Model#commitBrotherGym()`, causing another modified BrotherGym state to be saved into the `brotherGymStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitBrotherGym()`, so the BrotherGym state will not be saved into the `brotherGymStateList`.
</box>

Step 4. The user now decides that adding the student was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoBrotherGym()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous BrotherGym state, and restores the BrotherGym to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial BrotherGym state, then there are no previous BrotherGym states to restore. The `undo` command uses `Model#canUndoBrotherGym()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.
</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X), but due to a limitation of PlantUML, it continues to the end of the diagram.
</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoBrotherGym()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the BrotherGym to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `brotherGymStateList.size() - 1`, pointing to the latest BrotherGym state, then there are no undone BrotherGym states to restore. The `redo` command uses `Model#canRedoBrotherGym()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.
</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the BrotherGym, such as `list`, will usually not call `Model#commitBrotherGym()`, `Model#undoBrotherGym()` or `Model#redoBrotherGym()`. Thus, the `brotherGymStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitBrotherGym()`. Since the `currentStatePointer` is not pointing at the end of the `brotherGymStateList`, all BrotherGym states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo execute:**

* **Alternative 1 (current choice):** Saves the entire BrotherGym.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the student being deleted).
  * Cons: We must ensure that the implementation of each individual command is correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**: 
* Gym trainers who want to keep track of students and their workouts
* has a need to manage a significant number of contacts
* prefers desktop apps over other types of applications
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: 
A gym trainer who wants to plan & track the workouts of his student. He wants to plan the workout split of his students, selecting exercises from different muscle groups studentalised to each student. Afterwards, while the student goes to the gym, the trainer can log the actual reps that student has done, tracking his--her sets, reps, rest time and weights for progressive overload.

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

**Student management**

| Priority | As a ... | I want to ... | So that I can... |
|----------|----------|---------------|------------------|
| `* * *`  | gym trainer | add my student's contact number to the BrotherGym | keep their contact information accessible |
| `* * *`  | gym trainer with multiple students | hold multiple contact numbers in my BrotherGym | keep track of all my students' contact information |
| `* * *`  | gym trainer | remove a student's record from the BrotherGym | clear up space in the BrotherGym |

**Workout planning**

| Priority | As a ... | I want to ... | So that I can... |
|----------|----------|---------------|------------------|
| `* * *`  | gym trainer | store different types of workout plans per student | ensure each workout can focus on a different muscle group |
| `* * *`  | gym trainer | add different workouts to each workout plan | ensure that workout sessions are customised for each student |
| `* * *`  | gym trainer | view all the workout plans for each student | pick which plan to follow during the student's next session |

### Use cases

Note: For all use cases below, the **Software System** is BrotherGym and the **Actor** is a Gym Trainer.

| ID | Use case |
|----|----------|
| U1 | [Add a student](#add-a-student) |
| U2 | [View all students](#view-all-students) |
| U3 | [Delete a student](#delete-a-student) |
| U4 | [View workout plans for a specific student](#view-workout-plans-for-a-specific-student) |
| U5 | [Add a workout plan](#add-a-workout-plan) |
| U6 | [Remove a workout plan](#remove-a-workout-plan) |
| U7 | [View exercises of a workout plan](#view-exercises-of-a-workout-plan) |

### Add a student

Use case: U1 - Add a student

**MSS:**

1. Trainer <u>views all students (U2)</u>.
2. Trainer requests to add a new student, providing the student's name and handphone number.
3. BrotherGym adds the student.
4. BrotherGym displays a success message.
5. BrotherGym displays the updated list of students.

   Use case ends.

**Extensions:**

* 2a. Trainer provides student details in an invalid format.
  * 2a1. BrotherGym displays an error message.

    Use case resumes at step 2.
* 2b. The student already exists in BrotherGym.
  * 2b1. BrotherGym displays an error message.

    Use case resumes at step 2.

### View all students

Use case: U2 - View all students

**MSS:**

1. Trainer requests to view all students.
2. BrotherGym displays all students in the system.

   Use case ends.

### Delete a student

Use case: U3 - Delete a student

**MSS:**

1. Trainer <u>views all students (U2)</u>.
2. Trainer requests to delete a selected student.
3. BrotherGym deletes the student.
4. BrotherGym displays a success message.
5. BrotherGym displays the updated list of students.

   Use case ends.

**Extensions:**

* 2a. Trainer selects an invalid student.
  * 2a1. BrotherGym displays an error message.

    Use case resumes at step 2.

### View workout plans for a specific student

Use case: U4 - View workout plans for a specific student

**MSS:**

1. Trainer <u>views all students (U2)</u>.
2. Trainer selects a specific student.
3. BrotherGym displays all workout plans assigned to the student.

   Use case ends.

**Extensions:**

* 2a. Trainer selects an invalid student.
  * 2a1. BrotherGym displays an error message.

    Use case resumes at step 2.

### Add a workout plan

Use case: U5 - Add a workout plan

**MSS:**

1. Trainer <u>views workout plans for a specific student (U4)</u>.
2. Trainer requests to add a workout plan for the student, providing its name, description, and date.
3. BrotherGym assigns the workout plan to the student.
4. BrotherGym displays a success message.
5. BrotherGym displays the student's updated workout plans.

   Use case ends.

**Extensions:**

* 2a. Trainer provides invalid workout plan information.
  * 2a1. BrotherGym displays an error message.

    Use case resumes at step 2.

### Remove a workout plan

Use case: U6 - Remove a workout plan

**MSS:**

1. Trainer <u>views workout plans for a specific student (U4)</u>.
2. Trainer chooses a workout plan to remove.
3. BrotherGym removes the selected workout plan.
4. BrotherGym displays a success message.
5. BrotherGym displays the student's updated workout plans.

   Use case ends.

**Extensions:**

* 2a. Trainer selects an invalid workout plan.
  * 2a1. BrotherGym displays an error message.

    Use case resumes at step 2.

### View exercises of a workout plan

Use case: U7 - View exercises of a workout plan

**MSS:**

1. Trainer <u>views workout plans for a specific student (U4)</u>.
2. Trainer selects a workout plan belonging to the student.
3. BrotherGym displays information about the exercises in the selected workout plan.

   Use case ends.

**Extensions:**

* 2a. Trainer selects an invalid workout plan.
  * 2a1. BrotherGym displays an error message.

    Use case resumes at step 2.

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `25` or above installed.
2.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
3.  The application should support at least 1,000 students and 10,000 workout records on a typical modern computer.
4.  Common commands should complete within 2 seconds during typical usage.
5.  Student and workout data should be stored locally and should not be transmitted to external services by the application.
6.  Successfully completed changes to student and workout records should be saved automatically and remain recoverable from the local data file after a normal application restart.
7.  Invalid commands should display a clear error message without causing the application to crash or terminate unexpectedly.
8.  Commands should follow a consistent syntax and provide consistent feedback when they succeed or fail.
9.  New workout types and workout-planning features should be addable without requiring major changes to existing student-management features.
10. Core student-management and workout-tracking functions should work without an Internet connection.
11. The application should be distributable as a JAR file and should not require additional services to launch.
12. Core application logic should be sufficiently modular to support automated unit and integration testing.
13. Invalid student or workout data should be rejected without corrupting or unintentionally modifying existing records.
14. The application should launch within 5 seconds when using a data file containing up to 1,000 students and 10,000 workout records.
15. Student and workout information should be displayed in a clear and readable format so that trainers can identify relevant records quickly.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, or macOS
* **Trainer**: The user who manages students’ workout plans and records their workout progress.
* **Student**: A student whose workouts are planned and tracked by a trainer.
* **Workout plan**: A prescribed set of exercises and targets prepared for a student.
* **Workout session**: A specific occasion on which a student carries out a workout plan and their progress may be recorded.
* **Exercise**: A physical activity within a workout plan, such as a bench press.
* **Set**: A group of consecutive repetitions of an exercise.
* **Repetition (rep)**: One complete performance of an exercise movement.
* **Weight**: The resistance prescribed or recorded for an exercise, in kilograms.
* **Student ID**: The positive integer used to identify a student within BrotherGym.
* **Workout plan ID**: The positive integer used to identify a workout plan belonging to a student.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.
</box>

### Launch and shutdown

1. Initial launch

   1. Download the JAR file and copy it into an empty folder.

   1. Double-click the JAR file.<br>
      Expected: The GUI opens with a set of sample contacts. The window size may not be optimal.

1. Saving window preferences

   1. Resize the window to an optimal size. Move the window to a different location. Close the window.

   1. Relaunch the app by double-clicking the JAR file.<br>
       Expected: The most recent window size and location are retained.

1. _{ more test cases … }_

### Deleting a student

1. Deleting a student while all students are being shown

   1. Prerequisites: List all students using the `home` command, with multiple students in the list.

   1. Test case: `deleteStudent 1`<br>
      Expected: The first contact is deleted from the list. The status message shows the deleted contact's details.

   1. Test case: `deleteStudent 0`<br>
      Expected: No student is deleted. The status message shows error details.

   1. Other incorrect delete commands to try: `deleteStudent`, `deleteStudent x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases … }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{Explain how to simulate missing or corrupted data files and state the expected behavior.}_

1. _{ more test cases … }_
