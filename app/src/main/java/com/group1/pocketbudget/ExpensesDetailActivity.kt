package com.group1.pocketbudget

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.group1.pocketbudget.databinding.ActivityExpensesDetailBinding

class ExpensesDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExpensesDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpensesDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("Expense Title") ?: ""
        val body = intent.getStringExtra("Expense Description") ?: ""
        val category = intent.getStringExtra("Expense Type") ?: ""
        val amount = intent.getDoubleExtra("Expense Amount", 0.0)
        val essential = intent.getBooleanExtra("Essential Expense", false)

        binding.detailTitle.text = title
        binding.detailCategory.text = "Category: $category"
        binding.detailAmount.text = "Amount: $${"%.2f".format(amount)}"
        binding.detailDescription.text = "Notes: ${body.ifEmpty { "None" }}"
        binding.detailEssential.text = if (essential) "Essential Expense" else "Non-essential Expense"

        binding.BackButton.setOnClickListener {
            finish()
        }
    }
}
