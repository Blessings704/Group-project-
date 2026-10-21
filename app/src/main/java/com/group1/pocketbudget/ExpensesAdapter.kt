package com.group1.pocketbudget

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.group1.pocketbudget.databinding.ItemExpenseBinding

class ExpensesAdapter(
    private val items: List<Expense>,
    private val onClick: (Expense) -> Unit,
    private val onLongClick: (Expense) -> Unit
) : RecyclerView.Adapter<ExpensesAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemExpenseBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemExpenseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val expense = items[position]
        holder.binding.cardTitle.text = expense.title
        holder.binding.cardCategory.text = "Category: ${expense.category}"
        holder.binding.cardAmount.text = "MWK ${"%.2f".format(expense.amount)}"
        holder.binding.cardBody.text = expense.body.ifEmpty { "No note added" }
        
        holder.binding.root.setOnClickListener { onClick(expense) }
        
        // Long click listener to trigger delete
        holder.binding.root.setOnLongClickListener {
            onLongClick(expense)
            true
        }
    }

    override fun getItemCount(): Int = items.size
}
