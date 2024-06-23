package com.larryyu.valorantui.domain.model

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Json


@JsonClass(generateAdapter = true)
data class AgentsData(

	@Json(name="data")
	val data: List<AgentItem>? = null,

	@Json(name="status")
	val status: Int? = null
)


@JsonClass(generateAdapter = true)
data class RecruitmentData(

	@Json(name="levelVpCostOverride")
	val levelVpCostOverride: Int? = null,

	@Json(name="endDate")
	val endDate: String? = null,

	@Json(name="milestoneThreshold")
	val milestoneThreshold: Int? = null,

	@Json(name="milestoneId")
	val milestoneId: String? = null,

	@Json(name="useLevelVpCostOverride")
	val useLevelVpCostOverride: Boolean? = null,

	@Json(name="counterId")
	val counterId: String? = null,

	@Json(name="startDate")
	val startDate: String? = null
)

@JsonClass(generateAdapter = true)
data class AgentItem(

	@Json(name="killfeedPortrait")
	val killfeedPortrait: String? = null,

	@Json(name="role")
	val role: Role? = null,

	@Json(name="isFullPortraitRightFacing")
	val isFullPortraitRightFacing: Boolean? = null,

	@Json(name="displayName")
	val displayName: String? = null,

	@Json(name="isBaseContent")
	val isBaseContent: Boolean? = null,

	@Json(name="description")
	val description: String? = null,

	@Json(name="backgroundGradientColors")
	val backgroundGradientColors: List<String?>? = null,

	@Json(name="isAvailableForTest")
	val isAvailableForTest: Boolean? = null,

	@Json(name="uuid")
	val uuid: String? = null,

	@Json(name="characterTags")
	val characterTags: Any? = null,

	@Json(name="displayIconSmall")
	val displayIconSmall: String? = null,

	@Json(name="fullPortrait")
	val fullPortrait: String? = null,

	@Json(name="fullPortraitV2")
	val fullPortraitV2: String? = null,

	@Json(name="abilities")
	val abilities: List<AbilitiesItem?>? = null,

	@Json(name="displayIcon")
	val displayIcon: String? = null,

	@Json(name="recruitmentData")
	val recruitmentData: RecruitmentData? = null,

	@Json(name="bustPortrait")
	val bustPortrait: String? = null,

	@Json(name="background")
	val background: String? = null,

	@Json(name="assetPath")
	val assetPath: String? = null,

	@Json(name="voiceLine")
	val voiceLine: Any? = null,

	@Json(name="isPlayableCharacter")
	val isPlayableCharacter: Boolean? = null,

	@Json(name="developerName")
	val developerName: String? = null
)

@JsonClass(generateAdapter = true)
data class Role(

	@Json(name="displayIcon")
	val displayIcon: String? = null,

	@Json(name="displayName")
	val displayName: String? = null,

	@Json(name="assetPath")
	val assetPath: String? = null,

	@Json(name="description")
	val description: String? = null,

	@Json(name="uuid")
	val uuid: String? = null
)

@JsonClass(generateAdapter = true)
data class AbilitiesItem(

	@Json(name="displayIcon")
	val displayIcon: String? = null,

	@Json(name="displayName")
	val displayName: String? = null,

	@Json(name="description")
	val description: String? = null,

	@Json(name="slot")
	val slot: String? = null
)
