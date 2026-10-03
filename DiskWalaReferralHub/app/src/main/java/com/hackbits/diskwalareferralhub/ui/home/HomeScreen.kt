package com.hackbits.diskwalareferralhub.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hackbits.diskwalareferralhub.core.config.ReferralConfig
import com.hackbits.diskwalareferralhub.domain.model.ReferralEntry
import com.hackbits.diskwalareferralhub.domain.model.StatsResult
import com.hackbits.diskwalareferralhub.ui.components.AppCard
import com.hackbits.diskwalareferralhub.ui.components.AppLogo
import com.hackbits.diskwalareferralhub.ui.components.NoticeCard
import com.hackbits.diskwalareferralhub.ui.components.SectionTitle
import com.hackbits.diskwalareferralhub.ui.components.StatTile

@Composable
fun HomeScreen(
    state: HomeUiState,
    onCopy: () -> Unit,
    onShare: () -> Unit,
    onOpen: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Header()
        ReferralLinkCard(onCopy, onShare, onOpen)
        ReferralInfoCard()
        StatsSection(state, onRefresh)
    }
}

@Composable
private fun Header() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        AppLogo()
        Spacer(Modifier.width(16.dp))
        Column {
            Text("DiskWala Referral Hub", style = MaterialTheme.typography.headlineSmall)
            Text(
                "Independent referral companion",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun ReferralLinkCard(onCopy: () -> Unit, onShare: () -> Unit, onOpen: () -> Unit) {
    val onContainer = MaterialTheme.colorScheme.onPrimaryContainer
    AppCard(container = MaterialTheme.colorScheme.primaryContainer) {
        Text("My Referral Link", style = MaterialTheme.typography.titleMedium, color = onContainer)
        SelectionContainer {
            Text(
                ReferralConfig.REFERRAL_URL,
                style = MaterialTheme.typography.bodyMedium,
                color = onContainer,
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            FilledTonalButton(
                onClick = onCopy,
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 48.dp),
            ) { Text("Copy Link") }
            Button(
                onClick = onShare,
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 48.dp),
            ) {
                Icon(Icons.Filled.Share, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Share Link")
            }
        }
        OutlinedButton(
            onClick = onOpen,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp),
        ) { Text("Open DiskWala") }
    }
}

@Composable
private fun ReferralInfoCard() {
    AppCard {
        Text("Referral information", style = MaterialTheme.typography.titleMedium)
        Text(
            "Share your link only with people who may genuinely be interested. " +
                "Rewards, eligibility and payouts are decided solely by DiskWala under its " +
                "own terms. Earnings are not guaranteed, and this app never sends messages " +
                "or generates visits on your behalf.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun StatsSection(state: HomeUiState, onRefresh: () -> Unit) {
    val stats = (state.result as? StatsResult.Available)?.stats
    val dash = "—"

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionTitle("Referral statistics")
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatTile("Total genuine referrals", stats?.total?.toString() ?: dash, Modifier.weight(1f))
            StatTile("Eligible", stats?.eligible?.toString() ?: dash, Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatTile("Pending", stats?.pending?.toString() ?: dash, Modifier.weight(1f))
            StatTile("Confirmed", stats?.confirmed?.toString() ?: dash, Modifier.weight(1f))
        }
        StatTile("Earnings", stats?.earnings ?: dash, Modifier.fillMaxWidth())

        when (val result = state.result) {
            null -> if (state.isLoading) LinearProgressIndicator(Modifier.fillMaxWidth())
            StatsResult.Unavailable -> NoticeCard(ReferralConfig.STATS_UNAVAILABLE_MESSAGE)
            is StatsResult.Failure -> {
                NoticeCard(result.message)
                TextButton(onClick = onRefresh) { Text("Retry") }
            }
            is StatsResult.Available -> Unit
        }

        SectionTitle("Referral history")
        HistoryList(stats?.history.orEmpty())
    }
}

@Composable
private fun HistoryList(history: List<ReferralEntry>) {
    AppCard {
        if (history.isEmpty()) {
            Text(
                "No referral history is available in the app. " +
                    "Your DiskWala account is the source of truth.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        } else {
            history.forEach { entry ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(entry.label, style = MaterialTheme.typography.bodyMedium)
                    Text(
                        "${entry.status.name.lowercase().replaceFirstChar { it.uppercase() }} · ${entry.date}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}
