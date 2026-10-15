# SnapCook

A tiny, local-first Android companion for getting ingredients into a ChatGPT conversation.

Scan familiar products, name unfamiliar ones once, add loose food as text, and copy a meal context. ChatGPT remains the brains. SnapCook is the capture layer.

## Status

**Bootstrap / Codex handoff, not a finished app.** The repository contains a Kotlin/Compose launch screen, plain Kotlin data models and context formatter, formatter tests, and a Gradle wrapper. Barcode scanning, Room persistence, editing screens, clipboard/share actions, and JSON backup are the next implementation work. No household preferences or recipe ratings have been assumed.

Start with [AGENTS.md](AGENTS.md), then [docs/HANDOFF.md](docs/HANDOFF.md).

## Product contract

- Android only; primary test device is Ben's Pixel 9. Use normal responsive Android layouts.
- Persistent product dictionary: barcode string → human-readable product name.
- Known barcode: add saved name to this meal. Unknown barcode: ask for a name and remember it.
- No barcode: type a name such as “apples”; keyboard dictation is sufficient.
- One current meal list, recoverable after process death, cleared explicitly when starting a new meal.
- Household notes and favorite recipes with optional feedback are persistent and editable.
- Export notes/favorites first, then today's ingredients, as ordinary text for ChatGPT.
- Backup all user data as versioned JSON; restore through Android's file picker.

No stock counts, automatic pantry tracking, accounts, hosted database, paid UPC service, embedded AI, or synchronization in V1. A photo may still be sent to ChatGPT manually when naming a product; the app does not need to automate that round trip.

## Technology

Kotlin, Jetpack Compose/Material 3, one `app` module. Planned: Room over local SQLite, Google Code Scanner, Android clipboard/share intents and Storage Access Framework. Manual dependency construction is enough; no DI framework is required.

The starter pins a conservative known toolchain, not a claim to use the newest releases: AGP 8.9.2, Gradle 8.11.1, Kotlin/Compose compiler 2.1.20, Compose BOM 2025.04.01, JDK 17, SDK 35, min SDK 28. Add Room/KSP and scanner versions together after checking current compatibility. Upgrade coherently if needed; do not mix unrelated latest versions.

## Open and run

1. Install Android Studio supporting AGP 8.9.2 or newer, Android SDK Platform 35, and JDK 17.
2. Open this repository root and let Gradle sync. Android Studio manages `local.properties`; never commit it.
3. Enable developer options and USB debugging on the Pixel 9, connect it, and select it as the run target.
4. Run the `app` configuration. The current screen explicitly identifies itself as a starter.

From the root on macOS/Linux:

```sh
./gradlew :app:testDebugUnitTest :app:lintDebug :app:assembleDebug
./gradlew :app:installDebug
```

On Windows use `gradlew.bat` for the same tasks. Dependency downloads require network access. The APK, once built, is under `app/build/outputs/apk/debug/`.

## Validation at bootstrap

The wrapper launch was attempted on JDK 17 but distribution download failed with `Network is unreachable`. No Android build, lint run, unit-test run, or device execution is claimed. Source/XML and repository whitespace checks were performed. Codex's first task is to run the commands above in an Android-capable environment.

## Backup design

Use a logical JSON export rather than copying an open SQLite database. Include schema version, product names/barcodes, current meal, household notes, favorites, and feedback. Pick the destination with Android's document picker; users may choose local storage or an installed cloud file provider. Restore validates the entire file before changing anything, previews replacement counts, asks for confirmation, then applies atomically. Unsupported versions or malformed files must leave current data intact. See [docs/HANDOFF.md](docs/HANDOFF.md).

Automatic Android backup is disabled in the starter; explicit export/import is the planned recovery path. Until implemented, the starter stores no user data.

## References

- [Compose](https://developer.android.com/compose)
- [Room](https://developer.android.com/training/data-storage/room)
- [Google Code Scanner](https://developers.google.com/ml-kit/vision/barcode-scanning/code-scanner)
- [Storage Access Framework](https://developer.android.com/training/data-storage/shared/documents-files)
- [AGP compatibility](https://developer.android.com/build/releases/about-agp)
- [Compose compiler and BOM](https://developer.android.com/develop/ui/compose/bom)
