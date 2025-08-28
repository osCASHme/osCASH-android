# osCASH.me Critical Build Fixes & Solutions

**⚠️ CRITICAL KNOWLEDGE - Do NOT lose these fixes!**

Diese Datei dokumentiert alle kritischen Build-Probleme und ihre Lösungen für das osCASH.me Tier 3 Build-System.

## 🏗️ Tier 3 Architecture Setup Issues

### Problem 1: Module Dependency Conflict
**Error**: "No matching variant of project :molly-core:app was found"
**Ursache**: Versuch, molly-core:app als Library-Dependency zu verwenden
**Lösung**: 
```kotlin
// FALSCH in app/build.gradle.kts:
implementation(project(":molly-core:app"))  // app ist ein Application-Modul!

// RICHTIG: Verwende Molly-App direkt mit Environment-Variablen:
// Baue direkt :molly-core:app mit CI-Environment-Variablen
```

### Problem 2: Build Variant Filtering 
**Error**: "Cannot locate tasks that match ':molly-core:app:assembleProdFossWebsiteDebug'"
**Ursache**: Molly filtert Build-Varianten basierend auf `CI_BUILD_VARIANTS` Regex
**Lösung**:
```bash
# Korrekte Environment-Variable:
CI_BUILD_VARIANTS="prodFossWebsite"  # Muss als Regex mit selectableVariants matchen

# Molly Code (app/build.gradle.kts:388-391):
val selected = variant.name in selectableVariants
if (!(selected && buildVariants.toRegex().containsMatchIn(variant.name))) {
  variant.enable = false
}
```

### Problem 3: Deprecated targetSdk Configuration
**Warning**: "'targetSdk: Int?' is deprecated. Use testOptions.targetSdk or/and lint.targetSdk instead"
**Ursache**: Deprecated Gradle-Konfiguration in root build.gradle.kts
**Lösung**:
```kotlin
// FALSCH in build.gradle.kts:
defaultConfig {
    targetSdk = 35  // Deprecated!
}

// RICHTIG:
defaultConfig {
    // targetSdk entfernt für Libraries
}
lint {
    targetSdk = 35  // Korrekte Stelle für lint
}
```

## 🐳 Docker Build System Issues

### Problem 4: Quota Exceeded Errors
**Error**: "Quota exceeded" bei NDK/AAR Downloads
**Ursache**: Ungenügend Festplattenspeicher im Docker-Container  
**Lösung**: Mehr Speicherplatz bereitstellen (min. 20GB für vollständigen Build)

### Problem 5: Docker Volume Pfade
**Error**: APK Output nicht gefunden
**Ursache**: Falsche Volume-Pfade in docker-compose
**Lösung**:
```yaml
# FALSCH:
volumes:
  - ./outputs:/oscash/app/build/outputs

# RICHTIG für Molly-Core Build:
volumes:
  - ./outputs:/oscash/molly-core/app/build/outputs
```

## 🎯 Environment Variables Configuration

### Korrekte osCASH.me Branding-Konfiguration:
```bash
# docker-build/.env
CI_BUILD_VARIANTS=prodFossWebsite          # Aktiviert korrekte Molly-Variante
CI_KEYSTORE_PATH=/oscash/app/certs/oscash-release.keystore
CI_KEYSTORE_PASSWORD=osCASH2025
CI_KEYSTORE_ALIAS=oscash-key
CI_APP_TITLE=osCASH.me                     # App-Name in UI
CI_APP_FILENAME=osCASH.me                  # APK-Dateiname
CI_PACKAGE_ID=me.oscash.app                # Android Package ID
```

### Docker-Compose Command:
```yaml
# docker-compose.oscash.yml
command: :molly-core:app:assembleRelease --no-daemon
# NICHT: :app:assembleOscashBasicRelease (existiert nicht)
```

## 📱 APK Signing & Branding

### Problem 6: APK-Signing Schema
**Lösung**: Verwende identische Konfiguration wie Molly-Repro:
```kotlin
// Molly signing configuration (bereits in molly-core implementiert):
enableV1Signing = false  // Original Molly uses v2+v3 only
enableV2Signing = true   // APK signing scheme v2 (required)
enableV3Signing = true   // APK signing scheme v3 (required) 
enableV4Signing = false  // Disabled like original Molly
```

### Problem 7: Package ID Konflikte
**Lösung**: Verwende eindeutige osCASH.me Package ID:
- **Molly**: `im.molly.app`
- **osCASH.me**: `me.oscash.app` ✅

## 🔧 Verified Working Build Process

### Schritt 1: Environment Setup
```bash
cd /home/mayer/prog/claude/osCASH.me/android/molly-core/reproducible-builds
```

### Schritt 2: Build mit korrekten Environment-Variablen
```bash
CI_APP_TITLE="osCASH.me" \
CI_APP_FILENAME="osCASH.me" \
CI_PACKAGE_ID="me.oscash.app" \
CI_BUILD_VARIANTS="prodFossWebsite" \
docker compose run --rm assemble
```

### Schritt 3: Resultat
- **APK**: `osCASH.me-untagged-FOSS.apk` (84MB)
- **Location**: `outputs/apk/prodFossWebsite/release/`
- **Package ID**: `me.oscash.app`
- **Vollständig funktionsfähig** ✅

## 🚨 NEVER FORGET These Critical Points:

1. **Molly-App direkt bauen** - nicht als Library verwenden
2. **Environment-Variablen korrekt setzen** - CI_BUILD_VARIANTS muss exact matchen  
3. **Genug Speicherplatz** - min. 20GB für NDK/AAR Downloads
4. **Original Molly Docker-System verwenden** - nicht selbst gebastelte Lösungen

## 📈 Success Metrics

- ✅ **84MB APK** erstellt (vollständige Funktionalität)
- ✅ **Package ID `me.oscash.app`** korrekt gesetzt
- ✅ **osCASH.me Branding** in APK-Namen
- ✅ **GitHub Release** erfolgreich veröffentlicht
- ✅ **Reproducible Build** System funktioniert

---

**Letzte Aktualisierung**: 2025-08-28  
**Erfolgreicher Build**: v1.0.0-alpha3 (2025-08-28 03:03)  
**Status**: Production-Ready ✅