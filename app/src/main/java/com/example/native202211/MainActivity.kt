package com.example.native202211

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.example.native202211.screen.MainScreen
import com.example.native202211.ui.theme.Native202211Theme
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MainActivity : ComponentActivity() {

    private val logger: Logger by lazy { LoggerFactory.getLogger(javaClass.name) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logger.info("onCreate savedInstanceState=$savedInstanceState")
        setContent {
            Native202211Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        logger.info("onDestroy")
    }
}
