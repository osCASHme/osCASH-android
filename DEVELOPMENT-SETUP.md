# 🛠️ Lokales Development-Setup für osCASH.me

> Schritt-für-Schritt Einrichtung der Android-Entwicklungsumgebung

## 🎯 Ziel
Bis zum **30. August** (GitHub Launch! 🎂) eine funktionsfähige Entwicklungsumgebung

## 📋 System-Voraussetzungen prüfen

### 1. Java Development Kit (JDK)
```bash
# Aktuelle Java Version prüfen
java -version
javac -version

# Falls nicht installiert oder < JDK 17:
sudo apt update
sudo apt install openjdk-17-jdk openjdk-17-jre

# JAVA_HOME setzen
echo 'export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64' >> ~/.bashrc
echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.bashrc
source ~/.bashrc

# Verifikation
echo $JAVA_HOME
java -version  # Sollte OpenJDK 17.x anzeigen
```

### 2. Android SDK über Android Studio
```bash
# Android Studio downloaden (falls nicht vorhanden)
# https://developer.android.com/studio

# Oder über Command Line Tools:
wget https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip
unzip commandlinetools-linux-9477386_latest.zip
mkdir -p ~/Android/Sdk/cmdline-tools/latest
mv cmdline-tools/* ~/Android/Sdk/cmdline-tools/latest/

# ANDROID_HOME setzen
echo 'export ANDROID_HOME=$HOME/Android/Sdk' >> ~/.bashrc
echo 'export PATH=$ANDROID_HOME/cmdline-tools/latest/bin:$PATH' >> ~/.bashrc
echo 'export PATH=$ANDROID_HOME/platform-tools:$PATH' >> ~/.bashrc
source ~/.bashrc
```

### 3. Android NDK (Native Development Kit)
```bash
# Via Android Studio SDK Manager oder:
sdkmanager "ndk;25.1.8937393"  # Version aus Molly build.gradle

# NDK_HOME setzen
echo 'export NDK_HOME=$ANDROID_HOME/ndk/25.1.8937393' >> ~/.bashrc
source ~/.bashrc
```

### 4. Git LFS (Large File Storage)
```bash
# Für große Android-Assets
sudo apt install git-lfs
git lfs install --global

# Verifikation
git lfs version
```

## 🚀 Molly Repository Clone

### 1. Arbeitsverzeichnis vorbereiten
```bash
cd ~/data/share/Nextcloud/prog/claude/osCASH.me

# Molly als Referenz clonen (read-only)
git clone https://github.com/mollyim/mollyim-android.git molly-reference
cd molly-reference

# Submodules initialisieren
git submodule update --init --recursive

# Repository-Größe prüfen
du -sh .  # ~500MB nach Clone
```

### 2. Build-Dependencies analysieren
```bash
# Gradle Wrapper Version
cat gradle/wrapper/gradle-wrapper.properties
# distributionUrl=https://services.gradle.org/distributions/gradle-8.7-bin.zip

# Android Gradle Plugin Version
head -20 build.gradle.kts
# plugins { id 'com.android.application' version '8.5.1' }

# Compile SDK Version
grep compileSdk app/build.gradle.kts
# compileSdk 34
```

### 3. Erste Build-Analyse (ohne Ausführung)
```bash
# Build-Konfiguration anzeigen
./gradlew projects

# Verfügbare Build-Varianten
./gradlew app:tasks --group="build"

# Dependencies auflisten
./gradlew app:dependencies > dependencies.txt
```

## 🔧 Development-Tools installieren

### 1. Android Studio Extensions
```bash
# Für bessere Kotlin/Android Entwicklung
# - Kotlin Plugin (meist vorinstalliert)
# - Android APK Analyzer
# - Database Inspector
```

### 2. Command Line Tools
```bash
# APK Analyzer für Build-Analyse
sudo apt install aapt

# APK Signing Tools
sudo apt install apksigner

# Database-Tools für MobileCoin
sudo apt install sqlite3

# JSON-Tools für API-Entwicklung
sudo apt install jq
```

### 3. Debugging-Tools
```bash
# ADB (Android Debug Bridge) testen
adb version

# Logcat für Android-Logs
# (Teil von Android SDK)

# Network-Monitoring
sudo apt install wireshark-qt  # Für API-Debugging
```

## 📱 Test-Environment Setup

### 1. Android Emulator
```bash
# System Images downloaden
sdkmanager "system-images;android-34;google_apis;x86_64"
sdkmanager "system-images;android-33;google_apis;x86_64"

# AVD (Android Virtual Device) erstellen
avdmanager create avd \
  -n "osCASH_Test" \
  -k "system-images;android-34;google_apis;x86_64" \
  -d "pixel_4"

# Emulator starten (Test)
emulator @osCASH_Test
```

### 2. GrapheneOS Test-Setup (optional)
```bash
# Für echte GrapheneOS-Tests
# - GrapheneOS auf separatem Gerät
# - ADB über USB aktivieren
# - Developer Options eingeschaltet
```

## 🏗️ Build-System vorbereiten

### 1. Gradle-Konfiguration optimieren
```bash
# gradle.properties für Performance
cat >> ~/.gradle/gradle.properties << 'EOF'
# Performance Optimierungen
org.gradle.daemon=true
org.gradle.parallel=true
org.gradle.configureondemand=true
org.gradle.jvmargs=-Xmx4g -XX:+HeapDumpOnOutOfMemoryError

# Android Build-Optimierungen  
android.useAndroidX=true
android.enableJetifier=true
android.enableR8.fullMode=true
EOF
```

