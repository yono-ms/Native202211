package com.example.native202211.database

import androidx.room.*
import com.example.native202211.appLogger
import com.example.native202211.network.RepoModel
import com.example.native202211.network.UserModel
import com.example.native202211.network.toEntity
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
abstract class AppDao {

    @Insert
    abstract suspend fun insert(userEntity: UserEntity)

    @Update
    abstract suspend fun update(userEntity: UserEntity)

    @Query("SELECT * FROM users WHERE login = :login")
    abstract suspend fun getUser(login: String): UserEntity?

    @Transaction
    open suspend fun isCached(login: String): Boolean {
        val user = getUser(login)
        appLogger.debug("${user?.cachedAt} user.cachedAt")
        val cachedAt = user?.cachedAt ?: 0
        val cacheTimeLimit = 600_000
        val limit = Date().time - cacheTimeLimit
        appLogger.debug("$limit limit")
        return cachedAt > limit
    }

    @Transaction
    open suspend fun isUpdated(login: String, updatedAt: String): Boolean {
        appLogger.debug("$updatedAt updatedAt")
        val user = getUser(login)
        appLogger.debug("${user?.updatedAt} user.updatedAt")
        return updatedAt != user?.updatedAt
    }

    @Transaction
    open suspend fun insertUser(login: String, userModel: UserModel) {
        val id = getUser(login)?.id ?: 0
        if (id == 0) {
            appLogger.debug("insert")
            insert(userModel.toEntity())
        } else {
            appLogger.debug("update")
            update(userModel.toEntity(id))
        }
    }

    @Query("SELECT * FROM repos WHERE owner_id = :ownerId")
    abstract suspend fun getAllRepos(ownerId: Int): List<RepoEntity>

    @Insert
    abstract suspend fun insert(vararg repoEntity: RepoEntity)

    @Delete
    abstract suspend fun delete(vararg repoEntity: RepoEntity)

    @Transaction
    open suspend fun replaceRepos(repoModels: List<RepoModel>, ownerId: Int) {
        val list = getAllRepos(ownerId)
        appLogger.debug("list.size = ${list.size}")
        delete(*list.toTypedArray())
        val models = repoModels.toEntity(ownerId)
        appLogger.debug("models.size = ${models.size}")
        insert(*models.toTypedArray())
    }

    @Query("SELECT * FROM users ORDER BY cached_at DESC")
    abstract fun loadAllUser(): Flow<List<UserEntity>>

    @Query("SELECT * FROM repos WHERE owner_id = (SELECT login_id FROM users WHERE login = :login) ORDER BY updated_at DESC")
    abstract fun loadSelectedRepo(login: String): Flow<List<RepoEntity>>
}
