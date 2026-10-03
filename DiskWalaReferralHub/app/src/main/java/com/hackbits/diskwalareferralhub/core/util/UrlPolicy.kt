package com.hackbits.diskwalareferralhub.core.util

import android.net.Uri
import com.hackbits.diskwalareferralhub.core.config.ReferralConfig

/** Centralised URL rules so every screen applies the same security policy. */
object UrlPolicy {
    fun isHttps(uri: Uri): Boolean = uri.scheme.equals("https", ignoreCase = true)

    /** True only for https URLs on diskwala.com or its subdomains. */
    fun isDiskWala(uri: Uri): Boolean {
        if (!isHttps(uri)) return false
        val host = uri.host?.lowercase() ?: return false
        val domain = ReferralConfig.ALLOWED_DOMAIN
        return host == domain || host.endsWith(".$domain")
    }
}
