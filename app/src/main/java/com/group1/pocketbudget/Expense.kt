
package com.group1.pocketbudget

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val expenseId: Int = 0,

    val title: String,
    val body: String,
    val category: String,
    val amount: Double,
    val essential: Boolean,
    val recurring: Boolean = false
)
