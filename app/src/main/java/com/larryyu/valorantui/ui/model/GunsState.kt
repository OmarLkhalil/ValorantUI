package com.larryyu.valorantui.ui.model

import com.larryyu.valorantui.domain.model.BundlesData
import com.larryyu.valorantui.domain.model.GunsData


data class GunsState(
    val guns: List<GunsData> = emptyList(),
    val error: String = "",
    val isLoading: Boolean = false
) : State

data class BundlesState(
    val bundles: List<BundlesData> = emptyList(),
    val error: String = "",
    val isLoading: Boolean = false
) : State
