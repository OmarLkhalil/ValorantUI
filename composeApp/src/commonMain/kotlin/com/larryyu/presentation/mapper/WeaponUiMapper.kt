package com.larryyu.presentation.mapper

import com.larryyu.domain.model.ChromasItem
import com.larryyu.domain.model.GunsData
import com.larryyu.domain.model.SkinsItem
import com.larryyu.presentation.model.WeaponChromaUi
import com.larryyu.presentation.model.WeaponSkinUi
import com.larryyu.presentation.model.WeaponUiModel


fun GunsData.toUiModel(): WeaponUiModel {
    return WeaponUiModel(
        id = uuid ?: "",
        name = displayName ?: "Unknown Weapon",
        displayIconUrl = displayIcon ?: "",
        category = category ?: "Unknown",
        skins = skins?.mapNotNull { it?.toUiModel() } ?: emptyList()
    )
}


fun List<GunsData>.toUiModels(): List<WeaponUiModel> {
    return map { it.toUiModel() }
}


fun SkinsItem.toUiModel(): WeaponSkinUi {
    return WeaponSkinUi(
        id = uuid ?: "",
        name = displayName ?: "Unknown Skin",
        iconUrl = displayIcon ?: "",
        chromas = chromas?.mapNotNull { it?.toUiModel() } ?: emptyList()
    )
}


fun ChromasItem.toUiModel(): WeaponChromaUi {
    return WeaponChromaUi(
        name = displayName ?: "Default",
        renderUrl = fullRender ?: ""
    )
}

