package com.example.rockpaperscissors.Data

import android.content.Context
import com.example.rockpaperscissors.ResultActivitiy.Result

/**
 * Result Repository
 */
class ResultRepository (context: Context) {

    private val resultDao: ResultDao

    init {
        val database = ResultRoomDatabase.getDatabase(context)
        resultDao = database!!.resultDao()
    }

    suspend fun getAllResults(): List<Result> = resultDao.getAllResults()

    suspend fun insertResult(result: Result) = resultDao.insertResult(result)

    suspend fun deleteAllResults() = resultDao.deleteAllResults()

    suspend fun getAmountOfWins(string: String) = resultDao.getAmountOf(string)
}