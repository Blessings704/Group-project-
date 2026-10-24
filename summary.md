# Pocket Budget — Complete Lab-by-Lab Detailed Summary

Welcome to the comprehensive, beginner-friendly summary of the **Pocket Budget** app project! This document walks through every single lab (Lab 0 to Lab 8) from the ground up. It explains the core goals, the files created or modified, what specific changes were made, and what every technical term means in simple everyday English.

---

## Lab 0 — Set Up and Run on Device

### 1. Goal of the Lab
The primary goal of Lab 0 is to set up the Android Studio development environment, create a brand new Android project with the correct configuration settings, and successfully execute the empty app on both an emulator and a real physical phone over USB.

### 2. Files Created or Modified
- `app/build.gradle.kts`: Gradle build configuration file for the app module.
- `app/src/main/res/values/strings.xml`: Centralized resource file for text strings.
- `app/src/main/AndroidManifest.xml`: App configuration file registering app metadata.

### 3. What Was Modified or Added
- Configured the project name to `PocketBudget`, namespace/application ID to `com.group1.pocketbudget`, minimum SDK version to `24`, and build language to Kotlin DSL.
- Updated the application label in `strings.xml` to `Pocket Budget` so the title bar on the phone reads correctly.
- Verified physical device connection using `adb devices`.

### 4. What Each Update Means & Plain-English Technical Explanations
- **Namespace & Application ID (`com.group1.pocketbudget`)**: Think of this as the unique digital fingerprint or home address of your app across the entire Android ecosystem. No two apps on the Google Play Store can have the same application ID.
- **Minimum SDK (API 24 / Android 7.0)**: This tells Android: *"Our app will only run on phones running Android 7.0 or newer."* This ensures we can use modern programming tools while supporting almost all active phones today.
- **Gradle Build Script (`build.gradle.kts`)**: Gradle is the automated building assistant in Android Studio. It takes all your Kotlin files, images, and layout XML files, compiles them, and bundles them into an installed `.apk` app file.

---

## Lab 1 — The Entry Screen and Input Validation

### 1. Goal of the Lab
To build the main user entry screen where users can type in an expense description, input a monetary amount, write optional notes, and tap a Save button. We also enabled **ViewBinding** to safely talk to UI elements without crashes, and added robust input validation.

### 2. Files Created or Modified
- `app/src/main/res/layout/activity_main.xml`: The visual UI blueprint for the main screen.
- `app/src/main/res/values/strings.xml`: Added hint strings and validation error messages.
- `app/src/main/java/com/group1/pocketbudget/MainActivity.kt`: The Kotlin code controlling the main screen behavior.

### 3. What Was Modified or Added
- Enabled `viewBinding = true` in `app/build.gradle.kts` so we don't have to use error-prone `findViewById`.
- Created a `ConstraintLayout` inside a `NestedScrollView` in `activity_main.xml` containing:
  - Description `EditText` (text input).
  - Multi-line note `EditText`.
  - Amount `EditText` (`inputType="numberDecimal"`).
  - Save `Button` placed side-by-side with the amount field.
  - Status `TextView` to display feedback messages.
- Added validation logic in `MainActivity.kt`:
  - If the description is empty, show `Describe the expense first`.
  - If the amount is empty, non-numeric, or zero (parsed via `toDoubleOrNull()`), show `Enter an amount above zero`.
  - Otherwise, format the saved message using placeholders (`%1$s` and `%2$,.2f`) and display it.

### 4. What Each Update Means & Plain-English Technical Explanations
- **ViewBinding**: In older Android development, programmers used `findViewById(R.id.myButton)` to grab UI buttons. If you spelled an ID wrong, the app would instantly crash at runtime. ViewBinding automatically generates a safe Kotlin binding class for your XML layout, letting you access UI items directly with dot notation (`binding.saveButton`) with zero crash risk.
- **ConstraintLayout**: A flexible layout manager that lets you position UI elements relative to each other or to the parent screen boundaries (e.g., placing the Save button to the right of the Amount field).
- **`toDoubleOrNull()`**: A safe Kotlin function that attempts to convert user text into a decimal number. If the user types letters like `"abc"`, instead of crashing the app, it gracefully returns `null` so we can show a polite error message.

---

## Lab 2 — Categories, Switches, and Checkboxes

