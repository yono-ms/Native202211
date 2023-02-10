package com.example.native202211.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.native202211.appLogger
import com.example.native202211.toBestString
import com.example.native202211.ui.theme.Native202211Theme
import kotlinx.coroutines.delay
import java.util.*

@Composable
fun ClockScreen() {
    val dateText = remember {
        mutableStateOf("initializing...")
    }
    LaunchedEffect(Unit) {
        kotlin.runCatching {
            appLogger.info("LaunchedEffect START.")
            while (true) {
                dateText.value = Date().toBestString()
                delay(1000)
            }
        }.onFailure {
            appLogger.info("LaunchedEffect onFailure ${it.message}")
        }
    }
    Column {
        Text(text = "Clock Screen")
        Divider()
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = dateText.value,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClockPreview() {
    Native202211Theme {
        ClockScreen()
    }
}
