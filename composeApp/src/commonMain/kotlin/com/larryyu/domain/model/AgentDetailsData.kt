package com.larryyu.domain.model
import com.larryyu.data.model.RecruitmentResponseBody
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonElement
@Serializable
data class AgentDetailsData(
	@SerialName("killfeedPortrait")
	val killfeedPortrait: String? = null,
	@SerialName("role")
	val role: RoleDetails? = null,
	@SerialName("isFullPortraitRightFacing")
	val isFullPortraitRightFacing: Boolean? = null,
	@SerialName("displayName")
	val displayName: String? = null,
	@SerialName("isBaseContent")
	val isBaseContent: Boolean? = null,
	@SerialName("description")
	val description: String? = null,
	@SerialName("backgroundGradientColors")
	val backgroundGradientColors: List<String>? = null,
	@SerialName("isAvailableForTest")
	val isAvailableForTest: Boolean? = null,
	@SerialName("uuid")
	val uuid: String? = null,
	@SerialName("characterTags")
	val characterTags: JsonElement? = null,
	@SerialName("displayIconSmall")
	val displayIconSmall: String? = null,
	@SerialName("fullPortrait")
	val fullPortrait: String? = null,
	@SerialName("fullPortraitV2")
	val fullPortraitV2: String? = null,
	@SerialName("abilities")
	val abilities: List<AbilitiesItemDetails>? = null,
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
	val voiceLine: JsonElement? = null,
	@SerialName("isPlayableCharacter")
	val isPlayableCharacter: Boolean? = null,
	@SerialName("developerName")
	val developerName: String? = null
)
@Serializable
data class RoleDetails(
	@SerialName("uuid")
	val uuid: String? = null,
	@SerialName("displayName")
	val displayName: String? = null,
	@SerialName("description")
	val description: String? = null,
	@SerialName("displayIcon")
	val displayIcon: String? = null,
	@SerialName("assetPath")
	val assetPath: String? = null
)
@Serializable
data class AbilitiesItemDetails(
	@SerialName("slot")
	val slot: String? = null,
	@SerialName("displayName")
	val displayName: String? = null,
	@SerialName("description")
	val description: String? = null,
	@SerialName("displayIcon")
	val displayIcon: String? = null
)
