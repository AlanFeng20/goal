package com.alanfeng.goal.rep

import com.alanfeng.goal.TaskEntity
import com.alanfeng.goal.database
import com.alanfeng.goal.dispatcherIO
import kotlinx.coroutines.withContext

object TaskRep {
    suspend fun list(goalId: Long) = withContext(dispatcherIO) {
        database.transactionWithResult {
            database.taskQueries.selectAllByGoalId(goalId).executeAsList()
        }
    }

    suspend fun insert(task: TaskEntity) = withContext(dispatcherIO) {
        database.transaction {
            database.taskQueries.insertTask(task)
        }
    }

    suspend fun del(task: TaskEntity) = withContext(dispatcherIO) {
        database.transaction {
            database.taskQueries.deleteById(task.id)
        }
    }

    suspend fun update(task: TaskEntity) = withContext(dispatcherIO) {
        database.transaction {
            database.taskQueries.update(task)
        }
    }

    suspend fun listResult(id: Long) = withContext(dispatcherIO) {
        database.taskResultQueries.selectByTaskId(id)
    }

    suspend fun listResultIn(id: Long, min: Long, max: Long) = withContext(dispatcherIO) {
        database.taskResultQueries.selectPeriodByTaskId(id, min, max)
    }

}