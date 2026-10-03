package com.hackbits.diskwalareferralhub.ui.guide

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hackbits.diskwalareferralhub.ui.components.AppCard
import com.hackbits.diskwalareferralhub.ui.components.NumberedStep

private val Steps = listOf(
    "Copy your referral link.",
    "Share it with people who may genuinely be interested.",
    "They voluntarily visit DiskWala.",
    "They complete any qualifying requirements themselves.",
    "Eligible referrals are credited according to DiskWala's official terms.",
    "Check your DiskWala account for verified rewards.",
)

@Composable
fun GuideScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("How It Works", style = MaterialTheme.typography.headlineSmall)
        AppCard {
            Steps.forEachIndexed { index, step -> NumberedStep(index + 1, step) }
        }
        AppCard {
            Text("Good to know", style = MaterialTheme.typography.titleMedium)
            Text(
                "Visiting your own link, asking others to click it without interest, or using " +
                    "extra accounts can violate DiskWala's terms and may forfeit rewards. " +
                    "Always check DiskWala's current referral rules.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
