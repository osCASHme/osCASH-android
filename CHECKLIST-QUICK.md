# osCASH.me APK Build - Quick Checklist

## ⚠️ IMMER VOR APK BUILD AUSFÜHREN

### 1. Docker Image Check
```bash
docker images | grep reproducible-molly
# Wenn älter als letzte Code-Änderung → neu bauen!
```

### 2. Payment Button Check  
```bash
grep "AttachmentKeyboardButton.PAYMENT" /home/mayer/prog/claude/osCASH.me/android/molly-core/app/src/main/java/org/thoughtcrime/securesms/conversation/AttachmentKeyboard.java
```
**Muss gefunden werden!**

### 3. Kotlin Compile Check
```bash
grep -A5 "AttachmentKeyboardButton.PAYMENT" /home/mayer/prog/claude/osCASH.me/android/molly-core/app/src/main/java/org/thoughtcrime/securesms/conversation/v2/ConversationFragment.kt
```
**Muss PAYMENT branch in 'when' haben!**
**toast() muss R.string.resource_id verwenden, NICHT String literals!**

### 4. Richtiges Verzeichnis
```bash
cd /home/mayer/prog/claude/osCASH.me/android/molly-core/reproducible-builds
```

### 5. Docker Image neu bauen (nach Code-Änderungen)
```bash
docker compose build --no-cache assemble
```

### 6. APK bauen
```bash
CI_APP_TITLE="osCASH.me" CI_APP_VERSION="v7.53.5-XX-dev" CI_PACKAGE_ID="me.oscash.app" CI_BUILD_VARIANTS="prodFossWebsite" CI_KEYSTORE_PATH="/molly/app/certs/osCASH-release.keystore" CI_KEYSTORE_PASSWORD="saj9Bqn7QEkwMA01" CI_KEYSTORE_ALIAS="oscash-key" timeout 900 docker compose run --rm assemble
```

## ✅ Nach erfolgreichem Build
- APK von `/android/molly-core/reproducible-builds/outputs/apk/prodFossWebsite/release/`
- Kopieren nach `/outputs/osCASH.me-vX.X.X-XX-dev-FOSS.apk`
- GitHub Release erstellen

**Namasté** 🙏