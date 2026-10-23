package com.group1.pocketbudget

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.group1.pocketbudget.databinding.ActivityExpensesDetailBinding

class ExpensesDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExpensesDetailBinding

    @SuppressLint("SetTextI18n")
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
            setResult(RESULT_CANCELED)// added by Leah
            finish()

        }

        binding.ShareButton.setOnClickListener {
            val shareText = "$title: ${"%.2f".format(amount)} ($category, ${
                if (essential) "essential" else "non-essential"
            })"

            val shareIntent = android.content.Intent(
                android.content.Intent.ACTION_SEND
            ).apply {
                type = "text/plain"
                putExtra(android.content.Intent.EXTRA_TEXT, shareText)
            }

            startActivity(
                android.content.Intent.createChooser(
                    shareIntent,
                    "Share expense via"
                )
            )
        }

        binding.MarkReviewedButton.setOnClickListener {
            setResult(
                RESULT_OK,
                android.content.Intent().putExtra("reviewedTitle", title)
            )
            finish()
        }

    }
}
