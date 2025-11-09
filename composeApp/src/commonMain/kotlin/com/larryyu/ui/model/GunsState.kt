package com.larryyu.ui.model

import com.larryyu.domain.model.BundlesData
import com.larryyu.domain.model.GunsData
import com.larryyu.ui.viewmodel.UiState


data class GunsState(
    val guns: List<GunsData> = emptyList(),
    override val isLoading: Boolean = false,
    override val error: String? = null
)  : UiState

data class BundlesState(
    val bundles: List<BundlesData> = emptyList(),
    override val isLoading: Boolean = false,
    override val error: String? = null
) : UiState
