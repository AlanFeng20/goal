package com.alanfeng.goal

import com.squareup.sqldelight.ColumnAdapter
import com.squareup.sqldelight.db.SqlDriver
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

// in src/commonMain/kotlin
expect class DatabaseDriverFactory() {
    fun createDriver(): SqlDriver
}

val database by lazy {
    createDatabase(DatabaseDriverFactory())
}

private fun createDatabase(driverFactory: DatabaseDriverFactory): AppDatabase {
    val driver = driverFactory.createDriver()
    return AppDatabase(driver)
    // Do more work with the database (see below).
}

private inline fun <reified T : Any> jsonAdapter() =
    object :
        ColumnAdapter<T, String> {
        override fun decode(databaseValue: String): T =
            Json.decodeFromString(databaseValue)

        override fun encode(value: T) = Json.encodeToString(value)
    }
