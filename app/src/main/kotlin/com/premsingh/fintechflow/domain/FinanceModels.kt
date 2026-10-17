package com.premsingh.fintechflow.domain

data class Transaction(
    val id: String,
    val merchant: String,
    val amountMinor: Long,
    val isCredit: Boolean,
    val dateLabel: String,
    val category: String
)

data class DashboardState(
    val accountHolder: String = "Alex Morgan",
    val accountLastFour: String = "4829",
    val balanceMinor: Long = 1_248_050,
    val transactions: List<Transaction> = emptyList()
)

sealed interface TransferResult {
    data class Sent(val transaction: Transaction) : TransferResult
    data object MissingRecipient : TransferResult
    data object InvalidAmount : TransferResult
    data object InsufficientFunds : TransferResult
}
