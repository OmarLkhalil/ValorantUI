package com.larryyu.presentation.model

data class WeaponUiModel(
    val weaponId: String,
    val weaponName: String,
    val weaponIconUrl: String,
    val skins: List<WeaponSkinUiModel?>? = null
)

data class WeaponSkinUiModel(
    val skinId: String,
    val skinName: String,
    val skinIconUrl: String?,
    val chromas: List<ChromaUiModel?>? = null,
    val levels: List<LevelUiModel?>? = null
)

data class ChromaUiModel(
    val chromaId: String,
    val chromaName: String,
    val chromaImageUrl: String?,
    val chromaFullRender: String?
)

data class LevelUiModel(
    val levelId: String,
    val levelName: String,
    val levelIconUrl: String?
)

