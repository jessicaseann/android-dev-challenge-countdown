package com.example.androiddevchallenge.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.R

@Composable
fun ClockWidget() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        TimeClockWidget()
        Text(text = ":", modifier = Modifier.padding(8.dp), fontSize = 36.sp)
        TimeClockWidget()
    }
}

@Composable
fun TimeClockWidget() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(R.drawable.ic_arrow_up), contentDescription = null )
        Text(text = "01", modifier = Modifier.padding(8.dp), fontSize = 36.sp)
        Image(painter = painterResource(R.drawable.ic_arrow_down), contentDescription = null )
    }
}