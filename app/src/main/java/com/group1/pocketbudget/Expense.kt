package com.group1.pocketbudget

// Lab 6: Blueprint representing a saved expense
data class Expense(
    val title: String,
    val body: String,
    val category: String,
    val amount: Double,
    val essential: Boolean,
    val recurring: Boolean = false
)