package com.larryyu.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonElement

@Serializable
data class BundlesData(

    @SerialName("displayNameSubText")
    val displayNameSubText: String? = null,

    @SerialName("logoIcon")
    val logoIcon: JsonElement? = null,

    @SerialName("extraDescription")
    val extraDescription: JsonElement? = null,

    @SerialName("displayIcon")
    val displayIcon: String? = null,

    @SerialName("displayName")
    val displayName: String? = null,

    @SerialName("assetPath")
    val assetPath: String? = null,

    @SerialName("useAdditionalContext")
    val useAdditionalContext: Boolean? = null,

    @SerialName("verticalPromoImage")
    val verticalPromoImage: String? = null,

    @SerialName("description")
    val description: String? = null,

    @SerialName("uuid")
    val uuid: String? = null,

    @SerialName("promoDescription")
    val promoDescription: String? = null,

    @SerialName("displayIcon2")
    val displayIcon2: String? = null
)