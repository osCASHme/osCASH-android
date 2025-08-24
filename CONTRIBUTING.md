**[🇬🇧 English Version](languages/en/CONTRIBUTING.md)** | 🇩🇪 Deutsche Version

# Mitarbeit bei osCASH.me Android

Willkommen! Schön, dass du bei unserem Privacy-Messenger mit MobileCoin-Integration mitmachen möchtest.

## 🎯 Projekt-Vision

osCASH.me kombiniert **Privacy-first Messaging** mit **nahtloser Cryptocurrency-Integration**. Wir bauen auf bewährten Technologien auf und erweitern sie um innovative Features.

## 🛠️ Entwicklungsumgebung

### Voraussetzungen
```bash
# Android Development
- Android Studio Flamingo+ (2023.1+)
- Android SDK API 24+ (Android 7.0+)
- Android NDK 25+
- Java 11+

# MobileCoin Integration  
- Rust 1.70+
- Protocol Buffers 3.21+
- SGX SDK 2.19+ (für Enclave-Features)

# Optional für Tor-Integration
- Tor 0.4.7+
- Obfs4proxy
```

### Setup
```bash
# Repository clonen
git clone https://github.com/osCASHme/android.git
cd android

# Submodules initialisieren (Molly Core)
git submodule update --init --recursive

# Android SDK konfigurieren
echo "sdk.dir=/path/to/android-sdk" >> local.properties

# Rust für MobileCoin SDK
curl --proto '=https' --tlsv1.2 -sSf https://sh.rustup.rs | sh
rustup target add aarch64-linux-android

# Build testen
./gradlew assembleDebug
```

## 📁 Code-Struktur verstehen

### Haupt-Module
```
android/
├── molly-core/           # Basis Privacy Messenger (Submodule)
│   ├── app/             # Android App Hauptlogik
│   ├── libsignal-service/ # Signal Protocol Integration
│   └── core-util/       # Shared Utilities
├── oscash-core/         # osCASH.me spezifische Features
│   ├── payments-base/   # MobileCoin Wallet Core
│   ├── oscash-config/   # App-Konfiguration & Branding
│   └── addon-manager/   # Plugin-System für Erweiterungen
└── addons/              # Modulare Features
    ├── mesh-network/    # P2P Mesh Networking
    ├── qr-gateway/      # QR Code Integration
    └── multichain-bridge/ # Multi-Blockchain Support
```

### Wichtige Dateien
- `settings.gradle.kts` - Gradle Module Configuration
- `build.gradle.kts` - Main Build Configuration
- `gradle.properties` - Build Properties
- `molly-core/` - Upstream Molly Messenger (als Git Submodule)

## 🔧 Entwicklungs-Workflow

### 1. Feature Branch erstellen
```bash
git checkout -b feature/neue-wallet-funktion
```

### 2. Code-Standards befolgen
- **Kotlin** für Android App Logic
- **Rust** für MobileCoin Integration
- **Java** für Signal Protocol Komponenten
- **ktlint** für Kotlin Code Formatting
- **detekt** für Static Code Analysis

### 3. Testing
```bash
# Unit Tests
./gradlew testDebugUnitTest

# Instrumented Tests  
./gradlew connectedDebugAndroidTest

# MobileCoin Integration Tests
cd oscash-core/payments-base
cargo test
```

### 4. Pull Request erstellen
- **Beschreibung**: Was ändert sich und warum?
- **Testing**: Wie hast du die Änderungen getestet?
- **Screenshots**: Bei UI-Änderungen Screenshots anhängen
- **Privacy Impact**: Auswirkungen auf Privacy dokumentieren

## 🔐 Privacy & Security Guidelines

### Code Reviews
- **Alle** Crypto-relevanten Änderungen brauchen Review von mindestens 2 Core Contributors
- **MobileCoin Integration** muss besonders sorgfältig geprüft werden
- **Metadaten-Leaks** vermeiden - keine unnötigen Logs oder Analytics

### Sichere Entwicklung
```bash
# Private Keys NIEMALS committen
echo "*.key" >> .gitignore
echo "*.p12" >> .gitignore
echo "local.properties" >> .gitignore

# Dependency Scanning
./gradlew dependencyCheckAnalyze

# Security Linting
./gradlew detekt
```

## 💰 MobileCoin Integration

### Wallet Development
```kotlin
// Beispiel: eUSD Balance abfragen
val balance = mobileCoinWallet.getBalance(TokenId.eUSD)
Log.d("Wallet", "eUSD Balance: ${balance.value}")

// MOB Transaction senden
val transaction = mobileCoinWallet.buildTransaction(
    recipient = recipientAddress,
    amount = Amount.ofMOB("1.5"),
    fee = Fee.DEFAULT
)
```

### Testing mit Testnet
```bash
# MobileCoin Testnet konfigurieren
export MC_CONSENSUS_VALIDATORS="mc://node1.test.mobilecoin.com/"
export MC_FOG_INGEST="fog-ingest.test.mobilecoin.com:443"

# Test-eUSD erhalten
curl -X POST https://testnet-faucet.mobilecoin.com/faucet \
  -H "Content-Type: application/json" \
  -d '{"address":"deine-test-adresse"}'
```

## 🌐 Community & Support

### Kommunikation
- **GitHub Issues**: Bug Reports und Feature Requests
- **GitHub Discussions**: Allgemeine Fragen und Ideen
- **recode.at DAO**: [Governance](https://github.com/recodeat/dao-governance) für größere Entscheidungen

### Hilfe bekommen
- **Molly Core**: [Molly Documentation](https://github.com/mollyim/mollyim-android)
- **MobileCoin**: [Developer Docs](https://developers.mobilecoin.com/)
- **Signal Protocol**: [libsignal](https://github.com/signalapp/libsignal)

## 📄 Lizenz-Compliance

**Wichtig**: Verschiedene Komponenten haben verschiedene Lizenzen:
- **osCASH.me Code**: MIT License
- **Molly Core**: AGPLv3 (Copyleft!)
- **MobileCoin SDK**: Apache 2.0
- **Signal Libraries**: GPLv3 (Copyleft!)

Bei Änderungen an GPL-lizenzierten Komponenten müssen die Änderungen ebenfalls GPL-lizenziert werden.

---

**Bereit loszulegen?** Fork das Repository und erstelle deinen ersten Pull Request! 

**Fragen?** Eröffne eine Discussion oder schreib an github@recode.at

Namasté 🙏

---

**Andere Sprachen:** [English](languages/en/CONTRIBUTING.md)