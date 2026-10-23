
package com.group1.pocketbudget

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExpenseDao {

    @Insert
    suspend fun insert(expense: Expense)

    @Delete
    suspend fun delete(expense: Expense)

    @Query("SELECT * FROM expenses ORDER BY expenseId DESC")
    fun getAll(): LiveData<List<Expense>>

    @Query("SELECT * FROM expenses WHERE category = :category ORDER BY expenseId DESC")
    fun getByCategory(category: String): LiveData<List<Expense>>

    @Query("SELECT SUM(amount) FROM expenses")
    fun getTotal(): LiveData<Double?>
}
