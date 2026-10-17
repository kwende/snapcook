# Decisions and rationale

## 2026-10-15 — Product scope

The original friction was sending many package photos into ChatGPT and hitting attachment limits. The app only needs to convey enough ingredient information for a useful conversation; humans fill gaps.

OCR and collages were explored, then superseded as the primary capture path by barcodes. Most packaged groceries repeat in this household, so manually naming an unfamiliar barcode once is acceptable. External UPC lookup may be considered later but is not a V1 dependency. UPC Database advertised 100 lookups per day; coverage was not tested and caching terms were not established. Do not add it by default.

Both adults cook, so perpetual stock maintenance would fail. Store product identities persistently and build a fresh on-demand meal list. Persist that list only to survive interruption; old scans are not evidence of present availability.

Keep a small editable household note and favorite recipes/feedback. No actual preference or positive dinner rating has been supplied yet. Do not turn illustrative examples from discussion into asserted family facts.

Native Kotlin/Compose was chosen over MAUI because only Android is required. Use ordinary Android APIs and responsive layouts while testing primarily on Pixel 9. One app module is enough.

All user data stays local. Back up via user-initiated portable JSON. Do not create a hosted database, synchronization service, API key requirement, or second AI assistant. Clipboard/share is the initial bridge to ChatGPT; the user controls the destination conversation.

## Bootstrap boundary

This commit prepares implementation rather than completing the app. Dependency versions are a conservative starting baseline; runtime behavior remains unverified until an Android-capable environment builds and launches it. The first development task is validation, followed by the vertical slices in HANDOFF.md.
