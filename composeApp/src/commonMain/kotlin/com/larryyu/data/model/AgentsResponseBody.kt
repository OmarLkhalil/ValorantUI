package com.larryyu.data.model


import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class RecruitmentResponseBody(

    @SerialName("levelVpCostOverride")
    val levelVpCostOverride: Int? = null,

    @SerialName("endDate")
    val endDate: String? = null,

    @SerialName("milestoneThreshold")
    val milestoneThreshold: Int? = null,

    @SerialName("milestoneId")
    val milestoneId: String? = null,

    @SerialName("useLevelVpCostOverride")
    val useLevelVpCostOverride: Boolean? = null,

    @SerialName("counterId")
    val counterId: String? = null,

    @SerialName("startDate")
    val startDate: String? = null
)

@Serializable
data class AgentsResponseBody(

    @SerialName("killfeedPortrait")
    val killfeedPortrait: String? = null,

    @SerialName("role")
    val role: RoleResponseBody? = null,

    @SerialName("isFullPortraitRightFacing")
    val isFullPortraitRightFacing: Boolean? = null,

    @SerialName("displayName")
    val displayName: String? = null,

    @SerialName("isBaseContent")
    val isBaseContent: Boolean? = null,

    @SerialName("description")
    val description: String? = null,

    @SerialName("backgroundGradientColors")
    val backgroundGradientColors: List<String?>? = null,

    @SerialName("isAvailableForTest")
    val isAvailableForTest: Boolean? = null,

    @SerialName("uuid")
    val uuid: String? = null,

    @SerialName("characterTags")
    val characterTags: List<String?>? = null,

    @SerialName("displayIconSmall")
    val displayIconSmall: String? = null,

    @SerialName("fullPortrait")
    val fullPortrait: String? = null,

    @SerialName("fullPortraitV2")
    val fullPortraitV2: String? = null,

    @SerialName("abilities")
    val abilities: List<AbilitiesItemResponseBody?>? = null,

    @SerialName("displayIcon")
    val displayIcon: String? = null,

    @SerialName("recruitmentData")
    val recruitmentData: RecruitmentResponseBody? = null,

    @SerialName("bustPortrait")
    val bustPortrait: String? = null,

    @SerialName("background")
    val background: String? = null,

    @SerialName("assetPath")
    val assetPath: String? = null,

    @SerialName("voiceLine")
    val voiceLine: String? = null,

    @SerialName("isPlayableCharacter")
    val isPlayableCharacter: Boolean? = null,

    @SerialName("developerName")
    val developerName: String? = null
)

@Serializable
data class RoleResponseBody(

    @SerialName("displayIcon")
    val displayIcon: String? = null,

    @SerialName("displayName")
    val displayName: String? = null,

    @SerialName("assetPath")
    val assetPath: String? = null,

    @SerialName("description")
    val description: String? = null,

    @SerialName("uuid")
    val uuid: String? = null
)

@Serializable
data class AbilitiesItemResponseBody(

    @SerialName("displayIcon")
    val displayIcon: String? = null,

    @SerialName("displayName")
    val displayName: String? = null,

    @SerialName("description")
    val description: String? = null,

    @SerialName("slot")
    val slot: String? = null
)