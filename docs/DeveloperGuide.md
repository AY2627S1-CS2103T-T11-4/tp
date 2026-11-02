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

The UI is coordinated by `MainWindow`, which owns the command box, result display, student list, workout-plan list, and status bar, and opens the Help window.

* `StudentListPanel` displays students using `StudentCard` objects.
* `WorkoutPlanListPanel` displays the current student's workout plans using `WorkoutPlanCard` objects. It also controls which card is expanded to show its numbered exercises.
* `CommandBox` passes command text to `MainWindow`, while `ResultDisplay` shows the returned feedback or error message.
* The visible UI classes inherit from the abstract `UiPart` class, which provides their common FXML-loading behavior.

The `UI` component uses the JavaFX UI framework. The layouts of these UI parts are defined in matching `.fxml` files in `src/main/resources/view`. For example, [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml) specifies the layout of [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java).

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to the current student and selected workout plan exposed by `Logic`, so `MainWindow` can keep `StudentListPanel` and `WorkoutPlanListPanel` synchronized with the current navigation state.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
  * the `HelpWindow` also requires [command metadata](#metadata-classes) from `Logic` to render the command cards of the help page.
* depends on some classes in the `Model` component because it displays `Student` and `WorkoutPlan` objects from the model.

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

`Logic` exposes the filtered student list, the current student and its read-only observable property, GUI settings, and the command categories displayed in the Help window to the UI.

* **Student list and selection:** The UI observes the filtered student list so it reflects changes to the student filter. `MainWindow` observes the current-student property and updates the student selection and workout-plan panel when it changes.
* **GUI settings:** The UI reads these settings when initializing the window and updates them through `Logic` when exiting.
* **Workout plans and exercises:** The `home` command shows all students and clears the current selection. Plan commands operate on the selected student; `viewPlan` selects a plan within that student. Exercise commands operate only on this selected plan.
* **Help page metadata:** `LogicManager#getCommandCategories()` obtains the command categories from `CommandMetadataCatalog` and exposes them through the `Logic` interface for the UI to display.

#### Metadata Classes

The `logic.metadata` package supplies the command information displayed in the Help window.

<puml src="diagrams/CommandMetadataClasses.puml" width="500"/>

* `CommandMetadataCatalog` constructs and exposes the ordered list of command categories.
* `CommandCategory` groups related commands under a Help page section title.
* `CommandMetadata` stores a command's word, parameters, description, and example. Its `format()` method produces the command format displayed by the UI.

The `CommandMetadataCatalog` object is passed to the `HelpWindow` of the `Ui` component through the `LogicManager` facade, which uses it to create 
* one section for each `CommandCategory`; and 
* one command card for each `CommandMetadata` entry.

#### Parser Classes
Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="800"/>

Here are the classes dedicated to parsing the arguments of the command strings:

<puml src="diagrams/ArgumentProcessors.puml" width="600"/>

How the parsing works:

* After identifying the command word, `BrotherGymParser` delegates the arguments to one of the following parser categories:

| Parser category                    | Commands | Responsibility                                                                           |
|------------------------------------|----------|------------------------------------------------------------------------------------------|
| Dedicated `XYZCommandParser`       | `addStudent`, `addPlan`, `addExercise`, `editStudent`, `editPlan`, `editExercise`, `find` | Each command has its own dedicated parser (`XYZ` is a placeholder for the command name). |
| Generic `SingleIndexCommandParser` | `viewStudent`, `viewPlan`, `deleteStudent`, `deletePlan`, `deleteExercise`, `markPlan`, `unmarkPlan` | Generic parser for `SingleIndexCommand`s that accept a single positive index.            |
| Generic `NoArgumentCommandParser`  | `help`, `home`, `exit`, `clear` | Generic parser that rejects any arguments after the command word.                        |

* Every concrete parser represented in the table implements the `Parser` interface.

* The parser class uses a few dependencies to parse the user input into a valid command object (e.g. `AddStudentCommand`, `DeleteStudentCommand`):
  1. The user input string is passed into `ArgumentTokenizer` to create a `ArgumentMultimap` object to store the entered arguments.
  2. The `ArgumentValidator` ensures that the `ArgumentMultimap` contains the correct arguments for the command.
  3. The arguments in the `ArgumentMultimap` is then passed into the relevant `ParserUtil` class to generate the objects required for creating the corresponding command object

* The `BrotherGymParser` returns the created command as `Command` object.

The sequence diagram shows how the parser classes create the `DeleteStudentCommand` object 
when `BrotherGymParser#parseCommand("deleteStudent 1")` is called from `LogicManager`:

<puml src="diagrams/DeleteParserSequenceDiagram.puml" alt="Sequence Diagram for parsing 'delete 1'"/>

#### Parser Exception Handling
The `ArgumentValidator` and `ParserUtil` classes shown above also define the workflow’s two failure paths:
* Format errors (e.g. incorrect use of prefixes) of command inputs produce an `InputFormatInvalidException` during argument validation by `ArgumentValidator`
* Invalid values provided for the command arguments produce a `FieldValueInvalidException` during field parsing by `ParserUtil`.

Both exception types are subclasses of `ParseException`, and the parser methods wrap the raw exceptions with the command usage message
in another `ParseException` object to be thrown to the higher levels.

### Model component
**API** : [`Model.java`](https://github.com/AY2627S1-CS2103T-T11-4/tp/blob/master/src/main/java/seedu/address/model/Model.java)

The class diagram below shows the high-level structure of the `Model` component.

<puml src="diagrams/ModelClassDiagram.puml" width="650" />


The `Model` component,

* stores all `Student` objects in a `UniqueStudentList`.
* exposes students matching the current filter, such as search results, as an unmodifiable `ObservableList<Student>` that the UI can observe and bind to.
* stores a `UserPrefs` object that represents the user’s preferences (currently, just the GUI settings). This is exposed to the outside as a `ReadOnlyUserPrefs` object.
* tracks `currentStudent`, the `Student` whose workout plans the user has chosen to view or edit.
* tracks `currentWorkoutPlan`, the `WorkoutPlan` that belongs to `currentStudent`, of which the user has chosen to view or edit.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)


The entity diagram expands `UniqueStudentList` and `Student` to show student details, tags, and their workout plan list. Details of `WorkoutPlan` are omitted for clarity and will be shown in a later diagram:

<puml src="diagrams/ModelEntitiesClassDiagram.puml" alt="Student entities, tags, and workout plan list" width="800" />

The following diagram shows the entities referenced by `WorkoutPlan`.

<puml src="diagrams/WorkoutPlanClassDiagram.puml" alt="Workout plan entities, exercises, and tags" width="1000" />

#### Model checks and state updates

The `Model` package enforces domain rules when model objects are constructed or changed. This is accomplished in two main ways.

1. Entities enforce their own constraints when the entity objects are created. An example is the checking of the character limit of certain entity fields.

2. `ModelManager` imposes validity checks when a method attempts to change the state of `BrotherGym`, delegating membership and uniqueness checks to `BrotherGym` and the list classes.

#### Interaction with Model during command execution

Changes to nested plan and exercise data follow an immutable-update pattern: each edit builds replacement lists and entities up to a replacement `Student`, reusing unchanged fields, then replaces the stored student.

#### Entity construction

The construction stage depends on whether the entity can be created from the supplied fields alone or requires existing model data. Constructing an entity does not itself change the stored data; checks that depend on the selected student or existing plans take place during command execution.

For `addPlan`, parsing can produce a complete plan without reading the selected student's data. The command holds this plan until execution, when the selected student is known:

<puml src="diagrams/AddPlanEntityCreationSequenceDiagram.puml" alt="Creating a workout plan from addPlan input" width="850" />

For `editPlan`, parsing produces an `EditPlanDescriptor` containing the requested changes rather than a complete replacement plan. Construction must wait until execution because omitted fields depend on the stored plan. The target index is also checked against that student's plans at this stage:

<puml src="diagrams/EditPlanEntityCreationSequenceDiagram.puml" alt="Creating an edited workout plan from the selected student" width="1000" />

#### Updating stored data

The following diagrams show a successful `addPlan`. The second diagram expands the `Build updatedStudent` reference frame in the first.

<puml src="diagrams/AddPlanBrotherGymUpdateSequenceDiagram.puml" alt="Adding a workout plan to BrotherGym" width="1000" />

<puml src="diagrams/AddPlanUpdatedStudentSequenceDiagram.puml" alt="Constructing the updated student for addPlan" width="900" />

The command supplies the requested change through the Model API, while `ModelManager` maintains the stored data and selection together. This keeps commands from having to manage references to objects replaced by nested updates. Exercise changes follow the same approach, with an additional replacement `WorkoutPlan` containing the updated exercises.

Selection maintenance is omitted from the diagrams. After replacing the selected student, `ModelManager` points `currentStudent` to the stored replacement. A retained plan remains selected; when an edit or exercise change replaces the selected plan, selection follows its replacement. Removing that plan clears the plan selection, and a student that no longer matches the current filter has both selections cleared.

After a successful change to workout plans or exercises, the command refreshes the current student's workout statuses through `updateStudentWorkoutsState`. This can replace the student again; unchanged statuses reuse the existing student. The internals of status refresh are omitted from the diagrams above.

Status refresh preserves completed plans and determines whether other plans are pending or overdue using the current date. Deferring this refresh until the requested operation succeeds prevents a rejected operation, such as a duplicate addition, from changing existing statuses. If refresh replaces the selected plan, its selection is retained at the same list index.

### Storage component
**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

The `Storage` component persists BrotherGym data and user preferences as local JSON files. It converts stored data into model objects when the application starts and writes model data back to disk when requested by `LogicManager` or `MainApp`.

The `Storage` component,

<puml src="diagrams/StorageClassDiagram.puml" alt="Storage classes and their JSON adapters, including nested workout plans and exercises" width="450" />

* `Storage` exposes methods to read and save BrotherGym data and user preferences, and to retrieve their file paths. Callers use this interface without handling JSON conversion themselves.
* `StorageManager` implements `Storage` and delegates operations to `JsonBrotherGymStorage` and `JsonUserPrefsStorage`, with one storage object per file.
* `JsonBrotherGymStorage` handles the BrotherGym data file, using JSON adapters to keep its stored representation separate from the model.
* `JsonUserPrefsStorage` reads and writes `UserPrefs` directly, without a separate JSON adapter.

Both file storage classes use `JsonUtil` from the `Commons` component for JSON file access. Storage depends on the model types it persists; the model does not depend on Storage.

#### Loading and validation

During startup, `MainApp` loads preferences and BrotherGym data through Storage. `StorageManager` delegates the data read to `JsonBrotherGymStorage`, which uses `JsonUtil` to deserialize JSON into adapters. The adapters reconstruct the model hierarchy of students, workout plans, exercises, and tags for student and workout-plan.

<puml src="diagrams/StorageModelConversionSequenceDiagram.puml" alt="Nested conversion of JSON adapters into model objects" width="800" />

Adapters validate stored values during conversion, while model classes enforce domain constraints such as uniqueness and list limits. Storage returns the reconstructed model only after the entire conversion succeeds.

#### Writing data: from model objects to JSON

`StorageManager` delegates saves to `JsonBrotherGymStorage`, which converts the `ReadOnlyBrotherGym` model into `JsonSerializableBrotherGym` and its nested adapters. This preserves the student, workout-plan, and exercise hierarchy without modifying the model.

`JsonAdaptedWorkoutPlan` writes the plan's name under the existing `"title"` JSON key, so previously saved data remains readable.

`JsonUtil` uses Jackson to serialize the adapters and write the JSON file. Keeping conversion in the adapters separates the stored representation from domain logic.

#### Automatic saving

`LogicManager.execute()` calls `storage.saveBrotherGym(model.getBrotherGym())` after a command executes successfully and before returning its `CommandResult`. This happens for every successful command, including commands that only change the view. Parsing errors and command execution errors prevent this save step from being reached.

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Workout plan display

#### Implementation

The workout plan display is facilitated by `WorkoutPlanListPanel` and `WorkoutPlanCard`. `MainWindow` observes the current student and selected plan through `Logic#currentStudentProperty()` and `Logic#currentWorkoutPlanProperty()`.

Workout-plan statuses are refreshed explicitly when `viewStudent` is executed. `ViewStudentCommand` calls `Model#updateStudentWorkoutsState()` to recalculate each plan as pending or overdue for the current date, then calls `Model#setCurrentStudent()` with the refreshed student. This command-triggered refresh avoids a background timer and gives status changes a clear point in the application flow.

Changing either selection notifies `MainWindow`, which coalesces related property changes before passing the final student and plan state to `WorkoutPlanListPanel#showStudent()`. With no selected plan, the panel shows the student's collapsed plan cards. `viewPlan` expands the selected card in its original list position and renders numbered exercises inside it, while sibling cards remain available through the same outer scroll pane. An empty plan shows an `Exercises (0)` heading and an inline empty message. With no current student, the panel prompts the user to select one.

The same observable flow redraws the panel after commands such as `markPlan`, `unmarkPlan`, `editExercise`, and `deleteExercise` replace the selected plan. Because plans are immutable, `ModelManager` rebinds the selected plan to its replacement.

The panel treats the same stored instance, or its replacement at the same list index, as a retained selection. It then restores the previous scroll offset. Selecting a different plan scrolls only far enough to reveal its card.

Deleting another plan retains and reindexes the selection. Deleting the selected plan instead collapses the remaining list without selecting a replacement. Running `viewStudent` collapses the selection and returns the panel to the top, while navigating away from the student clears the panel state. This keeps the visible selection and subsequent exercise commands aligned with the same stored plan.

Each `WorkoutPlanCard` reads the plan's name, date, workout description, tags, and status through `WorkoutPlan` getters. Tags are sorted by name and rendered individually as labels, giving a stable display order without relying on `WorkoutPlan#toString()`. The card maps the status to visible `DONE`, `OVERDUE`, or `PENDING` text and applies matching CSS classes to both the badge and card.

The following sequence diagram shows how the panel is updated when the user executes `viewStudent 1`:

<puml src="diagrams/WorkoutPlanUiSequenceDiagram.puml" alt="Command-triggered workout status refresh and workout plan panel update" />

#### Design considerations

**Aspect: When to refresh time-dependent workout-plan statuses:**

* **Alternative 1 (current choice):** Refresh statuses explicitly when `viewStudent` is executed.
  * Pros: Status changes happen at a predictable point without a background timer, and the model remains responsible for calculating status.
  * Cons: A plan does not automatically become overdue while its student remains open across midnight; the user must execute `viewStudent` again to refresh it.
* **Alternative 2:** Recalculate statuses continuously or whenever a card is rendered.
  * Pros: The display always reflects the current time without requiring another command.
  * Cons: A timer adds lifecycle complexity, while calculating status in the UI mixes model rules with presentation logic.

**Aspect: Keeping the workout plan panel synchronized with navigation:**

* **Alternative 1 (current choice):** Observe the selected student and plan properties in `Logic` and expand the selected card within the existing list.
  * Pros: The panel updates when navigation or immutable replacements change either selection, while the surrounding plans remain visible for context.
  * Cons: Commands and `Model` operations must update both properties correctly, and the panel must preserve a sensible viewport while cards expand, collapse, or are reindexed.
* **Alternative 2:** Call `WorkoutPlanListPanel#showStudent()` manually after every successful command.
  * Pros: The refresh is explicit in the command-execution flow.
  * Cons: The UI is refreshed even for unrelated commands, and new commands can fail to refresh the panel if the manual call is omitted.

### Deleting an exercise

`deleteExercise EXERCISE_ID` uses `SingleIndexCommandParser` and the one-based exercise number shown by `viewPlan`. It requires a selected student and plan. Missing selection or an invalid index leaves the model unchanged.

`Model#removeExerciseForWorkoutPlan` replaces the immutable exercise list, plan, and student. The selected plan is rebound to its replacement, so the panel and subsequent exercise indexes reflect the deletion. Student and plan edits and status changes retain exercises; removing a selected parent or clearing data clears its selection.

### Editing an exercise

`EditExerciseCommandParser` accepts the displayed one-based exercise index followed by optional `--name` and `--description` values. It validates the argument structure before parsing the index and supplied fields. Repeated or unsupported prefixes, invalid field values, and commands without an edited field are rejected before a command is created.

When `EditExerciseCommand` executes, it first requires both a selected student and selected workout plan. It locates the exercise using the displayed index and copies any omitted field from the existing exercise. It then calls `Model#setExerciseForWorkoutPlan` with the replacement.

The exercise remains at the same position, and the selected workout card stays expanded to show the updated value. An out-of-range index, duplicate result, or stale selection produces a command error without a partial update.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

Undo/redo allows the user to move through complete application states. Each state contains the BrotherGym data, including students and their workout plans, together with the current student-list view. Undoing or redoing a command therefore restores both the data and where the user was working.

The following example shows how undo/redo behaves when the user adds a student, selects an existing student, and manages a workout plan. Assume that Alice is the first student in the BrotherGym and has no workout plans initially.

Step 1. The user launches the application. The initial state shows all students and no student is selected.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `addStudent David Tan --hp 91234567 --email david@example.com --addr David Street`. The student data changes, but the user remains on the home view.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `viewStudent 1` to select Alice and display her workout plans. This creates another restorable view state.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it does not create a history entry. The current data, view, and available redo states remain unchanged.
</box>

Step 4. The user executes `addPlan Strength --plan Upper body --date 31122099`. The plan is added to Alice and Alice remains selected.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />

Step 5. The user executes `undo`. The workout plan disappears, but Alice remains selected because the previous state was Alice's student view.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

<box type="info" seamless>

**Note:** If there is no previous state, the `undo` command returns an error rather than changing the data or view. The `redo` command behaves similarly when there is no previously undone state.
</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X), but due to a limitation of PlantUML, it continues to the end of the diagram.
</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

