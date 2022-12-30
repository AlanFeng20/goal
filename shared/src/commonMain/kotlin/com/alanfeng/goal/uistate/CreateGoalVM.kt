package com.alanfeng.goal.uistate

import com.alanfeng.goal.base.AccessStateFlow
import com.alanfeng.goal.base.ViewModel
import kotlinx.datetime.*
import org.koin.core.component.KoinComponent


class CreateGoalVM : ViewModel(), KoinComponent {
    var name = ""
        set(value) {
            field = value
            updateSug()
        }

    private val now = Clock.System.todayIn(TimeZone.currentSystemDefault())

    var endAt: LocalDate? = null
        set(value) {
            field = value
            if (field != null) {
                lastDay.value = (field!! - now).days
                updateSug()
            }
        }

    val lastDay = AccessStateFlow<Int?>(null)

    val tags = AccessStateFlow<List<String>>(mutableListOf())
    val canTagAdd = AccessStateFlow(true)

    val sug = AccessStateFlow<String?>(null)

    private fun updateSug() {
        when {
            name.isBlank() -> {

            }
            lastDay.value == 0 -> {

            }
        }
    }

}

