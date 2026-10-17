package com.premsingh.fintechflow.data

import com.premsingh.fintechflow.domain.TransferResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DemoFinanceRepositoryTest {
    @Test fun successfulTransferUpdatesBalanceAndAppearsFirstInActivity() {
        val repository = DemoFinanceRepository()
        val initialBalance = repository.dashboard.value.balanceMinor

        val result = repository.sendTransfer("Taylor", "25.50")

        assertTrue(result is TransferResult.Sent)
        assertEquals(initialBalance - 2_550, repository.dashboard.value.balanceMinor)
        assertEquals("Taylor", repository.dashboard.value.transactions.first().merchant)
        assertEquals(5, repository.dashboard.value.transactions.size)
    }

    @Test fun invalidTransferDoesNotChangeAccountState() {
        val repository = DemoFinanceRepository()
        val before = repository.dashboard.value

        assertEquals(TransferResult.MissingRecipient, repository.sendTransfer(" ", "20"))
        assertEquals(TransferResult.InvalidAmount, repository.sendTransfer("Taylor", "0"))
        assertEquals(TransferResult.InsufficientFunds, repository.sendTransfer("Taylor", "999999"))
        assertEquals(before, repository.dashboard.value)
    }
}
