package com.group1.pocketbudget

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.group1.pocketbudget.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //  Save button listner, action that takes place after you click save
        binding.saveButton.setOnClickListener {

            val description = binding.descriptionInput.text.toString().trim()
            val amount = binding.amountInput.text.toString().trim().toDoubleOrNull()

            //to check so that description must not be empty
            if (description.isEmpty()) {
                binding.statusText.text = getString(R.string.error_no_description)
                return@setOnClickListener
            }

            // this checks amount so that it must be a number and above 0.
            // and when the text is not a number toDoubleOrNull() gives null to avoid a crash
            if (amount == null || amount <= 0.0) {
                binding.statusText.text = getString(R.string.error_bad_amount)
                return@setOnClickListener
            }

            binding.statusText.text = getString(R.string.saved_format, description, amount)

            // LAB 2: show the full summary instead (overwrites the short message above)
            val category = getSelectedCategory()
            var summary = getString(R.string.summary_format, description, category, amount)
            if (binding.essentialSwitch.isChecked) {
                summary += " · " + getString(R.string.essential_tag)
            }
            binding.statusText.text = summary

            // LAB 2: if Recurring is ticked, write a note in Logcat
            if (binding.recurringCheckbox.isChecked) {
                Log.i("PocketBudget", "Recurring: $description, ${"%.2f".format(amount)}")
            }
        }

        // LAB 2: listener goes on the RadioGroup, not on each button
        binding.categoryGroup.setOnCheckedChangeListener { _, _ ->
            binding.statusText.text = getString(R.string.category_changed, getSelectedCategory())
        }

        // LAB 2: switch shows a message the moment it is flipped
        binding.essentialSwitch.setOnCheckedChangeListener { _, isChecked ->
            binding.statusText.text = if (isChecked) {
                getString(R.string.marked_essential)
            } else {
                getString(R.string.not_essential)
            }
        }
    }

    // LAB 2: finds which category is ticked and returns its name
    private fun getSelectedCategory(): String {
        return when (binding.categoryGroup.checkedRadioButtonId) {
            R.id.radioTransport -> getString(R.string.category_transport)
            R.id.radioBooks -> getString(R.string.category_books)
            R.id.radioOther -> getString(R.string.category_other)
            else -> getString(R.string.category_food)
        }
    }
}