# Claude Build Checkliste für osCASH.me APK

## ⚠️ KRITISCH: IMMER VOR APK BUILD PRÜFEN

### 1. HAUPT-PROBLEM: Docker Image Caching
**Das größte Problem:** Docker verwendet ein GECACHTES Image ohne die neuesten Code-Änderungen!

**LÖSUNG:** Docker Image MUSS neu gebaut werden wenn Code-Änderungen gemacht wurden:
- Docker baut das Image aus dem parent directory (`context: ..` in docker-compose.yml)
- Das bedeutet es kopiert `/android/molly-core/` ins Image beim Build
- Ein altes gecachtes Image enthält NICHT die neuesten Änderungen!
- **IMMER `docker compose build --no-cache assemble` ausführen nach Code-Änderungen!**

### 2. Verzeichnis-Struktur Problem
Es gibt **zwei verschiedene molly-core Verzeichnisse**:
- `/molly-core/` - das ALTE Verzeichnis (ohne Payment Fixes)  
- `/android/molly-core/` - das RICHTIGE Verzeichnis (mit Payment Fixes)

### 3. Kotlin Compile-Fehler Problem
**Problem:** Payment-Features verursachen Kotlin Compile-Fehler wenn nicht vollständig implementiert:
1. `PaymentSettingsFragment.kt` - Missing string resources
2. `ConversationFragment.kt` - Incomplete 'when' expression (missing PAYMENT branch)

**LÖSUNG:** Alle Payment-Features müssen vollständige Implementierung haben:
- Alle String-Referenzen müssen existieren
- Alle 'when' expressions müssen PAYMENT branch haben
- Payment Actions müssen implementiert sein (auch als TODO/Placeholder)
- toast() Aufrufe müssen String Resource IDs verwenden, NICHT String literals

### 4. Pflicht-Prüfungen vor jedem Build:

#### A) Payment Button Check:
```bash
# BEIDE Dateien müssen PAYMENT enthalten:
grep -A5 "DEFAULT_BUTTONS" /home/mayer/prog/claude/osCASH.me/molly-core/app/src/main/java/org/thoughtcrime/securesms/conversation/AttachmentKeyboard.java

grep -A5 "DEFAULT_BUTTONS" /home/mayer/prog/claude/osCASH.me/android/molly-core/app/src/main/java/org/thoughtcrime/securesms/conversation/AttachmentKeyboard.java
```
Beide müssen diese Zeile haben: `AttachmentKeyboardButton.PAYMENT,`

#### B) Kotlin Compile Check:
```bash
# ConversationFragment.kt muss PAYMENT branch in 'when' haben:
grep -A10 -B5 "when (button)" /home/mayer/prog/claude/osCASH.me/android/molly-core/app/src/main/java/org/thoughtcrime/securesms/conversation/v2/ConversationFragment.kt
```

#### C) Build-Verzeichnis:
```bash
# Build MUSS aus android/molly-core erfolgen:
cd /home/mayer/prog/claude/osCASH.me/android/molly-core
```

### 3. PFLICHT: Docker Image neu bauen (mit --no-cache):
```bash
cd /home/mayer/prog/claude/osCASH.me/android/molly-core/reproducible-builds
docker compose build --no-cache assemble
```

### 4. Korrekter Build Command:
```bash
CI_APP_TITLE="osCASH.me" CI_APP_VERSION="v7.53.5-XX-dev" CI_PACKAGE_ID="me.oscash.app" CI_BUILD_VARIANTS="prodFossWebsite" CI_KEYSTORE_PATH="/molly/app/certs/osCASH-release.keystore" CI_KEYSTORE_PASSWORD="saj9Bqn7QEkwMA01" CI_KEYSTORE_ALIAS="oscash-key" timeout 900 docker compose run --rm assemble
```

### 5. Nach dem Build:
- APK liegt in: `/android/molly-core/reproducible-builds/outputs/apk/prodFossWebsite/release/`
- Kopieren nach: `/home/mayer/prog/claude/osCASH.me/outputs/osCASH.me-vX.X.X-XX-dev-FOSS.apk`

## ⚠️ WICHTIG: Diese Datei VOR jedem Build lesen!