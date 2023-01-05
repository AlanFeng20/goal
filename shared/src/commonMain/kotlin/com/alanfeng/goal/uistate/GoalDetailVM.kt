package com.alanfeng.goal.uistate

import com.alanfeng.base.AccessStateFlow
import com.alanfeng.base.ViewModel
import com.alanfeng.goal.model.Goal
import com.alanfeng.goal.model.Task
import com.alanfeng.goal.rep.GoalRep
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.koin.core.component.KoinComponent


class GoalDetailVM : ViewModel(), KoinComponent {
    val goal = AccessStateFlow<Goal?>(null)
    val taskList = AccessStateFlow(emptyList<Task>())
    val progress = AccessStateFlow(0)
    fun toggleEnable() {
        val pre = goal.value ?: return
        scope.launch {
            withProcess {
                val copy = pre.copy(enable = pre.enable.not())
                GoalRep.update(copy)
                goal.value = copy
            }
        }
    }

    fun finish() {
        val pre = goal.value ?: return
        scope.launch {
            withProcess {
                val copy = pre.copy(finished_at = Clock.System.now().toEpochMilliseconds())
                GoalRep.update(copy)
                goal.value = copy
            }
        }
    }

    fun del() {
        val pre = goal.value ?: return
        scope.launch {
            withProcess {
                GoalRep.del(pre)
            }
        }
    }
}

