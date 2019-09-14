package com.revolut.transfer

import com.fasterxml.jackson.databind.SerializationFeature
import com.revolut.transfer.config.TransferApplicationConfig
import com.revolut.transfer.exception.AccountNotFoundExceptionMapper
import com.revolut.transfer.exception.InsufficientAmountExceptionMapper
import com.revolut.transfer.exception.InvalidTransferParamsExceptionHandler
import com.revolut.transfer.persistence.AccountRepository
import com.revolut.transfer.persistence.TransactionRepository
import com.revolut.transfer.web.AccountResource
import com.revolut.transfer.web.TransferResource
import com.revolut.transfer.service.AccountService
import com.revolut.transfer.service.TransferProcessor
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.dropwizard.Application
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import org.flywaydb.core.Flyway
import org.jooq.SQLDialect
import org.jooq.impl.DSL


class TransferApp : Application<TransferApplicationConfig>() {

    override fun getName() = "transfer"

    override fun initialize(bootstrap: Bootstrap<TransferApplicationConfig>?) {}

    override fun run(config: TransferApplicationConfig,
                     environment: Environment) {
        applyFlywayMigrations(config)
        configureJackson(environment)
        resolveDependencies(config, environment)
    }

    private fun applyFlywayMigrations(config: TransferApplicationConfig) {
        val flyway = Flyway.configure()
                .dataSource(config.database.url,
                        config.database.user,
                        config.database.password
                ).load()
        flyway.clean()
        flyway.migrate()
    }

    private fun configureJackson(environment: Environment) {
        environment.objectMapper?.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }

    private fun resolveDependencies(config: TransferApplicationConfig,
                                    environment: Environment) {
        val dataSource = HikariDataSource(
                HikariConfig().apply {
                    jdbcUrl = config.database.url
                    username = config.database.user
                    password = config.database.password
                }
        )
        val dslContext = DSL.using(dataSource, SQLDialect.H2)

        val accountRepository = AccountRepository(dslContext)
        val transactionRepository = TransactionRepository(dslContext)

        val accountService = AccountService(accountRepository, transactionRepository)
        val transferProcessor = TransferProcessor(dslContext, accountRepository, transactionRepository)

        environment.jersey()?.register(TransferResource(transferProcessor))
        environment.jersey()?.register(AccountResource(accountService))

        environment.jersey()?.register(AccountNotFoundExceptionMapper())
        environment.jersey()?.register(InsufficientAmountExceptionMapper())
        environment.jersey()?.register(InvalidTransferParamsExceptionHandler())
    }

}

fun main(args: Array<String>) {
    TransferApp().run(*args)
}
