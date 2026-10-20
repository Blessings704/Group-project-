package com.group1.pocketbudget

import android.os.Bundle
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
        }
    }
}