package com.example.androiddevchallenge.ui.utils

object NumberUtils {
    fun timeNumberToString(number: Int): String {
        return if (number / 10 == 0) {
            "0${number}"
        } else number.toString()
    }

    fun validateTimeNumber(number: Int): Int {
        return when {
            number >= 60 -> number % 60
            number < 0 -> number + 60
            else -> number
        }
    }
}