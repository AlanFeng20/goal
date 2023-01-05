package com.alanfeng.composebasicui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alanfeng.base.Logs
import kotlinx.datetime.*
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePick() {
    val now = remember { Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date }
    var selected by remember {
        mutableStateOf(now)
    }
    Card {
        CalendarPanel(
            now.year, now.monthNumber, DayOfWeek.SUNDAY
        ) { modifier, localDate ->
            Logs.e { "CalendarPanel item" }
            Text(
                text = localDate.dayOfMonth.toString(),
                modifier = modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    selected = localDate
                }.padding(vertical = 8.dp),
                color = if (selected == localDate) Color.Blue else Color.Unspecified,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun CalendarPanel(
    year: Int,
    month: Int,
    weekStart: DayOfWeek,
    weekDisplay: @Composable ((DayOfWeek) -> Unit)? = null,
    item: @Composable (Modifier, LocalDate) -> Unit
) {
    val ofWeeks = remember(weekStart) {
        weekStart.listFromThis()
    }
    Column {
        if (weekDisplay != null) {
            Row {
                ofWeeks.map {
                    weekDisplay(it)
                }
            }
        }
        Column(Modifier.height(IntrinsicSize.Min)) {
            val firstDay = LocalDate(year, month, 1)
            val days = firstDay.daysUntil(firstDay.plus(1, DateTimeUnit.MONTH))
            val aWeek = mutableListOf<LocalDate?>()
            var newRow = false
            var curDay = firstDay
            var firstRow = true
            val itemModifier = Modifier.weight(1f)
            var minus = -1
            repeat(days) { day ->
                if (firstRow && minus < 0) {
                    minus = abs(curDay.dayOfWeek.ordinal - weekStart.ordinal)
                    repeat(minus) {
                        aWeek.add(null)
                    }
                }
                aWeek.add(curDay)
                if (aWeek.size == 7 || day == days - 1) {
                    newRow = true
                }
                if (newRow) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (firstRow && minus > 0) {
                            Box(Modifier.weight(minus.toFloat()))
                        }
                        aWeek.forEach { date ->
                            if (date != null) {
                                item(itemModifier, date)
                            }
                        }
                        val lastMinus = 7 - aWeek.size
                        if (lastMinus > 0 && day == days - 1) {
                            Box(Modifier.weight(lastMinus.toFloat()))
                        }
                    }
                    newRow = false
                    firstRow = false
                    aWeek.clear()
                }
                curDay = curDay.plus(DateTimeUnit.DAY)
            }
        }
    }
}

private fun DayOfWeek.listFromThis(): MutableList<DayOfWeek> {
    val list = mutableListOf<DayOfWeek>()
    val weeks = DayOfWeek.values()
    var pos = weeks.indexOf(this)
    repeat(7) {
        list.add(weeks[pos])
        pos = (pos + 1) % (weeks.size)
    }
    return list
}