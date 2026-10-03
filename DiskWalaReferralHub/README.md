# DiskWala Referral Hub

Kotlin + Jetpack Compose (Material 3) app for sharing a DiskWala referral link.

Open the folder in Android Studio (Ladybug or newer, JDK 17), let Gradle sync, then Run.

Before publishing:
1. Replace `SUPPORT_EMAIL` in `core/config/ReferralConfig.kt`.
2. Have the policy text in `ui/safety/LegalContent.kt` reviewed and confirm DiskWala's current referral/affiliate terms.
3. If DiskWala ever offers an official API, implement it as a new `ReferralStatsRepository`
   (see the comment in `data/UnavailableReferralStatsRepository.kt`).

Layers: `core` (config, utils) · `domain` (models, repository interface, use case) ·
`data` (repository implementations) · `ui` (theme, components, screens).
