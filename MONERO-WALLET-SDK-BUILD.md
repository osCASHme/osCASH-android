# Monero Wallet SDK - Reproducible Build Setup

**Moderne Kotlin-Bibliothek für Monero Wallet2 Integration in osCASH.me**

## 🎯 Was ist Monero Wallet SDK?

Eine Kotlin-Bibliothek für die Integration von Monero-Wallets in Android-Apps. Sie "embeds Monero's wallet2 inside a sandboxed Android Service" mit einer asynchronen API für maximale Privacy.

## 🏗️ Technische Details

### Sprachen & Architektur
- **Kotlin** (59.9%) - Haupt-API und Android Integration
- **C++** (30.7%) - Native Monero wallet2 Implementation  
- **CMake** (6.5%) - Native Code Build-System
- **AIDL** (2.2%) - Android Interface Definition
- **C** (0.7%) - Low-level Komponenten

### Build-System & Ausgabe
- **Gradle** (Kotlin DSL) für Android-Library Build
- **Maven Central** Deployment
- **Ausgabe**: Android AAR (Android Archive) Library (~6.5 MB)
- **Demo App**: Jetpack Compose Wallet-Anwendung

### Mindestanforderungen
- **Android 8.0+** (API Level 26)
- **Kotlin 2.1.0**
- **Android Gradle Plugin 8.1.0+**

## 🔧 Reproducible Build Setup

### Voraussetzungen für Docker Build
```bash
# Android SDK + NDK in Docker-Container
FROM openjdk:11-jdk-slim

# Android SDK Installation
ENV ANDROID_HOME=/opt/android-sdk
ENV PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools
```

### 1. Repository Setup mit Submodules
```bash
# Fork klonen mit allen Submodules
git clone --recursive https://github.com/osCASHme/monero-wallet-sdk.git
cd monero-wallet-sdk

# Original Version ermitteln
ORIGINAL_VERSION=$(git describe --tags --abbrev=0)
echo "Building Monero Wallet SDK version: $ORIGINAL_VERSION"

# Submodule Status prüfen
git submodule status
```

### 2. Docker Build Environment
```bash
# Dockerfile für reproducible AAR builds
cat > Dockerfile.reproducible << 'EOF'
FROM openjdk:11-jdk-slim

# System Dependencies
RUN apt-get update && apt-get install -y \
    wget unzip git build-essential cmake ninja-build \
    && rm -rf /var/lib/apt/lists/*

# Android SDK Setup
ENV ANDROID_HOME=/opt/android-sdk
ENV PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools:$ANDROID_HOME/cmdline-tools/latest/bin

# Install Android SDK
RUN mkdir -p $ANDROID_HOME && \
    wget -q https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip -O cmdline-tools.zip && \
    unzip cmdline-tools.zip -d $ANDROID_HOME && \
    mv $ANDROID_HOME/cmdline-tools $ANDROID_HOME/cmdline-tools-temp && \
    mkdir -p $ANDROID_HOME/cmdline-tools/latest && \
    mv $ANDROID_HOME/cmdline-tools-temp/* $ANDROID_HOME/cmdline-tools/latest/ && \
    rm -rf $ANDROID_HOME/cmdline-tools-temp cmdline-tools.zip

# Accept licenses and install required components
RUN yes | sdkmanager --licenses && \
    sdkmanager "platforms;android-34" "build-tools;34.0.0" "ndk;25.2.9519653"

# Set NDK path
ENV ANDROID_NDK_ROOT=$ANDROID_HOME/ndk/25.2.9519653

# Reproducible Build Environment
ENV SOURCE_DATE_EPOCH=1640995200
ENV GRADLE_OPTS="-Dorg.gradle.daemon=false"

WORKDIR /build
COPY . .

# Build AAR
RUN ./gradlew clean assembleRelease
EOF

# Build mit Docker
docker build -f Dockerfile.reproducible -t monero-wallet-sdk-build .
```

