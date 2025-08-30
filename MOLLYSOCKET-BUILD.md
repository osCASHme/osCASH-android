# MollySocket - Reproducible Build Setup

**UnifiedPush Notifications für osCASH.me ohne Google Services**

## 🎯 Was ist MollySocket?

MollySocket ermöglicht Signal-Benachrichtigungen über UnifiedPush für Android. Es fungiert als verlinkte Device-Verbindung zum Signal-Server und leitet verschlüsselte Events als Push-Benachrichtigungen weiter.

## 🏗️ Technische Details

### Sprache & Build-System
- **Sprache**: Rust
- **Build-System**: Cargo (Rust Package Manager)
- **Output**: Binary executable `mollysocket`
- **Lizenz**: AGPL-3.0

### Architektur
- Web-Server für Device-Registrierung (optional)  
- VAPID Key-Generierung für Push-Authorization
- Konfigurierbare Endpoints und Account UUIDs
- Unterstützt Air-Gapped und Reverse Proxy Deployment

## 🔧 Reproducible Build Setup

### Voraussetzungen für Docker Build
```bash
# Rust Installation in Docker-Container
FROM rust:1.75-slim

# System Dependencies
RUN apt-get update && apt-get install -y \
    git \
    build-essential \
    pkg-config \
    libssl-dev
```

### 1. Repository Setup
```bash
# Fork klonen
git clone https://github.com/osCASHme/mollysocket.git
cd mollysocket

# Original Version ermitteln
ORIGINAL_VERSION=$(git describe --tags --abbrev=0)
echo "Building MollySocket version: $ORIGINAL_VERSION"
```

### 2. Docker Build Environment
```bash
# Dockerfile erstellen für reproducible builds
cat > Dockerfile.reproducible << 'EOF'
FROM rust:1.75-slim

# Reproducible Build Environment
RUN apt-get update && apt-get install -y \
    git build-essential pkg-config libssl-dev \
    && rm -rf /var/lib/apt/lists/*

# Set consistent build time
ENV SOURCE_DATE_EPOCH=1640995200

WORKDIR /build
COPY . .

# Build with fixed target
RUN cargo build --release --target x86_64-unknown-linux-gnu
EOF

# Build mit Docker
docker build -f Dockerfile.reproducible -t mollysocket-build .
```

### 3. Binary extrahieren und verifizieren
```bash
# Binary aus Container extrahieren
docker run --rm -v $(pwd)/outputs:/output mollysocket-build \
    cp target/x86_64-unknown-linux-gnu/release/mollysocket /output/

# Offizielle Binary herunterladen (falls verfügbar)
wget https://github.com/mollyim/mollysocket/releases/download/$ORIGINAL_VERSION/mollysocket-linux-x86_64

# Vergleich
ls -la outputs/mollysocket
ls -la mollysocket-linux-x86_64
```

## 🧪 Funktionstest

### 1. Konfiguration erstellen
```toml
# mollysocket.toml
host = "0.0.0.0"
port = 8020

# VAPID Keys generieren
[vapid]
private_key = "YOUR_PRIVATE_KEY"
public_key = "YOUR_PUBLIC_KEY"

# Allowed endpoints
allowed_endpoints = ["https://your.unifiedpush.server"]
```

### 2. Test-Server starten
```bash
# Mit unserer gebauten Binary
./outputs/mollysocket --config mollysocket.toml

# Oder mit Docker
docker run -p 8020:8020 -v $(pwd)/mollysocket.toml:/config/mollysocket.toml \
    mollysocket-build mollysocket --config /config/mollysocket.toml
```

## 🔐 Sicherheits-Features

### Privacy-First Design
- **Keine Encryption Keys**: MollySocket besitzt niemals Verschlüsselungsschlüssel
- **Linked Device Credentials**: Nur verlinkte Gerätedaten, keine direkten Account-Zugriffe
- **Air-Gapped Deployment**: Vollständige Isolation möglich

### osCASH.me Integration
- **UnifiedPush für Zahlungsbenachrichtigungen**: Sichere Notifications ohne Google
- **Privacy-konforme Push-Infrastruktur**: Keine Metadaten an Drittanbieter
- **Self-Hosted Deployment**: Vollständige Kontrolle über Notification-Server

## 📦 Deployment Varianten

### 1. Docker Compose Integration
```yaml
# docker-compose.yml für osCASH.me Stack
version: '3.8'

services:
  mollysocket:
    build:
      context: ./mollysocket
      dockerfile: Dockerfile.reproducible
    ports:
      - "8020:8020"
    volumes:
      - ./config/mollysocket.toml:/config/mollysocket.toml
    restart: unless-stopped
    environment:
      - RUST_LOG=info
```

### 2. Systemd Service
```ini
[Unit]
Description=MollySocket UnifiedPush Service
After=network.target

[Service]
Type=simple
User=mollysocket
ExecStart=/opt/mollysocket/bin/mollysocket --config /etc/mollysocket/mollysocket.toml
Restart=always
RestartSec=5

[Install]
WantedBy=multi-user.target
```

## 🎯 Nächste Schritte für osCASH.me GATE

1. **Reproducible Build erstellen** und gegen Original verifizieren
2. **Docker Integration** in osCASH.me Development Stack
3. **UnifiedPush Server Setup** für Community-Deployment  
4. **Payment Notification Integration** für Crypto-Transaktionen
5. **Documentation** für Community Self-Hosting

---

**MollySocket für osCASH.me - Privacy-konforme Push-Notifications ohne Big Tech**

*"Privacy First. Swiss Clockwork Reliability."*