package com.larryyu.domain.usecase
import com.larryyu.domain.entitiy.BaseResponse
import com.larryyu.domain.model.BundlesData
import com.larryyu.domain.repository.GunsRepo
import com.larryyu.domain.utils.DataState
import kotlinx.coroutines.flow.Flow
class GetAllBundlesUseCase (
    private val gunsRepo: GunsRepo
) {
    suspend operator fun invoke() : Flow<DataState<BaseResponse<List<BundlesData>>>> = gunsRepo.getAllBundles()
}
