package com.revolut.transfer.model

import java.math.BigDecimal

data class Account(

        var id: Long,

        var accountNumber: Int,

        var name: String,

        var balance: BigDecimal

)
