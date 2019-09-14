package com.revolut.transfer.web

import com.revolut.transfer.model.dto.request.TransferRequest
import com.revolut.transfer.model.dto.response.TransferResponse
import com.revolut.transfer.service.TransferProcessor
import mu.KotlinLogging
import javax.validation.Valid
import javax.validation.constraints.NotNull
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType


private val log = KotlinLogging.logger {}


@Path("/transfer")
@Produces(MediaType.APPLICATION_JSON)
class TransferResource(private val transferProcessor: TransferProcessor) {

    @POST
    fun transfer(@Valid @NotNull request: TransferRequest): TransferResponse? {
        log.info("Transfer request $request")
        return transferProcessor.transfer(request)
    }

}
