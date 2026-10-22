package com.group1.pocketbudget

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.group1.pocketbudget.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Lab 6: Mutable list storing saved expense items
    private val currentExpenses = mutableListOf<Expense>()

    // Lab 6: Adapter used to populate RecyclerView cards
    private lateinit var adapter: ExpensesAdapter

    // Activity launcher for opening expense detail screen
    private lateinit var detailLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Register detail activity launcher
        detailLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            // Optional: Handle result from detail activity if needed
        }

        // Initialize adapter with click and straight delete long click listeners
        adapter = ExpensesAdapter(
            items = currentExpenses,
            onClick = { expense ->
                val intent = Intent(this, ExpensesDetailActivity::class.java).apply {
                    putExtra("Expense Title", expense.title)
                    putExtra("Expense Description", expense.body)
                    putExtra("Expense Type", expense.category)
                    putExtra("Expense Amount", expense.amount)
                    putExtra("Essential Expense", expense.essential)
                }
                detailLauncher.launch(intent)
            },
            onLongClick = { expense ->
                // Direct delete on hold down without confirmation
                val index = currentExpenses.indexOf(expense)
                if (index != -1) {
                    currentExpenses.removeAt(index)
                    adapter.notifyItemRemoved(index)
                    adapter.notifyItemRangeChanged(index, currentExpenses.size - index)
                    binding.statusText.text = "Deleted: ${expense.title}"
                }
            }
        )

        binding.expenseList.layoutManager = GridLayoutManager(this, 1)
        binding.expenseList.adapter = adapter

        // LAB 2 EXTEND: remember the normal text color
        val defaultColor = binding.statusText.currentTextColor

        // LAB 2 EXTEND: Food is the default category, so Recurring starts disabled
        binding.recurringCheckbox.isEnabled = false

        // Save button listener
        binding.saveButton.setOnClickListener {
            val description = binding.descriptionInput.text.toString().trim()
            val note = binding.noteInput.text.toString().trim()
            val amount = binding.amountInput.text.toString().trim().toDoubleOrNull()

            // Check description is not empty
            if (description.isEmpty()) {
                binding.statusText.text = getString(R.string.error_no_description)
                return@setOnClickListener
            }

            // Check amount is valid and > 0
            if (amount == null || amount <= 0.0) {
                binding.statusText.text = getString(R.string.error_bad_amount)
                return@setOnClickListener
            }

            val category = getSelectedCategory()
            val isEssential = binding.essentialSwitch.isChecked

            // Create new expense and insert at the top (index 0)
            val newExpense = Expense(
                title = description,
                body = note,
                category = category,
                amount = amount,
                essential = isEssential,
                recurring = binding.recurringCheckbox.isChecked
            )
            currentExpenses.add(0, newExpense)
            adapter.notifyItemInserted(0)
            binding.expenseList.scrollToPosition(0)

            // Update status text summary
            var summary = getString(R.string.summary_format, description, category, amount)
            if (isEssential) {
                summary += " · " + getString(R.string.essential_tag)
            }
            binding.statusText.text = summary

            // Log if recurring is checked
            if (binding.recurringCheckbox.isChecked) {
                Log.i("PocketBudget", "Recurring: $description, ${"%.2f".format(amount)}")
            }

            // Clear input fields for next entry
            binding.descriptionInput.text?.clear()
            binding.noteInput.text?.clear()
            binding.amountInput.text?.clear()
        }

        // Category group listener
        binding.categoryGroup.setOnCheckedChangeListener { _, checkedId ->
            binding.statusText.text = getString(R.string.category_changed, getSelectedCategory())

            val allowed = checkedId != R.id.radioFood
            binding.recurringCheckbox.isEnabled = allowed
            if (!allowed) binding.recurringCheckbox.isChecked = false
        }

        // Essential switch listener
        binding.essentialSwitch.setOnCheckedChangeListener { _, isChecked ->
            binding.statusText.text = if (isChecked) {
                getString(R.string.marked_essential)
            } else {
                getString(R.string.not_essential)
            }
            binding.statusText.setTextColor(if (isChecked) Color.RED else defaultColor)
        }
    }

    // Returns selected category name
    private fun getSelectedCategory(): String {
        return when (binding.categoryGroup.checkedRadioButtonId) {
            R.id.radioTransport -> getString(R.string.category_transport)
            R.id.radioBooks -> getString(R.string.category_books)
            R.id.radioOther -> getString(R.string.category_other)
            else -> getString(R.string.category_food)
        }
    }
}
