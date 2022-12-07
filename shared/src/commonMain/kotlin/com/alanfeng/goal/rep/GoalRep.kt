package com.alanfeng.goal.rep

import com.alanfeng.goal.database
import com.alanfeng.goal.dispatcherIO
import com.alanfeng.goal.model.Goal
import kotlinx.coroutines.withContext

object GoalRep {
    suspend fun listGoals(): List<Goal> = withContext(dispatcherIO) {
        database.transactionWithResult {
            database.goalQueries.selectAvaible().executeAsList().map {
                val goalTagList = database.goalTagQueries.selectNameByGoalId(it.id).executeAsList()
                Goal(
                    parent_id = it.parent_id,
                    name = it.name,
                    tags = goalTagList,
                    created_at = it.created_at,
                    flag_del = it.flag_del,
                    enable = it.enable,
                    id = it.id,
                )
            }
        }
    }

    suspend fun insert(goal: Goal) = withContext(dispatcherIO) {
        database.transaction {
            database.goalQueries.insertGoal(
                parent_id = goal.parent_id,
                name = goal.name,
                created_at = goal.created_at,
                flag_del = goal.flag_del,
                enable = goal.enable
            )
            goal.tags.forEach {
                database.goalTagQueries.insertGoalTag(goal_id = goal.id, name = it)
            }
        }
    }

    suspend fun markDel(goal: Goal) = withContext(dispatcherIO) {
        update(goal.apply { flag_del = true })
    }

    suspend fun update(goal: Goal) = withContext(dispatcherIO) {
        database.transaction {
            database.goalQueries.update(
                id = goal.id,
                parent_id = goal.parent_id,
                name = goal.name,
                created_at = goal.created_at,
                flag_del = goal.flag_del,
                enable = goal.enable
            )
            goal.tags.forEach {
                database.goalTagQueries.insertGoalTag(goal_id = goal.id, name = it)
            }
        }
    }
}