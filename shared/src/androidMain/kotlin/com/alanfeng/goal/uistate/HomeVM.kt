package com.alanfeng.goal.uistate

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent


data class BottomItem(val name: String, val imageVector: ImageVector)

val bottoms = listOf(
    BottomItem("home", Icons.Outlined.Home),
    BottomItem("add", Icons.Outlined.Add),
    BottomItem("people", Icons.Outlined.Person),
)


class HomeVM:KoinComponent{
    private val _selected = MutableStateFlow(bottoms[0])
    val selected = _selected.asStateFlow()

    fun setCur(item: BottomItem) {
        _selected.value = item
    }

}