### 1. Goal of the Lab
To expand the entry form by adding categorization options (Food, Transport, Books, Other) using radio buttons, an Essential switch (`MaterialSwitch`), and a Monthly Recurring checkbox (`CheckBox`), along with instant UI feedback listeners.

### 2. Files Created or Modified
- `app/src/main/res/layout/activity_main.xml`: Added RadioGroup, MaterialSwitch, and CheckBox elements.
- `app/src/main/res/values/strings.xml`: Added category names, essential tags, and format strings.
- `app/src/main/java/com/group1/pocketbudget/MainActivity.kt`: Added event listeners and summary formatting.

### 3. What Was Modified or Added
- Added a horizontal `RadioGroup` with four `RadioButton` items (`Food`, `Transport`, `Books`, `Other`), with `Food` checked by default.
- Added a `MaterialSwitch` labelled `Essential` and a `CheckBox` labelled `Recurring every month`.
- Connected instant feedback listeners:
  - Toggling the switch instantly updates the status line text and changes its text color (red for essential, black for normal).
  - Changing the radio group selection instantly shows `Category: Transport` (etc.).
  - Selecting `Food` automatically disables and unticks the recurring checkbox (since food expenses don't recur monthly in this app design).
- On Save, combined all inputs into a complete summary string (`Lunch · Food · MWK 2,500.00 · ESSENTIAL`). If recurring is ticked, wrote `Log.i("PocketBudget", "Recurring: Lunch, 2500.00")` to Logcat.

### 4. What Each Update Means & Plain-English Technical Explanations
- **RadioGroup**: A container that groups multiple radio buttons together so that selecting one automatically unselects all the others.
- **Event Listeners (`setOnCheckedChangeListener`)**: Pieces of code that wait passively in the background and instantly wake up to execute instructions the moment a user flips a switch, taps a radio button, or clicks a button.
- **Logcat**: The built-in developer logging console in Android Studio where background messages (`Log.i` or `Log.d`) are printed for debugging purposes.

---

## Lab 3 — Surviving Rotation and Activity Lifecycle

### 1. Goal of the Lab
To understand Android's activity lifecycle callbacks, track the total money spent during the current app session (`sessionTotal`), and ensure user data (status text, session total, and switch position) survives screen rotation and process death.

### 2. Files Created or Modified
- `app/src/main/java/com/group1/pocketbudget/MainActivity.kt`: Added lifecycle logging overrides and state save/restore methods.
- `app/src/main/res/values/strings.xml`: Added session total format strings.

### 3. What Was Modified or Added
- Logged all 7 activity lifecycle callbacks (`onCreate`, `onStart`, `onResume`, `onPause`, `onStop`, `onRestart`, `onDestroy`) using `Log.d` with the tag `PocketBudgetLife`.
- Added a `Spent this session` TextView (`sessionTotalText`) that accumulates every amount saved while the app is open.
- Implemented `onSaveInstanceState` and `onRestoreInstanceState`:
  - Used `outState.putDouble` to save the `sessionTotal` `var`.
  - Saved the status text and essential switch state into the Bundle.
  - Tested process death resilience using `adb shell am kill com.group1.pocketbudget`.

### 4. What Each Update Means & Plain-English Technical Explanations
- **Activity Lifecycle**: Android activities go through various stages as the user interacts with them (e.g., opening, rotating, backgrounding, closing). Understanding these callbacks lets you run code at the exact right moment.
- **Screen Rotation & Activity Destruction**: When you rotate your phone, Android instantly destroys the current Activity and recreates a brand new one from scratch to load the landscape layout. Any temporary `var` in Kotlin (like `sessionTotal`) would normally be wiped out during this destruction unless you explicitly save it.
- **Bundle & `onSaveInstanceState`**: A lightweight key-value storage container provided by Android just before rotation happens. You pack your data into the bundle, rotate the phone, and unpack it in `onCreate` or `onRestoreInstanceState` so the user notices zero data loss.

---

## Lab 4 — Inside Pocket Budget (Architecture & Manifest)

### 1. Goal of the Lab
To document the internal project structure, review the application manifest, understand how Android discovers app activities, and analyze APK packaging.

### 2. Files Created or Modified
- `app/src/main/AndroidManifest.xml`: Configured app activities and intent filters.

### 3. What Was Modified or Added
- Reviewed and annotated `AndroidManifest.xml` explaining the application label, activity declarations, and the `MAIN` / `LAUNCHER` intent filter.
- Verified how commenting out the launcher intent filter removes the app icon from the phone's home screen.
- Mapped out the project file hierarchy (layouts, Kotlin classes, string resources).

### 4. What Each Update Means & Plain-English Technical Explanations
- **`AndroidManifest.xml`**: The master XML configuration passport required by the Android operating system for every app. It lists all screens (activities), permissions, and hardware requirements before the app can be installed or run.
- **Intent Filter**: A rulebook attached to an activity telling the Android system: *"Hey, my app responds to this specific action (like being opened from the phone's home screen launcher)"*.

---

## Lab 5 — The Detail Screen and Intents

### 1. Goal of the Lab
To build a secondary screen (`ExpensesDetailActivity`) that receives expense details from `MainActivity` via explicit Intents, allows sharing text to external apps, and returns a review status back using the modern Activity Result API.

### 2. Files Created or Modified
- `app/src/main/res/layout/activity_expenses_detail.xml`: Layout blueprint for the detail screen.
- `app/src/main/java/com/group1/pocketbudget/ExpensesDetailActivity.kt`: Kotlin code for the detail activity.
- `app/src/main/java/com/group1/pocketbudget/MainActivity.kt`: Added explicit intent launcher and result receiver.
- `app/src/main/res/values/strings.xml`: Added detail format strings and share chooser titles.

### 3. What Was Modified or Added
- Created `ExpensesDetailActivity` featuring title, category, amount, description, essential status, a Share button, a "Mark reviewed" button, and a Back button.
- Passed data from `MainActivity` using explicit Intents carrying extras defined as companion object constants (`EXTRA_TITLE`, `EXTRA_AMOUNT`, etc.).
- Implemented `registerForActivityResult` in `MainActivity` to receive results:
  - Tapping "Mark reviewed" returns `RESULT_OK` and displays `Reviewed: <title>`.
  - Pressing Back returns `RESULT_CANCELED` and displays `Review cancelled`.
- Implemented social sharing using an `ACTION_SEND` intent with `text/plain` and a chooser titled `Share expense via`.

### 4. What Each Update Means & Plain-English Technical Explanations
- **Explicit Intent**: A messaging object used in Android to explicitly request opening a specific, known target activity within your app (e.g., *"Open ExpensesDetailActivity and carry these extra values with you"*).
- **Activity Result API (`registerForActivityResult`)**: The modern, safe way to launch another screen expecting data back. Instead of old callback methods that could cause memory leaks, this clean contract handles responses safely.
- **Implicit Intent (`ACTION_SEND`)**: A messaging object where you don't specify *which* app should handle the request (e.g., *"I want to share this text!"*). Android pops open a share sheet showing WhatsApp, Email, Notes, etc., letting the user pick their preferred app.

---

## Lab 6 — The Expense List and RecyclerView

### 1. Goal of the Lab
To display saved expenses in a scrollable list of cards using `RecyclerView` and `CardView`, where tapping a card opens the detail screen and long-pressing a card deletes the expense.

### 2. Files Created or Modified
- `app/src/main/res/layout/item_expense.xml`: Card design layout using `MaterialCardView`.
- `app/src/main/java/com/group1/pocketbudget/ExpensesAdapter.kt`: The adapter connecting data to RecyclerView cards.
- `app/src/main/java/com/group1/pocketbudget/MainActivity.kt`: Wired RecyclerView adapter and lambda click listeners.

### 3. What Was Modified or Added
- Created `item_expense.xml` featuring a card layout where title and amount appear on the top row, and category/flags appear on the second row.
- Created `ExpensesAdapter` taking two lambda functions in its constructor: one for a standard tap and one for a long-press.
- Logged each `onCreateViewHolder` call to demonstrate view holder recycling.
- Added auto-scrolling to the top (`scrollToPosition(0)`) whenever a new expense is saved.

### 4. What Each Update Means & Plain-English Technical Explanations
- **`RecyclerView`**: A high-performance scrolling list container in Android. Unlike old list views, `RecyclerView` recycles view items as they scroll off-screen, saving massive amounts of memory and ensuring buttery-smooth 60fps scrolling.
- **Adapter**: The bridge between your raw list of data (`List<Expense>`) and the visual cards (`RecyclerView`). It translates data items into clickable visual rows.
- **Lambda Functions (`(Expense) -> Unit`)**: Passable blocks of executable code. Instead of forcing the adapter to know what clicking a card does, `MainActivity` passes custom actions right into the adapter's constructor.

---

## Lab 7 — Data Persistence with Room Database and LiveData

### 1. Goal of the Lab
To replace temporary in-memory lists with a permanent **Room SQLite database**, ensuring all expense records survive app restarts, process termination, and phone reboots. We also added automatic reactive UI updates using **LiveData** and calculated all-time totals using SQL `SUM(amount)`.

### 2. Files Created or Modified
- `app/build.gradle.kts`: Added Room database dependencies and KSP plugin.
- `app/src/main/java/com/group1/pocketbudget/Expense.kt`: Converted data class into a Room `@Entity`.
- `app/src/main/java/com/group1/pocketbudget/ExpenseDao.kt`: Created database query interface.
- `app/src/main/java/com/group1/pocketbudget/ExpenseDatabase.kt`: Created Room database singleton.
- `app/src/main/java/com/group1/pocketbudget/ExpenseRepository.kt`: Created data repository.
- `app/src/main/java/com/group1/pocketbudget/ExpenseViewModel.kt`: Created ViewModel with coroutine scoping.
- `app/src/main/java/com/group1/pocketbudget/MainActivity.kt`: Observed LiveData streams for list and all-time total.

### 3. What Was Modified or Added
- Annotated `Expense` with `@Entity(tableName = "expenses")` and `@PrimaryKey(autoGenerate = true)` on `expenseId`.
- Created `ExpenseDao` with `@Insert`, `@Delete`, `getAll()` ordered newest first, `getByCategory()`, and `getTotal()` returning `LiveData<Double?>` from `SELECT SUM(amount) FROM expenses`.
- Created `ExpenseDatabase` singleton named `expense_database`.
- Created `ExpenseViewModel` running database inserts and deletes inside background coroutines (`viewModelScope.launch`).
- Connected `MainActivity` to observe `allExpenses` and `total` LiveData streams, displaying `0.00` when the table is empty (`total ?: 0.0`).
- Configured long-press deletion to call `viewModel.delete(expense)` directly without any manual list-refreshing code.

### 4. What Each Update Means & Plain-English Technical Explanations
- **Room Database**: An official SQLite database abstraction library by Google. It gives you compile-time safety checks for your SQL queries and wraps raw database tables into clean Kotlin objects.
- **DAO (Data Access Object)**: An interface where you write SQL queries using annotations (like `@Query` or `@Insert`). Room automatically generates the underlying database code for you.
- **Singleton Pattern**: A software design pattern ensuring that only **one single instance** of the database ever exists in memory while your app is running, preventing resource leaks and race conditions.
- **Coroutines & `viewModelScope.launch`**: Database operations take time and can freeze the screen if run on the main UI thread. Coroutines allow us to perform heavy database tasks in the background safely.
- **`LiveData`**: A data holder class that is lifecycle-aware. When database records change, `LiveData` automatically notifies observers (like `MainActivity`) so the UI updates itself reactively without manual refreshing.

---

## Lab 8 — Debugging and Bug Logging

### 1. Goal of the Lab
To document real-world bugs encountered by team pairs during development in a structured bug log table, practice using Android Studio debugging tools (breakpoints and the Variables pane), and prepare for individual viva assessments.

### 2. Files Created or Modified
- `README.md`: Included project overview, lab mappings, and code file references.
- `Summary.md`: Comprehensive detailed summary.
- `Lab_8_Bug_Log.docx` & `Lab_8_Bug_Log.pdf`: Official printable group bug log reports.

### 3. What Was Modified or Added
- Compiled 5 detailed bug log entries covering all 5 work package pairs (WP1 to WP5) detailing symptoms, tools used, log output, faulty lines, and fixes.
- Created instructions for capturing breakpoint debugging screenshots.
- Outlined a 4-step debugging strategy for the individual assessment.

### 4. What Each Update Means & Plain-English Technical Explanations
- **Logcat**: The live developer log stream in Android Studio showing every system message, warning, and crash stack trace generated by your running app.
- **Breakpoints**: A debugging marker you place on a line of code. When the app reaches that line during execution, Android Studio pauses the app completely, letting you inspect every single variable value in real time in the **Variables pane**.
