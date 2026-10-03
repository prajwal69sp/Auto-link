package com.hackbits.diskwalareferralhub.ui.web

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.http.SslError
import android.webkit.CookieManager
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.hackbits.diskwalareferralhub.core.util.LinkActions
import com.hackbits.diskwalareferralhub.core.util.UrlPolicy

/**
 * Hardened WebView for viewing the DiskWala page.
 *  - HTTPS only, DiskWala domain only; other https links open in the user's browser
 *    and only after a real tap (no forced redirects).
 *  - No JavaScript bridge, no file/content access, no mixed content, Safe Browsing on.
 *  - Never clicks, fills or submits anything for the user; never reads cookies.
 */
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun SecureWebScreen(
    url: String,
    onClose: () -> Unit,
    onOpenInBrowser: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var webView by remember { mutableStateOf<WebView?>(null) }
    var progress by remember { mutableIntStateOf(0) }
    var canGoBack by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    BackHandler { if (canGoBack) webView?.goBack() else onClose() }

    DisposableEffect(lifecycleOwner, webView) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> webView?.onPause()
                Lifecycle.Event.ON_RESUME -> webView?.onResume()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Column(modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("DiskWala") },
            navigationIcon = {
                IconButton(onClick = onClose) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Close")
                }
            },
            actions = { TextButton(onClick = onOpenInBrowser) { Text("Open in browser") } },
            windowInsets = WindowInsets(0, 0, 0, 0),
        )
        if (progress in 1..99) {
            LinearProgressIndicator(progress = { progress / 100f }, modifier = Modifier.fillMaxWidth())
        }
        Box(Modifier.fillMaxSize()) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    WebView(ctx).apply {
                        settings.apply {
                            javaScriptEnabled = true // required for the DiskWala page itself
                            domStorageEnabled = true
                            allowFileAccess = false
                            allowContentAccess = false
                            mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
                            setSupportMultipleWindows(false)
                            javaScriptCanOpenWindowsAutomatically = false
                            safeBrowsingEnabled = true
                        }
                        CookieManager.getInstance().setAcceptThirdPartyCookies(this, false)

                        webViewClient = object : WebViewClient() {
                            override fun shouldOverrideUrlLoading(
                                view: WebView,
                                request: WebResourceRequest,
                            ): Boolean {
                                val uri = request.url
                                if (UrlPolicy.isDiskWala(uri)) return false
                                // Off-site: only follow a genuine user tap, in the real browser.
                                if (request.hasGesture() && UrlPolicy.isHttps(uri)) {
                                    LinkActions.openInBrowser(ctx, uri.toString())
                                }
                                return true // block everything else
                            }

                            override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
                                error = null
                            }

                            override fun doUpdateVisitedHistory(view: WebView, url: String?, isReload: Boolean) {
                                canGoBack = view.canGoBack()
                            }

                            override fun onReceivedError(
                                view: WebView,
                                request: WebResourceRequest,
                                err: WebResourceError,
                            ) {
                                if (request.isForMainFrame) {
                                    error = "Couldn't load the page. Check your connection and try again."
                                }
                            }

                            override fun onReceivedSslError(
                                view: WebView,
                                handler: SslErrorHandler,
                                err: SslError,
                            ) {
                                handler.cancel() // never proceed past certificate errors
                                error = "A secure connection could not be established."
                            }
                        }
                        webChromeClient = object : WebChromeClient() {
                            override fun onProgressChanged(view: WebView, newProgress: Int) {
                                progress = newProgress
                            }
                        }
                        loadUrl(url)
                    }.also { webView = it }
                },
                onRelease = {
                    it.stopLoading()
                    it.destroy()
                },
            )

            error?.let { message ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(message, textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyMedium)
                    Button(onClick = { error = null; webView?.reload() }) { Text("Retry") }
                    OutlinedButton(onClick = onClose) { Text("Close") }
                }
            }
        }
    }
}
