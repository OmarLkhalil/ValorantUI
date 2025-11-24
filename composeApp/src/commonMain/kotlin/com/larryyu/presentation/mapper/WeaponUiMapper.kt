package com.larryyu.presentation.mapper

import com.larryyu.domain.model.GunsData
import com.larryyu.presentation.model.WeaponUiModel


fun GunsData.toUiModel(): WeaponUiModel {
    return WeaponUiModel(
        weaponId = uuid ?: "",
        weaponName = displayName ?: "Unknown Weapon",
        weaponIconUrl = displayIcon ?: ""
    )
}

fun List<GunsData>.toUiModels(): List<WeaponUiModel> {
    return map { it.toUiModel() }
}


