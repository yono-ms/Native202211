package com.example.native202211.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class, RepoEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}
