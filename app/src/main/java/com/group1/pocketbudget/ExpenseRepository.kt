
package com.group1.pocketbudget

import androidx.lifecycle.LiveData

class ExpenseRepository(
    private val expenseDao: ExpenseDao
) {

    // Get all expenses, newest first
    val allExpenses: LiveData<List<Expense>> =
        expenseDao.getAll()

    // Get the total amount of all saved expenses
    val total: LiveData<Double?> =
        expenseDao.getTotal()

    // Get expenses belonging to a particular category
    fun getByCategory(category: String): LiveData<List<Expense>> {
        return expenseDao.getByCategory(category)
    }

    // Insert a new expense
    suspend fun insert(expense: Expense) {
        expenseDao.insert(expense)
    }

    // Delete an expense
    suspend fun delete(expense: Expense) {
        expenseDao.delete(expense)
    }
}
