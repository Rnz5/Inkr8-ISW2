package com.inkr8.domain.usecase

import com.inkr8.domain.repository.HomeRepository
import com.inkr8.domain.repository.SeasonRepository

class GetHome(private val repository: HomeRepository) { suspend operator fun invoke() = repository.getHome() }
class GetSeasonRanking(private val repository: SeasonRepository) { suspend operator fun invoke() = repository.getRanking() }
