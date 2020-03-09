package com.example.rockpaperscissors

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.rockpaperscissors.ResultActivitiy.Result
import com.example.rockpaperscissors.Data.ResultRepository
import com.example.rockpaperscissors.ResultActivitiy.ResultActivity
import com.example.rockpaperscissors.UI.Companion.getDrawableFromChoiche
import kotlinx.coroutines.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*

/**
 * @author Raeef Ibrahim
 * Student nr 500766393
 */
class MainActivity : AppCompatActivity() {
    lateinit var resultRepository: ResultRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        resultRepository = ResultRepository(this)
        initViews()
    }
    private  fun initViews() {
        tvStatstics.text= getResults()
        tvWinLoseMessage.text = ""
        imagePaper.setOnClickListener{ gameProces(Choices.PAPER) }
        imageRock.setOnClickListener{ gameProces(Choices.Rock) }
        imageScissors.setOnClickListener{ gameProces(Choices.SCISSOR) }

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_show_history -> {

               startResultActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveResultToDatabase(result: Result) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                resultRepository.insertResult(result)
            }
        }
    }
// Update images when Imagebtn clicked
    private fun updateUI(result: Result) {
        ivPlayerChoiche.setImageResource(getDrawableFromChoiche(result.playerChoice))
        ivComputerChoiche.setImageResource(getDrawableFromChoiche(result.computerChoice))
        tvWinLoseMessage.text = result.winner
        tvStatstics.text = getResults()
    }
// execute Game after the player chose
    private fun gameProces(playerChoiche: Choices) {
        val choiceComputer = RandomEnum.randomEnum()
        val result = match(playerChoiche , choiceComputer)
    // update images after player chosen
        updateUI(result)
        saveResultToDatabase(result)

    }

// Determine who is the winner
    private fun getWinner(playerChoiche: Choices, computerChoiche: Choices): String {
        if (playerChoiche.toString() == computerChoiche.toString()) {
            return "DRAW"
        }
        if (playerChoiche == Choices.PAPER && computerChoiche == Choices.Rock ||
            playerChoiche == Choices.Rock && computerChoiche == Choices.SCISSOR ||
            playerChoiche == Choices.SCISSOR && computerChoiche == Choices.PAPER
        ) {
            return "You win!!"
        }
        return "Computer wins!"
    }

    private fun getResults(): String {
        var  resultString = ""
        runBlocking(Dispatchers.IO) {
            val wins = async {
                resultRepository.getAmountOfWins("You win!!").toString()
            }
            val loses = async {
                resultRepository.getAmountOfWins("Computer wins!").toString()
            }
            val draws = async {
                resultRepository.getAmountOfWins("DRAW").toString()
            }
            wins.await() + loses.await() + draws.await()
            resultString = "Wins : " + wins.getCompleted() +" Loses : " + loses.getCompleted() + " Draws : " + draws.getCompleted()

        }


        return resultString
    }
    // navigate to Result page
    private fun startResultActivity(){
        val intent = Intent(this@MainActivity, ResultActivity::class.java)
        startActivity(intent)

    }

    //  The process of the match
    private fun match(playerChoiche: Choices, computerChoiche: Choices): Result {
        val winner = getWinner(playerChoiche, computerChoiche)
        return Result(
            playerChoiche.toString(),
            computerChoiche.toString(),
            winner,
            Calendar.getInstance().time.toString()
        )
    }
}
