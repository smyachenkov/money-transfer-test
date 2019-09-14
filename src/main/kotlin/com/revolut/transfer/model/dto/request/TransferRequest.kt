package com.revolut.transfer.model.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.revolut.transfer.model.serialization.MoneyDeserializer
import java.math.BigDecimal


data class TransferRequest(

        @JsonProperty("from")
        val from: Int,

        @JsonProperty("to")
        val to: Int,

        @JsonProperty("amount")
        @JsonDeserialize(using = MoneyDeserializer::class)
        val amount: BigDecimal

)
