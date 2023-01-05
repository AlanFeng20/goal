package com.alanfeng.composebasicui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alanfeng.base.Logs
import kotlinx.datetime.*
import kotlin.math.abs


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePick(
    initDate: LocalDate = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date,
    preventDate: ((LocalDate) -> Boolean)? = { it.dayOfWeek== DayOfWeek.THURSDAY },
) {
    var selectedDate by remember {
        mutableStateOf(initDate)
    }
    val preventTextColor = Color.Gray
    val focusColor = MaterialTheme.colorScheme.tertiary
    val focusTextColor = MaterialTheme.colorScheme.onTertiary
    val selectTextColor = MaterialTheme.colorScheme.onSecondary
    val selectBgColor = MaterialTheme.colorScheme.secondary
    val unSelectTextColor = MaterialTheme.colorScheme.onPrimaryContainer

    Card {
        CalendarPanel(
            initDate.year, initDate.monthNumber, DayOfWeek.FRIDAY
        ) { localDate ->
            Logs.e { "重组子项" }
            val prevented = preventDate?.invoke(localDate) ?: false
            val selected = selectedDate == localDate
            val interactionSource = remember { MutableInteractionSource() }
            val pressed by interactionSource.collectIsPressedAsState()

            val focusChangeColor by animateColorAsState(if(pressed) focusColor else Color.Transparent )
            Box(
                Modifier.height(maxWidth).let {
                    if (!prevented) {
                        it.clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) { selectedDate = localDate }
                    } else it
                }
            ) {
                Canvas(Modifier.fillMaxSize()) {
                    if (selected) {
                        drawCircle(selectBgColor)
                    } else if (pressed) {
                        drawCircle(focusChangeColor)
                    }
                }
                val textColor = when {
                    prevented -> preventTextColor
                    selected -> selectTextColor
                    pressed -> focusTextColor
                    else -> unSelectTextColor
                }
                Text(
                    text = localDate.dayOfMonth.toString(),
                    modifier = Modifier.align(Alignment.Center),
                    color = textColor,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


@Composable
fun CalendarPanel(
    year: Int,
    month: Int,
    weekStart: DayOfWeek,
    weekDisplay: @Composable ((DayOfWeek) -> Unit)? = null,
    item: @Composable BoxWithConstraintsScope.(LocalDate) -> Unit
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
        Column() {
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
                                BoxWithConstraints(itemModifier) {
                                    item(date)
                                }
                            }
                        }
                        val lastMinus = 7 - aWeek.size
                        if (day == days - 1 && lastMinus > 0) {
                            Box(Modifier.weight(lastMinus.toFloat()))
                        }
                    }
                    newRow = false
                    firstRow = false
                    aWeek.clear()
                }
                curDay = curDay.plus(DateTimeUnit.DAY)
                Logs.e { "遍历天数" }
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