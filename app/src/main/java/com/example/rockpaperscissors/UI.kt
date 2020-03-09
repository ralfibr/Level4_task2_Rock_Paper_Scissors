package com.example.rockpaperscissors

/**
 * get result in UI
 */

class UI {
    companion object {
        fun getDrawableFromChoiche(result: String): Int {
            if (result == Choices.SCISSOR.toString()) {
                return R.drawable.scissors
            }
            if (result == Choices.PAPER.toString()) {
                return R.drawable.paper
            }
            return R.drawable.rock
        }
    }
}