package com.hackbits.diskwalareferralhub.ui.safety

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hackbits.diskwalareferralhub.core.config.ReferralConfig
import com.hackbits.diskwalareferralhub.ui.components.AppCard
import com.hackbits.diskwalareferralhub.ui.components.ExpandableCard
import com.hackbits.diskwalareferralhub.ui.components.NoticeCard
import com.hackbits.diskwalareferralhub.ui.components.SectionTitle

@Composable
fun SafetyScreen(onEmailSupport: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("Trust & Safety", style = MaterialTheme.typography.headlineSmall)
        NoticeCard(LegalContent.EARNINGS)

        AppCard {
            Text("Our commitments", style = MaterialTheme.typography.titleMedium)
            LegalContent.COMMITMENTS.forEach {
                Text("•  $it", style = MaterialTheme.typography.bodyMedium)
            }
        }

        SectionTitle("Policies")
        ExpandableCard("Referral disclosure", LegalContent.DISCLOSURE)
        ExpandableCard("Privacy Policy", LegalContent.PRIVACY)
        ExpandableCard("Terms of Use", LegalContent.TERMS)

        SectionTitle("Contact & support")
        AppCard {
            Text(
                "Questions or concerns? Email ${ReferralConfig.SUPPORT_EMAIL}.",
                style = MaterialTheme.typography.bodyMedium,
            )
            Button(
                onClick = onEmailSupport,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp),
            ) {
                Icon(Icons.Filled.Email, contentDescription = null)
                Text("  Email support")
            }
        }
    }
}
