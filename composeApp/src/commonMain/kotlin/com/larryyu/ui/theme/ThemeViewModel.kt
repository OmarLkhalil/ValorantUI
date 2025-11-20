package com.larryyu.ui.theme

import com.larryyu.domain.usecase.GetThemeUseCase
import com.larryyu.domain.usecase.SetThemeUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class ThemeViewModel(
    private val getThemeUseCase: GetThemeUseCase,
    private val setThemeUseCase: SetThemeUseCase
) : KoinComponent {
    private val scope = CoroutineScope(Dispatchers.Default)
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme = _isDarkTheme.asStateFlow()

    private val _animationFraction = MutableStateFlow(0f)
    val animationFraction = _animationFraction.asStateFlow()

    init {
        scope.launch { _isDarkTheme.value = getThemeUseCase() }
    }

    fun toggleTheme() {
        val next = !_isDarkTheme.value
        _isDarkTheme.value = next
        scope.launch {
            setThemeUseCase(next)
        }
    }

    fun setAnimationFraction(fraction: Float) {
        _animationFraction.value = fraction
    }
}