Step 6. The user executes `undo` again. The application returns to the home view, with no student selected. David remains in the student list because the earlier `addStudent` change was not undone.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The user can now execute `redo` twice. The first `redo` restores Alice's selected-student view, and the second `redo` restores the workout plan. The same process works for student mutations such as `addStudent`, `editStudent`, `deleteStudent`, and `clear`, and for the other workout-plan mutations `editPlan`, `deletePlan`, `markPlan`, and `unmarkPlan`.

If the user executes `undo` once more and then executes `home`, the application creates a new view state. This discards the available redo branch, so a subsequent `redo` returns an error. A new successful data or view change after an undo behaves in the same way.

<box type="info" seamless>

**Note:** Repeating an identical `home` or `viewStudent` operation does not create an extra history entry. A view-only command such as `viewPlan`, `help`, or `exit` also leaves the undo/redo history unchanged.
</box>

`viewStudent` is a special case because it may refresh date-based workout-plan statuses. If the refresh changes a status, that data change and the new selected-student view are restored as one undoable step.

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo execute:**

* **Alternative 1 (current choice):** Saves the entire BrotherGym.
  * Pros: Easy to implement and restores students together with their workout plans.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `deleteStudent`, just save the student being deleted).
  * Cons: We must ensure that the implementation of each individual command is correct.

