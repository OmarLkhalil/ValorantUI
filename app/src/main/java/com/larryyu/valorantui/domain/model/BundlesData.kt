package com.larryyu.valorantui.domain.model

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Json


@JsonClass(generateAdapter = true)
data class BundlesData(

	@Json(name="displayNameSubText")
	val displayNameSubText: String? = null,

	@Json(name="logoIcon")
	val logoIcon: Any? = null,

	@Json(name="extraDescription")
	val extraDescription: Any? = null,

	@Json(name="displayIcon")
	val displayIcon: String? = null,

	@Json(name="displayName")
	val displayName: String? = null,

	@Json(name="assetPath")
	val assetPath: String? = null,

	@Json(name="useAdditionalContext")
	val useAdditionalContext: Boolean? = null,

	@Json(name="verticalPromoImage")
	val verticalPromoImage: String? = null,

	@Json(name="description")
	val description: String? = null,

	@Json(name="uuid")
	val uuid: String? = null,

	@Json(name="promoDescription")
	val promoDescription: String? = null,

	@Json(name="displayIcon2")
	val displayIcon2: String? = null
)
