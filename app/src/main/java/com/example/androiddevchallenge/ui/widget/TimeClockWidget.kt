/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.ui.widget

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.model.State
import com.example.androiddevchallenge.ui.utils.NumberUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@Composable
fun ClockWidget(state: State, onFinish: () -> Unit) {
    var minutes by rememberSaveable { mutableStateOf(4) }
    var seconds by rememberSaveable { mutableStateOf(0) }
    var isRunning by rememberSaveable { mutableStateOf(false) }
    var timerJob: Job? by rememberSaveable { mutableStateOf(null) }
    val infiniteTransition = rememberInfiniteTransition()
    val offset by infiniteTransition.animateFloat(
        initialValue = -4.0f,
        targetValue = 4.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(100, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    var shouldAnimate = false
    if (minutes == 0 && seconds == 0) {
        onFinish()
        isRunning = false
    } else if (minutes == 0 && seconds <= 5 && state == State.START) {
        shouldAnimate = true
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.absoluteOffset(x = if (shouldAnimate) offset.dp else 0.dp)
    ) {
        TimeClockWidget(minutes, state) { minutes = it }
        Text(text = ":", modifier = Modifier.padding(8.dp), fontSize = 36.sp)
        TimeClockWidget(seconds, state) { seconds = it }
    }

    if (state == State.START && !isRunning) {
        isRunning = true
        timerJob = CoroutineScope(Dispatchers.Default).launch {
            timer((minutes * 60) + seconds).collect {
                minutes = it / 60
                seconds = it % 60
            }
        }
    } else if (state == State.STOP && isRunning) {
        onFinish()
        if (timerJob?.isActive == true) timerJob?.cancel()
        minutes = 0
        seconds = 0
        isRunning = false
    }
}

fun timer(seconds: Int): Flow<Int> = flow {
    for (s in (seconds - 1) downTo 0) {
        delay(1000L)
        emit(s)
    }
}

@Composable
fun TimeClockWidget(number: Int, state: State, onNumberChange: (Int) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (state == State.STOP) Image(
            painter = painterResource(R.drawable.ic_arrow_up), contentDescription = null,
            modifier = Modifier.clickable(
                onClick = {
                    onNumberChange(
                        NumberUtils.validateTimeNumber(
                            number + 1
                        )
                    )
                }
            )
        )
        Text(
            text = NumberUtils.timeNumberToString(number),
            modifier = Modifier.padding(8.dp),
            fontSize = 36.sp
        )
        if (state == State.STOP) Image(
            painter = painterResource(R.drawable.ic_arrow_down), contentDescription = null,
            modifier = Modifier.clickable(
                onClick = {
                    onNumberChange(
                        NumberUtils.validateTimeNumber(
                            number - 1
                        )
                    )
                }
            )
        )
    }
}
