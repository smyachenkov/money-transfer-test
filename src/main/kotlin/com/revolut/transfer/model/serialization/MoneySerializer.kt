package com.revolut.transfer.model.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import java.math.BigDecimal
import java.math.RoundingMode

class MoneyDeserializer : JsonDeserializer<BigDecimal>() {

    override fun deserialize(parser: JsonParser, ctx: DeserializationContext): BigDecimal {
        val value = parser.codec.readTree<JsonNode>(parser)
        return BigDecimal(value.doubleValue()).setScale(2, RoundingMode.HALF_UP)
    }
}
