package com.mz.stopwatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mz.stopwatch.ui.theme.StopwatchTheme

class MainActivity : ComponentActivity() {

    val viewModel: StopwatchViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StopwatchTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val time = viewModel.state.collectAsState("00:00:00")
                    val startState = viewModel.startState.collectAsState(true)
                    val stopState = viewModel.startStop.collectAsState(false)

                    MainScreen(
                        time = time,
                        startState = startState,
                        stopState = stopState,
                        onStart = { viewModel.start() },
                        onStop = { viewModel.stop() },
                        onReset = { viewModel.reset() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    time: State<String>,
    startState: State<Boolean>,
    stopState: State<Boolean>,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onReset: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.padding(32.dp))

        Text(
            fontSize = 25.sp,
            text = time.value,
        )

        Spacer(modifier = Modifier.padding(32.dp))

        Button(
            enabled = startState.value,
            onClick = onStart
        ) {
            Text(text = "Start")
        }
        Button(
            enabled = stopState.value,
            onClick = onStop
        ) {
            Text(text = "Stop")
        }
        Button(onClick = onReset) {
            Text(text = "Reset")
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StopwatchTheme {
        MainScreen(
            time = mutableStateOf("00:00:00"),
            startState = mutableStateOf(true),
            stopState =  mutableStateOf(true),
            onStart = {  },
            onStop = {  },
            onReset = {  }
            )
    }
}