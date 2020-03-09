package com.example.rockpaperscissors.ResultActivitiy

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rockpaperscissors.Data.ResultRepository
import com.example.rockpaperscissors.MainActivity
import com.example.rockpaperscissors.R
import kotlinx.android.synthetic.main.activity_result_acitivity.*
import kotlinx.android.synthetic.main.content_result_acitivity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ResultActivity
 */

class ResultActivity : AppCompatActivity() {

    private val resultsList = arrayListOf<Result>()

    private lateinit var resultRepository: ResultRepository
    private val resultsAdapter = ResultsAdapter(resultsList)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_acitivity)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initViews()
        btnDelete.setOnClickListener { deleteAllResults() }
    }
    private fun initViews() {
        resultRepository = ResultRepository(this)
        rvResults.adapter = resultsAdapter
        rvResults.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvResults.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        getResultsFromDatabase()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete_results -> {
                deleteAllResults()
                true
            }
            android.R.id.home -> {
                backHome()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun getResultsFromDatabase() {
        this@ResultActivity.resultsList.clear()
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                resultsList.addAll( resultRepository.getAllResults())
            }
        }.invokeOnCompletion {
            this@ResultActivity.resultsAdapter.notifyDataSetChanged()
        }

    }
    // Navigate to home page
    private fun backHome() {
        val intent = Intent(this@ResultActivity, MainActivity::class.java)
        startActivity(intent)

    }
// Delete all results from database
  private fun deleteAllResults() {
      CoroutineScope(Dispatchers.Main).launch {
          withContext(Dispatchers.IO) {
              resultRepository.deleteAllResults()

          }
          getResultsFromDatabase()
      }


    }


}
