# Instructions for coding agents

## Read first

Read README.md, docs/HANDOFF.md, and docs/DECISIONS.md before implementation. Keep this a small Android capture app. The user is a developer who wants understandable code and a practical handoff, not an elaborate architecture.

## Fixed scope

- Kotlin + Compose, Android only, one app module. Primary test device: Pixel 9.
- Local Room/SQLite persistence. No server, account, cloud sync, telemetry, paid API, or embedded model in V1.
- Barcode → locally learned name. Unknown barcode → editable manual name. Loose food → plain text.
- A dictionary of product identities is not a pantry inventory. Never infer stock from historic scans.
- Prefer keyboard dictation over a custom speech subsystem. Prefer system scanner and file picker over custom implementations.
- ChatGPT handoff is copied/shared text. Do not imply access to an existing ChatGPT conversation through an API.
- Human corrections are authoritative. Do not build food taxonomy, nutrition inference, OCR, or image recognition without a concrete need.
- Keep real preferences and family data out of the public repository. Fixtures must be synthetic. The earlier dinner is not a confirmed favorite.

## Implementation rules

Use small cohesive files, simple ViewModels/state, coroutines for IO, and manual dependency construction. Add abstractions only for an actual testing or integration boundary. No multi-module clean-architecture ceremony or generic repository framework.

Barcodes are strings, never numbers. Preserve leading zeros. Document and test UPC-A / equivalent zero-prefixed EAN-13 normalization; do not strip arbitrary zeros. Repeated scanner callbacks must not repeatedly append the same ingredient. Naming cancellation must not save a blank product.

Persist the current meal for restart recovery; clearing it must not delete product knowledge or notes. Quantities are optional free text, never inferred from scan count. Renaming product knowledge should have explicitly defined behavior for existing meal entries.

Use Room migrations; never enable destructive migration for user data. Backup must be versioned, include every user-owned record, validate before mutation, and restore within a transaction. Request confirmation before replacing existing data. Do not expose raw stack traces or database terms in user flows.

Google Code Scanner may need a Play Services module download on first use. Handle cancellation and unavailable scanner with a manual-entry path. Do not promise first-launch offline scanning until verified.

## Verification and reporting

Run `./gradlew :app:testDebugUnitTest :app:lintDebug :app:assembleDebug` (Windows: `gradlew.bat`). Report environmental blockers honestly. Never call unexecuted tests passing.

Focus tests on barcode normalization/deduplication, text export, persistence behavior, and backup round-trip/invalid-input atomicity. Device-check scan, keyboard, rotation, process recreation, clipboard, and document export/import. See HANDOFF acceptance criteria.

Update README status and HANDOFF after each meaningful milestone. Explain what changed, why, what was verified, and what remains. Keep dependency versions pinned and do not commit SDK paths, credentials, release signing keys, build output, or personal backups.
