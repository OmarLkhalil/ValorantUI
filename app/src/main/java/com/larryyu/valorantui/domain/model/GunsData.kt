package com.larryyu.valorantui.domain.model

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Json


@JsonClass(generateAdapter = true)
data class ChromasItem(

	@Json(name="displayIcon")
	val displayIcon: Any? = null,

	@Json(name="swatch")
	val swatch: Any? = null,

	@Json(name="displayName")
	val displayName: String? = null,

	@Json(name="assetPath")
	val assetPath: String? = null,

	@Json(name="fullRender")
	val fullRender: String? = null,

	@Json(name="uuid")
	val uuid: String? = null,

	@Json(name="streamedVideo")
	val streamedVideo: Any? = null
)

@JsonClass(generateAdapter = true)
data class GunsData(

	@Json(name="displayIcon")
	val displayIcon: String? = null,

	@Json(name="contentTierUuid")
	val contentTierUuid: String? = null,

	@Json(name="wallpaper")
	val wallpaper: Any? = null,

	@Json(name="displayName")
	val displayName: String? = null,

	@Json(name="assetPath")
	val assetPath: String? = null,

	@Json(name="chromas")
	val chromas: List<ChromasItem?>? = null,

	@Json(name="uuid")
	val uuid: String? = null,

	@Json(name="themeUuid")
	val themeUuid: String? = null,

	@Json(name="levels")
	val levels: List<LevelsItem?>? = null
)

@JsonClass(generateAdapter = true)
data class LevelsItem(

	@Json(name="displayIcon")
	val displayIcon: String? = null,

	@Json(name="levelItem")
	val levelItem: Any? = null,

	@Json(name="displayName")
	val displayName: String? = null,

	@Json(name="assetPath")
	val assetPath: String? = null,

	@Json(name="uuid")
	val uuid: String? = null,

	@Json(name="streamedVideo")
	val streamedVideo: Any? = null
)
