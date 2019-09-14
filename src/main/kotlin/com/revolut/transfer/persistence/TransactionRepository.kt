package com.revolut.transfer.persistence

import com.revolut.transfer.model.Transaction
import org.jooq.DSLContext
import org.jooq.impl.DSL.*


class TransactionRepository(private val dslContext: DSLContext) {

    fun saveTransaction(transaction: Transaction) {
        dslContext.insertInto(table("transfer_transaction"),
                field("sender"), field("receiver"), field("amount"), field("tx_date"))
                .values(transaction.from, transaction.to, transaction.amount, transaction.date)
                .execute()
    }

    fun findRelatedTransactions(accountNumber: Int): List<Transaction> {
        return dslContext.select(
                field("sender"), field("receiver"), field("amount"), field("tx_date"))
                .from(table("transfer_transaction"))
                .where(field("sender").eq(accountNumber).or(field("receiver").eq(accountNumber)))
                .fetchInto(Transaction::class.java)
    }


}
