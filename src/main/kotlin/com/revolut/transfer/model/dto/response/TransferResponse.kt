package com.revolut.transfer.model.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class TransferResponse(

        @JsonProperty
        val from: Int,

        @JsonProperty
        val fromBalance: BigDecimal,

        @JsonProperty
        val to: Int,

        @JsonProperty
        val toBalance: BigDecimal,

        @JsonProperty
        val transferred: BigDecimal

)
