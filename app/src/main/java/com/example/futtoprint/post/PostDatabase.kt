package com.example.futtoprint.post

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Post::class], version = 1, exportSchema = false)
abstract class PostDatabase : RoomDatabase() {
    // Room generate Dao
    abstract fun postDao(): PostDao

    // A singleton of PostDatabase
    companion object {
        // atomic read and write
        @Volatile
        private var instance: PostDatabase? = null

        fun getDatabase(context: Context): PostDatabase =
            instance ?: synchronized(this) {
                // initialize in synchronized brock to avoid conflicts
                Room.databaseBuilder(context, PostDatabase::class.java, "post_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { instance = it }
            }
    }
}
