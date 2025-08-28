# osCASH.me Build Verification Guide

**Community-Anleitung zur Verifikation unserer Reproducible Builds**

## 🔍 Warum Verifikation wichtig ist

> *"Vertrauen bekommt man nicht geschenkt, man verdient sich Vertrauen! Immer und immer wieder!"*

Mit dieser Anleitung können Sie selbst überprüfen, dass unsere Binaries exakt aus dem öffentlichen Quellcode erstellt wurden - keine Backdoors, keine Manipulation.

## 📋 Voraussetzungen

- Docker installiert
- Git installiert  
- 10GB freier Speicherplatz
- Internet für Dependencies

## ✅ Schritt 1: MollySocket Binary verifizieren

### 1.1 Repository klonen
```bash
git clone https://github.com/osCASHme/mollysocket.git
cd mollysocket
```

### 1.2 Unsere Binary herunterladen
```bash
wget https://github.com/osCASHme/mollysocket/releases/download/v1.0.0/mollysocket-osCASH.me
```

### 1.3 Selbst bauen mit Docker
```bash
# Dockerfile.reproducible verwenden (bereits im Repo)
docker build -f Dockerfile.reproducible -t mollysocket-verify .

# Binary extrahieren
docker run --rm -v $(pwd):/output mollysocket-verify \
  cp /build/outputs/mollysocket /output/mollysocket-rebuilt
```

### 1.4 Binaries vergleichen
```bash
# Größe vergleichen
ls -la mollysocket-*

# Checksums vergleichen
sha256sum mollysocket-osCASH.me
sha256sum mollysocket-rebuilt

# Detaillierter Vergleich (optional)
diff mollysocket-osCASH.me mollysocket-rebuilt
```

### 1.5 Erwartetes Ergebnis
- Dateigröße: ~6MB (identisch)
- Funktionalität: `./mollysocket-rebuilt --version` zeigt 1.6.0

## ✅ Schritt 2: osCASH.me Android APK verifizieren

### 2.1 Repository Setup
```bash
git clone https://github.com/osCASHme/android.git
cd android
git checkout v1.0.0-alpha3-MOB
```

### 2.2 Offizielle APK herunterladen
```bash
wget https://github.com/osCASHme/android/releases/download/v1.0.0-alpha3-MOB/osCASH.me-v1.0.0-alpha3-MOB-FOSS.apk
```

### 2.3 Reproducible Build erstellen
```bash
cd molly-core/reproducible-builds

# Build mit exakt unseren Parametern
CI_APP_TITLE="osCASH.me" \
CI_PACKAGE_ID="me.oscash.app" \
CI_BUILD_VARIANTS="prodFossWebsite" \
docker compose run --rm assemble
```

### 2.4 APKs vergleichen
```bash
# Python-Script für APK-Vergleich
python apkdiff/apkdiff.py \
  ../../osCASH.me-v1.0.0-alpha3-MOB-FOSS.apk \
  outputs/apk/prodFossWebsite/release/Molly-Repro-*.apk
```

### 2.5 Erwartetes Ergebnis
- APK-Größe: ~87MB
- Code identisch (bis auf Signatur)
- Gleiche Funktionalität

## 🔐 Checksums für Schnell-Verifikation

Wenn Sie nicht selbst bauen möchten, vergleichen Sie diese Checksums:

```bash
# osCASH Android APK (based on Molly v7.53.5-1)
sha256sum osCASH-v7.53.5-01-FOSS.apk
# Erwarteter Hash: 7c6924b63df615a870165ab815894f6c2e103e03e7fa8f36c5be267dc332f353

# MollySocket Binary
sha256sum mollysocket-osCASH.me  
# Erwarteter Hash: 4763b8517b9f06d78cdc80ee91317d8798e7549ab8fe7f44d8ab714311ba824b
```

## 🚨 Red Flags - Warnsignale

**Melden Sie uns sofort wenn:**
- Checksums nicht übereinstimmen
- Dateigrößen stark abweichen (>1%)
- Build-Prozess unerwartet fehlschlägt
- Unerwartete Netzwerkzugriffe während Build

## 🛠️ Troubleshooting

### Docker Build fehlschlägt
```bash
# Cache löschen und neu versuchen
docker system prune -a
docker build --no-cache -f Dockerfile.reproducible .
```

### Unterschiedliche Binaries
- Prüfen Sie die Git-Version/Tag
- Stellen Sie sicher, dass SOURCE_DATE_EPOCH gesetzt ist
- Verwenden Sie exakt unsere Docker-Images

### Android Build zu groß/klein
- Richtige Build-Variante? (prodFossWebsite)
- Alle Submodules geklont? (`git submodule update --init`)

## 📊 Verifikations-Matrix

| Component | Our Hash | Your Hash | Match? |
|-----------|----------|-----------|--------|
| APK | [hash] | [your] | ✅/❌ |
| MollySocket | [hash] | [your] | ✅/❌ |

## 🤝 Community Verification

**Teilen Sie Ihre Ergebnisse:**
1. GitHub Issue mit Verification-Report
2. Discord/Matrix Community
3. GitHub Discussions

### Verification Badge
Nach erfolgreicher Verifikation können Sie dieses Badge verwenden:
```markdown
![Verified Build](https://img.shields.io/badge/Build-Verified%20✓-green)
```

## 📚 Weiterführende Dokumentation

- [Reproducible Builds Project](https://reproducible-builds.org)
- [REPRODUCIBLE-BUILDS.md](REPRODUCIBLE-BUILDS.md)
- [BUILD-STATUS.md](BUILD-STATUS.md)
- [Docker Documentation](https://docs.docker.com)

## ❓ FAQ

**Q: Warum sind die Binaries nicht 100% identisch?**
A: Signaturen unterscheiden sich, aber der Code ist identisch.

**Q: Kann ich auch auf Windows verifizieren?**
A: Ja, mit Docker Desktop oder WSL2.

**Q: Wie lange dauert die Verifikation?**
A: MollySocket: ~5 Min, Android APK: ~10 Min

**Q: Was wenn ich Abweichungen finde?**
A: Sofort als Issue auf GitHub melden!

## 🏆 Verification Hall of Fame

Community-Mitglieder die erfolgreich verifiziert haben:
- [Ihr Name hier nach erfolgreicher Verifikation]

---

**osCASH.me - Trust through Transparency**

*"Trust, but verify" - Ronald Reagan*

*"Privacy First. Swiss Clockwork Reliability." - osCASH.me Team*