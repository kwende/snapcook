# Codex handoff

## Current state (2026-10-16)

Repository started with only a README heading. Bootstrap adds a single-module Compose starter, Gradle wrapper, model/context-formatting code, and two unit tests. It is deliberately not a completed app.

Build baseline verified on Windows: `gradlew.bat :app:testDebugUnitTest :app:lintDebug :app:assembleDebug` succeeded using Android Studio's bundled JDK 21.0.8 (bytecode target 17), Gradle 8.11.1, SDK Platform 35 revision 2, and Build Tools 35.0.0. Both unit tests passed; lint reported 0 errors and 4 warnings; debug APK generated at `app/build/outputs/apk/debug/app-debug.apk`. The earlier network failure did not recur. The shared SDK was read-only and lacked API 35, so required components were installed in a user-local SDK with existing accepted licenses. Machine-specific SDK selection is kept in ignored `local.properties`; set `JAVA_HOME` to Android Studio's `jbr` for shell builds. WSL2 was not needed.

Remaining lint warnings: newer Compose BOM and Activity versions are available (pins retained); explicit Android 12+ backup/transfer rules are absent; application icon is absent. Packaging also emitted a nonfatal notice about retaining symbols in `libandroidx.graphics.path.so`. Before storing user data, settle explicit backup/transfer rules consistent with the local-data contract. Wireless ADB pairing, APK installation, and starter launch were verified on Pixel 9 / Android 17 (API 37) on 2026-10-16. Android reported MainActivity as the foreground activity; the process remained alive, no fatal exception appeared in the inspected app logs, and app-private debug access through run-as succeeded. Visual confirmation by the user and feature acceptance checks remain.

## Next task

Implement the smallest usable vertical slice: manually add an ingredient, persist it, reopen the app, copy its context. Then add local barcode learning, household notes/favorites, and backup. Do not reopen the product design discussion unless implementation exposes an actual conflict.

## Milestones

1. **Build baseline:** tests/lint/assemble verified on Windows; wireless install and starter launch verified on Pixel 9. Resolve genuine compatibility issues and record versions.
2. **Meal and persistence:** add Room/KSP with compatible pinned versions; ingredients screen, add/edit/remove, explicit new-meal action, restart recovery, copy/share formatted text. Keep UI responsive during database work.
3. **Barcode dictionary:** Google Code Scanner; known → add; unknown → prompt editable name → save/add; cancellation → no mutation. Make names editable later. Debounce repeat scan events; show “already added” instead of silently treating duplicates as quantities. Manual barcode entry is optional fallback; ordinary text entry is required.
4. **Household context:** editable notes; favorites with title, ingredients/instructions and feedback; include selected favorites (or all for a tiny list) before ingredients. No synthetic preferences seeded into production.
5. **Backup:** document-picker JSON export/import, validated versioned format, explicit replacement confirmation, transaction, round-trip and corrupt-file tests. Include product knowledge, current meal, notes, recipes, feedback. Restore on fresh install.

## Suggested persistence shape

- Product: canonical barcode string primary key, editable name.
- Meal item: stable ID, name snapshot, optional barcode reference, optional free-text note, display order.
- Household notes: one text record.
- Favorite: stable ID, title, recipe text, feedback text.

Name snapshots keep a meal stable when the dictionary is later edited. A subsequent scan uses the new name. Favorite recipe text may contain ingredients and instructions without a separate ingredient taxonomy. Avoid a historical meal database until requested.

Canonicalize valid UPC-A to its zero-prefixed EAN-13 equivalent so both scans resolve identically. Preserve supported other barcode strings. Test before implementing broad normalization. Scanner format filtering should support UPC-A, UPC-E, EAN-13 and EAN-8, with explicit handling of UPC-E if returned rather than blindly padding it.

## Backup contract

Top-level `schemaVersion: 1`, `products`, `mealItems`, `householdNotes`, `favorites`; optional `exportedAt`. Establish exact DTOs during implementation. Export logical records, not database internals. Validate required fields, supported version, duplicates, references, and reasonable input size before opening the write transaction. V1 restore replaces the whole dataset after confirmation; merge/sync is out of scope. Export all favorites, even if only selected favorites are in the meal prompt.

Use ACTION_CREATE_DOCUMENT / ACTION_OPEN_DOCUMENT through Activity Result contracts. Stream through ContentResolver; do not assume a filesystem path from a content URI. Cancellation is normal. Display useful success/failure feedback. No broad storage permission is needed for user-selected documents.

## Pixel acceptance checklist

- Fresh install starts empty, with a visible Add item action.
- Add “apples”; restart process; apples remains.
- Unknown packaged item → name it → add to meal; next scan recalls that name locally.
- Cancel naming or scanner: no accidental blank entries.
- Repeat scan: no unintended duplicate or invented quantity.
- Scanner unavailable/first-use module absent: manual entry remains usable.
- Edit/remove a meal item; new meal clears only the current list.
- Add household note and favorite feedback; copied text preserves them before ingredients.
- Copy text into an actual ChatGPT conversation. Check Share behavior on the installed apps; clipboard remains fallback.
- Export, reinstall/clear app data, restore: all records match.
- Invalid/newer-version backup: clear error, existing records unchanged.
- Confirm restore replacement before changing data.
- Test keyboard visibility, large font, rotation, and system bars on Pixel 9.

## Suggested Codex starting prompt

Read AGENTS.md and docs/HANDOFF.md. Verify the starter build, then implement milestones 2–5 incrementally while preserving the narrow scope. Use Kotlin, Compose, Room, Google Code Scanner, and JSON backup. Keep ChatGPT handoff as ordinary text. Update status and report actual verification; do not add a backend or pantry stock tracking.

## Wireless device workflow (2026-10-16)

Pixel 9 is the sole current deployment and debug target. Use the physical phone over wireless ADB; no emulator or system image is required. The verified host uses user-local SDK platform-tools 37.0.1 for both the ADB client and server.

Enable Wireless debugging on the phone on the same LAN as the laptop. Pair with `adb pair <ip>:<pairing-port>` and enter the current code from the Pair device with pairing code dialog. The dialog's pairing port differs from the main Wireless debugging screen's connection port. Using the connection port with `adb pair` produced a protocol fault; using the actual pairing port succeeded. Do not record transient pairing codes in the repository.

After pairing, mDNS discovered and connected the device automatically. If discovery fails, use `adb connect <ip>:<connection-port>` from the main screen. Confirm with `adb devices -l`, select the device explicitly with `-s`, install with `adb -s <device> install -r app/build/outputs/apk/debug/app-debug.apk`, and launch with `adb -s <device> shell am start -W -n com.kwende.snapcook/.MainActivity`. Inspect app-scoped logcat and activity/process state. Host pairing credentials remain outside the repository. Phone addresses/ports may change; discover them rather than hard-coding them. Breakpoint attachment and reconnect after wireless toggling have not yet been tested.

## Public repository hygiene (2026-10-16)

Tracked files and available local Git history were checked for the actual device endpoint, serial/GUID, pairing codes, private-key markers, and workstation SDK path; none were found. Owner-name references were removed from current prose. Historical prose/commit author metadata was not rewritten. The project is licensed under MIT; see the root LICENSE file.

Private development files belong in ignored `.local/`; SDK selection remains in ignored `local.properties`. See the README data-location table and AGENTS privacy rules. Pairing codes must never be saved, and ADB manages persistent pairing credentials outside the checkout. Public device evidence records only model, Android/API version, and test outcome. Gitignore cannot remove the pairing codes/endpoints already shared in the chat/tool transcript.
