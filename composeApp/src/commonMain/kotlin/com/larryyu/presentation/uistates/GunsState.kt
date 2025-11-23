package com.larryyu.presentation.uistates

import com.larryyu.presentation.model.WeaponUiModel
import com.larryyu.presentation.viewmodel.UiState


data class GunsState(
    val weapons: List<WeaponUiModel> = emptyList(),
    override val isLoading: Boolean = false,
    override val error: String? = null
) : UiState
