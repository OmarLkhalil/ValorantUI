package com.larryyu.valorantui.domain.model

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Json




@JsonClass(generateAdapter = true)
data class AgentDetailsData(
	@Json(name = "killfeedPortrait")
	val killfeedPortrait: String? = null,

	@Json(name = "role")
	val role: RoleDetails? = null,

	@Json(name = "isFullPortraitRightFacing")
	val isFullPortraitRightFacing: Boolean? = null,

	@Json(name = "displayName")
	val displayName: String? = null,

	@Json(name = "isBaseContent")
	val isBaseContent: Boolean? = null,

	@Json(name = "description")
	val description: String? = null,

	@Json(name = "backgroundGradientColors")
	val backgroundGradientColors: List<String>? = null,

	@Json(name = "isAvailableForTest")
	val isAvailableForTest: Boolean? = null,

	@Json(name = "uuid")
	val uuid: String? = null,

	@Json(name = "characterTags")
	val characterTags: Any? = null,

	@Json(name = "displayIconSmall")
	val displayIconSmall: String? = null,

	@Json(name = "fullPortrait")
	val fullPortrait: String? = null,

	@Json(name = "fullPortraitV2")
	val fullPortraitV2: String? = null,

	@Json(name = "abilities")
	val abilities: List<AbilitiesItemDetails>? = null,

	@Json(name = "displayIcon")
	val displayIcon: String? = null,

	@Json(name = "recruitmentData")
	val recruitmentData: RecruitmentData? = null,

	@Json(name = "bustPortrait")
	val bustPortrait: String? = null,

	@Json(name = "background")
	val background: String? = null,

	@Json(name = "assetPath")
	val assetPath: String? = null,

	@Json(name = "voiceLine")
	val voiceLine: Any? = null,

	@Json(name = "isPlayableCharacter")
	val isPlayableCharacter: Boolean? = null,

	@Json(name = "developerName")
	val developerName: String? = null
)

@JsonClass(generateAdapter = true)
data class RoleDetails(
	@Json(name = "uuid")
	val uuid: String? = null,

	@Json(name = "displayName")
	val displayName: String? = null,

	@Json(name = "description")
	val description: String? = null,

	@Json(name = "displayIcon")
	val displayIcon: String? = null,

	@Json(name = "assetPath")
	val assetPath: String? = null
)

@JsonClass(generateAdapter = true)
data class AbilitiesItemDetails(
	@Json(name = "slot")
	val slot: String? = null,

	@Json(name = "displayName")
	val displayName: String? = null,

	@Json(name = "description")
	val description: String? = null,

	@Json(name = "displayIcon")
	val displayIcon: String? = null
)