### 3. AAR extrahieren und verifizieren
```bash
# AAR aus Container extrahieren
docker run --rm -v $(pwd)/outputs:/output monero-wallet-sdk-build \
    bash -c "find . -name '*.aar' -exec cp {} /output/ \;"

# Maven Central AAR herunterladen (falls verfügbar)
# wget https://repo1.maven.org/maven2/im/molly/monero-wallet-sdk/[VERSION]/monero-wallet-sdk-[VERSION].aar

# Vergleich der AAR-Dateien
ls -la outputs/*.aar
```

### 4. AAR Inhalt-Analyse
```bash
# AAR ist ein ZIP-Archiv - Inhalt prüfen
cd outputs
unzip -l *.aar

# Classes und Native Libraries prüfen
unzip -q *.aar
ls -la
```

## 🧪 Demo App Test

### 1. Demo App kompilieren
```bash
# Demo App ebenfalls bauen
docker run --rm -v $(pwd)/demo-outputs:/output monero-wallet-sdk-build \
    bash -c "./gradlew :app:assembleDebug && cp app/build/outputs/apk/debug/*.apk /output/"
```

### 2. Funktionstest vorbereiten
```bash
# Demo APK installieren
adb install demo-outputs/app-debug.apk

# Monero Testnet Node für Tests
# (Benötigt für Wallet-Funktionalität)
```

## 🔐 Privacy & Security Features

### Sandboxed Architecture
- **Native Code Isolation**: wallet2 läuft in separatem Android Service
- **Asynchrone API**: Keine blocking UI-Operations
- **Pluggable Storage**: Flexibles `StorageProvider` Interface

### Monero Privacy Features
- **Ring Signatures**: Transaktions-Privacy durch default
- **Stealth Addresses**: Empfänger-Anonymität  
- **RingCT**: Betrags-Verschleierung
- **Dandelion++**: Netzwerk-Privacy

## 🎯 osCASH.me Integration Strategy

### Multi-Currency Wallet Vision
```
osCASH.me Wallet Architecture:

┌─────────────────────┐    ┌────────────────────┐
│   MobileCoin SDK    │    │ Monero Wallet SDK  │
│                     │    │                    │
│ • MOB Payments      │    │ • XMR Privacy      │
│ • eUSD Stablecoin   │    │ • Ring Signatures  │
│ • Fast Finality     │    │ • Stealth Addresses│
└─────────────────────┘    └────────────────────┘
          │                           │
          └─────────────┬─────────────┘
                        ▼
                ┌──────────────┐
                │ osCASH.me    │
                │ Unified      │
                │ Wallet API   │
                └──────────────┘
```

### Integration Steps
1. **AAR als Gradle Dependency** einbinden
2. **Service-basierte Wallet-Instanz** für Monero
3. **Unified Payment Interface** für MOB + XMR
4. **Privacy-optimierte UX** für verschiedene Use Cases

### Privacy Use Cases
- **MOB für Speed**: Schnelle Micropayments und tägliche Nutzung  
- **XMR für Privacy**: Maximale Anonymität für sensitive Transaktionen
- **Cross-Chain Privacy**: Optional Privacy-Bridge zwischen beiden

## 📦 Deployment Integration

### 1. AAR als lokale Dependency
```kotlin
// build.gradle.kts in osCASH.me
dependencies {
    implementation(files("libs/monero-wallet-sdk-${version}.aar"))
    // oder via Maven Central
    // implementation("im.molly:monero-wallet-sdk:${version}")
}
```

### 2. Wallet Service Integration
```kotlin
// MoneroWalletService Integration Beispiel
class OsCashWalletService : Service() {
    private lateinit var moneroWallet: MoneroWallet
    private lateinit var mobileCoinWallet: MobileCoinWallet
    
    override fun onCreate() {
        // Multi-Currency Wallet Setup
        moneroWallet = MoneroWallet.create(this)
        mobileCoinWallet = MobileCoinWallet.create(this)
    }
}
```

## 🎯 Nächste Schritte für osCASH.me

1. **Reproducible AAR Build** erstellen und verifizieren
2. **Demo App testen** für Funktionalität
3. **osCASH.me Integration** in molly-core
4. **Multi-Currency Interface** Design
5. **Privacy-optimierte UX** Entwicklung

---

**Monero Wallet SDK für osCASH.me - Maximum Privacy für Kryptowährungszahlungen**

*"Privacy First. Swiss Clockwork Reliability."*