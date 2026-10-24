package com.group1.pocketbudget

import androidx.lifecycle.LiveData

// harrybanda200, PfabyGo: repository class that handles data traffic between the DAO and ViewModel
class ExpenseRepository(
    private val expenseDao: ExpenseDao
) {

    // Get all expenses, newest first
    val allExpenses: LiveData<List<Expense>> =
        expenseDao.getAll()

    // Get the total amount of all saved expenses
    val total: LiveData<Double?> =
        expenseDao.getTotal()

    // grabs expenses filtered by category from the DAO. bet
    fun getByCategory(category: String): LiveData<List<Expense>> {
        return expenseDao.getByCategory(category)
    }

    // saves a new expense into the database through the DAO
    suspend fun insert(expense: Expense) {
        expenseDao.insert(expense)
    }

    // deletes an expense from the database. super clutch ;)
    suspend fun delete(expense: Expense) {
        expenseDao.delete(expense)
    }
}
