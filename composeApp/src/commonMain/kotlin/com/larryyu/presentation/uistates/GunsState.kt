package com.larryyu.presentation.uistates
import com.larryyu.domain.model.BundlesData
import com.larryyu.domain.model.GunsData
import com.larryyu.presentation.viewmodel.UiState
data class GunsState(
    val guns: List<GunsData> = emptyList(),
    val bundles: List<BundlesData> = emptyList(),
    override val isLoading: Boolean = false,
    override val error: String? = null
)  : UiState
