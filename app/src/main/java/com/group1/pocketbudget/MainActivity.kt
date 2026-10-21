package com.group1.pocketbudget

import android.os.Bundle
import android.util.Log
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import com.group1.pocketbudget.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    // LAB 3: Total amount saved during the current app session.
    private var sessionTotal = 0.0

    // LAB 3: Tag and keys used for lifecycle logging and state restoration.
    companion object {
        private const val TAG = "PocketBudgetLife"
        private const val KEY_STATUS = "status_text"
        private const val KEY_SESSION_TOTAL = "session_total"
        private const val KEY_ESSENTIAL = "essential_checked"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // LAB 3: Log when the Activity is created.
        Log.d(TAG, "onCreate")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // LAB 3: Show the initial session total when the screen opens.
        updateSessionTotal()

        // LAB 2 EXTEND: remember the normal text color (read from the view, never use 0)
        val defaultColor = binding.statusText.currentTextColor

        // LAB 2 EXTEND: Food is the default category, so Recurring starts disabled
        binding.recurringCheckbox.isEnabled = false

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

            // LAB 3: Add the valid expense amount to the session total.
            // Invalid descriptions and amounts never reach this line.
            sessionTotal += amount
            updateSessionTotal()

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
        binding.categoryGroup.setOnCheckedChangeListener { _, checkedId ->
            binding.statusText.text = getString(R.string.category_changed, getSelectedCategory())

            // LAB 2 EXTEND: Recurring only makes sense once a non-default category is chosen
            val allowed = checkedId != R.id.radioFood
            binding.recurringCheckbox.isEnabled = allowed
            if (!allowed) binding.recurringCheckbox.isChecked = false
        }

        // LAB 2: switch shows a message the moment it is flipped, and turns the text red when on
        binding.essentialSwitch.setOnCheckedChangeListener { _, isChecked ->
            binding.statusText.text = if (isChecked) {
                getString(R.string.marked_essential)
            } else {
                getString(R.string.not_essential)
            }
            binding.statusText.setTextColor(if (isChecked) Color.RED else defaultColor)
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

    // LAB 3: Update the TextView whenever the session total changes.
    private fun updateSessionTotal() {
        binding.sessionTotalText.text = getString(
            R.string.session_total_format,
            sessionTotal
        )
    }

    // LAB 3: Called when the Activity becomes visible.
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    // LAB 3: Called when the Activity becomes interactive.
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    // LAB 3: Called when the Activity is losing focus.
    override fun onPause() {
        Log.d(TAG, "onPause")
        super.onPause()
    }

    // LAB 3: Called when the Activity is no longer visible.
    override fun onStop() {
        Log.d(TAG, "onStop")
        super.onStop()
    }

    // LAB 3: Called when a stopped Activity is returning.
    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart")
    }

    // LAB 3: Called when the Activity is being destroyed.
    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
    }


    // LAB 3: Save the required UI state before Android recreates the Activity.
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(
            KEY_STATUS,
            binding.statusText.text.toString()
        )

        // Store the session total as a Double.
        outState.putDouble(KEY_SESSION_TOTAL, sessionTotal)

        // Store whether the Essential switch is checked.
        outState.putBoolean(
            KEY_ESSENTIAL,
            binding.essentialSwitch.isChecked
        )

        Log.d(TAG, "onSaveInstanceState: total=$sessionTotal")

        super.onSaveInstanceState(outState)
    }

    // LAB 3: Restore the saved state after Activity recreation.
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

            // Restore the Essential switch first.
            binding.essentialSwitch.isChecked =
                savedInstanceState.getBoolean(KEY_ESSENTIAL, false)

            // Restore the saved status after the switch listener runs.
            binding.statusText.text =
                savedInstanceState.getString(KEY_STATUS, "")

            // Restore the session total.
            sessionTotal = savedInstanceState.getDouble(
                KEY_SESSION_TOTAL, 0.0
            )
            updateSessionTotal()

            Log.d(TAG, "onRestoreInstanceState: total=$sessionTotal")
        }

    }