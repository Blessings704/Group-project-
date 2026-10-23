
package com.group1.pocketbudget

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ExpenseViewModel(
    private val repository: ExpenseRepository
) : ViewModel() {

    // Observe all expenses
    val allExpenses: LiveData<List<Expense>> =
        repository.allExpenses

    // Observe the all-time total
    val total: LiveData<Double?> =
        repository.total

    // Get expenses by category
    fun getByCategory(category: String): LiveData<List<Expense>> {
        return repository.getByCategory(category)
    }

    // Insert an expense in the background
    fun insert(expense: Expense) {
        viewModelScope.launch {
            repository.insert(expense)
        }
    }

    // Delete an expense in the background
    fun delete(expense: Expense) {
        viewModelScope.launch {
            repository.delete(expense)
        }
    }
}
