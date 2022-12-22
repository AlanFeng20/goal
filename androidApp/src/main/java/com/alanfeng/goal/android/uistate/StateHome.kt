package com.alanfeng.goal.android.uistate

import com.alanfeng.goal.android.ui.bottoms
import kotlinx.coroutines.flow.MutableStateFlow

object StateHome{
    val selected= MutableStateFlow(bottoms[0])
}