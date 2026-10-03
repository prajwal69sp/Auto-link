package com.hackbits.diskwalareferralhub.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hackbits.diskwalareferralhub.core.config.ReferralConfig
import com.hackbits.diskwalareferralhub.core.util.LinkActions
import com.hackbits.diskwalareferralhub.ui.guide.GuideScreen
import com.hackbits.diskwalareferralhub.ui.home.HomeScreen
import com.hackbits.diskwalareferralhub.ui.home.HomeViewModel
import com.hackbits.diskwalareferralhub.ui.safety.SafetyScreen
import com.hackbits.diskwalareferralhub.ui.web.SecureWebScreen
import kotlinx.coroutines.launch

enum class Destination(val label: String, val icon: ImageVector?) {
    HOME("Home", Icons.Filled.Home),
    GUIDE("Guide", Icons.Filled.Info),
    SAFETY("Safety", Icons.Filled.Lock),
    WEB("DiskWala", null),
}

@Composable
fun AppRoot() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }
    var dest by rememberSaveable { mutableStateOf(Destination.HOME) }
    val viewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
    val homeState by viewModel.state.collectAsState()

    BackHandler(enabled = dest == Destination.GUIDE || dest == Destination.SAFETY) {
        dest = Destination.HOME
    }

    fun toast(message: String) = scope.launch { snackbar.showSnackbar(message) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        bottomBar = {
            if (dest != Destination.WEB) {
                NavigationBar {
                    Destination.entries.filter { it.icon != null }.forEach { item ->
                        NavigationBarItem(
                            selected = dest == item,
                            onClick = { dest = item },
                            icon = { Icon(item.icon!!, contentDescription = null) },
                            label = { Text(item.label) },
                        )
                    }
                }
            }
        },
    ) { padding ->
        AnimatedContent(
            targetState = dest,
            transitionSpec = { fadeIn(tween(220)) togetherWith fadeOut(tween(160)) },
            label = "screen",
        ) { target ->
            val content = Modifier.padding(padding)
            when (target) {
                Destination.HOME -> HomeScreen(
                    state = homeState,
                    onCopy = {
                        LinkActions.copy(context, ReferralConfig.REFERRAL_URL)
                        toast("Link copied")
                    },
                    onShare = {
                        if (!LinkActions.share(context, ReferralConfig.SHARE_TEXT, ReferralConfig.REFERRAL_URL)) {
                            toast("No app available to share with")
                        }
                    },
                    onOpen = { dest = Destination.WEB },
                    onRefresh = viewModel::refresh,
                    modifier = content,
                )
                Destination.GUIDE -> GuideScreen(content)
                Destination.SAFETY -> SafetyScreen(
                    onEmailSupport = {
                        if (!LinkActions.emailSupport(context, ReferralConfig.SUPPORT_EMAIL)) {
                            toast("No email app available")
                        }
                    },
                    modifier = content,
                )
                Destination.WEB -> SecureWebScreen(
                    url = ReferralConfig.REFERRAL_URL,
                    onClose = { dest = Destination.HOME },
                    onOpenInBrowser = {
                        if (!LinkActions.openInBrowser(context, ReferralConfig.REFERRAL_URL)) {
                            toast("No browser available")
                        }
                    },
                    modifier = content,
                )
            }
        }
    }
}
