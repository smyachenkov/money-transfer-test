package com.revolut.transfer.service

import com.revolut.transfer.exception.AccountNotFoundException
import com.revolut.transfer.exception.InsufficientAmountException
import com.revolut.transfer.exception.InvalidTransferParams
import com.revolut.transfer.model.Transaction
import com.revolut.transfer.model.dto.request.TransferRequest
import com.revolut.transfer.model.dto.response.TransferResponse
import com.revolut.transfer.persistence.AccountRepository
import com.revolut.transfer.persistence.TransactionRepository
import mu.KotlinLogging
import org.jooq.DSLContext
import java.time.OffsetDateTime

private val log = KotlinLogging.logger {}


class TransferProcessor(private val dslContext: DSLContext,
                        private val accountRepository: AccountRepository,
                        private val transactionRepository: TransactionRepository) {

    fun transfer(request: TransferRequest): TransferResponse? {
        log.info { "Processing transfer $request" }
        if (request.from == request.to) throw InvalidTransferParams("Can't transfer to same account!")
        var result: TransferResponse? = null
        dslContext.transaction { _ ->
            val fromAccount = accountRepository.selectAccountForUpdate(request.from)
                    ?: throw AccountNotFoundException(request.from)
            if (fromAccount.balance < request.amount) {
                throw InsufficientAmountException()
            }
            val toAccount = accountRepository.selectAccountForUpdate(request.to)
                    ?: throw AccountNotFoundException(request.to)
            log.info { "Transferring ${request.amount} from $fromAccount to $toAccount " }
            val toAccountBalance = toAccount.balance + request.amount
            val fromAccountBalance = fromAccount.balance - request.amount
            accountRepository.updateBalance(toAccount.accountNumber, toAccountBalance)
            accountRepository.updateBalance(fromAccount.accountNumber, fromAccountBalance)
            log.info { "New balance for account $fromAccount: $fromAccountBalance, for $toAccount to $toAccountBalance" }
            transactionRepository.saveTransaction(
                    Transaction(
                            fromAccount.accountNumber,
                            toAccount.accountNumber,
                            request.amount,
                            OffsetDateTime.now()
                    )
            )
            result = TransferResponse(
                    from = fromAccount.accountNumber,
                    to = toAccount.accountNumber,
                    fromBalance = fromAccountBalance,
                    toBalance = toAccountBalance,
                    transferred = request.amount
            )
        }
        return result
    }

}
