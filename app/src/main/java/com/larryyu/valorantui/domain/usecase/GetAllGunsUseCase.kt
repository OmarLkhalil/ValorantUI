package com.larryyu.valorantui.domain.usecase

import com.larryyu.valorantui.domain.entitiy.BaseResponse
import com.larryyu.valorantui.domain.model.GunsData
import com.larryyu.valorantui.domain.repository.GunsRepo
import com.larryyu.valorantui.domain.utils.DataState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetAllGunsUseCase @Inject constructor(
    private val gunsRepo: GunsRepo
) {
    suspend operator fun invoke() : Flow<DataState<BaseResponse<List<GunsData>>>> = gunsRepo.getAllGuns()
}