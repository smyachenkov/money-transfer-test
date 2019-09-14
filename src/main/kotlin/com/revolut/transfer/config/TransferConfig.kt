package com.revolut.transfer.config

import com.fasterxml.jackson.annotation.JsonProperty
import io.dropwizard.Configuration

class TransferApplicationConfig(

        @JsonProperty("name")
        val name: String = "transfer",

        @JsonProperty("database")
        val database: DbConfig = DbConfig()

) : Configuration()

data class DbConfig(

        val user: String = "",

        val password: String = "",

        val url: String = ""

)
