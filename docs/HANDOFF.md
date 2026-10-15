# Codex handoff

## Current state (2026-10-15)

Repository started with only a README heading. Bootstrap adds a single-module Compose starter, Gradle wrapper, model/context-formatting code, and two unit tests. It is deliberately not a completed app.

Build validation is blocked here: Gradle 8.11.1 download failed with `Network is unreachable`. Run build/lint/tests first in the development environment. No APK has been generated or device-tested.

## Next task

Implement the smallest usable vertical slice: manually add an ingredient, persist it, reopen the app, copy its context. Then add local barcode learning, household notes/favorites, and backup. Do not reopen the product design discussion unless implementation exposes an actual conflict.

## Milestones

1. **Build baseline:** sync pinned toolchain, run tests/lint/assemble, launch on Pixel 9. Resolve genuine compatibility issues and record versions.
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
