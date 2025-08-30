# Reproducible Builds für osCASH.me

**Vertrauen durch Transparenz - Überprüfbare Builds vom Quellcode zur APK**

## 🎯 Warum Reproducible Builds?

**"Vertrauen bekommt man nicht geschenkt, man verdient sich Vertrauen! Immer und immer wieder!"**

Mit Reproducible Builds können Sie **selbst überprüfen**, dass die osCASH.me APK exakt aus dem öffentlichen Quellcode erstellt wurde - keine versteckten Backdoors, keine Manipulation, nur der Code, den Sie auf GitHub sehen.

## 📋 Voraussetzungen

- Docker
- Docker Compose  
- Python 3
- Git

## 🔍 Build und Verifikation

Sie können Ihre eigene osCASH.me Release in einem Docker-Container kompilieren und die resultierende APK mit der offiziell verteilten APK vergleichen:

### 1. Repository klonen
```bash
# osCASH.me Quellcode klonen
git clone https://github.com/osCASHme/osCASH-android.git
cd android
```

### 2. Release Version auswählen
```bash
# Setzen Sie die Release-Version, die Sie überprüfen möchten
export VERSION=v1.0.0-alpha3-MOB

# Checken Sie den Release-Tag aus
git checkout $VERSION
```

### 3. Reproducible Build erstellen
```bash
# Wechseln Sie ins molly-core Verzeichnis
cd molly-core/reproducible-builds

# APK mit Docker-Environment bauen
CI_APP_TITLE="osCASH.me" \\
CI_PACKAGE_ID="me.oscash.app" \\
CI_BUILD_VARIANTS="prodFossWebsite" \\
docker compose run --rm assemble
```

### 4. Offizielle APK herunterladen
```bash
# Laden Sie die offizielle osCASH.me APK herunter
wget https://github.com/osCASHme/osCASH-android/releases/download/$VERSION/osCASH.me-$VERSION-FOSS.apk
```

### 5. APKs vergleichen
```bash
# Führen Sie das Vergleichs-Script aus
python apkdiff/apkdiff.py \\
    osCASH.me-$VERSION-FOSS.apk \\
    outputs/apk/prodFossWebsite/release/Molly-Repro-untagged-FOSS.apk
```

## ✅ Was Sie erwarten können

**Bei erfolgreichem Reproducible Build:**
- ✅ Identische APK-Größe
- ✅ Identische Datei-Hashes
- ✅ Identische Code-Signaturen (bis auf Signing-Zertifikat)
- ✅ **Beweis: Die APK wurde aus dem öffentlichen Code erstellt**

## 🔐 osCASH.me Signing Certificate

**Unsere offizielle APK ist signiert mit:**
```
Keystore: osCASH-release.keystore
Alias: oscash-key
CN: osCASH.me
OU: osCASH Development
O: osCASH.me
```

**Ihr selbst gebauter APK wird unsigned oder mit Ihrem eigenen Debug-Zertifikat signiert sein - das ist normal und erwartet.**

## 🏗️ Build-Environment Details

### Docker-basiert für Konsistenz
- **Reproduzierbare Umgebung**: Gleiche JDK, Android SDK, Gradle Versionen
- **Isoliert**: Keine Abhängigkeiten von Ihrem lokalen System
- **Transparent**: Alle Build-Schritte sind in Docker-Files dokumentiert

### Environment Variables
```bash
CI_APP_TITLE="osCASH.me"           # App-Name
CI_PACKAGE_ID="me.oscash.app"       # Android Package ID
CI_BUILD_VARIANTS="prodFossWebsite" # Build-Variante (FOSS ohne Google)
```

## 🛡️ Sicherheit & Vertrauen

### Privacy-First Approach
- **Lokale Builds**: Keine Builds auf fremden CI/CD Servern
- **Vollständige Kontrolle**: Sie kontrollieren den gesamten Build-Prozess
- **Keine Black Box**: Jeder Schritt ist transparent und überprüfbar

### Swiss Clockwork Reliability
- **Deterministische Builds**: Gleicher Input = gleicher Output
- **Versionierte Dependencies**: Alle Abhängigkeiten sind gelockt
- **Comprehensive Logs**: Alle Build-Schritte werden protokolliert

## 🚨 Red Flags - Wann Vorsicht geboten ist

**Warnen Sie uns, falls:**
- APK-Größen unterschiedlich sind (>1% Abweichung)
- Datei-Hashes sich unterscheiden
- Build-Prozess mit Fehlern abbricht
- Unerwartete Netzwerk-Zugriffe während dem Build

## 📚 Weiterführende Informationen

- **Reproducible Builds Project**: https://reproducible-builds.org/
- **osCASH.me Documentation**: `/readme/` Verzeichnis
- **Molly Reproducible Builds**: `molly-core/reproducible-builds/README.md`
- **GitHub Issues**: Bei Problemen oder Fragen

## 🤝 Community Verification

**Wir ermutigen die Community zur regelmäßigen Verifikation:**
- Teilen Sie Ihre Verifikations-Ergebnisse im GitHub Repository
- Melden Sie Abweichungen sofort als Issue
- Helfen Sie anderen bei der Verifikation

---

**osCASH.me - Vertrauen durch Transparenz und überprüfbare Sicherheit**

*"Trust, but verify" - Ronald Reagan*