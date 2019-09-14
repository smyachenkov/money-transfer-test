package com.revolut.transfer.web

import com.revolut.transfer.model.Account
import com.revolut.transfer.model.Transaction
import com.revolut.transfer.model.dto.request.AccountRequest
import com.revolut.transfer.service.AccountService
import mu.KotlinLogging
import javax.validation.Valid
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response


private val log = KotlinLogging.logger {}


@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
class AccountResource(private val accountService: AccountService) {

    @GET
    @Path("/list")
    fun getAll(): List<Account> {
        log.info("Get all accounts")
        return accountService.getAllAccounts()
    }

    @GET
    @Path("/{accountNumber}")
    fun get(@PathParam("accountNumber") accountNumber: Int): Account {
        log.info("Get account $accountNumber")
        return accountService.getAccount(accountNumber)
    }

    @GET
    @Path("/{accountNumber}/history/")
    fun getAccountHistory(@PathParam("accountNumber") accountNumber: Int): List<Transaction> {
        log.info("Get history for account $accountNumber")
        return accountService.getAccountHistory(accountNumber)
    }

    @POST
    fun create(@Valid request: AccountRequest): Response {
        log.info("Create account $request")
        accountService.createAccount(request)
        return Response.status(Response.Status.CREATED).build()
    }

}
