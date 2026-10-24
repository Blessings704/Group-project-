package com.group1.pocketbudget

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

// harrybanda200, PfabyGo: ViewModel class holding UI data and running background coroutine tasks
class ExpenseViewModel(
    private val repository: ExpenseRepository
) : ViewModel() {

    // Observe all expenses
    val allExpenses: LiveData<List<Expense>> =
        repository.allExpenses

    // Observe the all-time total
    val total: LiveData<Double?> =
        repository.total

    // fetches expenses for a specific category from the repository. totally valid
    fun getByCategory(category: String): LiveData<List<Expense>> {
        return repository.getByCategory(category)
    }

    // runs expense insertion on a background coroutine so the UI doesn't freeze
    fun insert(expense: Expense) {
        viewModelScope.launch {
            repository.insert(expense)
        }
    }

    // runs expense deletion on a background coroutine. pretty ez :)
    fun delete(expense: Expense) {
        viewModelScope.launch {
            repository.delete(expense)
        }
    }
}
