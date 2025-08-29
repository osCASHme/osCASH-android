# Lessons Learned - osCASH.me APK Build Problems

## Problem Historie (Aug 29, 2025)

### Das Problem
Mehrere APK Builds (v7.53.5-03 bis v7.53.5-06) hatten alle dieselben Fehler:
- Payment Button fehlte im Chat Attachment Menu
- 13 Branding-Probleme (Molly statt osCASH.me)
- Änderungen im Code kamen nicht in den APKs an

### Root Cause Analysis

#### 1. Docker Image Caching Problem (HAUPTPROBLEM)
**Problem:** Docker verwendete ein 46 Stunden altes gecachtes Image
- Docker Image `reproducible-molly:latest` war veraltet
- Neue Code-Änderungen wurden NICHT ins Docker Image übernommen
- Builds verwendeten immer den alten Code aus dem gecachten Image

**Lösung:** 
```bash
docker compose build --no-cache assemble
```
Dies zwingt Docker, das Image komplett neu zu bauen mit dem aktuellen Code.

#### 2. Falsches Verzeichnis Problem
**Problem:** Zwei verschiedene molly-core Verzeichnisse existieren:
- `/home/mayer/prog/claude/osCASH.me/molly-core/` (alt, ohne Fixes)
- `/home/mayer/prog/claude/osCASH.me/android/molly-core/` (richtig, mit Fixes)

**Lösung:** 
- Immer aus `/android/molly-core/reproducible-builds/` bauen
- Beide AttachmentKeyboard.java Dateien synchron halten

#### 3. Docker Context Problem
**Problem:** docker-compose.yml verwendet `context: ..`
- Das bedeutet Docker baut aus dem parent directory
- Änderungen müssen im richtigen Verzeichnis sein

### Erkenntnisse

1. **Docker Caching ist gefährlich:** Alte Images enthalten alte Code-Versionen
2. **Verzeichnis-Struktur beachten:** Mehrere Kopien des Codes können zu Verwirrung führen
3. **Systematische Prüfung nötig:** Vor jedem Build checken ob Änderungen im Image sind

### Korrekter Build-Prozess

1. Code-Änderungen machen in `/android/molly-core/`
2. Docker Image neu bauen: `docker compose build --no-cache assemble`
3. APK bauen mit den richtigen Environment Variables
4. APK testen bevor Release erstellt wird

### Zeitverlust
- 6 fehlerhafte APK Builds erstellt
- ~3 Stunden verschwendet durch Docker Caching Problem
- Mehrfache Frustration beim User durch wiederholte Fehler

### Zusätzliche Erkenntnisse (Build v7.53.5-07-dev)

#### 3. Kotlin Compile-Fehler Problem
**Problem:** Unvollständige Payment-Implementation führt zu Kotlin Compile-Fehlern:
1. `PaymentSettingsFragment.kt:64` - String resource 'material_nav_bar_back_button' existiert nicht
2. `ConversationFragment.kt:4281` - 'when' expression nicht exhaustive (PAYMENT branch fehlt)

**Root Cause:** Payment-Features wurden nur teilweise implementiert
- AttachmentKeyboardButton.PAYMENT enum existiert
- DEFAULT_BUTTONS Liste enthält PAYMENT
- Aber ConversationFragment behandelt PAYMENT nicht in switch-case
- PaymentSettingsFragment referenziert nicht-existente String-Ressourcen

**Fixes Applied:**
- `PaymentSettingsFragment.kt`: material_nav_bar_back_button → ConversationFragment__content_description_back_button
- `ConversationFragment.kt`: PAYMENT branch in 'when' expression hinzugefügt mit Placeholder

#### 4. Git Repository Problem
**Problem:** Docker Container findet kein .git Verzeichnis
- build.gradle.kts Funktion `getCommitTag()` schlägt fehl
- Docker Context kopiert Code, aber nicht .git Verzeichnis

**Fix:** Try-catch um git commands in build.gradle.kts

#### 5. Toast String Resource Type Error
**Problem:** `ConversationFragment.kt:4292` - toast() erwartet String Resource ID, nicht String literal
```kotlin
// Falsch:
toast("Payment feature coming soon!", Toast.LENGTH_SHORT)
// Korrekt: 
toast(R.string.AttachmentManager_cant_open_media_selection, Toast.LENGTH_SHORT)
```

**Root Cause:** Android toast() Funktion erwartet immer eine String Resource ID (Int), keine String literals
**Fix:** String literal → R.string.resource_id

### Präventionsmaßnahmen
- CLAUDE.md Checkliste erstellt und erweitert mit Kotlin Compile Checks
- Docker Image Build in den Standard-Prozess aufgenommen
- Klare Dokumentation der Verzeichnis-Struktur
- Vollständige Payment-Implementation erforderlich (keine Partial-Features)
- Git-safe build.gradle.kts mit Fallback-Werten