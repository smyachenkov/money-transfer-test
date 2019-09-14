package com.revolut.transfer

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL

fun initTestDb() : DSLContext {
    val flyway = Flyway.configure()
            .dataSource("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "test", "test")
            .load()
    flyway.clean()
    flyway.migrate()
    val dataSource = HikariDataSource(
            HikariConfig().apply {
                jdbcUrl = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
                username = "test"
                password = "test"
            }
    )
    return DSL.using(dataSource, SQLDialect.H2)
}
