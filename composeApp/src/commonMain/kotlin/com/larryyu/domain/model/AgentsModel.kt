package com.larryyu.domain.model


import kotlinx.serialization.Serializable


@Serializable
data class AgentsModel(
    val displayName: String? = null,
    val uuid: String,
    val fullPortrait: String? = null,
    val role: Role? = null,
    val fullPortraitV2: String? = null
)

@Serializable
data class Role(
    val displayName: String? = null
)

//class RoleConverter {
//
//    @TypeConverter
//    fun fromRole(role: Role?): String? {
//        return Gson().toJson(role)
//    }
//
//    @TypeConverter
//    fun toRole(roleString: String?): Role? {
//        return Gson().fromJson(roleString, object : TypeToken<Role>() {}.type)
//    }
//}