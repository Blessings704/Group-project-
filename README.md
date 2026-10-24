# Pocket Budget

A personal expense tracking Android app built with Kotlin and Jetpack architecture components (Room Database, LiveData, ViewModel, and ViewBinding).

---

## 1. Program Overview in Brief

Pocket Budget allows users to record, organize, view, and manage personal expenses easily:
- **Record Expenses**: Input an expense description, amount, optional note, category (Food, Transport, Books, Other), and flags (Essential, Recurring).
- **View Expense List**: Saved expenses are automatically displayed as cards in a scrollable list, ordered with the newest expenses at the top.
- **Track Spent Totals**: Displays two distinct totals simultaneously:
  1. *Spent this session*: Resets each time the app process launches.
  2. *All-time total*: Persistently calculated from the database using SQL `SUM(amount)`.
- **Expense Details & Sharing**: Tap any card to open a detail view to share the expense or mark it reviewed.
- **Delete Expense**: Long-press any card to permanently delete it from the database.
- **Data Persistence**: All expense records survive phone restarts and process death.

---

## 2. Explicit Manually Created Files

The following files were created and edited by the developer:

| File Name | Location | Purpose |
| :--- | :--- | :--- |
| **[`Expense.kt`](file:///media/p/Shared/Programmer/Android-KT/Group-project-/app/src/main/java/com/group1/pocketbudget/Expense.kt)** | `app/src/main/java/com/group1/pocketbudget/` | Database entity data class representing an expense record. |
| **[`ExpenseDao.kt`](file:///media/p/Shared/Programmer/Android-KT/Group-project-/app/src/main/java/com/group1/pocketbudget/ExpenseDao.kt)** | `app/src/main/java/com/group1/pocketbudget/` | Data Access Object defining SQL queries (`insert`, `delete`, `getAll`, `getTotal`). |
| **[`ExpenseDatabase.kt`](file:///media/p/Shared/Programmer/Android-KT/Group-project-/app/src/main/java/com/group1/pocketbudget/ExpenseDatabase.kt)** | `app/src/main/java/com/group1/pocketbudget/` | Room database singleton builder named `expense_database`. |
| **[`ExpenseRepository.kt`](file:///media/p/Shared/Programmer/Android-KT/Group-project-/app/src/main/java/com/group1/pocketbudget/ExpenseRepository.kt)** | `app/src/main/java/com/group1/pocketbudget/` | Repository mediating data access between DAO and ViewModel. |
| **[`ExpenseViewModel.kt`](file:///media/p/Shared/Programmer/Android-KT/Group-project-/app/src/main/java/com/group1/pocketbudget/ExpenseViewModel.kt)** | `app/src/main/java/com/group1/pocketbudget/` | ViewModel managing background coroutines (`viewModelScope.launch`). |
| **[`ExpensesAdapter.kt`](file:///media/p/Shared/Programmer/Android-KT/Group-project-/app/src/main/java/com/group1/pocketbudget/ExpensesAdapter.kt)** | `app/src/main/java/com/group1/pocketbudget/` | RecyclerView adapter linking expense data to item card views. |
| **[`MainActivity.kt`](file:///media/p/Shared/Programmer/Android-KT/Group-project-/app/src/main/java/com/group1/pocketbudget/MainActivity.kt)** | `app/src/main/java/com/group1/pocketbudget/` | Primary activity handling user inputs, validation, and LiveData updates. |
| **[`ExpensesDetailActivity.kt`](file:///media/p/Shared/Programmer/Android-KT/Group-project-/app/src/main/java/com/group1/pocketbudget/ExpensesDetailActivity.kt)** | `app/src/main/java/com/group1/pocketbudget/` | Secondary activity displaying full expense details with sharing options. |
| **[`activity_main.xml`](file:///media/p/Shared/Programmer/Android-KT/Group-project-/app/src/main/res/layout/activity_main.xml)** | `app/src/main/res/layout/` | XML layout for the main screen. |
| **[`activity_expenses_detail.xml`](file:///media/p/Shared/Programmer/Android-KT/Group-project-/app/src/main/res/layout/activity_expenses_detail.xml)** | `app/src/main/res/layout/` | XML layout for the expense detail screen. |
| **[`item_expense.xml`](file:///media/p/Shared/Programmer/Android-KT/Group-project-/app/src/main/res/layout/item_expense.xml)** | `app/src/main/res/layout/` | XML layout for individual expense item cards. |
| **[`strings.xml`](file:///media/p/Shared/Programmer/Android-KT/Group-project-/app/src/main/res/values/strings.xml)** | `app/src/main/res/values/` | Centralized text resource file for app labels and format strings. |
| **[`AndroidManifest.xml`](file:///media/p/Shared/Programmer/Android-KT/Group-project-/app/src/main/AndroidManifest.xml)** | `app/src/main/` | Application configuration file registering activities and intent filters. |

---

## 3. What Each Lab Does & Code Locations

### Lab 0 & Lab 1 — App Setup & Entry Screen
- **What it does**: Creates the layout with description, amount, and note fields, a Save button, and status text. Validates inputs on save (non-empty description, valid amount > 0).
- **Code Locations**:
  - Main input screen layout: [`activity_main.xml` (L16-L70)](file:///media/p/Shared/Programmer/Android-KT/Group-project-/app/src/main/res/layout/activity_main.xml#L16-L70)
  - String messages & labels: [`strings.xml` (L8-L16)](file:///media/p/Shared/Programmer/Android-KT/Group-project-/app/src/main/res/values/strings.xml#L8-L16)
  - Input validation & Save logic: [`MainActivity.kt` (L269-L347)](file:///media/p/Shared/Programmer/Android-KT/Group-project-/app/src/main/java/com/group1/pocketbudget/MainActivity.kt#L269-L347)

### Lab 2 — Categories & Options
- **What it does**: Adds 4 category radio buttons (Food, Transport, Books, Other), an "Essential" switch, and a "Recurring" checkbox with live update listeners.
- **Code Locations**:
  - Radio group, switch, & checkbox layouts: [`activity_main.xml` (L86-L144)](file:///media/p/Shared/Programmer/Android-KT/Group-project-/app/src/main/res/layout/activity_main.xml#L86-L144)
  - Category radio listener: [`MainActivity.kt` (L351-L373)](file:///media/p/Shared/Programmer/Android-KT/Group-project-/app/src/main/java/com/group1/pocketbudget/MainActivity.kt#L351-L373)
  - Essential switch listener: [`MainActivity.kt` (L377-L394)](file:///media/p/Shared/Programmer/Android-KT/Group-project-/app/src/main/java/com/group1/pocketbudget/MainActivity.kt#L377-L394)

### Lab 3 — Lifecycle & Session Total
- **What it does**: Logs lifecycle callbacks, tracks total spent in the current session (`sessionTotal`), and saves/restores state across screen rotations or process termination.
- **Code Locations**:
  - Lifecycle logging: [`MainActivity.kt` (L47-L86)](file:///media/p/Shared/Programmer/Android-KT/Group-project-/app/src/main/java/com/group1/pocketbudget/MainActivity.kt#L47-L86)
  - Save & restore state handlers: [`MainActivity.kt` (L88-L136)](file:///media/p/Shared/Programmer/Android-KT/Group-project-/app/src/main/java/com/group1/pocketbudget/MainActivity.kt#L88-L136)
  - Session total display logic: [`MainActivity.kt` (L238-L244)](file:///media/p/Shared/Programmer/Android-KT/Group-project-/app/src/main/java/com/group1/pocketbudget/MainActivity.kt#L238-L244)

### Lab 4 — Application Architecture
- **What it does**: Configures the app architecture in the manifest, designating launcher activities and window soft input modes.
- **Code Locations**:
  - App manifest setup: [`AndroidManifest.xml` (L5-L25)](file:///media/p/Shared/Programmer/Android-KT/Group-project-/app/src/main/AndroidManifest.xml#L5-L25)

### Lab 5 — Detail Screen & Intents
- **What it does**: Launches `ExpensesDetailActivity` via an explicit Intent to display expense details, allows sharing text to external apps (`ACTION_SEND`), and returns review results back to `MainActivity`.
- **Code Locations**:
  - Expense detail screen layout: [`activity_expenses_detail.xml` (L1-L62)](file:///media/p/Shared/Programmer/Android-KT/Group-project-/app/src/main/res/layout/activity_expenses_detail.xml#L1-L62)
  - Detail activity logic & share intent: [`ExpensesDetailActivity.kt` (L21-L105)](file:///media/p/Shared/Programmer/Android-KT/Group-project-/app/src/main/java/com/group1/pocketbudget/ExpensesDetailActivity.kt#L21-L105)
  - Activity Result launcher: [`MainActivity.kt` (L248-L265)](file:///media/p/Shared/Programmer/Android-KT/Group-project-/app/src/main/java/com/group1/pocketbudget/MainActivity.kt#L248-L265)

### Lab 6 — Expense List (RecyclerView)
- **What it does**: Displays expenses in a list of card items. Tapping a card opens the detail screen; long-pressing deletes the card.
- **Code Locations**:
  - Card view layout: [`item_expense.xml` (L2-L53)](file:///media/p/Shared/Programmer/Android-KT/Group-project-/app/src/main/res/layout/item_expense.xml#L2-L53)
  - List adapter implementation: [`ExpensesAdapter.kt` (L10-L75)](file:///media/p/Shared/Programmer/Android-KT/Group-project-/app/src/main/java/com/group1/pocketbudget/ExpensesAdapter.kt#L10-L75)
  - List setup in main screen: [`MainActivity.kt` (L168-L219)](file:///media/p/Shared/Programmer/Android-KT/Group-project-/app/src/main/java/com/group1/pocketbudget/MainActivity.kt#L168-L219)

### Lab 7 — Data Persistence (Room Database & LiveData)
- **What it does**: Stores all expense items in a Room SQLite database. Uses `LiveData` to observe database changes automatically and updates the all-time running total (`SELECT SUM(amount)`).
- **Code Locations**:
  - Room Entity: [`Expense.kt` (L6-L16)](file:///media/p/Shared/Programmer/Android-KT/Group-project-/app/src/main/java/com/group1/pocketbudget/Expense.kt#L6-L16)
  - DAO SQL Queries: [`ExpenseDao.kt` (L10-L25)](file:///media/p/Shared/Programmer/Android-KT/Group-project-/app/src/main/java/com/group1/pocketbudget/ExpenseDao.kt#L10-L25)
  - Database Singleton: [`ExpenseDatabase.kt` (L8-L34)](file:///media/p/Shared/Programmer/Android-KT/Group-project-/app/src/main/java/com/group1/pocketbudget/ExpenseDatabase.kt#L8-L34)
  - Repository layer: [`ExpenseRepository.kt` (L5-L30)](file:///media/p/Shared/Programmer/Android-KT/Group-project-/app/src/main/java/com/group1/pocketbudget/ExpenseRepository.kt#L5-L30)
  - ViewModel Coroutines: [`ExpenseViewModel.kt` (L8-L39)](file:///media/p/Shared/Programmer/Android-KT/Group-project-/app/src/main/java/com/group1/pocketbudget/ExpenseViewModel.kt#L8-L39)
  - LiveData Observers: [`MainActivity.kt` (L223-L234)](file:///media/p/Shared/Programmer/Android-KT/Group-project-/app/src/main/java/com/group1/pocketbudget/MainActivity.kt#L223-L234)

### Lab 8 — Debugging & Diagnostics
- **What it does**: Documents bug tracking, Logcat analysis, and breakpoint debugging for project quality.
- **Reference Document**: Bug log table PDF exported at `Lab_8_Bug_Log.pdf`.
