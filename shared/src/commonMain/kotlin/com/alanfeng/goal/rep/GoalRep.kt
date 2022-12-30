package com.alanfeng.goal.rep

import com.alanfeng.goal.GoalEntity
import com.alanfeng.goal.database
import com.alanfeng.goal.dispatcherIO
import com.alanfeng.goal.model.Goal
import kotlinx.coroutines.*

object GoalRep {
    suspend fun listGoals(): List<Goal> = withContext(dispatcherIO) {
        database.transactionWithResult {
            database.goalQueries.selectAll().executeAsList().map {
                val goalTagList = database.goalTagQueries.selectNameByGoalId(it.id).executeAsList()
                Goal(
                    name = it.name,
                    tags = goalTagList,
                    created_at = it.created_at,
                    enable = it.enable,
                    id = it.id,
                    end_at = it.end_at,
                    finished_at = it.finished_at
                )
            }
        }
    }

    suspend fun insert(goal: Goal) = withContext(dispatcherIO) {
        database.transaction {
            database.goalQueries.insertGoal(goal.toGoalEntity())
            goal.tags.forEach {
                database.goalTagQueries.insertGoalTag(goal_id = goal.id, name = it)
            }
        }
    }

    suspend fun del(goal: Goal) = withContext(dispatcherIO) {
        database.transaction {
            database.goalQueries.deleteById(goal.id)
        }
    }

    suspend fun update(goal: Goal) = withContext(dispatcherIO) {
        database.transaction {
            database.goalQueries.update(goal.toGoalEntity())
            database.goalTagQueries.deleteByGoalId(goal_id = goal.id)
            goal.tags.forEach {
                database.goalTagQueries.insertGoalTag(goal_id = goal.id, name = it)
            }
        }
    }

    suspend fun goalTaskResultCount(goalId: Long): Long = withContext(dispatcherIO) {
        database.transactionWithResult {
            database.taskQueries.selectAllByGoalId(goalId).executeAsList().sumOf {
                database.taskResultQueries.countByTaskId(it.id).executeAsOne()
            }
        }
    }
}

private fun Goal.toGoalEntity(): GoalEntity = GoalEntity(
    id = id,
    name = name,
    created_at = created_at,
    end_at = end_at,
    enable = enable,
    finished_at = finished_at
)
