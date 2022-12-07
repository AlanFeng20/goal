package com.alanfeng.goal.rep

import com.alanfeng.goal.SqlIdPlaceholder
import com.alanfeng.goal.TaskRemindTimeEntity
import com.alanfeng.goal.database
import com.alanfeng.goal.dispatcherIO
import com.alanfeng.goal.model.RemindTime
import com.alanfeng.goal.model.Task
import kotlinx.coroutines.withContext
import kotlinx.datetime.DayOfWeek

object TaskRep {
    suspend fun list(goalId: Long) = withContext(dispatcherIO) {
        database.transactionWithResult {
            database.taskQueries.selectAvaiableByGoalId(goalId).executeAsList()
                .map { taskEntity ->
                    val remindTime =
                        database.taskRemindTimeQueries.selectByTaskId(taskEntity.id).executeAsList()
                            .map { remindTimeEntity ->
                                val remindWeeks = mapOf(
                                    DayOfWeek.SUNDAY to remindTimeEntity.hasSun,
                                    DayOfWeek.MONDAY to remindTimeEntity.hasMon,
                                    DayOfWeek.FRIDAY to remindTimeEntity.hasFri,
                                    DayOfWeek.WEDNESDAY to remindTimeEntity.hasWed,
                                    DayOfWeek.THURSDAY to remindTimeEntity.hasThur,
                                    DayOfWeek.FRIDAY to remindTimeEntity.hasFri,
                                    DayOfWeek.SATURDAY to remindTimeEntity.hasSat,
                                ).filter { it.value }.keys
                                RemindTime(
                                    remindTimeEntity.hour,
                                    remindTimeEntity.minute,
                                    remindWeeks
                                )
                            }
                    Task(
                        id = taskEntity.id,
                        name = taskEntity.name,
                        remind_time = remindTime,
                        created_at = taskEntity.created_at,
                        flag_del = taskEntity.flag_del,
                        enable = taskEntity.enable,
                        finish_mark = taskEntity.finish_mark,
                        goal_id = taskEntity.goal_id,
                        remind_way = taskEntity.remind_way
                    )
                }
        }
    }

    suspend fun insert(task: Task) = withContext(dispatcherIO) {
        database.transaction {
            database.taskQueries.insertTask(
                goal_id = task.goal_id,
                name = task.name,
                remind_way = task.remind_way,
                created_at = task.created_at,
                flag_del = task.flag_del,
                enable = task.enable,
                finish_mark = task.finish_mark
            )
            task.remind_time.forEach {
                database.taskRemindTimeQueries.insertTaskRemindTime(
                    it.toEntity(task.id)
                )
            }
        }
    }

    suspend fun markDel(task: Task) = withContext(dispatcherIO) {
        update(task.apply { flag_del = true })
    }

    suspend fun update(task: Task) = withContext(dispatcherIO) {
        database.transaction {
            database.taskQueries.update(
                id = task.id,
                name = task.name,
                created_at = task.created_at,
                flag_del = task.flag_del,
                enable = task.enable,
                goal_id = task.goal_id,
                remind_way = task.remind_way,
                finish_mark = task.finish_mark
            )
            database.taskRemindTimeQueries.deleteBy(task.id)
            task.remind_time.forEach {
                database.taskRemindTimeQueries.insertTaskRemindTime(it.toEntity(task.id))
            }
        }
    }

    suspend fun listResult(id: Long) = withContext(dispatcherIO) {
        database.taskResultQueries.selectByTaskId(id)
    }

    suspend fun listResultIn(id: Long, min: Long, max: Long) = withContext(dispatcherIO) {
        database.taskResultQueries.selectPeriodByTaskId(id, min, max)
    }

    private fun RemindTime.toEntity(taskId: Long) = TaskRemindTimeEntity(
        task_id = taskId,
        hour = hour,
        minute = minutes,
        hasMon = weekDays.contains(DayOfWeek.MONDAY),
        hasTues = weekDays.contains(DayOfWeek.TUESDAY),
        hasWed = weekDays.contains(DayOfWeek.WEDNESDAY),
        hasThur = weekDays.contains(DayOfWeek.THURSDAY),
        hasFri = weekDays.contains(DayOfWeek.FRIDAY),
        hasSat = weekDays.contains(DayOfWeek.SATURDAY),
        hasSun = weekDays.contains(DayOfWeek.SUNDAY),
        id = SqlIdPlaceholder
    )
}