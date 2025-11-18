package com.larryyu.ui.theme

import com.larryyu.domain.usecase.GetThemeUseCase
import com.larryyu.domain.usecase.SetThemeUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class ThemeViewModel(
    private val getThemeUseCase: GetThemeUseCase,
    private val setThemeUseCase: SetThemeUseCase
) : KoinComponent {
    private val scope = CoroutineScope(Dispatchers.IO)
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme = _isDarkTheme.asStateFlow()

    init {
        scope.launch { _isDarkTheme.value = getThemeUseCase() }
    }

    fun toggleTheme() {
        val next = !_isDarkTheme.value
        scope.launch {
            setThemeUseCase(next)
            _isDarkTheme.value = next
        }
    }
}
