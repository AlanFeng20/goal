package com.alanfeng.goal.android.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.alanfeng.goal.uistate.BottomItem
import com.alanfeng.goal.uistate.bottoms

@Composable
fun BottomBar(selected: com.alanfeng.goal.uistate.BottomItem, onSelect:(com.alanfeng.goal.uistate.BottomItem)->Unit) {

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        val colorSpec = TweenSpec<Color>(500)
        val sizeSpec = TweenSpec<IntSize>(500)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            com.alanfeng.goal.uistate.bottoms.forEach {
                IconButton(modifier = Modifier.weight(1f),
                    onClick = { onSelect(it) }
                ) {

                    val color by
                    animateColorAsState(
                        targetValue = if (selected == it) MaterialTheme.colorScheme.secondary else Color.Transparent,
                        animationSpec = colorSpec
                    )
                    val modifier = if (selected == it) Modifier
                        .scale(1.2f)
                    else Modifier
                    Icon(
                        modifier = modifier
                            .background(color, shape = CircleShape)
                            .padding(8.dp)
                            .animateContentSize(animationSpec = sizeSpec),
                        imageVector = it.imageVector,
                        contentDescription = ""
                    )
                }
            }
        }
    }
}