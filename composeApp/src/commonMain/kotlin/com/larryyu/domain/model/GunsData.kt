package com.larryyu.domain.model

	import kotlinx.serialization.Serializable
	import kotlinx.serialization.SerialName
	import kotlinx.serialization.json.JsonElement

	@Serializable
	data class ChromasItem(

		@SerialName("displayIcon")
		val displayIcon: JsonElement? = null,

		@SerialName("swatch")
		val swatch: JsonElement? = null,

		@SerialName("displayName")
		val displayName: String? = null,

		@SerialName("assetPath")
		val assetPath: String? = null,

		@SerialName("fullRender")
		val fullRender: String? = null,

		@SerialName("uuid")
		val uuid: String? = null,

		@SerialName("streamedVideo")
		val streamedVideo: JsonElement? = null
	)

	@Serializable
	data class GunsData(

		@SerialName("displayIcon")
		val displayIcon: String? = null,

		@SerialName("contentTierUuid")
		val contentTierUuid: String? = null,

		@SerialName("wallpaper")
		val wallpaper: JsonElement? = null,

		@SerialName("displayName")
		val displayName: String? = null,

		@SerialName("assetPath")
		val assetPath: String? = null,

		@SerialName("chromas")
		val chromas: List<ChromasItem?>? = null,

		@SerialName("uuid")
		val uuid: String? = null,

		@SerialName("themeUuid")
		val themeUuid: String? = null,

		@SerialName("levels")
		val levels: List<LevelsItem?>? = null
	)

	@Serializable
	data class LevelsItem(

		@SerialName("displayIcon")
		val displayIcon: String? = null,

		@SerialName("levelItem")
		val levelItem: JsonElement? = null,

		@SerialName("displayName")
		val displayName: String? = null,

		@SerialName("assetPath")
		val assetPath: String? = null,

		@SerialName("uuid")
		val uuid: String? = null,

		@SerialName("streamedVideo")
		val streamedVideo: JsonElement? = null
	)