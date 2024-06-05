package com.example.futtoprint.post

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    @Insert
    suspend fun insert(post: Post)

    @Query("SELECT * FROM posts ORDER BY timestamp DESC")
    fun selectAll(): Flow<List<Post>>
}
