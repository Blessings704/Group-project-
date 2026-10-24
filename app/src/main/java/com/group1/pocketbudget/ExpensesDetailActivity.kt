package com.group1.pocketbudget

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.group1.pocketbudget.databinding.ActivityExpensesDetailBinding
import java.util.Locale

// Leah Lusinje, Oscar: Activity screen showing full details for a single selected expense
class ExpensesDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExpensesDetailBinding

    companion object {
        const val EXTRA_TITLE = "expense_title"
        const val EXTRA_DESCRIPTION = "expense_description"
        const val EXTRA_CATEGORY = "expense_category"
        const val EXTRA_AMOUNT = "expense_amount"
        const val EXTRA_ESSENTIAL = "expense_essential"
        const val EXTRA_REVIEWED_TITLE = "reviewed_title"
    }

    // grabs intent extras from MainActivity and hooks up share and review buttons. major glow up ;)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExpensesDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Receive expense details from MainActivity
        val title = intent.getStringExtra(EXTRA_TITLE) ?: ""
        val body = intent.getStringExtra(EXTRA_DESCRIPTION) ?: ""
        val category = intent.getStringExtra(EXTRA_CATEGORY) ?: ""
        val amount = intent.getDoubleExtra(EXTRA_AMOUNT, 0.0)
        val essential = intent.getBooleanExtra(EXTRA_ESSENTIAL, false)

        // Display expense details
        binding.detailTitle.text = title

        binding.detailCategory.text =
            getString(R.string.detail_category_format, category)

        binding.detailAmount.text =
            getString(R.string.detail_amount_format, amount)

        binding.detailDescription.text = getString(
            R.string.detail_notes_format,
            body.ifEmpty { getString(R.string.no_notes) }
        )

        binding.detailEssential.text = if (essential) {
            getString(R.string.essential_expense)
        } else {
            getString(R.string.nonessential_expense)
        }

        // Share the expense
        binding.shareButton.setOnClickListener {
            val essentialText = if (essential) {
                ", essential"
            } else {
                ", non-essential"
            }

            val shareText = String.format(
                Locale.getDefault(),
                "%s: MWK %,.2f (%s%s)\n%s",
                title,
                amount,
                category,
                essentialText,
                body
            )

            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareText)
            }

            startActivity(
                Intent.createChooser(
                    sendIntent,
                    getString(R.string.share_chooser_title)
                )
            )
        }

        // Mark the expense as reviewed
        binding.reviewButton.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra(EXTRA_REVIEWED_TITLE, title)
            }

            setResult(RESULT_OK, resultIntent)
            finish()
        }

        // Return a cancelled result when Back is pressed
        binding.backButton.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}
