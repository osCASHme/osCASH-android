# Nächster Build: osCASH-v7.53.5-02

**Fix für App Display Name**

## Problem
- Aktueller Build zeigt "osCASH" statt "osCASH.me" im App Launcher
- Verursacht durch `CI_APP_TITLE="osCASH"` Parameter

## Lösung für v7.53.5-02

### FOSS Build Kommando
```bash
cd molly-core/reproducible-builds

CI_APP_TITLE="osCASH.me" \
CI_APP_VERSION="v7.53.5-02" \
CI_PACKAGE_ID="me.oscash.app" \
CI_BUILD_VARIANTS="prodFossWebsite" \
CI_KEYSTORE_PATH="/molly/app/certs/osCASH-release.keystore" \
CI_KEYSTORE_PASSWORD="saj9Bqn7QEkwMA01" \
CI_KEYSTORE_ALIAS="oscash-key" \
docker compose run --rm assemble
```

### Google FCM Build Kommando
```bash
CI_APP_TITLE="osCASH.me" \
CI_APP_VERSION="v7.53.5-02" \
CI_PACKAGE_ID="me.oscash.app" \
CI_BUILD_VARIANTS="prodGmsWebsite" \
CI_KEYSTORE_PATH="/molly/app/certs/osCASH-release.keystore" \
CI_KEYSTORE_PASSWORD="saj9Bqn7QEkwMA01" \
CI_KEYSTORE_ALIAS="oscash-key" \
docker compose run --rm assemble
```

## Erwartetes Resultat
- App Name im Launcher: **osCASH.me** ✅
- APK Dateien: `osCASH-v7.53.5-02-FOSS.apk` und `osCASH-v7.53.5-02.apk`

## Notizen
- Keystore und Passwort bleiben gleich
- Nur CI_APP_TITLE Parameter geändert
- Build-Nummer von 01 → 02 erhöht