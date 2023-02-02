package com.example.native202211.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.example.native202211.ui.theme.Native202211Theme

const val LOGIN_MAX_LENGTH = 32

@Composable
fun InputScreen(
    draftLogin: String,
    onChange: (text: String) -> Unit,
    onDone: () -> Unit
) {
    val errorMessage = rememberSaveable { mutableStateOf("") }
    Column {
        TextField(
            value = draftLogin,
            onValueChange = {
                when {
                    it.contains("\n") -> errorMessage.value = "contains RETURN"
                    it.length > LOGIN_MAX_LENGTH -> errorMessage.value =
                        "length ${it.length} ($LOGIN_MAX_LENGTH)"
                    else -> {
                        errorMessage.value = ""
                        onChange(it)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            isError = errorMessage.value.isNotBlank(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                keyboardType = KeyboardType.Ascii,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                onDone()
            }),
            singleLine = true,
            maxLines = 1
        )
        if (errorMessage.value.isNotBlank()) {
            Text(text = errorMessage.value, color = Color.Red)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InputPreview() {
    Native202211Theme {
        InputScreen("test", {}) {}
    }
}
