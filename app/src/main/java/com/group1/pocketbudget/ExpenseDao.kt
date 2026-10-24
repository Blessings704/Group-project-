package com.group1.pocketbudget

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

// harrybanda200, PfabyGo: dao interface holding our database query functions. no cap
@Dao
interface ExpenseDao {

    // harrybanda200, PfabyGo: adds a brand new expense into the database. say less
    @Insert
    suspend fun insert(expense: Expense)

    // wipes a specific expense from the database when it gets cooked
    @Delete
    suspend fun delete(expense: Expense)

    // grabs all expenses ordered so the latest ones stay on top
    @Query("SELECT * FROM expenses ORDER BY expenseId DESC")
    fun getAll(): LiveData<List<Expense>>

    // filters and returns expenses for a specific category. super valid
    @Query("SELECT * FROM expenses WHERE category = :category ORDER BY expenseId DESC")
    fun getByCategory(category: String): LiveData<List<Expense>>

    // calculates the sum of all money spent so far. highkey useful
    @Query("SELECT SUM(amount) FROM expenses")
    fun getTotal(): LiveData<Double?>
}
