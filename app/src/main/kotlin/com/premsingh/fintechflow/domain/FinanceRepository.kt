package com.premsingh.fintechflow.domain

import kotlinx.coroutines.flow.StateFlow

interface FinanceRepository {
    val dashboard: StateFlow<DashboardState>
    fun sendTransfer(recipient: String, amount: String): TransferResult
}
