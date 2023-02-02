package com.example.native202211

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.native202211.network.RepoModel
import com.example.native202211.network.UserModel
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitStringResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MainViewModel : ViewModel() {

    private val logger: Logger by lazy { LoggerFactory.getLogger(javaClass.name) }

    private val json: Json by lazy {
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            isLenient = true
        }
    }

    private val _message = MutableStateFlow("Initializing...")
    val message: StateFlow<String> = _message

    private val _busy = MutableStateFlow(true)
    val busy: StateFlow<Boolean> = _busy

    fun onGet(login: String) {
        logger.info("onGet $login")
        if (_busy.value) {
            logger.info("busy.")
            return
        }
        viewModelScope.launch {
            runCatching {
                _busy.value = true
                _message.value = ""

                if (appDao.isCached(login)) {
                    logger.info("cache exist.")
                } else {
                    logger.info("cache expired.")
                    val userModel = getUserFromServer(login)
                    val isUpdated = appDao.isUpdated(login, userModel.updatedAt)
                    appDao.insertUser(login, userModel)
                    if (isUpdated) {
                        logger.info("user updated.")
                        val ownerId = userModel.id
                        val repoModels = getReposFromServer(userModel.reposUrl)
                        appDao.replaceRepos(repoModels, ownerId)
                    } else {
                        logger.info("user is not updated.")
                    }
                }
            }.onSuccess {
                logger.info("onGet success.")
            }.onFailure {
                logger.error("onGet error.", it)
                _message.value = it.localizedMessage ?: ""
            }.also {
                _busy.value = false
            }
        }
    }

    private suspend fun getUserFromServer(login: String): UserModel {
        logger.info("getUserFromServer $login")
        val url = "https://api.github.com/users/${login}"
        val (request, response, json) = Fuel.get(url).awaitStringResponse()
        logger.debug(request.toString())
        logger.debug(response.toString())
        val userModel = this.json.decodeFromString<UserModel>(json)
        logger.debug(userModel.toString())
        return userModel
    }

    private suspend fun getReposFromServer(url: String): List<RepoModel> {
        logger.info("getReposFromServer $url")
        val (request, response, json) = Fuel.get(url).awaitStringResponse()
        logger.debug(request.toString())
        logger.debug(response.toString())
        val repoModels = this.json.decodeFromString<List<RepoModel>>(json)
        logger.debug("repoModels size=${repoModels.size}")
        return repoModels
    }

    init {
        logger.info("init.")
        _message.value = ""
        _busy.value = false
    }
}
