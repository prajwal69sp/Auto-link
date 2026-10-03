package com.hackbits.diskwalareferralhub.domain.repository

import com.hackbits.diskwalareferralhub.domain.model.StatsResult

interface ReferralStatsRepository {
    suspend fun load(): StatsResult
}
