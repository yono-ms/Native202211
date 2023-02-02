package com.example.native202211.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.native202211.MainViewModel
import com.example.native202211.appDao

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val viewModel: MainViewModel = viewModel()
    Column(modifier = Modifier.fillMaxSize()) {
        val message = viewModel.message.collectAsState()
        val busy = viewModel.busy.collectAsState()
        if (message.value.isNotBlank()) {
            Text(text = message.value)
        }
        if (busy.value) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        val login = rememberSaveable { mutableStateOf("") }
        val draftLogin = rememberSaveable { mutableStateOf("") }
        NavHost(navController = navController, startDestination = NavRoute.HOME.name) {
            composable(NavRoute.HOME.name) {
                val menuItems = listOf(NavRoute.USER)
                HomeScreen(menuItems = menuItems) { navController.navigate(it.name) }
            }
            composable(NavRoute.USER.name) {
                val users = appDao.loadAllUser().collectAsState(initial = emptyList())
                UserScreen(
                    login = login.value,
                    users = users.value,
                    onSelect = { login.value = it },
                    onInput = {
                        draftLogin.value = login.value
                        navController.navigate(NavRoute.INPUT.name)
                    }, onGet = {
                        viewModel.onGet(login.value)
                        navController.navigate(NavRoute.REPO.name)
                    }
                )
            }
            composable(NavRoute.INPUT.name) {
                InputScreen(
                    draftLogin = draftLogin.value,
                    onChange = { draftLogin.value = it },
                    onDone = {
                        login.value = draftLogin.value
                        navController.popBackStack()
                    }
                )
            }
            composable(NavRoute.REPO.name) {
                val repos =
                    appDao.loadSelectedRepo(login.value).collectAsState(initial = emptyList())
                RepoScreen(repos = repos.value)
            }
        }
    }
}
