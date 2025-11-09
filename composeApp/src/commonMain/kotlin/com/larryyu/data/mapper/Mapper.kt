package com.larryyu.data.mapper

import com.larryyu.data.model.AgentsResponseBody
import com.larryyu.data.model.RoleResponseBody
import com.larryyu.domain.entitiy.BaseResponse
import com.larryyu.domain.model.AgentsModel
import com.larryyu.domain.model.Role



internal fun AgentsResponseBody.toDomainModel():AgentsModel {
    return  AgentsModel(
        displayName = displayName,
        uuid = uuid.orEmpty(),
        fullPortrait = fullPortrait,
        role = role?.toDomainModel(),
        fullPortraitV2 = fullPortraitV2
    )
}

internal fun RoleResponseBody.toDomainModel(): Role {
    return Role(
        displayName = displayName
    )
}