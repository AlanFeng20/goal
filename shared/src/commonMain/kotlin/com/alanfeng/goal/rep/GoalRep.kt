package com.alanfeng.goal.rep

import com.alanfeng.goal.GoalEntity
import com.alanfeng.goal.database
import com.alanfeng.goal.dispatcherIO
import kotlinx.coroutines.withContext

object GoalRep {
    suspend fun listGoals()= withContext(dispatcherIO) {
        database.transactionWithResult {
            database.goalQueries.selectAll().executeAsList()
        }
    }

    suspend fun insert(goal: GoalEntity) = withContext(dispatcherIO) {
        database.transaction {
            database.goalQueries.insertGoal(goal)
        }
    }

    suspend fun del(goal: GoalEntity) = withContext(dispatcherIO) {
        database.transaction {
            database.goalQueries.deleteById(goal.id)
        }
    }

    suspend fun update(goal: GoalEntity) = withContext(dispatcherIO) {
        database.transaction {
            database.goalQueries.update(goal)
        }
    }
}