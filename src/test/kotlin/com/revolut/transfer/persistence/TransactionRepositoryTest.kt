package com.revolut.transfer.persistence

import com.revolut.transfer.initTestDb
import com.revolut.transfer.model.Transaction
import org.jooq.impl.DSL
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.time.OffsetDateTime

class TransactionRepositoryTest {

    private val dslContext = initTestDb()

    private val transactionRepository = TransactionRepository(dslContext)

    @Before
    fun initData() {
        dslContext.execute("""truncate table transfer_transaction""")
        dslContext.execute("""insert into account (name, account_number, balance) 
                                  values ('Alice', 1, 100),  ('Bob', 2, 100)""")
    }

    @Test
    fun `saveTransaction should create new entry`() {
        val transaction = Transaction(
                from = 1,
                to = 2,
                amount = BigDecimal(50),
                date = OffsetDateTime.now()
        )
        transactionRepository.saveTransaction(transaction)
        assertEquals(1, dslContext.fetchCount(DSL.table("transfer_transaction")))
    }

    @Test
    fun `findRelatedTransactions should return only related entries`() {
        dslContext.execute("""insert into account (name, account_number, balance) 
                                  values ('Eve', 3, 100)""")
        val transaction = Transaction(
                from = 1,
                to = 2,
                amount = BigDecimal(50),
                date = OffsetDateTime.now()
        )
        transactionRepository.saveTransaction(transaction)
        transactionRepository.saveTransaction(transaction)
        transactionRepository.saveTransaction(transaction.copy(from = 3))
        assertEquals(2, transactionRepository.findRelatedTransactions(1).size)
    }

}
