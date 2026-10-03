package com.hackbits.diskwalareferralhub.data

import com.hackbits.diskwalareferralhub.domain.model.StatsResult
import com.hackbits.diskwalareferralhub.domain.repository.ReferralStatsRepository

/**
 * Default repository: DiskWala has no documented public referral API, so the app
 * reports "unavailable" and never invents numbers.
 *
 * ── WHERE AN OFFICIAL DISKWALA API WOULD PLUG IN ─────────────────────────────
 * 1. Confirm in writing that DiskWala offers an official/authorized API and that
 *    your use is permitted by its terms.
 * 2. Create `DiskWalaApiReferralStatsRepository : ReferralStatsRepository`.
 *    Call the official endpoint over HTTPS, map the response to [ReferralStats]
 *    and return [StatsResult.Available]; map network/HTTP errors to
 *    [StatsResult.Failure].
 * 3. Authentication must use the provider's official OAuth/API-key flow. Never ask
 *    for or store the user's DiskWala password. Keep tokens in EncryptedSharedPreferences
 *    or the Android Keystore, and only if the API requires them.
 * 4. Swap the repository in [com.hackbits.diskwalareferralhub.ui.home.HomeViewModel.Factory].
 *    The UI already renders [StatsResult.Available], so no screen changes are needed.
 * ─────────────────────────────────────────────────────────────────────────────
 */
class UnavailableReferralStatsRepository : ReferralStatsRepository {
    override suspend fun load(): StatsResult = StatsResult.Unavailable
}
