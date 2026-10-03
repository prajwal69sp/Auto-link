package com.hackbits.diskwalareferralhub.core.util

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * User-initiated link actions. Nothing here runs automatically: every function is
 * called from a button tap, and sharing always goes through the system share sheet
 * so the user picks the app and the recipients.
 */
object LinkActions {

    fun copy(context: Context, url: String) {
        val clipboard = context.getSystemService(ClipboardManager::class.java)
        clipboard.setPrimaryClip(ClipData.newPlainText("DiskWala referral link", url))
    }

    fun share(context: Context, message: String, url: String): Boolean {
        val send = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "$message\n$url")
        }
        return start(context, Intent.createChooser(send, "Share your link"))
    }

    /** Opens an https URL in the user's browser. Non-https URLs are refused. */
    fun openInBrowser(context: Context, url: String): Boolean {
        val uri = Uri.parse(url)
        if (!UrlPolicy.isHttps(uri)) return false
        return start(context, Intent(Intent.ACTION_VIEW, uri))
    }

    fun emailSupport(context: Context, address: String): Boolean =
        start(context, Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$address")))

    private fun start(context: Context, intent: Intent): Boolean = try {
        context.startActivity(intent)
        true
    } catch (e: ActivityNotFoundException) {
        false
    }
}
