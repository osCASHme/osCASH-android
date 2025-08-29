# 🍴 Molly Fork Guide für osCASH.me

> Schritt-für-Schritt Anleitung zum Erstellen eines Molly Forks

## 📋 Voraussetzungen

### 1. GitHub Account
- Erstelle einen [GitHub Account](https://github.com) falls noch nicht vorhanden
- Aktiviere 2FA für bessere Sicherheit

### 2. Git Installation prüfen
```bash
git --version
# Sollte Git 2.x+ anzeigen
```

### 3. SSH Key für GitHub einrichten (empfohlen)
```bash
# SSH Key generieren
ssh-keygen -t ed25519 -C "deine-email@example.com"

# Public Key anzeigen und zu GitHub hinzufügen
cat ~/.ssh/id_ed25519.pub
```

## 🚀 Fork-Erstellung

### Schritt 1: Molly Repository forken
1. Gehe zu: https://github.com/mollyim/mollyim-android
2. Klicke auf **"Fork"** Button (oben rechts)
3. Wähle deinen GitHub Account als Ziel
4. **Repository Name**: `oscash-messenger` 
5. **Description**: "osCASH.me - Open Source CASH Messenger with MobileCoin"
6. ✅ **Copy the main branch only** (aktivieren)
7. Klicke **"Create fork"**

### Schritt 2: Fork-Einstellungen anpassen

#### Repository Settings
```
Settings → General → Features:
✅ Issues
✅ Projects  
✅ Wiki
✅ Discussions
❌ Sponsorships
```

#### Branch Protection (später wichtig)
```
Settings → Branches → Add rule:
Branch name: main
✅ Require a pull request before merging
✅ Require status checks to pass
```

### Schritt 3: Repository Topics hinzufügen
```
About → Topics:
android, messenger, mobilecoin, privacy, signal-protocol, 
payments, cryptocurrency, open-source, f-droid
```

## 🔗 Remote-Konfiguration

### Lokaler Clone
```bash
# Clone deinen Fork
git clone git@github.com:DEIN_USERNAME/oscash-messenger.git
cd oscash-messenger

# Upstream Remote hinzufügen (für Updates)
git remote add upstream https://github.com/mollyim/mollyim-android.git
git remote add signal https://github.com/signalapp/Signal-Android.git

# Remotes prüfen
git remote -v
# origin    git@github.com:DEIN_USERNAME/oscash-messenger.git (fetch)
# origin    git@github.com:DEIN_USERNAME/oscash-messenger.git (push)
# upstream  https://github.com/mollyim/mollyim-android.git (fetch)
# upstream  https://github.com/mollyim/mollyim-android.git (push)
# signal    https://github.com/signalapp/Signal-Android.git (fetch)
# signal    https://github.com/signalapp/Signal-Android.git (push)
```

### Submodules initialisieren
```bash
git submodule update --init --recursive
```

## 🏷️ Branch-Strategie

### Main Branches
```bash
# Development Branch erstellen
git checkout -b develop
git push origin develop

# Feature Branches für Module
git checkout -b feature/mobilecoin-integration
git checkout -b feature/qr-payment-gateway
git checkout -b feature/addon-system
```

### Update-Branches (für Molly Updates)
```bash
# Beispiel für regelmäßige Updates
git checkout -b update/molly-v7.54.0
git fetch upstream
git merge upstream/main
# Konflikte lösen
git push origin update/molly-v7.54.0
# Pull Request erstellen
```

## 📦 Repository-Struktur vorbereiten

### README anpassen
```bash
# Original Molly README sichern
mv README.md MOLLY-README.md

# Eigene README erstellen
cat > README.md << 'EOF'
# osCASH.me - Open Source CASH Messenger

> MobileCoin-Wallet basierend auf Molly Fork

## Features
- 💰 MobileCoin/eUSD Zahlungen
- 📱 QR-Code Gateway  
- 🔒 Privacy-First Design
- 🔄 Regelmäßige Molly Updates

[Vollständige Dokumentation](./docs/)
EOF
```

### Dokumentation-Struktur
```bash
mkdir -p docs/{development,deployment,api}
mkdir -p addons/{mobilecoin,qr-gateway,mesh-network}
```

## 🔄 Update-Workflow einrichten

### GitHub Actions für Auto-Updates
```yaml
# .github/workflows/upstream-sync.yml
name: Sync with Molly Upstream

on:
  schedule:
    - cron: '0 6 * * 1'  # Jeden Montag 6:00 UTC
  workflow_dispatch:

jobs:
  sync:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout
        uses: actions/checkout@v4
        with:
          token: ${{ secrets.GITHUB_TOKEN }}
          
      - name: Sync upstream
        run: |
          git remote add upstream https://github.com/mollyim/mollyim-android.git
          git fetch upstream
          git checkout main
          git merge upstream/main
          git push origin main
```

## 📝 Commit-Convention

### Commit-Format
```
type(scope): description

types:
- feat: neue Feature
- fix: Bugfix  
- docs: Dokumentation
- style: Formatierung
- refactor: Code-Umstrukturierung
- test: Tests
- chore: Build/Tools

Beispiele:
feat(mobilecoin): add wallet integration
fix(qr): resolve payment parsing issue
docs(readme): update installation guide
```

### Pre-commit Hook
```bash
# .gitmessage Template erstellen
cat > .gitmessage << 'EOF'
# type(scope): description
#
# Longer description if needed
#
# Co-Authored-By: Claude <noreply@anthropic.com>
EOF

git config commit.template .gitmessage
```

## 🚦 Nächste Schritte

Nach dem Fork-Setup:

1. ✅ **Fork erstellt und konfiguriert**
2. ⏳ Lokales Development-Setup
3. ⏳ Build-System anpassen  
4. ⏳ Add-On Architektur implementieren
5. ⏳ MobileCoin Integration

## 🔍 Fork-Verifikation

### Checklist
```bash
# Repository-Status prüfen
git status
git log --oneline -5

# Remotes testen
git fetch upstream
git fetch origin

# Submodules prüfen
git submodule status

# Build-Test (später)
./gradlew assembleDebug
```

---

**Wichtig**: Dieser Fork bleibt immer mit Molly kompatibel für einfache Updates! 

*Namasté* 🙏