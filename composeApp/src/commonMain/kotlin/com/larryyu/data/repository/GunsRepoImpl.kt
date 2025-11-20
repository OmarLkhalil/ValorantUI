package com.larryyu.data.repository

import com.larryyu.data.datasource.WeaponsEndPoint
import com.larryyu.db.ValorantDatabase
import com.larryyu.domain.entitiy.BaseResponse
import com.larryyu.domain.model.BundlesData
import com.larryyu.domain.model.GunsData
import com.larryyu.domain.repository.GunsRepo
import com.larryyu.domain.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class GunsRepoImpl(
    private val api: WeaponsEndPoint,
    database: ValorantDatabase
) : GunsRepo {

    private val gunsQueries = database.gunsQueries
    private val bundlesQueries = database.bundlesQueries

    override suspend fun getAllGuns(): Flow<DataState<BaseResponse<List<GunsData>>>> = flow {
        emit(DataState.Loading)

        // Get cached guns from database
        val cachedGuns = gunsQueries.selectAllGuns().executeAsList().map { gun ->
            GunsData(
                uuid = gun.uuid,
                displayName = gun.displayName,
                displayIcon = gun.displayIcon,
                contentTierUuid = null,
                wallpaper = null,
                assetPath = null,
                chromas = null,
                themeUuid = null,
                levels = null
            )
        }

        // Emit cached data if available
        if (cachedGuns.isNotEmpty()) {
            emit(DataState.Success(BaseResponse(data = cachedGuns, status = 200)))
        }

        // Try to fetch from network
        try {
            val remoteResponse = api.getAllGuns()
            val remoteGuns = remoteResponse.data ?: emptyList()

            // Clear old data and insert new
            gunsQueries.clearGuns()
            remoteGuns.forEach { gun ->
                gunsQueries.insertGun(
                    uuid = gun.uuid ?: "",
                    displayName = gun.displayName,
                    displayIcon = gun.displayIcon
                )
            }

            // Emit fresh data
            emit(DataState.Success(BaseResponse(data = remoteGuns, status = 200)))
        } catch (e: Exception) {
            // If network fails and no cache, emit error
            if (cachedGuns.isEmpty()) {
                emit(DataState.Error(e))
            }
            // If we have cache, we already emitted it above
        }
    }

    override suspend fun getAllBundles(): Flow<DataState<BaseResponse<List<BundlesData>>>> = flow {
        emit(DataState.Loading)

        // Get cached bundles from database
        val cachedBundles = bundlesQueries.selectAllBundles().executeAsList().map { bundle ->
            BundlesData(
                uuid = bundle.uuid,
                displayName = bundle.displayName,
                displayIcon = bundle.displayIcon,
                displayNameSubText = bundle.displayNameSubText,
                description = bundle.description,
                assetPath = null,
                logoIcon = null,
                extraDescription = null,
                useAdditionalContext = null,
                verticalPromoImage = null,
                promoDescription = null,
                displayIcon2 = null
            )
        }

        // Emit cached data if available
        if (cachedBundles.isNotEmpty()) {
            emit(DataState.Success(BaseResponse(data = cachedBundles, status = 200)))
        }

        // Try to fetch from network
        try {
            val remoteResponse = api.getAllBundles()
            val remoteBundles = remoteResponse.data ?: emptyList()

            // Clear old data and insert new
            bundlesQueries.clearBundles()
            remoteBundles.forEach { bundle ->
                bundlesQueries.insertBundle(
                    uuid = bundle.uuid ?: "",
                    displayName = bundle.displayName,
                    displayIcon = bundle.displayIcon,
                    displayNameSubText = bundle.displayNameSubText,
                    description = bundle.description,
                    extraDescription = bundle.extraDescription.toString()
                )
            }

            // Emit fresh data
            emit(DataState.Success(BaseResponse(data = remoteBundles, status = 200)))
        } catch (e: Exception) {
            // If network fails and no cache, emit error
            if (cachedBundles.isEmpty()) {
                emit(DataState.Error(e))
            }
            // If we have cache, we already emitted it above
        }
    }
} 