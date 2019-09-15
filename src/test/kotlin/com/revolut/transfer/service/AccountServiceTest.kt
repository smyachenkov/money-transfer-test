package com.revolut.transfer.service

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.revolut.transfer.exception.AccountNotFoundException
import com.revolut.transfer.model.Account
import com.revolut.transfer.model.dto.request.AccountRequest
import com.revolut.transfer.persistence.AccountRepository
import com.revolut.transfer.persistence.TransactionRepository
import org.junit.Test
import java.math.BigDecimal
import kotlin.test.assertEquals

class AccountServiceTest {

    private val accountRepository = mock<AccountRepository>()
    private val transactionRepository = mock<TransactionRepository>()

    private val service = AccountService(accountRepository, transactionRepository)

    private val account = Account(1L, 1, "name", BigDecimal.ONE)

    @Test
    fun `createAccount should call account repo`() {
        service.createAccount(AccountRequest("name", 1, BigDecimal.ONE))
        verify(accountRepository).createAccount(AccountRequest("name", 1, BigDecimal.ONE))
    }

    @Test
    fun `getAllAccounts should call account repo`() {
        service.getAllAccounts()
        verify(accountRepository).selectAllAccounts()
    }

    @Test
    fun `getAccount should return existing account`() {
        whenever(accountRepository.selectAccount(1)).thenReturn(account)
        val result = service.getAccount(1)
        assertEquals(account, result)
    }

    @Test(expected = AccountNotFoundException::class)
    fun `getAccount should produce exception if account is absent`() {
        whenever(accountRepository.selectAccount(1)).thenReturn(null)
        service.getAccount(1)
    }

    @Test(expected = AccountNotFoundException::class)
    fun `getAccountHistory should produce exception if account is absent`() {
        whenever(accountRepository.selectAccount(1)).thenReturn(null)
        service.getAccountHistory(1)
    }

    @Test(expected = AccountNotFoundException::class)
    fun `getAccountHistory return transactions from repo`() {
        whenever(accountRepository.selectAccount(1)).thenReturn(account)
        service.getAccountHistory(1)
        verify(transactionRepository.findRelatedTransactions(1))
    }

}
