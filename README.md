**[🇬🇧 English Version](languages/en/README.md)** | 🇩🇪 Deutsche Version

# osCASH.me Android - Privacy Messenger mit eUSD & MOB Wallet

Der offizielle Android Client für osCASH.me - ein Privacy-fokussierter Messenger mit integrierter eUSD und MOB Cryptocurrency Wallet auf der MobileCoin Blockchain.

## 🔒 Privacy First

**Warum osCASH.me?**
- **End-to-End Verschlüsselung** für alle Nachrichten und Anrufe
- **Keine Metadaten-Speicherung** auf unseren Servern
- **Integrierte Crypto Wallet** für sichere, private Payments
- **Open Source** und community-auditiert
- **Dezentral** - deine Daten gehören dir

## 💰 Integrierte Wallet-Features

### MobileCoin Integration
- **eUSD Stablecoin** - stabile Währung für tägliche Nutzung
- **MOB Token** - native MobileCoin für Privacy-optimierte Transaktionen  
- **Sichere Aufbewahrung** - Keys bleiben auf deinem Gerät
- **Schnelle Transaktionen** - Sub-Sekunden Bestätigung
- **Privacy-by-Design** - keine Blockchain-Überwachung möglich

### Zahlungsfunktionen
- **In-Chat Payments** - Geld direkt im Gespräch senden
- **QR-Code Scanner** - einfache Adresseingabe
- **Transaction History** - verschlüsselte Zahlungshistorie
- **Multi-Currency** - eUSD und MOB in einer App

## 🏗️ Technische Basis

### Molly Core Integration
Basiert auf der bewährten **Molly** Privacy-Messenger Technologie:
- Signal Protocol für Verschlüsselung
- Tor-Integration für Anonymität
- Metadata-Schutz
- Sichere Datenspeicherung

### MobileCoin Blockchain
- **Privacy-optimierte Blockchain** - CryptoNote Technologie
- **Schnelle Finality** - keine langen Wartezeiten
- **Umweltfreundlich** - Proof-of-Stake Konsens
- **Regulatorisch konform** - eUSD ist ein regulierter Stablecoin

## 📱 Installation

### Aus den Releases
```bash
# Download der neuesten APK
wget https://github.com/osCASHme/android/releases/latest/osCASH.me.apk

# Installation
adb install osCASH.me.apk
```

### Aus dem Source Code
```bash
# Repository clonen
git clone https://github.com/osCASHme/android.git
cd android

# Dependencies installieren
./gradlew build

# Debug Build
./gradlew assembleDebug
```

## 🛡️ Sicherheitsfeatures

- **Signal Protocol** - bewährte End-to-End Verschlüsselung
- **Perfect Forward Secrecy** - alte Nachrichten bleiben sicher
- **Sealed Sender** - keine Metadaten-Leaks
- **Screen Lock** - biometrischer App-Schutz
- **Auto-Delete Messages** - Nachrichten verschwinden automatisch
- **Tor Integration** - vollständige Anonymität optional

## 🤝 Entwicklung & Beitragen

### Community
- **GitHub Wiki**: [github.com/osCASHme/android/wiki](https://github.com/osCASHme/android/wiki)
- **GitHub Discussions**: [github.com/osCASHme/android/discussions](https://github.com/osCASHme/android/discussions)
- **Issues**: [github.com/osCASHme/android/issues](https://github.com/osCASHme/android/issues)
- **Reproducible Builds**: [REPRODUCIBLE-BUILDS.md](REPRODUCIBLE-BUILDS.md)

### Voraussetzungen
```bash
# Android SDK
- Android API Level 24+ (Android 7.0+)
- NDK für native Crypto-Libraries
- Java 11+

# MobileCoin SDK
- Rust 1.70+
- Protocol Buffers
- SGX SDK (für Enclave-Features)
```

### Development Setup
```bash
git clone https://github.com/osCASHme/android.git
cd android

# Submodules initialisieren
git submodule update --init --recursive

# Build
./gradlew assembleDebug
```

### Code-Struktur
```
android/
├── molly-core/           # Molly Privacy Messenger Basis
├── oscash-core/          # osCASH.me spezifische Features
│   ├── payments-base/    # MobileCoin Wallet Integration
│   ├── oscash-config/    # App-Konfiguration
│   └── addon-manager/    # Modulare Erweiterungen
├── addons/               # Optionale Features
│   ├── mesh-network/     # P2P Mesh Networking
│   ├── qr-gateway/       # QR Code Integration
│   └── multichain-bridge/# Multi-Blockchain Support
└── build.gradle.kts      # Main Build Configuration
```

## 🔗 Links

- **Website**: [osCASH.me](https://osCASH.me)
- **recode.at DAO**: [github.com/recodeat](https://github.com/recodeat)
- **MobileCoin**: [mobilecoin.com](https://mobilecoin.com)
- **Molly Messenger**: [molly.im](https://molly.im)

## 📄 Lizenz

MIT Lizenz - siehe [LICENSE](LICENSE) für Details.

**Abhängigkeiten:**
- Molly Core: AGPLv3
- MobileCoin SDK: Apache 2.0
- Signal Libraries: GPLv3

---

*Verbunden, um frei zu SEIN* 🔒💰
