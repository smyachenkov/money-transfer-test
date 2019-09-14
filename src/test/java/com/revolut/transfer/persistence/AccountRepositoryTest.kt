package com.revolut.transfer.persistence

import com.revolut.transfer.initTestDb
import com.revolut.transfer.model.Account
import com.revolut.transfer.toCurrency
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AccountRepositoryTest {

    private val dslContext = initTestDb()

    private val accountRepository = AccountRepository(dslContext)

    @Before
    fun initData() {
        dslContext.execute("""delete from account""")
        dslContext.execute("""
             insert into account (name, account_number, balance)
             values ('Alice', 1, 100),  ('Bob', 2, 100)""")
    }

    @Test
    fun `exists should return true for existing account`() {
        assertTrue(accountRepository.exists(1))
    }

    @Test
    fun `selectAllAccounts should return list of all accounts`() {
        assertEquals(2, accountRepository.selectAllAccounts().size)
    }

    @Test
    fun `selectAccount should return null for absent account`() {
        assertNull(accountRepository.selectAccount(111))
    }

    @Test
    fun `selectAccount should return existing account`() {
        val expected = Account(
                id = 1L,
                balance = 100.00.toCurrency(),
                name = "Alice",
                accountNumber = 1
        )
        assertEquals(expected, accountRepository.selectAccount(1))
    }

    @Test
    fun `updateBalance should update account balance`() {
        val expected = Account(
                id = 1L,
                balance = 99.00.toCurrency(),
                name = "Alice",
                accountNumber = 1
        )
        accountRepository.updateBalance(1, BigDecimal(99.00))
        assertEquals(expected, accountRepository.selectAccount(1))
    }

}
