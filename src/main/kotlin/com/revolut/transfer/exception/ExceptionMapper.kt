package com.revolut.transfer.exception

import org.eclipse.jetty.http.HttpStatus
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class AccountNotFoundExceptionMapper : ExceptionMapper<AccountNotFoundException> {
    override fun toResponse(exception: AccountNotFoundException): Response =
            Response.status(HttpStatus.NOT_FOUND_404)
                    .entity("Account ${exception.id} is not found")
                    .type("text/plain")
                    .build()
}

@Provider
class InsufficientAmountExceptionMapper : ExceptionMapper<InsufficientAmountException> {
    override fun toResponse(exception: InsufficientAmountException): Response =
            Response.status(HttpStatus.CONFLICT_409)
                    .entity("Insufficient funds on sender account")
                    .type("text/plain")
                    .build()
}

@Provider
class InvalidTransferParamsExceptionHandler : ExceptionMapper<InvalidTransferParams> {
    override fun toResponse(exception: InvalidTransferParams): Response =
            Response.status(HttpStatus.CONFLICT_409)
                    .entity("InvalidTransferParams: ${exception.message}")
                    .type("text/plain")
                    .build()
}
