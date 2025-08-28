# osCASH.me Messenger - Quick Start Guide 🚀

> **Tier 3 Implementation:** osCASH.me Messenger mit MOB/eUSD Wallet Features

---

## 📋 **Aktueller Status (28. August 2025)**

### **✅ Repository-Struktur etabliert:**
```
osCASH.me/android/
├── molly-core/              # Symlink → ../mollyim-android (Molly-Repro)
├── oscash-core/             # osCASH.me Core Extensions
│   ├── payments-base/       # MobileCoin Reaktivierung
│   ├── addon-manager/       # Plugin-System Core
│   └── oscash-config/       # osCASH.me Konfiguration  
├── addons/                  # Modulare Add-Ons
│   ├── qr-gateway/          # QR-Code Payment Gateway
│   ├── multichain-bridge/   # eUSD/USDT/USDC Support (optional)
│   └── mesh-network/        # Offline Mesh Payments (optional)
└── app/                     # Main osCASH.me App
```

### **✅ Gradle Integration konfiguriert:**
- **settings.gradle.kts:** Alle Molly-Core Module eingebunden
- **app/build.gradle.kts:** Dependencies zu Molly + osCASH Extensions
- **Package ID:** `me.oscash.app` (unabhängig von Molly/Signal)
- **Versionierung:** v1.0.0-alpha1-molly-7.53.5

---

## 🏗️ **Build-System (Docker-First)**

### **Warum Docker?**
- **Reproducible Builds** wie bei Molly-Repro
- **Konsistente Build-Umgebung** unabhängig vom Host-System
- **Einfache Integration** des bewährten Molly-Build-Systems

### **Geplante Docker-Integration:**
```dockerfile
# Dockerfile (geplant)
FROM openjdk:17-jdk-alpine

# Android SDK Setup (wie Molly-Repro)
COPY --from=molly-builder /opt/android-sdk-linux /opt/android-sdk-linux

# osCASH.me spezifische Dependencies
RUN apt update && apt install -y mobilecoin-dev

# Build
WORKDIR /oscash
COPY . .
RUN ./gradlew assembleRelease
```

---

## 🎯 **Entwicklungsschritte (Priorität)**

### **Phase 1: Schritt 1 - Rebranding ✅**
- [x] Repository-Struktur etabliert
- [x] Molly-Core Symlink erstellt  
- [x] Gradle Build-Konfiguration
- [ ] Erste APK bauen (Docker)
- [ ] osCASH.me Branding (App-Name, Icons)

### **Phase 2: Schritt 2 - MOB Wallet Aktivierung**
- [ ] MobileCoin SDK Dependencies
- [ ] `payments-base` Module implementieren
- [ ] RemoteConfig Override (android.payments.kill = false)
- [ ] Payments UI reaktivieren
- [ ] MOB Balance & Transactions

### **Phase 3: Schritt 3 - eUSD Analysis**  
- [ ] eUSD vs MOB Blockchain-Analyse
- [ ] Transaction-Format Vergleich
- [ ] UI-Integration für beide Currencies

---

## 🔧 **Aktuelle Build-Probleme & Lösungen**

### **Problem 1: Java/Android SDK fehlt auf Host**
**Status:** ⚠️ Bekannt  
**Lösung:** Docker-Build-System verwenden (wie Molly-Repro)
```bash
# Geplant:
docker compose -f docker-compose.oscash.yml up --build
```

### **Problem 2: MobileCoin Dependencies**
**Status:** 📋 Geplant  
**Repository:** `maven { url = uri("https://s3-us-west-1.amazonaws.com/mobilecoin.chain") }`
**Dokumentation:** Bereits in settings.gradle.kts konfiguriert

### **Problem 3: Gradle Module Dependencies**
**Status:** ⚠️ Ungetestet  
**Risiko:** Molly-Core Module könnten inkompatible Dependencies haben
**Mitigation:** Schrittweiser Build-Test mit Docker

---

## 💡 **Nächste konkrete Schritte**

### **1. Docker Build-System erstellen**
```bash
# Basierend auf Molly-Repro Dockerfile
cp ../mollyim-android/Dockerfile ./Dockerfile.oscash
# Anpassungen für osCASH.me Module
```

### **2. Erste Build-Tests**
```bash
# Nur oscash-core Module
./gradlew :oscash-core:payments-base:build
./gradlew :oscash-core:addon-manager:build

# Dann Full Build
./gradlew :app:assembleDebug
```

### **3. MOB Payments Core Implementation**
```kotlin
// oscash-core/payments-base/src/main/kotlin/PaymentEngine.kt
class MobileCoinPaymentEngine {
    fun enablePayments(): Boolean {
        // RemoteConfig Override
        SignalStore.payments().setPaymentsEnabled(true)
        return true
    }
}
```

---

## 🔗 **Referenzen & Dependencies**

### **Upstream Repositories:**
- **Molly-Repro:** `../mollyim-android` (Tier 2 - 1:1 Reproducible)
- **Original Molly:** `https://github.com/mollyim/mollyim-android`
- **Signal Android:** `https://github.com/signalapp/Signal-Android` (MOB Wallet Referenz)

### **MobileCoin Resources:**
- **SDK Documentation:** https://mobilecoin.com/developer-docs
- **eUSD Stablecoin:** https://mobilecoin.com/eusd
- **Blockchain Explorer:** https://block-explorer.mobilecoin.com

### **Build-Referenzen:**
- **Molly-Repro Build:** Bewährtes Docker-System verwenden
- **Critical Fixes:** `../mollyim-android/readme/osCASH-CRITICAL-FIXES.md`
- **APK Signing:** Gleiche Prinzipien wie Molly-Repro (v2+v3)

---

**Status:** 🚧 **Architecture Ready - Implementation in Progress**  
**Next:** Docker Build-System + MOB Payments Aktivierung  
**Target:** Funktionsfähige osCASH.me APK mit MOB Wallet

*28. August 2025 - osCASH.me Tier 3 Kickoff* 🎯