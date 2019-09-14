package com.revolut.transfer.service

import com.revolut.transfer.exception.AccountNotFoundException
import com.revolut.transfer.model.Account
import com.revolut.transfer.model.Transaction
import com.revolut.transfer.model.dto.request.AccountRequest
import com.revolut.transfer.persistence.AccountRepository
import com.revolut.transfer.persistence.TransactionRepository
import mu.KotlinLogging

private val log = KotlinLogging.logger {}

class AccountService(
        private val accountRepository: AccountRepository,
        private val transactionRepository: TransactionRepository
) {

    fun createAccount(accountRequest: AccountRequest) {
        log.info { "Creating new account $accountRequest" }
        return accountRepository.createAccount(accountRequest)
    }

    fun getAllAccounts(): List<Account> {
        log.info { "Retrieving all accounts" }
        return accountRepository.selectAllAccounts()
    }

    fun getAccount(accountNumber: Int): Account {
        log.info { "Selecting account with number $accountNumber" }
        return accountRepository.selectAccount(accountNumber)
                ?: throw AccountNotFoundException(accountNumber)
    }

    fun getAccountHistory(accountNumber: Int): List<Transaction> {
        log.info { "Getting history for account with number $accountNumber" }
        if (!accountRepository.exists(accountNumber)) {
            throw AccountNotFoundException(accountNumber)
        }
        return transactionRepository.findRelatedTransactions(accountNumber)
    }

}
