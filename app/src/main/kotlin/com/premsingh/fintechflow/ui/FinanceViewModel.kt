package com.premsingh.fintechflow.ui

import androidx.lifecycle.ViewModel
import com.premsingh.fintechflow.data.DemoFinanceRepository
import com.premsingh.fintechflow.domain.FinanceRepository
import com.premsingh.fintechflow.domain.TransferResult

class FinanceViewModel(
    private val repository: FinanceRepository = DemoFinanceRepository()
) : ViewModel() {
    val dashboard = repository.dashboard

    fun sendTransfer(recipient: String, amount: String): TransferResult =
        repository.sendTransfer(recipient, amount)
}
