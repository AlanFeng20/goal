package com.alanfeng.goal.uistate

import com.alanfeng.base.AccessStateFlow
import com.alanfeng.base.ViewModel
import com.alanfeng.goal.model.Goal
import com.alanfeng.goal.rep.GoalRep
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent


class GoalListVM : ViewModel(), KoinComponent {
    val list = AccessStateFlow(emptyList<Goal>())

    fun update(){
        scope.launch {
            list.value=GoalRep.listGoals()
        }
    }
}

