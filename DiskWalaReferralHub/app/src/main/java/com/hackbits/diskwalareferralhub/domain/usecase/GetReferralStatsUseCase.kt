package com.hackbits.diskwalareferralhub.domain.usecase

import com.hackbits.diskwalareferralhub.domain.model.StatsResult
import com.hackbits.diskwalareferralhub.domain.repository.ReferralStatsRepository

class GetReferralStatsUseCase(private val repository: ReferralStatsRepository) {
    suspend operator fun invoke(): StatsResult = try {
        repository.load()
    } catch (e: Exception) {
        StatsResult.Failure("Could not load statistics. Please try again later.")
    }
}
