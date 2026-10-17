package com.premsingh.fintechflow.data

import com.premsingh.fintechflow.domain.DashboardState
import com.premsingh.fintechflow.domain.FinanceRepository
import com.premsingh.fintechflow.domain.Transaction
import com.premsingh.fintechflow.domain.TransferResult
import com.premsingh.fintechflow.domain.parseAmountMinor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

/** In-memory repository with illustrative values only; nothing is persisted or transmitted. */
class DemoFinanceRepository : FinanceRepository {
    private val state = MutableStateFlow(
        DashboardState(
            transactions = listOf(
                Transaction("coffee", "Northstar Coffee", 580, false, "Today · 9:41 AM", "Food & drink"),
                Transaction("market", "Metro Market", 4_235, false, "Yesterday · 6:12 PM", "Shopping"),
                Transaction("salary", "Salary deposit", 320_000, true, "Oct 14 · 8:00 AM", "Income"),
                Transaction("music", "Streamline Music", 1_099, false, "Oct 12 · 2:16 PM", "Subscription")
            )
        )
    )

    override val dashboard: StateFlow<DashboardState> = state.asStateFlow()

    override fun sendTransfer(recipient: String, amount: String): TransferResult {
        val cleanRecipient = recipient.trim()
        if (cleanRecipient.isEmpty()) return TransferResult.MissingRecipient
        val amountMinor = parseAmountMinor(amount) ?: return TransferResult.InvalidAmount
        if (amountMinor > state.value.balanceMinor) return TransferResult.InsufficientFunds

        val transfer = Transaction(
            id = UUID.randomUUID().toString(),
            merchant = cleanRecipient,
            amountMinor = amountMinor,
            isCredit = false,
            dateLabel = "Just now",
            category = "Transfer"
        )
        state.update { current ->
            current.copy(
                balanceMinor = current.balanceMinor - amountMinor,
                transactions = listOf(transfer) + current.transactions
            )
        }
        return TransferResult.Sent(transfer)
    }
}
