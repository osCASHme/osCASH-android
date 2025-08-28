# osCASH.me Molly Fork - FAQ & Knowledge Base

**Häufig gestellte Fragen und Erkenntnisse zur osCASH.me Tier 3 Architektur**

## 🏗️ Architektur & Konzept

### Q: Was ist die Tier 3 Architektur?
**A**: Systematische 3-stufige Entwicklung:
- **Tier 1**: Signal → Molly (upstream, community maintained)
- **Tier 2**: Molly → Molly-Repro (1:1 reproducible builds) ✅
- **Tier 3**: Molly-Repro → **osCASH.me** (enhanced with crypto payments) 🔥

### Q: Warum basiert osCASH.me auf Molly statt direkt auf Signal?
**A**: Molly bietet:
- **Reproducible Builds** (vertrauenswürdiger)
- **Enhanced Security Features** (Passphrase-Schutz, etc.)
- **FOSS Variante** ohne Google Dependencies
- **Bewährte Basis** für Crypto-Integration

### Q: Ist osCASH.me kompatibel mit Signal/Molly Nutzern?
**A**: ✅ **Ja, vollständig!** 
- Verwendet das **Signal Protocol**
- Kann mit Signal- und Molly-Nutzern kommunizieren
- Package ID `me.oscash.app` vermeidet Konflikte

## 🔧 Build System

### Q: Warum Docker-basierte Builds?
**A**: 
- **Reproducible**: Identische Builds auf allen Systemen
- **Verified**: Basiert auf bewährtem Molly-Repro System
- **Isolated**: Keine lokalen Dependencies-Konflikte
- **Trusted**: Exakt wie Molly v7.53.5-1 Builds

### Q: Welche Molly-Version verwenden wir?
**A**: **Molly v7.53.5** (neueste stabile)
- Getestet mit osCASH.me Tier 3 System ✅
- Vollständig kompatibel mit aktuellen Signal-Features
- Bewährt für Reproducible Builds

### Q: Wie unterscheidet sich osCASH.me von Standard-Molly?
**A**:
```
Molly:         im.molly.app          (Original)
osCASH.me:     me.oscash.app         (Distinct Package)
Branding:      "osCASH.me"           (CI_APP_TITLE)
Extensions:    oscash-core modules   (Payment System)
Future:        MOB/eUSD Integration  (Crypto Wallets)
```

## 🐳 Docker & Environment

### Q: Welche Environment-Variablen sind kritisch?
**A**: 
```bash
CI_APP_TITLE="osCASH.me"           # App-Name in UI
CI_APP_FILENAME="osCASH.me"        # APK-Dateiname
CI_PACKAGE_ID="me.oscash.app"      # Android Package ID
CI_BUILD_VARIANTS="prodFossWebsite" # Molly Variant Filter
```

### Q: Warum `prodFossWebsite` als Build-Variante?
**A**:
- **FOSS**: Keine Google Play Services (privacy-focused)
- **Website**: Für direkte Distribution (nicht Play Store)
- **Prod**: Production-ready (nicht Staging)
- **Molly-Compatible**: Matcht Molly's `selectableVariants` Filter

### Q: Wie wird das osCASH.me Branding angewendet?
**A**: Molly's Build-System liest Environment-Variablen:
```kotlin
// Molly Code (app/build.gradle.kts):
val baseAppTitle = getCiEnv("CI_APP_TITLE") ?: properties["baseAppTitle"]
val baseAppFileName = getCiEnv("CI_APP_FILENAME") ?: properties["baseAppFileName"]  
val basePackageId = getCiEnv("CI_PACKAGE_ID") ?: properties["basePackageId"]
```

## 🔍 Build Issues & Solutions

### Q: "No matching variant of project :molly-core:app was found"
**A**: ❌ **Häufiger Fehler** - Molly-Core:app ist ein **Application**-Modul, kein Library!
```kotlin
// FALSCH:
implementation(project(":molly-core:app"))  

// RICHTIG: 
// Baue molly-core:app direkt mit Environment-Variablen
```

