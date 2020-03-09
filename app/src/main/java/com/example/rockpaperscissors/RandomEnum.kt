package com.example.rockpaperscissors

import kotlin.random.Random

/**
 * Random Enum class to get a random integer to use it for computer choices
 */
class RandomEnum {
    companion object {
        private val SEED = Math.random().toInt()
        private val RANDOM = Random(SEED)
        fun randomEnum(): Choices {
            return Choices.values()[RANDOM.nextInt(Choices.values().size)]
        }
    }

}