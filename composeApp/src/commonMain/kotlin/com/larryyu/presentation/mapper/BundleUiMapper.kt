package com.larryyu.presentation.mapper

import com.larryyu.domain.model.BundlesData
import com.larryyu.presentation.model.BundleUiModel

fun BundlesData.toUiModel(): BundleUiModel {
    return BundleUiModel(
        bundleId = uuid ?: "",
        bundleName = displayName ?: "Unknown Bundle",
        bundleIconUrl = displayIcon ?: "",
        bundleSubText = displayNameSubText
    )
}

fun List<BundlesData>.toBundleUiModels(): List<BundleUiModel> {
    return map { it.toUiModel() }
}

