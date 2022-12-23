package com.alanfeng.goal.uistate

import com.alanfeng.goal.base.AccessStateFlow
import com.alanfeng.goal.base.ViewModel
import org.koin.core.component.KoinComponent


class CreateGoalVM : ViewModel(), KoinComponent {
    val name = AccessStateFlow("")
    val lastDays = AccessStateFlow(0)

    val sug = AccessStateFlow<String?>(null)

    val enable = AccessStateFlow(false)

    fun setName(string: String) {
        name.value = string
        updateSug()
    }

    fun setLastDays(days: Int) {
        lastDays.value = days
        updateSug()
    }

    private fun updateSug() {
        when{
            name.value.isBlank()->{

            }
            lastDays.value==0->{

            }
        }
    }

}

