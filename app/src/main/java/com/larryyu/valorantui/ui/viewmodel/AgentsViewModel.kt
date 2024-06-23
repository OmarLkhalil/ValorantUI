package com.larryyu.valorantui.ui.viewmodel

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.larryyu.valorantui.domain.usecase.AgentsUseCase
import com.larryyu.valorantui.domain.utils.DataState
import com.larryyu.valorantui.ui.intent.AgentsIntent
import com.larryyu.valorantui.ui.model.AgentsReducer
import com.larryyu.valorantui.ui.model.AgentsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class AgentsViewModel @Inject constructor(
    private val agentsUseCase: AgentsUseCase,
    private val reducer: AgentsReducer
) : ViewModel() {

    private val _state = MutableStateFlow(AgentsState())
    val state = _state.asStateFlow()

    private val intentFlow = MutableSharedFlow<AgentsIntent>()

    init {
        viewModelScope.launch {
            intentFlow.collect { intent ->
                handleIntent(intent)
            }
        }
    }

    private suspend fun handleIntent(intent: AgentsIntent) {
        when (intent) {
            is AgentsIntent.FetchAgents -> fetchAgents()
            else -> _state.value = reducer.reduce(_state.value, intent)
        }
    }

    private suspend fun fetchAgents() {
        _state.value = reducer.reduce(_state.value, AgentsIntent.Loading)
        agentsUseCase().collectLatest { dataState ->
            _state.value = when (dataState) {
                is DataState.Success -> {
                    Log.d("AgentsViewModel", "fetchAgents: ${dataState.data.data}")
                    _state.value.copy(
                        agents = dataState.data.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is DataState.Error -> {
                    Log.e("AgentsViewModel", "fetchAgents error: ${dataState.exception.message}")
                    _state.value.copy(
                        error = dataState.exception.message ?: "",
                        isLoading = false
                    )
                }
                is DataState.Loading -> {
                    Log.d("AgentsViewModel", "fetchAgents: Loading")
                    _state.value.copy(isLoading = true)
                }

                DataState.Idle -> {
                    Log.d("AgentsViewModel", "fetchAgents: Idle")
                    _state.value.copy(isLoading = false)
                }
            }
        }
    }


    fun calcDomaintColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bmp).generate { palette ->
            val swatches = palette?.swatches
            val lightestSwatch = swatches?.maxByOrNull { swatch -> swatch.hsl[1] }
            lightestSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }

    fun dispatch(intent: AgentsIntent) {
        viewModelScope.launch {
            intentFlow.emit(intent)
        }
    }
}
