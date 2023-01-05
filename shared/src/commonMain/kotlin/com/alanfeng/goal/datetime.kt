package com.alanfeng.goal

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn

fun LocalDate.toInstant() = this.atStartOfDayIn(TimeZone.currentSystemDefault())