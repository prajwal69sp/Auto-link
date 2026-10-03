package com.hackbits.diskwalareferralhub.core.config

/** Single source of truth for app-wide constants. */
object ReferralConfig {
    const val REFERRAL_URL = "https://www.diskwala.com/app/6ab279e72a52418b24d348db"

    /** Only this domain (and its subdomains) may load inside the in-app WebView. */
    const val ALLOWED_DOMAIN = "diskwala.com"

    /** TODO: replace with your real support address before publishing. */
    const val SUPPORT_EMAIL = "support@example.com"

    /** Neutral share text: no income claims, no pressure. */
    const val SHARE_TEXT = "Take a look at DiskWala:"

    const val STATS_UNAVAILABLE_MESSAGE =
        "Referral statistics are managed by DiskWala. Check your DiskWala account for verified earnings."
}
