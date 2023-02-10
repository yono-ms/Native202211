package com.example.native202211.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.native202211.ui.theme.Native202211Theme

@Composable
fun HomeScreen(menuItems: List<NavRoute>, onSelect: (item: NavRoute) -> Unit) {
    Column {
        Text(text = "Menu")
        Divider()
        LazyColumn {
            items(menuItems) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .clickable { onSelect(it) }) {
                    Text(text = it.name, modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    Native202211Theme {
        HomeScreen(listOf(NavRoute.HOME, NavRoute.CLOCK)) {}
    }
}