### 2. Signing-Konfiguration (Development)
```bash
# Debug-Keystore erstellen (für lokale Builds)
keytool -genkeypair -v \
  -keystore ~/.android/debug.keystore \
  -storepass android \
  -alias androiddebugkey \
  -keypass android \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000 \
  -dname "CN=Android Debug,O=Android,C=US"
```

### 3. Build-Wrapper Scripts
```bash
# Schnelle Build-Commands
cat > build-debug.sh << 'EOF'
#!/bin/bash
echo "🏗️ Building osCASH.me Debug..."
./gradlew assembleDebug
echo "✅ Debug APK: app/build/outputs/apk/debug/"
EOF

cat > build-release.sh << 'EOF' 
#!/bin/bash
echo "🚀 Building osCASH.me Release..."
./gradlew assembleRelease
echo "✅ Release APK: app/build/outputs/apk/release/"
EOF

chmod +x build-*.sh
```

## 📊 Development-Dashboard

### 1. Build-Status Script
```bash
cat > dev-status.sh << 'EOF'
#!/bin/bash
echo "📱 osCASH.me Development Status"
echo "================================"
echo "📅 Target: GitHub Launch 30. August 2024 🎂"
echo ""
echo "🔧 Environment:"
echo "Java: $(java -version 2>&1 | head -1)"
echo "Android SDK: $ANDROID_HOME"
echo "NDK: $NDK_HOME"
echo ""
echo "📊 Repository:"
echo "Size: $(du -sh . | cut -f1)"
echo "Commits: $(git rev-list --count HEAD 2>/dev/null || echo 'N/A')"
echo ""
echo "🏗️ Last Build:"
if [ -f "app/build/outputs/apk/debug/app-debug.apk" ]; then
    echo "Debug APK: ✅ $(stat -c%y app/build/outputs/apk/debug/app-debug.apk)"
else
    echo "Debug APK: ❌ Not built yet"
fi
EOF

chmod +x dev-status.sh
```

### 2. IDE-Konfiguration
```bash
# IntelliJ/Android Studio Settings
mkdir -p .idea
cat > .idea/workspace.xml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<project version="4">
  <component name="PropertiesComponent">
    <property name="android.sdk.path" value="$USER_HOME$/Android/Sdk" />
    <property name="android.project.structure.last.edited" value="Modules" />
  </component>
</project>
EOF
```

## 🧪 Erste Build-Tests

### 1. Gradle-Test (ohne APK)
```bash
# Nur Configuration Phase
./gradlew help

# Dependencies laden
./gradlew app:dependencies > /dev/null
echo "✅ Dependencies resolved"
```

### 2. Lint-Checks
```bash
# Code-Quality ohne Build
./gradlew app:lintDebug
echo "✅ Lint checks completed"
```

### 3. Unit-Tests vorbereiten
```bash
# Test-Runner ohne Execution
./gradlew app:testDebugUnitTest --dry-run
echo "✅ Test environment ready"
```

## 📦 Projekt-Template erstellen

### 1. osCASH.me Branding vorbereiten
```bash
mkdir -p branding/{logos,colors,strings}
cat > branding/app-config.json << 'EOF'
{
  "appName": "osCASH.me",
  "packageName": "me.oscash.messenger",
  "versionName": "1.0.0-alpha",
  "minSdkVersion": 26,
  "targetSdkVersion": 34,
  "branding": {
    "primaryColor": "#FF6B35",
    "accentColor": "#004E89"
  }
}
EOF
```

### 2. Add-On Struktur vorbereiten
```bash
mkdir -p addons/{mobilecoin,qr-gateway,mesh-network}
mkdir -p addons/mobilecoin/{src,res,docs}
mkdir -p addons/qr-gateway/{src,res,docs}

# Add-On Template
cat > addons/README.md << 'EOF'
# osCASH.me Add-Ons

## Architektur
- 📱 **mobilecoin/**: MobileCoin Wallet Integration
- 📋 **qr-gateway/**: QR-Code Payment Gateway  
- 🌐 **mesh-network/**: Offline Mesh-Network

## Entwicklung
Jedes Add-On ist ein separates Gradle-Modul für:
- Einfache Updates von Molly Upstream
- Modulare Entwicklung
- Saubere Trennung der Features
EOF
```

## ✅ Setup-Verifikation

### Checklist vor dem ersten Build
```bash
echo "🔍 Development Setup Verifikation"
echo "=================================="

# Java
java -version && echo "✅ Java OK" || echo "❌ Java fehlt"

# Android SDK
[ -d "$ANDROID_HOME" ] && echo "✅ Android SDK OK" || echo "❌ Android SDK fehlt"

# Git LFS
git lfs version && echo "✅ Git LFS OK" || echo "❌ Git LFS fehlt"

# Gradle
[ -f "./gradlew" ] && echo "✅ Gradle Wrapper OK" || echo "❌ Gradle fehlt"

# Repository
git status && echo "✅ Git Repository OK" || echo "❌ Git nicht initialisiert"

echo ""
echo "🎯 Ready for GitHub Launch: 30. August 2024! 🎂"
```

---

## 🚀 Nächste Schritte

1. ✅ **Development-Setup** (aktuell)
2. ⏳ Molly-Fork lokal einrichten
3. ⏳ Add-On Architektur implementieren  
4. ⏳ Erste MobileCoin-Integration
5. ⏳ **30. August**: GitHub Launch! 🎉

**Tipp**: Führe `./dev-status.sh` regelmäßig aus für den Entwicklungsstand!

*Namasté* 🙏