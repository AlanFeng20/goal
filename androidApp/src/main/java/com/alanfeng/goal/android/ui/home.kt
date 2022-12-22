package com.alanfeng.goal.android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alanfeng.goal.android.uistate.StateHome

data class BottomItem(val name: String, val imageVector: ImageVector)

val bottoms = listOf(
    BottomItem("home", Icons.Outlined.Home),
    BottomItem("add", Icons.Outlined.Add),
    BottomItem("people", Icons.Outlined.Person),
)

@Preview
@Composable
fun BottomAppBar() {
    val selected by StateHome.selected.collectAsState()
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
            bottoms.forEach {
                IconButton(modifier = Modifier.weight(1f), onClick = { /*TODO*/ }) {
                    val modifier = if (selected == it) Modifier
                        .scale(1.5f)
                        .background(MaterialTheme.colorScheme.secondary, shape = CircleShape)
                    else Modifier
                    Icon(
                        modifier = modifier.padding(3.dp),
                        imageVector = it.imageVector,
                        contentDescription = ""
                    )
                }
            }
        }
    }
}