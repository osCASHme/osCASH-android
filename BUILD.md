**[🇬🇧 English Version](languages/en/BUILD.md)** | 🇩🇪 Deutsche Version

# osCASH.me Android - Build Guide

Anleitung zum lokalen Erstellen der osCASH.me Privacy Messenger APK.

## 🚀 Schnellstart (GitHub Actions)

**Empfohlen für die meisten Nutzer:**

1. **Tag erstellen** für neuen Release:
```bash
git tag v1.0.0-alpha
git push origin v1.0.0-alpha
```

2. **GitHub Actions** erstellt automatisch:
   - Debug & Release APKs
   - GitHub Release mit Dokumentation
   - Sicherheits-Scans und Tests

3. **APK herunterladen** von GitHub Releases:
   - `https://github.com/osCASHme/osCASH-android/releases/latest`

## 🛠️ Lokale Entwicklungsumgebung

### Voraussetzungen
```bash
# Java Development Kit
Java 17+ (OpenJDK oder Oracle JDK)

# Android SDK
Android Studio Flamingo+ (2023.1+)
Android SDK API 24-34
Android Build Tools 34.0.0
Android NDK 25+

# Build Tools
Gradle 8.11+ (über Gradle Wrapper)
Git 2.30+ (für Submodules)
```

### Setup
```bash
# Repository clonen
git clone https://github.com/osCASHme/osCASH-android.git
cd android

# Submodules initialisieren (Molly Core)
git submodule update --init --recursive

# Android SDK Pfad konfigurieren
echo "sdk.dir=/pfad/zu/android-sdk" >> local.properties

# Rust für MobileCoin SDK (optional)
curl --proto '=https' --tlsv1.2 -sSf https://sh.rustup.rs | sh
rustup target add aarch64-linux-android armv7-linux-androideabi
```

### Build-Kommandos

#### Debug Build (Development)
```bash
# Einfacher Debug Build
./gradlew assembleDebug

# Mit detailliertem Logging  
./gradlew assembleDebug --info --stacktrace

# Ohne Dependency Verification (bei Problemen)
./gradlew assembleDebug -Dorg.gradle.dependency.verification=lenient
```

#### Release Build (Production)
```bash
# Unsigned Release APK
./gradlew assembleRelease

# Mit Signing (keystore erforderlich)
./gradlew assembleRelease \
  -Pandroid.injected.signing.store.file=release.keystore \
  -Pandroid.injected.signing.store.password=STORE_PASSWORD \
  -Pandroid.injected.signing.key.alias=KEY_ALIAS \
  -Pandroid.injected.signing.key.password=KEY_PASSWORD
```

#### Tests ausführen
```bash
# Unit Tests
./gradlew test

# Instrumented Tests (Emulator erforderlich)
./gradlew connectedAndroidTest

# Alle Tests
./gradlew check
```

## 🔧 Build-Konfiguration

### Gradle Properties anpassen
```bash
# gradle.properties bearbeiten
vim gradle.properties

# Wichtige Optionen:
oscash.version=1.0.0-alpha
oscash.enableMobileCoin=true
oscash.enableTor=true
molly.version=7.53.4
```

### Build Variants
```bash
# Debug Builds
./gradlew assembleDebug              # Standard Debug
./gradlew assembleFossDebug          # FOSS Version (ohne Google Services)
./gradlew assemblePlayDebug          # Play Store Version

# Release Builds  
./gradlew assembleRelease            # Standard Release
./gradlew assembleFossRelease        # FOSS Release
./gradlew assemblePlayRelease        # Play Store Release
```

## 🚨 Bekannte Build-Probleme

### Dependency Verification Errors
```bash
# Temporary Fix
./gradlew build --write-verification-metadata sha256,sha512

# Oder Verification deaktivieren
./gradlew build -Dorg.gradle.dependency.verification=lenient
```

### Repository Configuration Conflicts
```bash
# settings.gradle.kts anpassen:
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
}
```

### Molly-Core Submodule Issues
```bash
# Submodule aktualisieren
git submodule update --remote --merge

# Bei Konflikten: Clean Build
./gradlew clean
rm -rf .gradle build
./gradlew assembleDebug
```

### Memory Issues
```bash
# gradle.properties erweitern:
org.gradle.jvmargs=-Xmx6g -XX:MaxMetaspaceSize=512m
```

## 🔒 Release Signing

### Keystore erstellen (Production)
```bash
# Neuen Keystore generieren
keytool -genkey -v -keystore osCASH-release.keystore \
  -alias osCASH-key -keyalg RSA -keysize 2048 -validity 10000

# Keystore Informationen
keytool -list -v -keystore osCASH-release.keystore
```

### Automatisches Signing konfigurieren
```bash
# local.properties (nicht committen!)
KEYSTORE_FILE=osCASH-release.keystore
KEYSTORE_PASSWORD=your_keystore_password
KEY_ALIAS=osCASH-key
KEY_PASSWORD=your_key_password
```

## 📱 APK Installation & Testing

### Lokale Installation
```bash
# APK finden
find . -name "*.apk" -type f

# Installation via ADB
adb install app/build/outputs/apk/debug/app-debug.apk

# Über USB-Debugging am Gerät installieren
```

### APK Analyse
```bash
# APK Größe und Inhalt
aapt dump badging app-debug.apk

# Permissions prüfen
aapt dump permissions app-debug.apk

# DEX Analyse
./gradlew analyzeDexMethods
```

## 🔗 Weiterführende Links

- **Molly Build Guide**: [mollyim.org/docs/build](https://github.com/mollyim/mollyim-android)
- **MobileCoin SDK**: [developers.mobilecoin.com](https://developers.mobilecoin.com)
- **Android Build**: [developer.android.com/build](https://developer.android.com/build)
- **Signal Protocol**: [signal.org/docs](https://signal.org/docs/)

---

**Bei Build-Problemen**: Erstelle ein [GitHub Issue](https://github.com/osCASHme/osCASH-android/issues) mit detaillierten Logs.

**Andere Sprachen:** [English](languages/en/BUILD.md)