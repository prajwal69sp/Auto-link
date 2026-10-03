package com.hackbits.diskwalareferralhub.domain.model

enum class ReferralStatus { PENDING, ELIGIBLE, CONFIRMED }

data class ReferralEntry(
    val label: String,
    val status: ReferralStatus,
    val date: String,
)

/** Real numbers as reported by an official DiskWala source. Never estimated by the app. */
data class ReferralStats(
    val total: Int,
    val eligible: Int,
    val pending: Int,
    val confirmed: Int,
    val earnings: String?,
    val history: List<ReferralEntry>,
)

/** Outcome of asking for statistics. */
sealed interface StatsResult {
    /** No official DiskWala data source exists / is configured. The default today. */
    data object Unavailable : StatsResult
    data class Available(val stats: ReferralStats) : StatsResult
    data class Failure(val message: String) : StatsResult
}
