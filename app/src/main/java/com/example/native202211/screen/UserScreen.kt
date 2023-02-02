package com.example.native202211.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.native202211.database.UserEntity
import com.example.native202211.toBestString
import com.example.native202211.ui.theme.Native202211Theme
import java.util.*

@Composable
fun UserScreen(
    login: String,
    users: List<UserEntity>,
    onSelect: (text: String) -> Unit,
    onInput: () -> Unit,
    onGet: () -> Unit
) {
    Column {
        Text(text = "User")
        Divider()
        Row {
            TextButton(onClick = { onInput() }, modifier = Modifier.weight(1f)) {
                Text(text = login)
            }
            Button(
                onClick = { onGet() },
                modifier = Modifier.padding(8.dp, 0.dp),
                enabled = login.isNotBlank()
            ) {
                Text(text = "Get")
            }
        }
        Divider()
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(users) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            onSelect(it.login)
                        }) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = Date(it.cachedAt).toBestString(), fontSize = 10.sp)
                        Text(text = it.loginId.toString(), fontSize = 10.sp)
                        Text(text = it.login, fontSize = 10.sp)
                    }
                    Text(text = it.name)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserPreview() {
    Native202211Theme {
        val users = listOf(
            UserEntity(1, 1111111, "user login 1", 11, "user name 1", "url", 1, "2023"),
            UserEntity(2, 2222222, "user login 2", 12, "user name 2", "url", 2, "2023"),
            UserEntity(3, 3333333, "user login 3", 13, "user name 3", "url", 3, "2023"),
        )
        UserScreen("microsoft", users, { }, {}) {}
    }
}
