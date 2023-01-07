package com.alanfeng.goal.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.alanfeng.composebasicui.DatePick
import com.alanfeng.goal.android.ui.theme.GoalTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GoalTheme() {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    Navigator(screen = HomeScreen())
                    DatePick(modifier = Modifier.fillMaxWidth(0.75f).wrapContentHeight(), onCancel = {}, onChoose = {})
                }
            }
        }
    }
}

