package com.mz.stopwatch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StopwatchViewModel(

) : ViewModel() {

    private val time = MutableStateFlow(0L)

    val state: Flow<String> = time.map { format(it) }
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
        )

    val startState: StateFlow<Boolean> get() = _startState

    private val _startState: MutableStateFlow<Boolean> = MutableStateFlow(true)

    val startStop: StateFlow<Boolean> get() = _startStop

    private val _startStop: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private var job: Job? = null


    fun start() {
        _startState.update { false }
        _startStop.update { true }

        job = viewModelScope.launch {
            val start = System.currentTimeMillis() - time.value
            while (true) {
                time.update { System.currentTimeMillis() - start }
                delay(10)
            }
        }
    }

    fun stop() {
        _startState.update { true }
        _startStop.update { false }
        job?.cancel()
    }

    fun reset() {
        stop()
        time.update { 0 }
        start()
    }

    private fun format(time: Long): String {
        val seconds = (time / 1000) % 60
        val minutes = (time / 1000) / 60
        val milliseconds = (time % 1000) / 10
        return String.format("%02d:%02d:%02d", minutes, seconds, milliseconds)
    }
}