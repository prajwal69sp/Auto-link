package com.hackbits.diskwalareferralhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.hackbits.diskwalareferralhub.ui.AppRoot
import com.hackbits.diskwalareferralhub.ui.theme.DiskWalaReferralHubTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { DiskWalaReferralHubTheme { AppRoot() } }
    }
}
