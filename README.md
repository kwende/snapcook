# SnapCook

A tiny, local-first Android companion for getting ingredients into a ChatGPT conversation.

Scan familiar products, name unfamiliar ones once, add loose food as text, and copy a meal context. ChatGPT remains the brains. SnapCook is the capture layer.

## Status

**Bootstrap / Codex handoff, not a finished app.** The repository contains a Kotlin/Compose launch screen, plain Kotlin data models and context formatter, formatter tests, and a Gradle wrapper. Barcode scanning, Room persistence, editing screens, clipboard/share actions, and JSON backup are the next implementation work. No household preferences or recipe ratings have been assumed.

Start with [AGENTS.md](AGENTS.md), then [docs/HANDOFF.md](docs/HANDOFF.md).

## Product contract

- Android only; current supported test target is Pixel 9. Use normal responsive Android layouts.
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

## Build validation (2026-10-15)

Windows build verified with Android Studio's bundled JDK 21.0.8, Gradle 8.11.1, Android SDK Platform 35 revision 2, and Build Tools 35.0.0. Java/Kotlin bytecode targets remain 17. The earlier bootstrap network failure did not recur.

`gradlew.bat :app:testDebugUnitTest :app:lintDebug :app:assembleDebug` succeeded: 2 tests passed, lint reported 0 errors and 4 warnings, and `app/build/outputs/apk/debug/app-debug.apk` was generated. Warnings concern two newer dependency versions, Android 12+ backup/transfer rules, and the missing app icon. Wireless install and starter launch were verified on Pixel 9 / Android 17 (API 37) on 2026-10-16; feature acceptance checks remain.

If Java is not on PATH, set `JAVA_HOME` to Android Studio's bundled `jbr` directory before running Gradle. Use a writable SDK containing Platform 35 and Build Tools 35.0.0, selected through `ANDROID_HOME` or ignored `local.properties` (`sdk.dir`). The shared SDK under Program Files was read-only, so the missing components were installed in a user-local SDK. No WSL2 or administrator installation was needed. Keep machine-specific paths out of tracked files.

## Backup design

Use a logical JSON export rather than copying an open SQLite database. Include schema version, product names/barcodes, current meal, household notes, favorites, and feedback. Pick the destination with Android's document picker; users may choose local storage or an installed cloud file provider. Restore validates the entire file before changing anything, previews replacement counts, asks for confirmation, then applies atomically. Unsupported versions or malformed files must leave current data intact. See [docs/HANDOFF.md](docs/HANDOFF.md).

Automatic Android backup is disabled in the starter; explicit export/import is the planned recovery path. Until implemented, the starter stores no user data.

## Public source and local development data

Keep source, synthetic test fixtures, and generic setup instructions in Git. Keep workstation settings and real device/user data in the following locations:

| Data | Local location | Git policy |
| --- | --- | --- |
| Android SDK path | `local.properties` | Ignored |
| Optional phone endpoint/ADB device selection | `.local/device.properties` | Ignored; prefer live discovery |
| Logs, screenshots, recordings, device dumps | `.local/diagnostics/` | Ignored; redact excerpts before sharing |
| Personal JSON exports and database copies | `.local/backups/` | Ignored; keep an additional backup outside the checkout |
| Wireless pairing codes | Interactive prompt only | Never save |
| ADB pairing keys | ADB-managed per-user directory outside the checkout | Never copy into the repo |
| Release signing keys/passwords | Private storage outside the checkout | Never commit; key/property patterns also ignored |

The `.local/` directory is optional and is not read by the application or Gradle. It is a workspace convention for private development files. Do not place private files in app assets/resources: packaging them would expose them in the APK regardless of Git ignore rules. Example commands and documentation use placeholders rather than real addresses, serials, or pairing codes. Device model and Android/API version may be recorded as compatibility evidence.

Ignore rules prevent ordinary accidental staging; they do not encrypt local files, protect forced additions, remove tracked content/history, or erase chat/tool transcripts. Review `git diff --cached` before publishing. Treat raw logs and screenshots as potentially personal. The repository currently has no license file; an explicit license choice is still needed for open-source distribution.

## References

- [Compose](https://developer.android.com/compose)
- [Room](https://developer.android.com/training/data-storage/room)
- [Google Code Scanner](https://developers.google.com/ml-kit/vision/barcode-scanning/code-scanner)
- [Storage Access Framework](https://developer.android.com/training/data-storage/shared/documents-files)
- [AGP compatibility](https://developer.android.com/build/releases/about-agp)
- [Compose compiler and BOM](https://developer.android.com/develop/ui/compose/bom)
