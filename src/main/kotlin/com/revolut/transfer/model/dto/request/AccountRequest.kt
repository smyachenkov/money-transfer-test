package com.revolut.transfer.model.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.revolut.transfer.model.serialization.MoneyDeserializer
import java.math.BigDecimal

data class AccountRequest(

        @JsonProperty("name")
        val name: String,

        @JsonProperty("accountNumber")
        val accountNumber: Int,

        @JsonProperty("balance")
        @JsonDeserialize(using = MoneyDeserializer::class)
        val balance: BigDecimal
)