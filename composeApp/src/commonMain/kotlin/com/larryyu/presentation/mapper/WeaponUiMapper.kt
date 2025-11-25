package com.larryyu.presentation.mapper

import com.larryyu.domain.model.ChromasItem
import com.larryyu.domain.model.GunsData
import com.larryyu.domain.model.LevelsItem
import com.larryyu.domain.model.SkinsItem
import com.larryyu.presentation.model.ChromaUiModel
import com.larryyu.presentation.model.LevelUiModel
import com.larryyu.presentation.model.WeaponSkinUiModel
import com.larryyu.presentation.model.WeaponUiModel
import kotlinx.serialization.json.JsonPrimitive

fun GunsData.toUiModel(): WeaponUiModel {
    return WeaponUiModel(
        weaponId = uuid ?: "",
        weaponName = displayName ?: "Unknown Weapon",
        weaponIconUrl = displayIcon ?: "",
        skins = skins?.map { it?.toUiModel() }
    )
}

fun SkinsItem.toUiModel(): WeaponSkinUiModel {
    return WeaponSkinUiModel(
        skinId = uuid ?: "",
        skinName = displayName ?: "Unknown Skin",
        skinIconUrl = displayIcon,
        chromas = chromas?.map { it?.toUiModel() },
        levels = levels?.map { it?.toUiModel() }
    )
}

fun ChromasItem.toUiModel(): ChromaUiModel {
    return ChromaUiModel(
        chromaId = uuid ?: "",
        chromaName = displayName ?: "Unknown Chroma",
        chromaImageUrl = when (val icon = displayIcon) {
            is JsonPrimitive -> icon.content
            else -> null
        },
        chromaFullRender = fullRender
    )
}

fun LevelsItem.toUiModel(): LevelUiModel {
    return LevelUiModel(
        levelId = uuid ?: "",
        levelName = displayName ?: "Unknown Level",
        levelIconUrl = displayIcon
    )
}

fun List<GunsData>.toUiModels(): List<WeaponUiModel> {
    return map { it.toUiModel() }
}


