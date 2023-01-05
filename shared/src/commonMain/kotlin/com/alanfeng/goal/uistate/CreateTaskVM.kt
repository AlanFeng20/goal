package com.alanfeng.goal.uistate

import com.alanfeng.base.AccessStateFlow
import com.alanfeng.goal.base.FormItem
import com.alanfeng.base.ViewModel
import com.alanfeng.goal.model.Task
import com.alanfeng.goal.rep.TaskRep
import com.alanfeng.goal.toInstant
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import org.koin.core.component.KoinComponent


class CreateTaskVM : ViewModel(), KoinComponent {
    val time = AccessStateFlow(FormItem<LocalTime?>(null, true))
    val weeks = AccessStateFlow(FormItem<List<DayOfWeek>>(emptyList(), true))
    val remindWay = AccessStateFlow(FormItem<String?>(null, false))
    val endAt = AccessStateFlow(FormItem<LocalDate?>(null, true))
    private var name: String = ""

    fun setName(string: String){
        name=string.trim()
    }
    fun setTime(localTime: LocalTime) {
        time.value = time.value.copy(data = localTime)
    }

    fun setWeeks(weekList: List<DayOfWeek>) {
        weeks.value = weeks.value.copy(data = weekList)
    }

    fun setRemindWay(way: String) {
        remindWay.value = remindWay.value.copy(data = way)
    }

    fun setEndAt(localDate: LocalDate) {
        endAt.value = endAt.value.copy(data = localDate)
    }

    fun submit(goalId: Long, onOk: () -> Unit) {
        if (name.isNotEmpty()) {
            var ok = true
            listOf(time, remindWay, endAt).forEach {
                val formItem = it.value
                if (formItem.required && formItem.data == null) {
                    ok = false
                    it.value = formItem.copy(hint = true)
                }
            }
            if (ok) {
                scope.launch {
                    TaskRep.insert(
                        Task(
                            goal_id = goalId,
                            name = name,
                            remind_way = remindWay.value.data,
                            card_time = time.value.data!!,
                            created_at = Clock.System.now().toEpochMilliseconds(),
                            end_at = endAt.value.data!!.toInstant().toEpochMilliseconds(),
                            card_weeks = weeks.value.data,
                            id = 0,
                            enable = true,
                            finished_at = null,
                            finish_mark = null
                        )
                    )
                    onOk()
                }
            }
        }
    }
}

