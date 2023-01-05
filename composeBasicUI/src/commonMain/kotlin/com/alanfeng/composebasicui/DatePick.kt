package com.alanfeng.composebasicui

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.min
import com.alanfeng.base.Logs
import kotlinx.datetime.*
import kotlin.math.abs


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePick(
    initDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
    preventDate: ((LocalDate) -> Boolean)? = { it.dayOfWeek == DayOfWeek.THURSDAY },
    onChoose: (LocalDate) -> Unit,
    onCancel: () -> Unit
) {
    var selectedDate by remember {
        mutableStateOf(initDate)
    }
    val preventTextColor = Color.Gray
    val focusTextColor = Color.Unspecified
    val selectTextColor = MaterialTheme.colorScheme.onSecondary
    val selectBgColor = MaterialTheme.colorScheme.secondary
    val unSelectTextColor = MaterialTheme.colorScheme.onPrimaryContainer

    val yearRange = 1970..2500
    Card {
        Column {
            Row {
                WheelItem(selectedDate.year, yearRange,{it.toString()},true){
                    selectedDate= LocalDate(it,selectedDate.monthNumber,selectedDate.dayOfMonth)
                }
            }
            CalendarPanel(
                selectedDate.year, selectedDate.monthNumber, DayOfWeek.FRIDAY
            ) { localDate ->
                Logs.e { "重组子项" }
                val prevented = preventDate?.invoke(localDate) ?: false
                val selected = selectedDate == localDate
                val interactionSource = remember { MutableInteractionSource() }
                val pressed by interactionSource.collectIsPressedAsState()
                val size = min(maxWidth, maxHeight)
                val ripple = rememberRipple(false, size / 2)
                val bgColor = if (selected) selectBgColor else Color.Transparent
                Box(
                    Modifier.align(Alignment.Center).width(size).height(size)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = ripple,
                            enabled = !prevented
                        ) {
                            selectedDate = localDate
                        }.background(bgColor, CircleShape)
                ) {
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
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onCancel) {
                    Text("Cancel")
                }
                TextButton(onClick = { onChoose(selectedDate) }) {
                    Text("OK")
                }
            }
        }
    }
}

@Composable
fun WheelItem(
    cur: Int,
    range: IntRange,
    display: (Int) -> String,
    loop: Boolean,
    onChange: (Int) -> Unit
) {
    if (cur !in range) {
        throw IllegalArgumentException("cur outOf range:cur=$cur,range=$range")
    }
    val startEnable = loop|| cur > range.first
    val endEnable = loop|| cur > range.last
    val size = range.size()
    val pos = cur-range.first
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = {
            onChange((pos - 1 + size) % size+range.first)
        }, enabled = startEnable) {
            Icon(Icons.Default.KeyboardArrowLeft, null)
        }
        Text(text = display(cur))
        IconButton(onClick = {
            onChange((pos + 1 + size) % size+range.first)
        }, enabled = endEnable) {
            Icon(Icons.Default.KeyboardArrowRight, null)
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

fun IntRange.size() = last - first + 1