### Q: "Cannot locate tasks that match assembleProdFossWebsiteDebug"
**A**: Molly filtert Build-Varianten basierend auf `CI_BUILD_VARIANTS` Regex:
```kotlin
// Molly Code:
val selected = variant.name in selectableVariants  
if (!(selected && buildVariants.toRegex().containsMatchIn(variant.name))) {
  variant.enable = false
}
```
**Lösung**: `CI_BUILD_VARIANTS="prodFossWebsite"` (exact match)

### Q: "Quota exceeded" Fehler beim Build?
**A**: Docker braucht **min. 20GB Speicher** für:
- NDK Download (~2GB)
- Android SDK Components
- AAR Dependencies (libsignal, ringrtc)
- Build Artifacts

## 📱 APK & Installation

### Q: Wie groß ist eine osCASH.me APK?
**A**: **~84MB** (identisch zu Molly)
- Vollständige Signal/Molly Funktionalität
- FOSS Build (keine Play Services)
- Native Libraries (libsignal, webrtc)

### Q: Wie installiere ich osCASH.me neben Molly/Signal?
**A**: ✅ **Parallele Installation möglich**
- osCASH.me: `me.oscash.app`
- Molly: `im.molly.app` 
- Signal: `org.thoughtcrime.securesms`
- **Keine Konflikte** zwischen den Apps

### Q: Wie verifiziere ich eine osCASH.me APK?
**A**: 
```bash
# SHA256 Hash prüfen:
sha256sum osCASH-v1.0.0-alpha3-molly-7.53.5-FOSS.apk
# Expected: 2c5bc5a1621eaccbc148263ff1d07eb4edf96336c1705dd715ea206732f32f48

# APK-Inhalt prüfen:
aapt dump badging osCASH-*.apk | grep package
# Expected: package: name='me.oscash.app'
```

## 💰 Crypto & Payments

### Q: Welche Kryptowährungen wird osCASH.me unterstützen?
**A**: **Geplante Roadmap**:
1. **MOB (MobileCoin)** - bereits in Signal/Molly vorhanden, wird aktiviert
2. **eUSD** - Erweiterung für Stablecoin-Support  
3. **Multi-Chain** - Weitere Chains nach Bedarf

### Q: Wie funktioniert die MOB-Integration?
**A**: Signal hat bereits **MobileCoin-Support**:
- Molly enthält den **kompletten Payment-Code**
- osCASH.me **aktiviert** diese Funktionalität
- **Package ID** wird für MOB-Context angepasst

### Q: Ist osCASH.me ready für Payments?
**A**: **Architektur bereit** (2025-08-28):
- ✅ Stable Build System
- ✅ Working APK (84MB)
- ✅ Extension Modules (`oscash-core/payments-base`)
- 🔄 **Next**: MOB activation

## 🔗 Links & Resources

### GitHub Repositories:
- **osCASH.me**: https://github.com/osCASHme/android
- **Molly Base**: https://github.com/mollyim/mollyim-android
- **Signal**: https://github.com/signalapp/Signal-Android

### Latest Releases:
- **osCASH.me v1.0.0-alpha3**: https://github.com/osCASHme/android/releases/tag/v1.0.0-alpha3
- **APK**: `osCASH-v1.0.0-alpha3-molly-7.53.5-FOSS.apk` (84MB)

### Documentation:
- **Critical Fixes**: `readme/osCASH-CRITICAL-FIXES.md`
- **Session Summary**: `readme/SESSION_SUMMARY_2025-08-28.md`  
- **Claude Guide**: `readme/CLAUDE.md`

## 🎯 Development Status

### ✅ Completed (Tier 3 Foundation):
- Repository structure with Molly-Core integration
- Docker-based reproducible build system  
- osCASH.me branding and package configuration
- Working 84MB APK with full Signal/Molly compatibility
- GitHub release pipeline
- Knowledge management system

### 🔄 In Progress (Payment Integration):
- MOB payment functionality activation
- osCASH.me payment context configuration
- Multi-chain wallet architecture design

### 🔮 Planned (Future Phases):
- eUSD stablecoin integration
- Advanced wallet features
- Cross-chain payment bridges

---

**Last Updated**: 2025-08-28  
**Molly Version**: v7.53.5  
**osCASH.me Status**: Alpha 3 - Foundation Complete ✅  
**Next Milestone**: MOB Payment Activation 🚀