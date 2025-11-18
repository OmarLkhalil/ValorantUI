package com.larryyu.domain.usecase

import com.larryyu.domain.repository.PreferencesRepo

class GetThemeUseCase(
    private val themeRepository: PreferencesRepo
) {
    suspend operator fun invoke(): Boolean = themeRepository.isDarkTheme()
}