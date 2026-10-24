
package com.group1.pocketbudget

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.group1.pocketbudget.databinding.ItemExpenseBinding
import java.util.Locale //locale specific 4matting yay

class ExpensesAdapter(
    private val items: List<Expense>,
    private val onClick: (Expense) -> Unit,
    private val onLongClick: (Expense) -> Unit
) : RecyclerView.Adapter<ExpensesAdapter.ViewHolder>() {

    class ViewHolder(
        val binding: ItemExpenseBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        Log.d("PocketBudgetList", "onCreateViewHolder called")

        val binding = ItemExpenseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val expense = items[position]
        val ctx = holder.binding.root.context

        // Display the expense title
        holder.binding.cardTitle.text = expense.title

        // Display the category and expense labels
        val parts = mutableListOf(expense.category)

        if (expense.essential) {
            parts.add(ctx.getString(R.string.flag_essential))
        }

        if (expense.recurring) {
            parts.add(ctx.getString(R.string.flag_recurring))
        }

        holder.binding.cardCategory.text =
            parts.joinToString(" · ")

        // Display the expense amount
        holder.binding.cardAmount.text =
            String.format(Locale.US, "MWK %,.2f", expense.amount)

        // Display the note, or a default message if it is empty
        holder.binding.cardBody.text =
            expense.body.ifEmpty { "No note added" }

        // Open expense details when tapped
        holder.binding.root.setOnClickListener {
            onClick(expense)
        }

        // Delete expense when long-pressed
        holder.binding.root.setOnLongClickListener {
            onLongClick(expense)
            true
        }
    }

    override fun getItemCount(): Int = items.size
}
