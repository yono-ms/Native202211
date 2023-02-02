package com.example.native202211

import android.app.Application
import androidx.room.Room
import com.example.native202211.database.AppDao
import com.example.native202211.database.AppDatabase
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MyApp : Application() {
    companion object {
        private lateinit var db: AppDatabase
        fun getDb(): AppDatabase {
            return db
        }
    }

    private val logger: Logger by lazy { LoggerFactory.getLogger(javaClass.name) }

    override fun onCreate() {
        super.onCreate()
        logger.info("onCreate")
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app_database")
            .fallbackToDestructiveMigration().build()
    }
}

val appDao: AppDao
    get() = MyApp.getDb().appDao()

val appLogger: Logger by lazy { LoggerFactory.getLogger("AppLogger") }
