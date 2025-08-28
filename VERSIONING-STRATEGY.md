# osCASH.me Versioning Strategy

**Transparent, nachvollziehbar, Swiss Clockwork präzise**

## 📋 Versionsnummern-Schema

### Basis: Molly Version Tracking
Wir verwenden die Molly-Versionsnummer als Basis und erweitern sie mit unserem eigenen Build-Counter:

```
osCASH-v{MOLLY_VERSION}-{BUILD_NUMBER}[-VARIANT]

Beispiele:
- osCASH-v7.53.5-01-FOSS.apk    (Erster Build basierend auf Molly v7.53.5-1)
- osCASH-v7.53.5-01.apk          (Google Play Services Variante)
- osCASH-v7.53.5-02-FOSS.apk    (Zweiter Build mit Bugfixes)
- osCASH-v7.54.0-01-FOSS.apk    (Update auf neue Molly Version)
```

## 🎯 Vorteile dieser Strategie

### Transparenz
- **Community sieht sofort**: Auf welcher Molly-Version basieren wir
- **Security Updates trackbar**: Molly v7.53.5 = Signal Security Patches enthalten
- **Upstream-Kompatibilität**: Einfacher Vergleich mit Molly/Signal Features

### Versionsverwaltung
```
Molly Version: v7.53.5-1
↓
osCASH Builds:
├── osCASH-v7.53.5-01  (Initial Release)
├── osCASH-v7.53.5-02  (Bugfix)
├── osCASH-v7.53.5-03  (Feature Addition)
└── osCASH-v7.53.5-04  (Performance Update)
```

## 📦 Varianten-Management

### Aktuelle Varianten (Production)
1. **FOSS** - Für F-Droid, ohne Google Services
2. **Standard** - Mit optionalen Google Services

### Zukünftige Varianten (Planned)
3. **F-DROID** - Speziell für F-Droid Store (wenn benötigt)
4. **ACCRESCENT** - Für GrapheneOS Store (wenn benötigt)

## 🔧 Build-Konfiguration

### Dateinamen-Konvention
```bash
# FOSS Version (ohne Google)
osCASH-v7.53.5-01-FOSS.apk

# Standard Version (mit Google optional)
osCASH-v7.53.5-01.apk

# Store-spezifische Versionen (später)
osCASH-v7.53.5-01-FDROID.apk
osCASH-v7.53.5-01-ACCRESCENT.apk
```

### Version Code Berechnung
```kotlin
// Android versionCode Berechnung
// Beispiel für v7.53.5-01
versionCode = 7535001  // 7.53.5 = 753500, Build 01 = 1

// Für v7.53.5-12
versionCode = 7535012  // 7.53.5 = 753500, Build 12 = 12
```

## 📅 Release-Zyklus

### Molly Updates
- **Alle 2 Wochen**: Molly released Updates
- **Wir folgen**: 1-3 Tage später mit osCASH Build
- **Security Patches**: Sofort (innerhalb 24h)

### osCASH Eigene Updates
- Build-Nummer erhöhen bei:
  - Bugfixes
  - Feature Additions
  - Configuration Changes
  - Branding Updates

## 🏷️ Git Tag Strategie

```bash
# Tag Format
git tag -a osCASH-v7.53.5-01 -m "osCASH v7.53.5-01 based on Molly v7.53.5-1"

# Release Branch
release/osCASH-v7.53.5

# Development Branch  
develop/osCASH-v7.54.0
```

## 📊 Version Mapping Tabelle

| Molly Version | osCASH Version | Release Date | Notes |
|--------------|----------------|--------------|--------|
| v7.53.5-1 | osCASH-v7.53.5-01 | 2025-08-28 | Initial osCASH Release |
| v7.53.5-1 | osCASH-v7.53.5-02 | 2025-08-29 | MobileCoin Fix |
| v7.54.0-1 | osCASH-v7.54.0-01 | 2025-09-xx | Update to new Molly |

## 🔄 Migration von alter Nummerierung

**Alt**: `osCASH.me-v1.0.0-alpha3-MOB-FOSS.apk`
**Neu**: `osCASH-v7.53.5-01-FOSS.apk`

### Mapping für bestehende Releases
- v1.0.0-alpha3-MOB → osCASH-v7.53.5-01

## ✅ Implementierung Checklist

- [ ] Rename existing APK files
- [ ] Update build scripts mit neuer Namenskonvention
- [ ] Git Tags anpassen
- [ ] CI/CD Environment Variables updaten
- [ ] Documentation aktualisieren
- [ ] Release Notes Template anpassen

## 🎯 Beispiel: Nächster Release

```bash
# Aktuell bei Molly v7.53.5-1
# Unser erster offizieller Build:

# Build erstellen
CI_APP_TITLE="osCASH" \
CI_APP_VERSION="v7.53.5-01" \
CI_BUILD_VARIANTS="prodFossWebsite" \
docker compose run --rm assemble

# Output
osCASH-v7.53.5-01-FOSS.apk
osCASH-v7.53.5-01.apk

# GitHub Release
gh release create osCASH-v7.53.5-01 \
  --title "osCASH v7.53.5-01 (Based on Molly v7.53.5-1)" \
  --notes "MobileCoin enabled, Privacy First" \
  osCASH-v7.53.5-01-FOSS.apk \
  osCASH-v7.53.5-01.apk
```

## 🚀 Vorteile für die Community

1. **Klar erkennbare Upstream-Version**
2. **Security-Update-Status sofort sichtbar**
3. **Einfache Migrations-Pfade**
4. **Reproducible Builds verifizierbar**
5. **Store-Kompatibilität vorbereitet**

---

**osCASH.me - Transparent versioning for transparent builds**

*"Privacy First. Swiss Clockwork Reliability."*