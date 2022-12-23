package com.alanfeng.goal.android.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import com.alanfeng.goal.android.ui.BottomBar
import com.alanfeng.goal.uistate.HomeVM
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 *
 * @author: fenglongjun
 *
 * @create: 2022-12-23 17:45
 **/
class HomeScreen : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val vm by inject<HomeVM>()
        val selected by vm.selected.collectAsState()
        BottomBar(selected) {
            vm.setCur(it)
        }
    }

}