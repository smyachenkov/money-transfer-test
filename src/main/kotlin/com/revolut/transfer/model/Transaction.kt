package com.revolut.transfer.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.revolut.transfer.model.serialization.MoneyDeserializer
import java.math.BigDecimal
import java.time.OffsetDateTime

data class Transaction(

        val from: Int,

        val to: Int,

        @JsonDeserialize(using = MoneyDeserializer::class)
        val amount: BigDecimal,

        @JsonProperty
        val date: OffsetDateTime

)
