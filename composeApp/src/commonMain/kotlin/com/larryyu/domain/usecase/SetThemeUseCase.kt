package com.larryyu.domain.usecase
import com.larryyu.domain.repository.PreferencesRepo
class SetThemeUseCase(
    private val themeRepository: PreferencesRepo
) {
    suspend operator fun invoke(enabled: Boolean) = themeRepository.setDarkTheme(enabled)
}
