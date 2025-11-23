package com.larryyu.presentation.model


data class WeaponUiModel(
    val id: String,
    val name: String,
    val displayIconUrl: String,
    val category: String,
    val skins: List<WeaponSkinUi>
)


data class WeaponSkinUi(
    val id: String,
    val name: String,
    val iconUrl: String,
    val chromas: List<WeaponChromaUi>
)


data class WeaponChromaUi(
    val name: String,
    val renderUrl: String
)

