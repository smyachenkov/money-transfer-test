package com.revolut.transfer.persistence

import com.revolut.transfer.model.Account
import com.revolut.transfer.model.dto.request.AccountRequest
import org.jooq.DSLContext
import org.jooq.impl.DSL.*
import java.math.BigDecimal

class AccountRepository(private val dslContext: DSLContext) {

    fun exists(accountNumber: Int): Boolean {
        return dslContext.fetchExists(dslContext.selectOne()
                .from(table("account"))
                .where(field("account_number").eq(accountNumber)))
    }

    fun createAccount(account: AccountRequest) {
        dslContext.insertInto(table("account"),
                field("account_number"), field("name"), field("balance"))
                .values(account.name, account.accountNumber, account.balance)
                .execute()

    }

    fun selectAllAccounts(): List<Account> {
        return dslContext.select(field("id"), field("account_number"), field("name"), field("balance"))
                .from(table("account"))
                .fetchInto(Account::class.java)
    }

    fun selectAccount(accountNumber: Int): Account? {
        return dslContext.select(field("id"), field("account_number"), field("name"), field("balance"))
                .from(table("account"))
                .where(field("account_number").eq(accountNumber))
                .fetchInto(Account::class.java)
                .firstOrNull()
    }

    fun selectAccountForUpdate(accountNumber: Int): Account? {
        return dslContext.select(field("id"), field("account_number"), field("name"), field("balance"))
                .from(table("account"))
                .where(field("account_number").eq(accountNumber))
                .forUpdate()
                .fetchInto(Account::class.java)
                .firstOrNull()
    }

    fun updateBalance(accountNumber: Int, newBalance: BigDecimal) {
        dslContext.update(table("account"))
                .set(field("balance"), newBalance)
                .where(field("account_number").eq(accountNumber))
                .execute()
    }

}
