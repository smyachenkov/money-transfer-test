package com.revolut.transfer.web

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.revolut.transfer.model.Account
import com.revolut.transfer.model.dto.request.AccountRequest
import com.revolut.transfer.service.AccountService
import com.revolut.transfer.toCurrency
import io.dropwizard.testing.junit.ResourceTestRule
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal
import javax.ws.rs.client.Entity
import kotlin.test.assertEquals


class AccountResourceTest {

    private val account = Account(
            id = 1L,
            accountNumber = 1,
            name = "Alice",
            balance = BigDecimal.ONE
    )

    private val accountService = mock<AccountService>()

    @get:Rule
    val resource: ResourceTestRule = ResourceTestRule.builder()
            .addResource(AccountResource(accountService))
            .build()

    @Test
    fun `get list should call listAllAccounts`() {
        val response = resource.client().target("/account/list").request().get()
        assertEquals(200, response.status)
        verify(accountService).getAllAccounts()
    }

    @Test
    fun `get account by number should call getAccount`() {
        whenever(accountService.getAccount(123)).thenReturn(account)
        val response = resource.client().target("/account/123").request().get()
        assertEquals(200, response.status)
        verify(accountService).getAccount(123)
    }

    @Test
    fun `get account history by number should call getAccountHistory`() {
        val response = resource.client().target("/account/123/history/").request().get()
        assertEquals(200, response.status)
        verify(accountService).getAccountHistory(123)
    }

    @Test
    fun `post should call createAccount`() {
        val account = AccountRequest(
                accountNumber = 1,
                name = "name",
                balance = 1.00.toCurrency()
        )
        val response = resource.client().target("/account").request().post(Entity.json(account))
        val captor = argumentCaptor<AccountRequest>()
        verify(accountService).createAccount(captor.capture())
        assertEquals(201, response.status)
        assertEquals(account, captor.firstValue)
    }

}
