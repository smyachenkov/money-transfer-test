package com.revolut.transfer.web

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.revolut.transfer.model.dto.request.TransferRequest
import com.revolut.transfer.service.TransferProcessor
import com.revolut.transfer.toCurrency
import io.dropwizard.testing.junit.ResourceTestRule
import org.junit.Rule
import org.junit.Test
import javax.ws.rs.client.Entity
import kotlin.test.assertEquals

class TransferResourceTest {

    private val transferProcessor = mock<TransferProcessor>()

    @get:Rule
    val resource: ResourceTestRule = ResourceTestRule.builder()
            .addResource(TransferResource(transferProcessor))
            .build()

    @Test
    fun `post should call transferProcessor`() {
        val request = TransferRequest(
                from = 1,
                to = 2,
                amount = 100.00.toCurrency()
        )
        val response = resource.client().target("/transfer").request().post(Entity.json(request))
        assertEquals(204, response.status)
        verify(transferProcessor).transfer(request)
    }

}
