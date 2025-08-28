# osCASH.me Release Notes - Reproducible Builds v1.0

**Datum:** 28. August 2025  
**Version:** 1.0.0-alpha  
**Motto:** *"Privacy First. Swiss Clockwork Reliability."*

## 🚀 Erfolgreich erstellte Reproducible Builds

### 1. osCASH.me Android App (mollyim-android)
- **Datei**: `osCASH.me-v1.0.0-alpha3-MOB-FOSS.apk`
- **Größe**: 87 MB
- **Build**: Docker-basiert mit reproducible-builds Environment
- **Features**: 
  - ✅ Signal Protocol End-to-End Verschlüsselung
  - ✅ MobileCoin Integration (MOB + eUSD aktiviert)
  - ✅ Molly Privacy Features
  - ✅ FOSS Version ohne Google Services
- **Getestet**: Erfolgreich auf Android-Gerät

### 2. MollySocket UnifiedPush Server
- **Datei**: `mollysocket-osCASH.me`
- **Größe**: 6 MB
- **Version**: 1.6.0
- **Typ**: Linux x86_64 ELF Binary
- **Build**: Rust 1.75-slim Docker Environment
- **Features**:
  - ✅ UnifiedPush Notifications ohne Google
  - ✅ Privacy-konforme Push-Infrastructure
  - ✅ Air-gapped Deployment möglich
  - ✅ Signal-kompatible Benachrichtigungen

## 🔧 Build-Umgebung

### Docker-basierte Reproducible Builds
```bash
# MollySocket Build
cd /home/mayer/prog/claude/osCASH.me/mollysocket
docker build -f Dockerfile.reproducible -t mollysocket-build:osCASH.me .

# mollyim-android Build  
cd /home/mayer/prog/claude/osCASH.me/android/molly-core/reproducible-builds
CI_APP_TITLE="osCASH.me" \
CI_PACKAGE_ID="me.oscash.app" \
CI_BUILD_VARIANTS="prodFossWebsite" \
docker compose run --rm assemble
```

### Verifizierte Dependencies
- **Rust**: 1.75-slim (MollySocket)
- **OpenJDK**: 17.0.16 (Android Build)
- **Go**: 1.19.8 (Native Libraries)
- **Docker**: Reproducible Environment

## 🔐 Verifikation & Trust

### SHA256 Checksums
```
# Generiere Checksums für Verifikation
sha256sum osCASH.me-v1.0.0-alpha3-MOB-FOSS.apk
sha256sum mollysocket-osCASH.me
```

### Reproducible Build Verifikation
Jeder kann diese Builds selbst erstellen und verifizieren:
1. Repository klonen
2. Docker-Environment starten
3. Build-Commands ausführen
4. Binaries vergleichen

**"Trust, but verify"** - Alle Build-Schritte sind transparent dokumentiert.

## 📦 Installation

### Android App (APK)
```bash
# Download
wget https://github.com/osCASHme/android/releases/download/v1.0.0-alpha3-MOB/osCASH.me-v1.0.0-alpha3-MOB-FOSS.apk

# Installation via ADB
adb install osCASH.me-v1.0.0-alpha3-MOB-FOSS.apk
```

### MollySocket Server
```bash
# Download Binary
wget https://github.com/osCASHme/mollysocket/releases/download/v1.0.0/mollysocket-osCASH.me

# Ausführbar machen
chmod +x mollysocket-osCASH.me

# Version prüfen
./mollysocket-osCASH.me --version
```

## 🎯 Nächste Schritte

### In Development
- **monero-wallet-sdk**: Android SDK Setup für vollständigen Build
- **Multi-Architecture**: ARM64 Support für Mobile Devices
- **Automated Verification**: CI/CD für Community Builds

### Community Engagement
- **GitHub Discussions**: https://github.com/osCASHme/android/discussions
- **Reproducible Builds**: Community-Verifikation erwünscht
- **Feedback**: Issues und Pull Requests willkommen

## 🤝 Beitragen

**Wir laden die Community ein:**
- Builds selbst zu verifizieren
- Reproducible Build Prozess zu testen
- Feedback zu Security & Privacy zu geben
- Multi-Platform Support zu erweitern

## 📄 Lizenz & Credits

- **osCASH.me**: MIT License
- **Signal/Molly Base**: AGPL v3
- **MobileCoin SDK**: Apache 2.0
- **MollySocket**: AGPL v3

**Basiert auf der Arbeit von:**
- Signal Foundation (Signal Protocol)
- Molly.im (Privacy Enhancements)
- MobileCoin Foundation (Crypto Payments)

---

**osCASH.me - Verbunden, um frei zu SEIN** 🔒💰

*Privacy First. Sicher & zuverlässig wie ein Schweizer Uhrwerk.*