The active BrotherGym data is saved after every successful command, including successful undo and redo. The undo/redo history and the current view are kept in memory only. After restarting the application, the latest saved data remains, but previous undo/redo steps are no longer available and the application starts at its default home view.

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

## **Appendix: Instructions for Manual Testing**

The following walkthrough provides a starting point for manual testing. Testers are encouraged to explore
additional variations. Refer to the [User Guide](UserGuide.md#features) for command syntax and field constraints.
This appendix should only serve as a starting point to familiarise testers with the functionalities of BrotherGym.

### Before you begin

* Follow the [Quick start](UserGuide.md#quick-start) instructions to launch the JAR from a separate, empty
  test folder. Keep the same working directory when relaunching so the app uses the same data file.
* Use only test data: the walkthrough includes `clear` and deletion commands.
* Enter `help` and check that the help window opens with readable command instructions; close it to continue.
* Run the sections in order and enter each command on a separate line. Unless a step says otherwise, retain the
  data from the previous section. IDs below assume this order; after exploratory changes, use the displayed IDs
  or recreate the dataset.

### Prepare a predictable dataset

Enter the following commands. `clear` removes the initial sample data in this test instance.

```text
clear
addStudent Alex Tan --hp 91234567 --email alex@example.com --addr 10 Clementi Road --tag beginner
addStudent Beth Lim --hp 92345678 --email beth@example.com --addr 20 Dover Road
viewStudent 1
addPlan Strength Session --plan Practise controlled movements --date 31122099 --tag strength
addPlan Previous Session --plan Review completed training --date 01012000
viewPlan 1
addExercise Squat --description 3 sets of 8 reps at 30kg
addExercise Plank --description 3 sets of 30 seconds
```

Expected: There are two students. Alex has two plans, with two numbered exercises in the expanded first plan.
Beth has no plans. The first plan is `PENDING` and the second is `OVERDUE`, assuming the test date is between
2 January 2000 and 31 December 2099 (inclusive). For the later checks, which change the first plan
to 30 December 2099, the expected `PENDING` status assumes the test date is on or before that date.

### Manage students and navigate between views

1. Enter the following commands:

   ```text
   find bEtH
   viewStudent 1
   home
   viewStudent 1
   ```

   Expected: The search displays only Beth, numbered 1, and `viewStudent 1` shows her empty workout list.
   `home` restores both students and clears the selection. The final command shows Alex's two plans.

2. Update Alex's details and tags:

   ```text
   editStudent 1 --name Alex Teo --hp 93456789 --email alexteo@example.com --addr 30 Clementi Road --tag regular
   editStudent 1 --clearTags
   ```

   Expected: The first command updates the specified details and replaces `beginner` with `regular`.
   The second removes all student tags. Alex's plans and exercises remain intact.

3. Try adding a duplicate:

   ```text
   addStudent alex teo --hp 94567890 --email duplicate@example.com --addr 40 Dover Road
   ```

   Expected: The command is rejected despite the different letter case and contact details. There are still two students.

### Manage workout plans and track progress

1. Select Alex and edit the first plan:

   ```text
   viewStudent 1
   viewPlan 1
   editPlan 1 --name Strength Training --plan Focus on controlled technique --date 30122099 --tag technique
   editPlan 1 --clearTags
   ```

   Expected: The first plan's details update, its tags are replaced and then cleared, and its two exercises remain.
   The plan stays expanded throughout these edits.
   Try `addPlan Strength Training --plan Focus on controlled technique --date 30122099`.
   Expected: The duplicate is rejected and there are still two plans.

2. Check status changes:

   ```text
   viewStudent 1
   markPlan 1
   markPlan 1
   unmarkPlan 1
   markPlan 2
   unmarkPlan 2
   unmarkPlan 2
   ```

   Expected: Plan 1 becomes `DONE`, the repeated mark is rejected, and unmarking restores `PENDING`.
   Plan 2 becomes `DONE`, then returns to `OVERDUE`; the repeated unmark is rejected.

3. Check calendar validation:

   ```text
   editPlan 1 --date 31022027
   editPlan 1 --date 29022028
   editPlan 1 --date 30122099
   ```

   Expected: The impossible date is rejected without changing the plan. The leap-day date is accepted, and the
   final command restores the future date. For the today boundary, temporarily set an unmarked plan's date to
   today's date in `DDMMYYYY` format: its status should be `PENDING`. Restore the original date afterward.

4. Enter `viewPlan 2`, then `viewPlan 1`.

   Expected: Only the selected plan expands, while both plans remain in their original order from the one
   scrollable list. Plan 2 shows an empty exercise list. The selected plan's details and numbered exercises
   are readable.
   `viewStudent 1` collapses the expansion and returns the list to the top; enter `viewPlan 1` again to
   continue.

### Manage exercises within a selected plan

Prerequisite: Alex's first plan is expanded and still contains Squat and Plank.

1. Enter:

   ```text
   editExercise 1 --description 3 sets of 10 reps at 30kg
   addExercise Squat --description 3 sets of 10 reps at 30kg
   editExercise 2 --name Squat --description 3 sets of 10 reps at 30kg
   ```

   Expected: The first exercise keeps its name and position while its description changes. Both subsequent
   commands are rejected as duplicates, leaving the two exercises unchanged.

2. Enter:

   ```text
   editExercise 2 --name Side Plank
   deleteExercise 1
   deleteExercise 1
   ```

   Expected: Renaming retains the second exercise's description. The first deletion leaves Side Plank numbered 1;
   the second leaves an empty plan showing `Exercises (none)` and an empty message. The plan stays expanded.

3. Add an exercise back for the saving checks:

   ```text
   addExercise Goblet Squat --description 3 sets of 10 reps at 12kg
   viewPlan 2
   viewStudent 2
   viewStudent 1
   viewPlan 1
   ```

   Expected: Plan 2 remains empty and Beth still has no plans. Returning to Alex's first plan shows Goblet Squat,
   confirming that exercise changes affected only the selected plan.

### Check selection rules and rejected inputs

1. Enter these commands in order:

   ```text
   home
   addPlan Unselected Session --plan Selection check --date 31122099
   viewStudent 1
   addExercise Unselected Exercise --description Selection check
   viewPlan 1
   ```

   Expected: The plan command is rejected because no student is selected. The exercise command is rejected
   because no plan is selected. Selecting the student and plan afterward succeeds without losing existing data.

2. With Alex and plan 1 selected, try this representative set of invalid inputs:

   | Input | What to check |
   | --- | --- |
   | `viewStudent 0` | Non-positive index is rejected. |
   | `viewPlan 01` | Leading-zero index is rejected. |
   | `deleteExercise 999` | Out-of-range index is rejected; Goblet Squat remains. |
   | `addExercise Curl` | Missing required description is reported. |
   | `editExercise 1 --tag strength` | Unsupported prefix is rejected. |
   | `editExercise 1 --name Curl --name Row` | Repeated single-value prefix is rejected. |
   | `editStudent 1` | An edit without any fields is rejected. |
   | `find !!!` | Search without a valid keyword is rejected. |
   | `home extra` | Extra arguments to a no-argument command are rejected. |

   Expected for each: Feedback explains the error and no student, plan, or exercise data changes.
   Enter `viewStudent 1` and `viewPlan 2` afterward to confirm that valid commands still work.

### Verify saving, deletion, and recovery

1. **Persist the nested data.** Enter `markPlan 2`, then `exit`. Relaunch from the same working directory and
   enter `viewStudent 1`, then `viewPlan 1`.

   Expected: Alex's edited contact details, cleared tags, both plans, and Goblet Squat are retained. Plan 2 is
   still `DONE`. Check the saved data rather than expecting the previous view selection to be restored.

2. **Delete plans around an expanded plan.** Enter `viewPlan 2`, then `deletePlan 1`.

   Expected: Previous Session remains expanded and becomes plan 1. Enter `deletePlan 1` again.
   The selected plan disappears and the list collapses without selecting another plan.

3. **Delete a student with nested data.** Recreate a plan and exercise for Alex:

   ```text
   addPlan Cleanup Session --plan Check nested deletion --date 31122099
   viewPlan 1
   addExercise Lunge --description 2 sets of 10 reps
   home
   find Alex
   deleteStudent 1
   home
   ```

   Expected: Alex and the associated plan and exercise are removed. Beth remains unchanged. Enter `clear`,
   then `exit`, and relaunch. The student list remains empty rather than being replaced by sample data.

4. **Missing data file.** Close the app and copy `data/brothergym.json` to a backup outside the `data` folder.
   Remove the original file and relaunch from the same working directory.

   Expected: The app opens with sample data. Close the app and restore the backup before the next check.

5. **Corrupted data file.** With the app closed and the backup retained, replace the contents of
   `data/brothergym.json` with the following invalid JSON, then relaunch:

   ```text
   { invalid json
   ```

   Expected: The app opens with an empty student list. Before entering any command, inspect the file externally:
   it should still contain the invalid text. Enter `home` and inspect it again: the successful command saves
   valid data for the empty list. Close the app, restore the backup, and relaunch to verify recovery.
