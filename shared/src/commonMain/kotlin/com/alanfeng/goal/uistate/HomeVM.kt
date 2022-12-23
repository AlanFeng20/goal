package com.alanfeng.goal.uistate

import com.alanfeng.goal.base.AccessStateFlow
import com.alanfeng.goal.base.ViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import org.koin.core.component.KoinComponent


data class BottomItem(val name: String, val imageVector: ImageVector)

val bottoms = listOf(
    BottomItem("home", Icons.Outlined.Home),
    BottomItem("add", Icons.Outlined.Add),
    BottomItem("people", Icons.Outlined.Person),
)


class HomeVM: ViewModel(),KoinComponent{
    val selected = AccessStateFlow(bottoms[0])

    fun setCur(item: BottomItem) {
        selected.value = item
    }

}

