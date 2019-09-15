package com.revolut.transfer.service

import com.revolut.transfer.exception.AccountNotFoundException
import com.revolut.transfer.exception.InsufficientAmountException
import com.revolut.transfer.exception.InvalidTransferParams
import com.revolut.transfer.initTestDb
import com.revolut.transfer.model.dto.request.TransferRequest
import com.revolut.transfer.model.dto.response.TransferResponse
import com.revolut.transfer.persistence.AccountRepository
import com.revolut.transfer.persistence.TransactionRepository
import com.revolut.transfer.toCurrency
import org.junit.Test
import java.math.BigDecimal
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class TransferProcessorTest {

    private val dslContext = initTestDb()
    private val accountRepository = AccountRepository(dslContext)
    private val transactionRepository = TransactionRepository(dslContext)
    private val processor = TransferProcessor(
            dslContext,
            accountRepository,
            transactionRepository
    )

    private val request = TransferRequest(
            from = 1,
            to = 2,
            amount = BigDecimal.ONE
    )

    @BeforeTest
    fun initData() {
        dslContext.execute("delete from account")
        dslContext.execute("""insert into account (name, account_number, balance)
            |values ('Alice', 1, 100),  ('Bob', 2, 100)""".trimMargin())
    }

    @Test(expected = AccountNotFoundException::class)
    fun `should throw exception if from account is absent`() {
        processor.transfer(request.copy(from = 3))
    }

    @Test(expected = AccountNotFoundException::class)
    fun `should throw exception if to account is absent`() {
        processor.transfer(request.copy(to = 3))
    }

    @Test(expected = InsufficientAmountException::class)
    fun `should throw exception if not enough funds on from account`() {
        processor.transfer(request.copy(amount = BigDecimal(1000)))
    }

    @Test(expected = InvalidTransferParams::class)
    fun `should throw exception if to and from are the same`() {
        processor.transfer(request.copy(from = 1, to = 1))
    }

    @Test
    fun `should return correct response`() {
        val actual = processor.transfer(request)
        val expected = TransferResponse(
                from = request.from,
                to = request.to,
                fromBalance = 100.00.toCurrency() - request.amount,
                toBalance = 100.00.toCurrency() + request.amount,
                transferred = request.amount
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `should update accounts balance`() {
        processor.transfer(request)
        val fromAccount = accountRepository.selectAccount(request.from)
        val toAccount = accountRepository.selectAccount(request.to)
        assertEquals(100.00.toCurrency() - request.amount, fromAccount!!.balance)
        assertEquals(100.00.toCurrency() + request.amount, toAccount!!.balance)
    }
}
