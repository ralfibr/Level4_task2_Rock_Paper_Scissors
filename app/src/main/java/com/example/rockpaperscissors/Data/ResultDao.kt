package com.example.rockpaperscissors.Data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.rockpaperscissors.ResultActivitiy.Result

/**
 * Data access object
 */
@Dao
interface ResultDao {

    @Query("SELECT * FROM resultTable")
    suspend fun getAllResults(): List<Result>

    @Insert
    suspend fun insertResult(product: Result)

    @Query("DELETE FROM resultTable")
    suspend fun deleteAllResults()

    @Query("SELECT COUNT(id) as total FROM resultTable WHERE winner = :string")
    suspend fun getAmountOf(string: String): Int
}
