package com.alanfeng.goal

import com.squareup.sqldelight.ColumnAdapter
import com.squareup.sqldelight.db.SqlDriver
import kotlinx.datetime.DayOfWeek
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
    return AppDatabase(driver, GoalEntity.Adapter(jsonAdapter()) , TaskEntity.Adapter(weekAdapter()) )
    // Do more work with the database (see below).
}

private inline fun <reified T : Any> jsonAdapter() =
    object :
        ColumnAdapter<T, String> {
        override fun decode(databaseValue: String): T =
            Json.decodeFromString(databaseValue)

        override fun encode(value: T) = Json.encodeToString(value)
    }
private inline fun  weekAdapter() =
    object :
        ColumnAdapter<List<DayOfWeek>, String> {
        override fun decode(databaseValue: String):List<DayOfWeek> =
            databaseValue.split(",").map { DayOfWeek.valueOf(it) }

        override fun encode(value: List<DayOfWeek>) = value.joinToString(",") { it.name }
    }