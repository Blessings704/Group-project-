
package com.group1.pocketbudget

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.group1.pocketbudget.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ExpenseViewModel
    private lateinit var adapter: ExpensesAdapter
    private lateinit var detailLauncher: ActivityResultLauncher<Intent>

    private val currentExpenses = mutableListOf<Expense>()
    private var sessionTotal = 0.0

    companion object {
        private const val TAG = "PocketBudgetLife"

        private const val KEY_SESSION_TOTAL = "sessionTotal"
        private const val KEY_STATUS_TEXT = "statusText"
        private const val KEY_ESSENTIAL = "essentialSwitch"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logLifecycle("onCreate")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupDetailLauncher()
        setupExpenseList()
        observeExpenses()
        setupCategoryListener()
        setupEssentialSwitch()
        setupSaveButton()

        if (savedInstanceState != null) {
            sessionTotal = savedInstanceState.getDouble(
                KEY_SESSION_TOTAL,
                0.0
            )
        }

        updateSessionTotal()
    }

    // Lab 3: Activity lifecycle callbacks

    override fun onStart() {
        super.onStart()
        logLifecycle("onStart")
    }

    override fun onResume() {
        super.onResume()
        logLifecycle("onResume")
    }

    override fun onPause() {
        logLifecycle("onPause")
        super.onPause()
    }

    override fun onStop() {
        logLifecycle("onStop")
        super.onStop()
    }

    override fun onRestart() {
        super.onRestart()
        logLifecycle("onRestart")
    }

    override fun onDestroy() {
        logLifecycle("onDestroy")
        super.onDestroy()
    }

    private fun logLifecycle(callback: String) {
        Log.d(TAG, callback)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putDouble(KEY_SESSION_TOTAL, sessionTotal)
        outState.putString(
            KEY_STATUS_TEXT,
            binding.statusText.text.toString()
        )
        outState.putBoolean(
            KEY_ESSENTIAL,
            binding.essentialSwitch.isChecked
        )

        Log.d(TAG, "onSaveInstanceState: state saved")

        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        sessionTotal = savedInstanceState.getDouble(
            KEY_SESSION_TOTAL,
            0.0
        )

        binding.essentialSwitch.isChecked =
            savedInstanceState.getBoolean(KEY_ESSENTIAL, false)

        binding.statusText.text = savedInstanceState.getString(
            KEY_STATUS_TEXT,
            ""
        )

        updateSessionTotal()

        Log.d(TAG, "onRestoreInstanceState: state restored")
    }

    // Lab 7: Connect the database, repository and ViewModel

    private fun setupViewModel() {
        val database = ExpenseDatabase.getDatabase(this)
        val repository = ExpenseRepository(database.expenseDao())

        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>
            ): T {
                if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return ExpenseViewModel(repository) as T
                }

                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }

        viewModel = ViewModelProvider(
            this,
            factory
        )[ExpenseViewModel::class.java]
    }

    // Lab 6: Display expenses in a RecyclerView

    private fun setupExpenseList() {
        adapter = ExpensesAdapter(
            currentExpenses,

            onClick = { expense ->
                val intent = Intent(
                    this,
                    ExpensesDetailActivity::class.java
                ).apply {
                    putExtra(
                        ExpensesDetailActivity.EXTRA_TITLE,
                        expense.title
                    )
                    putExtra(
                        ExpensesDetailActivity.EXTRA_DESCRIPTION,
                        expense.body
                    )
                    putExtra(
                        ExpensesDetailActivity.EXTRA_CATEGORY,
                        expense.category
                    )
                    putExtra(
                        ExpensesDetailActivity.EXTRA_AMOUNT,
                        expense.amount
                    )
                    putExtra(
                        ExpensesDetailActivity.EXTRA_ESSENTIAL,
                        expense.essential
                    )
                }

                detailLauncher.launch(intent)
            },

            onLongClick = { expense ->
                viewModel.delete(expense)

                binding.statusText.text = getString(
                    R.string.deleted_format,
                    expense.title
                )
            }
        )

        binding.expenseList.layoutManager =
            GridLayoutManager(this, 1)

        binding.expenseList.adapter = adapter
    }

    // Lab 7: Observe database changes

    private fun observeExpenses() {
        viewModel.allExpenses.observe(this) { expenses ->
            currentExpenses.clear()
            currentExpenses.addAll(expenses)
            adapter.notifyDataSetChanged()
        }

        viewModel.total.observe(this) { total ->
            binding.allTimeTotalText.text = getString(
                R.string.all_time_total_format,
                total ?: 0.0
            )
        }
    }

    // Lab 3: Display the total for the current session

    private fun updateSessionTotal() {
        binding.sessionTotalText.text = getString(
            R.string.session_total_format,
            sessionTotal
        )
    }

    // Lab 5: Receive the result from ExpensesDetailActivity

    private fun setupDetailLauncher() {
        detailLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val reviewedTitle = result.data?.getStringExtra(
                    ExpensesDetailActivity.EXTRA_REVIEWED_TITLE
                ).orEmpty()

                binding.statusText.text = getString(
                    R.string.reviewed_format,
                    reviewedTitle
                )
            } else {
                binding.statusText.text =
                    getString(R.string.review_cancelled)
            }
        }
    }

    // Lab 1 and Lab 2: Validate and save an expense

    private fun setupSaveButton() {
        binding.saveButton.setOnClickListener {
            val description =
                binding.descriptionInput.text.toString().trim()

            val note =
                binding.noteInput.text.toString().trim()

            val amountText =
                binding.amountInput.text.toString().trim()

            // Check that the description is not empty.
            if (description.isEmpty()) {
                binding.descriptionInput.error =
                    getString(R.string.error_no_description)
                return@setOnClickListener
            }

            // Convert the amount safely to a Double.
            val amount = amountText.toDoubleOrNull()

            if (amount == null || amount <= 0.0) {
                binding.amountInput.error =
                    getString(R.string.error_bad_amount)
                return@setOnClickListener
            }

            val category = getSelectedCategory()
            val isEssential = binding.essentialSwitch.isChecked
            val isRecurring = binding.recurringCheckbox.isChecked

            val expense = Expense(
                title = description,
                body = note,
                category = category,
                amount = amount,
                essential = isEssential,
                recurring = isRecurring
            )

            // Update the session total.
            sessionTotal += amount
            updateSessionTotal()

            // Save the expense through the ViewModel.
            viewModel.insert(expense)

            // Log recurring expenses as required by Lab 2.
            if (isRecurring) {
                Log.i(
                    "PocketBudget",
                    "Recurring: $description, $amount"
                )
            }

            // Prepare the expense summary.
            var summary = getString(
                R.string.summary_format,
                description,
                category,
                amount
            )

            if (isEssential) {
                summary += " · ${getString(R.string.essential_tag)}"
            }

            // Clear the input fields.
            binding.descriptionInput.text.clear()
            binding.noteInput.text.clear()
            binding.amountInput.text.clear()

            // Reset the options.
            binding.essentialSwitch.isChecked = false
            binding.recurringCheckbox.isChecked = false
            binding.categoryGroup.check(binding.radioFood.id)

            // Keep the summary visible after resetting the options.
            binding.statusText.text = summary

            // Show the newest expense at the top.
            binding.expenseList.post {
                binding.expenseList.scrollToPosition(0)
            }

            Toast.makeText(
                this,
                R.string.expense_saved,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Lab 2: Respond when the category changes

    private fun setupCategoryListener() {
        binding.categoryGroup.setOnCheckedChangeListener {
                _, checkedId ->

            val category = getSelectedCategory()

            binding.statusText.text = getString(
                R.string.category_changed,
                category
            )

            // Keep the existing behaviour: recurring is disabled for Food.
            val isFood = checkedId == binding.radioFood.id

            binding.recurringCheckbox.isEnabled = !isFood

            if (isFood) {
                binding.recurringCheckbox.isChecked = false
            }
        }
    }

    // Lab 2: Respond when the Essential switch changes

    private fun setupEssentialSwitch() {
        binding.essentialSwitch.setOnCheckedChangeListener {
                _, isChecked ->

            if (isChecked) {
                binding.statusText.text =
                    getString(R.string.marked_essential)

                binding.statusText.setTextColor(Color.RED)
            } else {
                binding.statusText.text =
                    getString(R.string.not_essential)

                binding.statusText.setTextColor(Color.BLACK)
            }
        }
    }

    // Return the selected category name from strings.xml.

    private fun getSelectedCategory(): String {
        return when (binding.categoryGroup.checkedRadioButtonId) {
            binding.radioTransport.id ->
                getString(R.string.category_transport)

            binding.radioBooks.id ->
                getString(R.string.category_books)

            binding.radioOther.id ->
                getString(R.string.category_other)

            else ->
                getString(R.string.category_food)
        }
    }
}
