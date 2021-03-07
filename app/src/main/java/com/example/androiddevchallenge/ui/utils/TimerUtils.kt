package com.example.androiddevchallenge.ui.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object TimerUtils {
    fun timer(seconds: Int): Flow<Int> = flow {
        for (s in (seconds - 1) downTo 0) {
            kotlinx.coroutines.delay(1000L)
            emit(s)
        }
    }
}