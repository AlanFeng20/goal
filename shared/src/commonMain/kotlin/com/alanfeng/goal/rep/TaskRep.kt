package com.alanfeng.goal.rep

import com.alanfeng.goal.database
import com.alanfeng.base.dispatcherIO
import com.alanfeng.goal.model.Task
import kotlinx.coroutines.withContext

object TaskRep {
    suspend fun list(goalId: Long) = withContext(dispatcherIO) {
        database.transactionWithResult {
            database.taskQueries.selectAllByGoalId(goalId).executeAsList()
        }
    }

    suspend fun insert(task: Task) = withContext(dispatcherIO) {
        database.transaction {
            database.taskQueries.insertTask(task)
        }
    }

    suspend fun del(taskId: Long) = withContext(dispatcherIO) {
        database.transaction {
            database.taskQueries.deleteById(taskId)
        }
    }

    suspend fun update(task: Task) = withContext(dispatcherIO) {
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