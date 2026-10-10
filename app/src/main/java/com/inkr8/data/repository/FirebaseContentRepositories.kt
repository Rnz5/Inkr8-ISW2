package com.inkr8.data.repository

import com.inkr8.data.mapper.toHome
import com.inkr8.data.mapper.toRanking
import com.inkr8.data.source.FunctionsDataSource
import com.inkr8.domain.repository.HomeRepository
import com.inkr8.domain.repository.SeasonRepository

internal class FirebaseHomeRepository(private val source: FunctionsDataSource) : HomeRepository { override suspend fun getHome() = source.call("getHome").toHome() }
internal class FirebaseSeasonRepository(private val source: FunctionsDataSource) : SeasonRepository { override suspend fun getRanking() = source.call("getSeasonRanking").toRanking() }
