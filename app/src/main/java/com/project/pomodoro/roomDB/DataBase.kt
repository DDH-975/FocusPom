package com.project.pomodoro.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [StudySession::class, StudySummary::class], version = 2)
abstract class DataBase : RoomDatabase() {
    abstract fun studySessionDao(): StudySessionDao
    abstract fun studySummaryDao(): StudySummaryDao

    //싱글톤 패턴 적용
    companion object {
        @Volatile
        private var INSTANCE: DataBase? = null

        fun getDatabase(context: Context): DataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DataBase::class.java,
                    "studyTime-database"
                ).addMigrations(MIGRATION_1_2).build()
                INSTANCE = instance
                instance
            }
        }

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("UPDATE StudySummary SET totalStudyTime = 0 WHERE totalStudyTime IS NULL")
                database.execSQL("UPDATE StudySummary SET basicMode = 0 WHERE basicMode IS NULL")
                database.execSQL("UPDATE StudySummary SET longFocus = 0 WHERE longFocus IS NULL")
                database.execSQL("UPDATE StudySummary SET shortFocus = 0 WHERE shortFocus IS NULL")
                database.execSQL("UPDATE StudySummary SET ultraFocus = 0 WHERE ultraFocus IS NULL")
                database.execSQL("UPDATE StudySummary SET coustomMode = 0 WHERE coustomMode IS NULL")
            }
        }

    }
}