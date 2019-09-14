package com.revolut.transfer

import java.math.BigDecimal
import java.math.RoundingMode

fun Double.toCurrency(): BigDecimal = BigDecimal(this).setScale(2, RoundingMode.HALF_UP)
