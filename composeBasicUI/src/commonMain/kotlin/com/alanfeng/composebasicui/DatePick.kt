package com.alanfeng.composebasicui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.alanfeng.base.Logs
import kotlinx.datetime.*
import kotlin.math.abs


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePick(
    modifier: Modifier = Modifier.fillMaxWidth(0.75f)
        .wrapContentHeight(),
    title: String = "选择日期",
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
    Card(
        modifier
    ) {
        Column(Modifier.padding(vertical = 16.dp, horizontal = 8.dp)) {
            Text(
                title,
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.headlineMedium
            )
            WheelItem(
                Modifier.align(Alignment.End).padding(end = 8.dp),
                selectedDate.year,
                selectedDate.monthNumber,
                yearRange,
                { y, m -> "$y ${Month(m).name.substring(0, 3)}" }) { y, m ->
                selectedDate = legalDate(y, m, selectedDate.dayOfMonth)
            }
            CalendarPanel(
                selectedDate.year, selectedDate.monthNumber,yearRange, DayOfWeek.SUNDAY
            ) { localDate ->
                SimpleItem(
                    preventDate?.invoke(localDate) ?: false,
                    selectedDate == localDate,
                    localDate,
                    { selectedDate = it },
                    selectBgColor,
                    preventTextColor,
                    selectTextColor,
                    focusTextColor,
                    unSelectTextColor
                )
            }
            Row(modifier = Modifier.align(Alignment.End)) {
                TextButton(onClick = onCancel) {
                    Text("Cancel")
                }
                TextButton(
                    modifier = Modifier.padding(end = 32.dp, start = 16.dp),
                    onClick = { onChoose(selectedDate) }) {
                    Text("OK")
                }
            }
        }
    }
}

@Composable
private fun BoxWithConstraintsScope.SimpleItem(
    prevented: Boolean,
    selected: Boolean,
    localDate: LocalDate,
    onSelect: (LocalDate) -> Unit,
    selectBgColor: Color,
    preventTextColor: Color,
    selectTextColor: Color,
    focusTextColor: Color,
    unSelectTextColor: Color
) {
    Logs.e { "重组子项" }
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val size = min(maxWidth, maxHeight)
    val ripple = rememberRipple(false, size / 2)
    val bgColor = if (selected) selectBgColor else Color.Transparent
    Box(
        Modifier.align(Alignment.Center)
            .width(size)
            .height(size)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple,
                enabled = !prevented
            ) {
                onSelect(localDate)
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

private fun legalDate(
    y: Int,
    m: Int,
    day: Int
): LocalDate = try {
    LocalDate(y, m, day)
} catch (e: Exception) {
    if (y > 0 && m in 1..12 && day in 1..31) {
        legalDate(y, m, day - 1)
    } else {
        throw e
    }
}

@Composable
fun WheelItem(
    modifier: Modifier,
    year: Int,
    month: Int,
    yearRange: IntRange,
    display: (Int, Int) -> String,
//    loop: Boolean,
    onChange: (Int, Int) -> Unit
) {
    if (year !in yearRange) {
        throw IllegalArgumentException("cur outOf range:cur=$year,range=$yearRange")
    }
    val startEnable = year > yearRange.first
    val endEnable = year < yearRange.last
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = {
            val preMonth = month - 1
            val m = if (preMonth > 0) preMonth else 12
            val y = year - if (preMonth > 0) 0 else 1
            onChange(y, m)
        }, enabled = startEnable) {
            Icon(Icons.Default.KeyboardArrowLeft, null)
        }
        Text(text = display(year, month), style = MaterialTheme.typography.titleLarge)
        IconButton(onClick = {
            val nextMonth = month + 1
            val m = if (nextMonth > 12) 1 else nextMonth
            val y = year + if (nextMonth > 12) 1 else 0
            onChange(y, m)
        }, enabled = endEnable) {
            Icon(Icons.Default.KeyboardArrowRight, null)
        }
    }

}


@Composable
fun CalendarPanel(
    year: Int,
    month: Int,
    yearRange: IntRange,
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
        androidx.compose.foundation.gestures.FlingBehavior
        LazyRow (flingBehavior = ){
            items(yearRange.size(),null,{null}){
                Column(Modifier.fillParentMaxWidth().animateContentSize()) {
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